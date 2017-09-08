package com.pawanjia.listviewbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Description
 *
 * @author liupeng502
 * @data 2017/9/7
 */

public class ListIndicator extends View {

    private Paint mPaint;
    private String[] text={"概况","病因","检查","检查","临床","表现","解决办法","注意"};
    private int measuredWidth;
    private int sectionHeight;
    private int measuredHeight;
    private float downY;
    private OnTouchListner mTouchListner;
    private int selectedPosition=0;

    public ListIndicator(Context context) {
        this(context,null);
    }

    public ListIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ListIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_353535));
        mPaint.setTextSize(60);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        sectionHeight = measuredHeight / text.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        int ascent = fm.ascent;

        for (int i = 0; i < text.length; i++) {
            canvas.drawLine(0,sectionHeight*i,measuredWidth,sectionHeight*i,mPaint);
            //int textY = sectionHeight / 2 + i * sectionHeight - ascent/2;
            int textY = (sectionHeight -ascent) / 2 + i * sectionHeight;
            if (selectedPosition == i) {
                mPaint.setColor(getResources().getColor(R.color.color_ff6602));
            }else{
                mPaint.setColor(getResources().getColor(R.color.color_353535));
            }
            canvas.drawText(text[i],measuredWidth/2,textY,mPaint);

        }
        canvas.drawLine(0,measuredHeight,measuredWidth,measuredHeight,mPaint);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                int position = (int) (downY / sectionHeight);
                selectedPosition = position;
                Log.d("tag", "position="+position);
                if (mTouchListner != null) {
                    mTouchListner.onTouch(position);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                int movePosition = (int) (moveY / sectionHeight);
                selectedPosition=movePosition;
                Log.d("tag", "position="+movePosition);
                if (mTouchListner != null) {
                    mTouchListner.onTouch(movePosition);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
        }
        invalidate();
        return true;
    }

    public void setSelectedPosition(int position){
        selectedPosition=position;
        invalidate();
    }

    interface OnTouchListner{
        void onTouch(int position);
    }
    public void setOnTouchListner(OnTouchListner touchListner){
        mTouchListner = touchListner;
    }
}
