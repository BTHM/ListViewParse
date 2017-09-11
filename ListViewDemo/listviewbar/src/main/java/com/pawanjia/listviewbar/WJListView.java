package com.pawanjia.listviewbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Description
 *
 * @author liupeng502
 * @data 2017/9/11
 */

public class WJListView extends ListView {

    private float downX;
    private float downY;
    private float initX;
    private float initY;

    public WJListView(Context context) {
        this(context,null);
    }

    public WJListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WJListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        downX = ev.getX();
        downY = ev.getY();
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                initX=downX;
                initY=downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = downX - initX;
                float dy = downY - initY;
                if (Math.abs(dy) > Math.abs(dx)) {
                    initX=downX;
                    initY=downY;
                    return super.onInterceptTouchEvent(ev);

                }

                break;
            default:
        }
        return false;
    }
}
