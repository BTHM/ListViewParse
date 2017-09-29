package com.pawanjia.listviewbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class ViewActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_action);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                WJLog.d("ListIndicator3", "activity...dispatchTouchEvent..." + "ACTION_DOWN");

                break;
            case MotionEvent.ACTION_MOVE:
                WJLog.d("ListIndicator3", "activity...dispatchTouchEvent..." + "ACTION_MOVE");
                break;
            default:
        }
        WJLog.d("ListIndicator3", "activity...dispatchTouchEvent..." + super.dispatchTouchEvent(ev));
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                WJLog.d("ListIndicator3", "activity...onTouchEvent..." + "ACTION_DOWN");

                break;
            case MotionEvent.ACTION_MOVE:
                WJLog.d("ListIndicator3", "activity...onTouchEvent..." + "ACTION_MOVE");
                break;
            default:
        }
        WJLog.d("ListIndicator3", "activity...onTouchEvent..." + super.onTouchEvent(event));
        //return super.onTouchEvent(event);
        return true;
    }
}
