###事件处理机制
####view的事件处理机制
先来看下dispatchTouchEvent(),该方法的默认会返回值由onTouch()和onTouchEvent()方法确定。

* 该方法返回true，自己的事件处理流程onTouch/onTouchEvent一定可以收到事件，无论onTouch/onTouchEvent的返回值是什么（告诉上层自己处理事件了，但是activity层的dispatchTouchEvent()依旧能获得事件，因其getWindow().superDispatchTouchEvent(ev)返回true,所以activity的onTouchEvent此时就无法获得到事件了）；
>   
		
	//由activity的dispatchTouchEvent
		public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {//view处理了事件，事件就不会再传给自己的onTouchEvent();
            return true;
        }
        return onTouchEvent(ev);
    }     

* 返回false，不管onTouch/onTouchEvent的返回值是什么都不得事件（告诉上层自己没有处理），以上说的得不到事件不包含Down事件

事件机制非常像上级给下级安排工作，上级获得到一个事件，第一时间是向下级问有没有人要处理该事件，下级的dispatchTouchEvent()方法返回值就是对上级的响应，默认情况下当然处理事件还是要靠动手的，onTouch/onTouchEvent就是手，当下级表示要处理的事件的时候，其也可以根据自身情况，决定是否动手处理该事件，onTouch/onTouchEvent返回true则表示下级动手处理了事件，返回false则表示下级并没有处理该事件，放任其自由，并不是所有的事件都有处理的必要。如果是true则表示我要处理，如果是false则表示我不处理。
当然默认下级的响应是要看自己的手能否处理得了事件，即看onTouch/onTouchEvent的返回值来决定对上级的响应，即决定着dispatchTouchEvent()的返回值。如果下级决定不处理事件，自己的dispatchTouchEvent()返回值为false，事件就会回交上级。

* 上层的onTouchEvent能否收到事件，要看下层的dispatchTouchEvent()返回值，即下层是否已将事件处理了，不管是下层否处理了事件上层的dispatchTouchEvent()都会收到事件。


* view的onTouch(),onTouchEvent默认返回值都为false,所以其dispatchTouchEvent()默认返回值也是false，即是不处理事件的。

* 作为view其dispatchTouchEvent的返回值设置为true后，不管其onTouch/onTouchEvent返回值为什么，其都能接收到事件，处理还是放任不管都会接收到，当然onTouch/onTouchEvent也是有优先级之分的。

> 
 
	这是view的dispatchTouchEvent的核心源码，先判断mOnTouchListener，及onTouch的返回值，
	然后在根据会返回值判断是否到达onTouchEvent，再获取onTouchEvent返回值
	 if (li != null && li.mOnTouchListener != null
                    && (mViewFlags & ENABLED_MASK) == ENABLED
                    && li.mOnTouchListener.onTouch(this, event)) {
                result = true;
            }

            if (!result && onTouchEvent(event)) {
                result = true;
            }



	这是activity的dispatchTouchEvent的源码，通过window将事件交给view，getWindow().superDispatchTouchEvent(ev)，
	再根据superDispatchTouchEvent(ev)返回值确定事件能否到自身的onTouchEvent();再获取onTouchEvent返回值
	public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


下面是自己结合API25源码及鸿洋大神的分析,来分析下onTouchEvent的源码

private static final int PFLAG_PRESSED             = 0x00004000;//长按标示

private static final int PFLAG_PREPRESSED          = 0x02000000;//短按标示

private static final int DEFAULT_LONG_PRESS_TIMEOUT = 500;//长按时间检测

private static final int TAP_TIMEOUT = 100;//短按时间检测

Down时：
* 首先设置标志为PFLAG_PREPRESSED，设置mHasPerformedLongPress=false ;然后发出一个TAP_TIMEOUT=100ms后的mPendingCheckForTap；
* 如果100ms内没有触发up事件，则标志位将清除PFLAG_PREPRESSED，设置为PFLAG_PRESSED，同时发出一个延时为500-100ms的，检测长按的任意任务消息；
* 如果达到500ms（从down触发起），则触发LongClickListener；

Move时：

* 主要内容是检测滑动事件是否划出控件，如果已划出：100ms内，直接移除mPendingCheckForTap;100ms后，将标志位PFLAG_PRESSED清除,同时移除长按的检查：removeLongPressCallback();

UP时：

* 如果115ms内，触发UP，此时标志为PREPRESSED，则执行UnsetPressedState，setPressed(false);会把setPress转发下去，可以在View中复写dispatchSetPressed方法接收；
* 如果是115ms-500ms间，即长按还未发生，则首先移除长按检测，执行onClick回调；
* 如果是500ms以后，那么有两种情况：
i.设置了onLongClickListener，且onLongClickListener.onClick返回true，则点击事件OnClick事件无法触发；
ii.没有设置onLongClickListener或者onLongClickListener.onClick返回false，则点击事件OnClick事件依然可以触发；
* 最后执行mUnsetPressedState.run()，将setPressed传递下去，然后将PRESSED标识去除；
* 最后问个问题，然后再运行个例子结束：
1、setOnLongClickListener和setOnClickListener是否只能执行一个
不是的，只要setOnLongClickListener中的onClick返回false，则两个都会执行；返回true则会屏蔽setOnClickListener

有个大家应该都知道的问题就是setClickable(false)的问题，该方法在setOnClickListener()/setOnLongClickListener之前调用是没有效果的，由源码可得知，所以要在之后调用，另外setClickable（false）和setEnable（false）笼统的都是禁止点击的意思。但是setEnable(false)将控件能完全禁用，颜色会变灰色。

	public void setOnClickListener(@Nullable OnClickListener l) {
        if (!isClickable()) {
            setClickable(true);
        }
        getListenerInfo().mOnClickListener = l;
    }	



后面在分析下viewGroup的事件机制。