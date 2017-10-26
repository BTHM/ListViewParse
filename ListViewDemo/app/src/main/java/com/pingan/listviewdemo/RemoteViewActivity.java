package com.pingan.listviewdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;

public class RemoteViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_view);

        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
        notification.tickerText = "hello notification";
        notification.when = System.currentTimeMillis();

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
       /**/
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);//RemoveViews所加载的布局文件
        remoteViews.setTextViewText(R.id.tv, "这是一个Test");//设置文本内容
        remoteViews.setTextColor(R.id.tv, Color.parseColor("#abcdef"));//设置文本颜色
        remoteViews.setImageViewResource(R.id.iv, R.mipmap.ic_launcher);//设置图片
        PendingIntent openActivity2Pending = PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);//设置RemoveViews点击后启动界面
        remoteViews.setOnClickPendingIntent(R.id.tv, openActivity2Pending);
        notification.contentView = remoteViews;
        notification.contentIntent = pendingIntent;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, notification);
        manager.notify(3, notification);
    }
}
