package com.pawanjia.listviewbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Description
 *
 * @author liupeng502
 * @data 2017/9/7
 */

public class ListViewIndicator extends ListView {


    private float mDownY;
    private int mSectionHeight;

    public ListViewIndicator(Context context) {
        super(context);
    }

    public ListViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    public void setSectionHeight(int sectionHeight){
        mSectionHeight = sectionHeight;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
               // mDownY/mSectionHeight;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();

                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
        }

        return true;
    }
}
