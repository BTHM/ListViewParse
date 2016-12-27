package com.pingan.listviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_view);
        //mListView.setAdapter(new ImageAdapter(this,0,Images.imageUrls));
        mListView.setAdapter(new ImageAdapter2(this,0,Images.imageUrls));
        //mListView.setAdapter(new ImageAdapter3(this,0,Images.imageUrls));
    }

}
