package com.pingan.valueanimatordemo;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author：liupeng on 2016/12/27 21:21
 * Address：liupeng264@pingan.com.cn
 */
public class MyAnimView extends View {

    private Paint mPaint;
    private int RADIUS = 50;
    private Point mCurrentPoint;

    public MyAnimView(Context context) {
        super(context);
        init();
    }

    public MyAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentPoint == null) {
            mCurrentPoint = new Point(RADIUS, RADIUS);
            drawCircle(canvas);
            startAnimation();
        } else {
            drawCircle(canvas);
        }
    }

    public void drawCircle(Canvas canvas) {
        mCurrentPoint.getX();
        canvas.drawCircle(mCurrentPoint.getX(), mCurrentPoint.getY(), RADIUS, mPaint);
    }

    private void startAnimation() {
        Point startPoint = new Point(RADIUS, RADIUS);
        Point endPoint = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.setDuration(5000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.start();
    }


    private class PointEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            int  x = (int) (startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX()));
            int  y = (int) (fraction * endPoint.getY() + (1 - fraction) * startPoint.getY());
            Point point = new Point(x, y);
            return point;
        }
    }
}