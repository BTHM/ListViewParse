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

public class ListIndicatorScroll extends View {

    private Paint mPaint;
    private String[] text = {"概况", "病因", "临床表现", "检查", "诊断", "并发症", "治疗", "预后", "预防", "护理"};
    private int measuredWidth;
    private int sectionHeight;
    private int measuredHeight;
    private float downY;
    private OnTouchListner mTouchListner;
    private int selectedPosition = 0;
    private int textSize;
    private int textX;
    private int maxPosition;
    private float downX;
    private float totaldiffY;

    public ListIndicatorScroll(Context context) {
        this(context, null);
    }

    public ListIndicatorScroll(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListIndicatorScroll(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textSize = ToolUtils.sp2px(getContext(), 14);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_353535));
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        sectionHeight = ToolUtils.dip2px(context, 48);
        maxPosition = text.length - 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        // sectionHeight = measuredHeight / text.length;
        textX = ToolUtils.dip2px(getContext(), 19) + (measuredWidth - ToolUtils.dip2px(getContext(), 19)) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetricsInt fm = mPaint.getFontMetricsInt();
        int ascent = fm.ascent;

        for (int i = 0; i < text.length; i++) {
            int y = sectionHeight / 2 + i * sectionHeight - ascent / 2;
            //int y = (sectionHeight -ascent) / 2 + i * sectionHeight;
            if (selectedPosition == i) {
                mPaint.setColor(getResources().getColor(R.color.color_ff6602));
            } else {
                mPaint.setColor(getResources().getColor(R.color.color_353535));
            }
            canvas.drawText(text[i], textX, y, mPaint);

        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                downX = event.getX();
                int position = (int) (downY / sectionHeight);

                if (maxPosition <= position) {
                    position = maxPosition;
                }
                selectedPosition = position;
                Log.d("tag", "position=" + position);
                if (mTouchListner != null) {
                    mTouchListner.onTouch(position);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                float moveX = event.getX();
                float diffY = moveY - downY;
                float diffX = moveX - downX;
                totaldiffY +=diffY;
                if (Math.abs(diffX) < Math.abs(diffY)) {
                    int movePosition = (int) (moveY / sectionHeight);
                    if (maxPosition <= movePosition) {
                        movePosition = maxPosition;
                    }
                    selectedPosition = movePosition;
                    Log.d("tag", "position=" + movePosition);
                    if (mTouchListner != null) {
                        mTouchListner.onTouch(movePosition);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }
        invalidate();
        return true;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        invalidate();
    }

    interface OnTouchListner {
        void onTouch(int position);
        //void scrollX();
    }

    public void setOnTouchListner(OnTouchListner touchListner) {
        mTouchListner = touchListner;
    }
}
