####事件分发顺序
 Android中的事件处理一直是Android自定义控件开发中的重难点，想写出交互多点的控件处理好触摸事件是非常关键的，触摸事件就是对手指触摸到手机屏幕后产生的一系列事件，即手指按下的Down事件，一个或者一连串的Move事件(Move事件很敏感)，手指抬起时的Up事件...,而我们要通过具体的交互场景，告诉我们自己代码，当前的事件应该给谁来处理，或者是不处理。
这里分享一个链接，有兴趣的可以去了解下：[Activity+Window+View简单说明](http://blog.csdn.net/chunqiuwei/article/details/46740761)
简要说明下首先得到事件的是当前所在的Activity，然后是Window，再到DecorView，再到ViewGroup。即
Activity——>Window（实现类PhoneWindow）——>DecorView——>ViewGroup；
>
	   ![Activity+Window+View简单说明](http://img.blog.csdn.net/20171011143522625?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ2l0X3Blbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

下面我们从源码入手分析下是怎么实现的：
起于activity:
手指在触摸屏上滑动所产生的一系列事件，当Activity接收到这些事件通过调用Activity的dispatchTouchEvent方法来进行对事件的分发操作，下面来看下其源码

	public boolean dispatchTouchEvent(MotionEvent ev) {
	        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
	            onUserInteraction();//空方法，暂时忽略之  
	        }
	        //把事件ev交给Window来处理，在window中是抽象方法，看下其实现类PhoneWindow对应的具体方法 
	        if (getWindow().superDispatchTouchEvent(ev)) {
	        //如果返回true表明整个事件到此结束，处理完毕
	            return true;
	        }
	        //如果View未处理事件，则直接调用activity自己的onTouchEvent
	        return onTouchEvent(ev);
	    }
PhoneWindow：
		这里面的mDecor它是一个DecorView，DecorView它是一个Activity的顶级View。
		  它是PhoneWindow的一个内部类，继承自FrameLayout。于是在这个时候事件又交由
		  DecorView的superDispatchTouchEvent方法来处理
		  
	@Override
    public boolean superDispatchTouchEvent(MotionEvent event) {
        return mDecor.superDispatchTouchEvent(event);
    }
    
DecorView：
在这个时候就能够很清晰的看到DecorView它调用了父类的dispatchTouchEvent方法。
		在上面说到DecorView它继承了FrameLayout，而这个FrameLayout又继承自ViewGroup。
		我们在Activity界面中你能见到的各个View都是DecorView的子View。到此为止事件已经分发到View上面

	public boolean superDispatchTouchEvent(MotionEvent event) {
	        //已经分发到ViewGroup上了
	        return super.dispatchTouchEvent(event);
	    }


####ViewGroup事件分发源码分析

在事件处理中充分发挥了View与ViewGroup的子父类关系，在setContentView（）设置布局就是一个具体的ViewGroup子类。但其内部包含一些View或者ViewGroup，且ViewGroup本身继承自View，所以ViewGroup也是一个View。而通过前面的结论是先到达ViewGroup，这时候ViewGroup可以拦截给本身对事件做出处理，也可以下发到它的子View并交由子View进行处理，如果子View不处理则又会回到布局父节点，正是这种循环调用的设计思想使开发者能巧妙地应对不同的业务场景需求，下面看下最复杂的ViewGroup事件处理源码
看到这里我们要说下ViewGroup中常提到的几个事件处理相关的方法：
> public boolean dispatchTouchEvent(MotionEvent ev)
public boolean onInterceptTouchEvent(MotionEvent ev)
public boolean onTouchEvent(MotionEvent event)

下面来主要分析下viewGroup中的dispatchTouchEvent中的源码，梳理下相互间的联系

	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onTouchEvent(ev, 1);
        }

        // If the event targets the accessibility focused view and this is it, start
        // normal event dispatch. Maybe a descendant is what will handle the click.
        if (ev.isTargetAccessibilityFocus() && isAccessibilityFocusedViewOrHost()) {
            ev.setTargetAccessibilityFocus(false);
        }

        boolean handled = false;
        if (onFilterTouchEventForSecurity(ev)) {
            final int action = ev.getAction();
            final int actionMasked = action & MotionEvent.ACTION_MASK;

            // Handle an initial down.
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                // Throw away all previous state when starting a new touch gesture.
                // The framework may have dropped the up or cancel event for the previous gesture
                // due to an app switch, ANR, or some other state change.
                cancelAndClearTouchTargets(ev);//mFirstTouchTarget初始化
                resetTouchState();//状态位重置，所以即使我们设置了//这个标志位通过requestDisallowInterceptTouchEvent(true)，viewGroup也依旧能收到down事件
                if (!disallowIntercept) {
            }

            // Check for interception.
            final boolean intercepted;
			//ACTION_DOWN或者mFirstTouchTarget != null都会进行到if当中，意味着除了Down以外的事件也是可能进入IF中，mFirstTouchTarget对象很关键，此时其已经为非空
            if (actionMasked == MotionEvent.ACTION_DOWN
                    || mFirstTouchTarget != null) {
                final boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
                if (!disallowIntercept) {
                    intercepted = onInterceptTouchEvent(ev);//调用拦截方法并获取其返回值
                    ev.setAction(action); // restore action in case it was changed
                } else {
                    intercepted = false;
                }
            } else {
                // There are no touch targets and this action is not an initial down
                // so this view group continues to intercept touches.
                intercepted = true;
            }

            // If intercepted, start normal event dispatch. Also if there is already
            // a view that is handling the gesture, do normal event dispatch.
            if (intercepted || mFirstTouchTarget != null) {
                ev.setTargetAccessibilityFocus(false);
            }

            // Check for cancelation.
            final boolean canceled = resetCancelNextUpFlag(this)
                    || actionMasked == MotionEvent.ACTION_CANCEL;

            // Update list of touch targets for pointer down, if needed.
            final boolean split = (mGroupFlags & FLAG_SPLIT_MOTION_EVENTS) != 0;
            TouchTarget newTouchTarget = null;
            boolean alreadyDispatchedToNewTouchTarget = false;
			//没有被取消并且没有被拦截，则进入下面方法，
            if (!canceled && !intercepted) {

                // If the event is targeting accessiiblity focus we give it to the
                // view that has accessibility focus and if it does not handle it
                // we clear the flag and dispatch the event to all children as usual.
                // We are looking up the accessibility focused host to avoid keeping
                // state since these events are very rare.
                View childWithAccessibilityFocus = ev.isTargetAccessibilityFocus()
                        ? findChildWithAccessibilityFocus() : null;

                if (actionMasked == MotionEvent.ACTION_DOWN
                        || (split && actionMasked == MotionEvent.ACTION_POINTER_DOWN)
                        || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                    final int actionIndex = ev.getActionIndex(); // always 0 for down
                    final int idBitsToAssign = split ? 1 << ev.getPointerId(actionIndex)
                            : TouchTarget.ALL_POINTER_IDS;

                    // Clean up earlier touch targets for this pointer id in case they
                    // have become out of sync.
                    removePointersFromTouchTargets(idBitsToAssign);

                    final int childrenCount = mChildrenCount;
                    if (newTouchTarget == null && childrenCount != 0) {
                        final float x = ev.getX(actionIndex);
                        final float y = ev.getY(actionIndex);
                        // Find a child that can receive the event.
                        // Scan children from front to back.
                        final ArrayList<View> preorderedList = buildTouchDispatchChildList();
                        final boolean customOrder = preorderedList == null
                                && isChildrenDrawingOrderEnabled();
                        final View[] children = mChildren;
						//倒序遍历所有的子view
                        for (int i = childrenCount - 1; i >= 0; i--) {
                            final int childIndex = getAndVerifyPreorderedIndex(
                                    childrenCount, i, customOrder);
                            final View child = getAndVerifyPreorderedView(
                                    preorderedList, children, childIndex);

                            // If there is a view that has accessibility focus we want it
                            // to get the event first and if not handled we will perform a
                            // normal dispatch. We may do a double iteration but this is
                            // safer given the timeframe.
                            if (childWithAccessibilityFocus != null) {
                                if (childWithAccessibilityFocus != child) {
                                    continue;
                                }
                                childWithAccessibilityFocus = null;
                                i = childrenCount - 1;
                            }

                            if (!canViewReceivePointerEvents(child)
                                    || !isTransformedTouchPointInView(x, y, child, null)) {
                                ev.setTargetAccessibilityFocus(false);
                                continue;
                            }
							//调用getTouchTarget方法去查找当前子View是否在mFirstTouchTarget.next这条target链中，
                            //如果存在则返回这个target，否则返回null。
                            newTouchTarget = getTouchTarget(child);
                            if (newTouchTarget != null) {
                                // Child is already receiving touch within its bounds.
                                // Give it the new pointer in addition to the ones it is handling.
                                newTouchTarget.pointerIdBits |= idBitsToAssign;
                                break;
                            }

                            resetCancelNextUpFlag(child);
							//调用dispatchTransformedTouchEvent()方法将Touch事件传递给特定的子View
                            //该方法返回false--则说明子view未消耗点击事件，从而下面的newTouchTarget = addTouchTarget(child, idBitsToAssign)方法无法调用，mFirstTouchTarget则为null
                            //该方法返回true--则说明子view消耗点击事件，从而进入if区域，从而mFirstTouchTarget不为null。
                            if (dispatchTransformedTouchEvent(ev, false, child, idBitsToAssign)) {
                                // Child wants to receive touch within its bounds.
                                mLastTouchDownTime = ev.getDownTime();
                                if (preorderedList != null) {
                                    // childIndex points into presorted list, find original index
                                    for (int j = 0; j < childrenCount; j++) {
                                        if (children[childIndex] == mChildren[j]) {
                                            mLastTouchDownIndex = j;
                                            break;
                                        }
                                    }
                                } else {
                                    mLastTouchDownIndex = childIndex;
                                }
                                mLastTouchDownX = ev.getX();
                                mLastTouchDownY = ev.getY();
                                newTouchTarget = addTouchTarget(child, idBitsToAssign);
                                alreadyDispatchedToNewTouchTarget = true;
                                break;
                            }

                            // The accessibility focus didn't handle the event, so clear
                            // the flag and do a normal dispatch to all children.
                            ev.setTargetAccessibilityFocus(false);
                        }
                        if (preorderedList != null) preorderedList.clear();
                    }

                    if (newTouchTarget == null && mFirstTouchTarget != null) {
                        // Did not find a child to receive the event.
                        // Assign the pointer to the least recently added target.
                        newTouchTarget = mFirstTouchTarget;
                        while (newTouchTarget.next != null) {
                            newTouchTarget = newTouchTarget.next;
                        }
                        newTouchTarget.pointerIdBits |= idBitsToAssign;
                    }
                }
            }

            // Dispatch to touch targets.
            // mFirstTouchTarget == null说明点击事件被拦截，或者子view没有消耗事件，有这里也可以得到onInterceptTouchEvent方法并不是通过自己将事件拦截下来给自己，只是通过返回值使dispatchTouchEvent()方法不进入查找子view的dispatchTouchEvent()，使事件隔离开了与子view的关系
            if (mFirstTouchTarget == null) {
                // No touch targets so treat this as an ordinary view.
                handled = dispatchTransformedTouchEvent(ev, canceled, null,
                        TouchTarget.ALL_POINTER_IDS);
			//调用父类dispatchTouchEvent，再调用onTouchEvent处理焦点
            } else {
                // Dispatch to touch targets, excluding the new touch target if we already
                // dispatched to it.  Cancel touch targets if necessary.
                TouchTarget predecessor = null;
                TouchTarget target = mFirstTouchTarget;
                while (target != null) {
                    final TouchTarget next = target.next;
			//根据alreadyDispatchedToNewTouchTarget 判断，如果已经分发了，则返回true
                    if (alreadyDispatchedToNewTouchTarget && target == newTouchTarget) {
                        handled = true;
                    } else {
						//点击事件未被上面的子view消耗时，事件传递过程
                        //比如：有些人强制在ViewGroup中MotionEvent.ACTION_DOWN时，onInterceptTouchEvent返回false，ACTION_MOVE时，返回true，则进入到此方法
                        //清除掉mFirstTouchTarget链表中所有target，及mFirstTouchTarget==null；这样下一次直接跑入到
                        //if (mFirstTouchTarget == null)内容区域内，则点击事件传递到ViewGroup的onTouchEvent处理焦点
                        final boolean cancelChild = resetCancelNextUpFlag(target.child)
                                || intercepted;
                        if (dispatchTransformedTouchEvent(ev, cancelChild,
                                target.child, target.pointerIdBits)) {
                            handled = true;
                        }
                        if (cancelChild) {
                            if (predecessor == null) {
                                mFirstTouchTarget = next;
                            } else {
                                predecessor.next = next;
                            }
                            target.recycle();
                            target = next;
                            continue;
                        }
                    }
                    predecessor = target;
                    target = next;
                }
            }

            // Update list of touch targets for pointer up or cancel, if needed.
            if (canceled
                    || actionMasked == MotionEvent.ACTION_UP
                    || actionMasked == MotionEvent.ACTION_HOVER_MOVE) {
                resetTouchState();
            } else if (split && actionMasked == MotionEvent.ACTION_POINTER_UP) {
                final int actionIndex = ev.getActionIndex();
                final int idBitsToRemove = 1 << ev.getPointerId(actionIndex);
                removePointersFromTouchTargets(idBitsToRemove);
            }
        }

        if (!handled && mInputEventConsistencyVerifier != null) {
            mInputEventConsistencyVerifier.onUnhandledEvent(ev, 1);
        }
        return handled;
    }

>看下多次用到的dispatchTransformedTouchEvent（）方法，
	
	private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel,
            View child, int desiredPointerIdBits) {
        final boolean handled;

        // Canceling motions is a special case.  We don't need to perform any transformations
        // or filtering.  The important part is the action, not the contents.
        final int oldAction = event.getAction();
        if (cancel || oldAction == MotionEvent.ACTION_CANCEL) {
            event.setAction(MotionEvent.ACTION_CANCEL);
			//事件处理vIEW链表底部
            if (child == null) {
				//父类view的dispatchTouchEvent(),根据返回值由自己的onTouchEvent来决定
                handled = super.dispatchTouchEvent(event);
            } else {
				//由child的onTouchEvent来决定，如果是viewGroup的话 也能进行循环遍历
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }

        // Calculate the number of pointers to deliver.
        final int oldPointerIdBits = event.getPointerIdBits();
        final int newPointerIdBits = oldPointerIdBits & desiredPointerIdBits;

        // If for some reason we ended up in an inconsistent state where it looks like we
        // might produce a motion event with no pointers in it, then drop the event.
        if (newPointerIdBits == 0) {
            return false;
        }

        // If the number of pointers is the same and we don't need to perform any fancy
        // irreversible transformations, then we can reuse the motion event for this
        // dispatch as long as we are careful to revert any changes we make.
        // Otherwise we need to make a copy.
        final MotionEvent transformedEvent;
        if (newPointerIdBits == oldPointerIdBits) {
            if (child == null || child.hasIdentityMatrix()) {
                if (child == null) {
			//父类view的dispatchTouchEvent(),根据返回值由自己的onTouchEvent来决定
                    handled = super.dispatchTouchEvent(event);
                } else {
					//按照子控件进行坐标转换，如果是viewGroup的话 也能进行循环遍历
                    final float offsetX = mScrollX - child.mLeft;
                    final float offsetY = mScrollY - child.mTop;
                    event.offsetLocation(offsetX, offsetY);

                    handled = child.dispatchTouchEvent(event);

                    event.offsetLocation(-offsetX, -offsetY);
                }
                return handled;
            }
            transformedEvent = MotionEvent.obtain(event);
        } else {
            transformedEvent = event.split(newPointerIdBits);
        }

        // Perform any necessary transformations and dispatch.
        if (child == null) {
            handled = super.dispatchTouchEvent(transformedEvent);
        } else {
            final float offsetX = mScrollX - child.mLeft;
            final float offsetY = mScrollY - child.mTop;
            transformedEvent.offsetLocation(offsetX, offsetY);
            if (! child.hasIdentityMatrix()) {
                transformedEvent.transform(child.getInverseMatrix());
            }

            handled = child.dispatchTouchEvent(transformedEvent);
        }

        // Done.
        transformedEvent.recycle();
        return handled;
    }
下面用一幅图来概况下整个流程

![事件分发流程](http://img.blog.csdn.net/20171011234807469?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ2l0X3Blbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

####源码得到的总结

	1.android的事件传递是先传递到父类，再到子类的。 
	
	2.ViewGroup中可以通过onInterceptTouchEvent方法对事件传递进行拦截，但是子View可以通过requestDisallowInterceptTouchEvent(boolean disallowIntercept)控制父类的拦截事件是否调用。 
	
	3.子View消耗掉点击事件后，父类onTouchEvent方法不会调用，子View不消耗点击事件，会传到父类onTouchEvent方法，父类onTouchEvent方法返回false，则最终传递到activity的onTouchEvent方法。 
	
	4.ViewGroup一旦调用onInterceptTouchEvent方法拦截点击事件后，本次点击序列事件则都交于该ViewGroup处理，并且onInterceptTouchEvent将不再执行。判断是否父类是否拦截点击事件中的解释。 
	
	5.当dispatchTouchEvent在进行事件分发的时候，只有前一个action返回true，才会触发下一个action.也就是说，子view 未消耗点击事件，及dispatchTouchEvent返回false，这样mFirstTouchTarget =null，，则后续action直接由ViewGroup执行，不传递给子View。
