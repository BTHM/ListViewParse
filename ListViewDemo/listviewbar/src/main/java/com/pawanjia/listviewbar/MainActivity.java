package com.pawanjia.listviewbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pawanjia.listviewbar.camera.CustomCameraActivity;
import com.pawanjia.listviewbar.camera.VideoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);

        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1:
                startActivity(new Intent(this,IndicatorGroupActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this,AnimatorActivity.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(this,ListLayoutActivity.class));
                break;

            case R.id.btn4:
                startActivity(new Intent(this,ViewActionActivity.class));
                break;
            case R.id.btn5:
                startActivity(new Intent(this,ViewGroupActionActivity.class));
                break;
            case R.id.btn7:
                startActivity(new Intent(this,VideoActivity.class));
                break;
            case R.id.btn8:
                startActivity(new Intent(this,CustomCameraActivity.class));
                break;
            default:
        }

    }
}
