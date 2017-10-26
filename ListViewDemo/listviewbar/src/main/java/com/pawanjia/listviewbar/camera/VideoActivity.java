package com.pawanjia.listviewbar.camera;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.pawanjia.listviewbar.R;

import java.io.File;

public class VideoActivity extends AppCompatActivity {
   private int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE=200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "test" + ".mp4";
        File file = new File(path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);  // set the image file name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
}
