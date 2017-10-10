####起于activity
	 /**
     * When set, this ViewGroup should not intercept touch events.
     * {@hide}
     */
    protected static final int FLAG_DISALLOW_INTERCEPT = 0x80000;


将mMotionTarget置为null
>
		final boolean disallowIntercept = (mGroupFlags &FLAG_DISALLOW_INTERCEPT) = 0;
                if (!disallowIntercept) {
					//允许拦截问是否拦截
                    intercepted = onInterceptTouchEvent(ev);
                    ev.setAction(action); // restore action in case it was changed
                } else {
					//不允许拦截则直接不拦截
                    intercepted = false;
                }






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
            // mFirstTouchTarget == null说明点击事件被拦截，或者子view没有消耗事件
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

3.总结

	1.android的事件传递是先传递到父类，再到子类的。 
	
	2.ViewGroup中可以通过onInterceptTouchEvent方法对事件传递进行拦截，但是子View可以通过requestDisallowInterceptTouchEvent(boolean disallowIntercept)控制父类的拦截事件是否调用。 
	
	3.子View消耗掉点击事件后，父类onTouchEvent方法不会调用，子View不消耗点击事件，会传到父类onTouchEvent方法，父类onTouchEvent方法返回false，则最终传递到activity的onTouchEvent方法。 
	
	4.ViewGroup一旦调用onInterceptTouchEvent方法拦截点击事件后，本次点击序列事件则都交于该ViewGroup处理，并且onInterceptTouchEvent将不再执行。原因见【2）继续看29-42行，判断是否父类是否拦截点击事件】中的解释。 
	
	5.当dispatchTouchEvent在进行事件分发的时候，只有前一个action返回true，才会触发下一个action.也就是说，子view 未消耗点击事件，及dispatchTouchEvent返回false，这样mFirstTouchTarget =null，后面只能执行172行代码，则后续action直接由ViewGroup执行，不传递给子View