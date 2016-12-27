package com.pingan.valueanimatordemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ValueAnimator.AnimatorUpdateListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getLayout();
        //initData();
       // getXmlAnimator(textView);
        //ObjectaAnimator(textView);
        //valueAnitorDemo();
    }

    private void getLayout() {
        mLinearLayout = (LinearLayout) findViewById(R.id.activity_main);
        mLinearLayout.setGravity(Gravity.CENTER);
        TextView textView = new TextView(this);
        textView.setText("property Animation");
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.gravity=  Gravity.CENTER_VERTICAL;
        mLinearLayout.addView(textView,layoutParams);
    }

    private void initData() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(300);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
               // Log.d(TAG, "onAnimationUpdate:"+value);
            }
        });







    }

    private void valueAnitorDemo() {
        ValueAnimator anim = ValueAnimator.ofFloat(0f, 5f, 3f, 100f);
        anim.setDuration(3000);
        anim.start();
        anim.addUpdateListener(this);
    }

    private void ObjectaAnimator(TextView textView) {
        ObjectAnimator ob= ObjectAnimator.ofFloat(textView,"rotation",0f,360f);
        ob.setDuration(5000);
        ob.setRepeatCount(2);
        ob.start();
        ob.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void getXmlAnimator(TextView textView) {
        Animator animator1 = AnimatorInflater.loadAnimator(this, R.animator.animator_set);
        animator1.setTarget(textView);
        animator1.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();
        Log.d(TAG, "onAnimationUpdate:"+value);
    }


}
