package com.pingan.listviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mListView = (ListView) findViewById(R.id.list_view);
//        //mListView.setAdapter(new ImageAdapter(this,0,Images.imageUrls));
//        mListView.setAdapter(new ImageAdapter2(this,0,Images.imageUrls));
        //mListView.setAdapter(new ImageAdapter3(this,0,Images.imageUrls));
        findViewById(R.id.btn1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, RemoteViewActivity.class));
        Intent intent = new Intent(this, ImgAppWidgetProvider.class);
        intent.setAction("cn.hudp.androiddevartnote.action.click");
        sendBroadcast(intent);
    }
}
