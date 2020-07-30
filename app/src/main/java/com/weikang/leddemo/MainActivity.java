package com.weikang.leddemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.DefaultTaskExecutor;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "weikang";
    private Button showNotification, deleteNotification;
    private NotificationManager manager;
    private RemoteViews remoteViews;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showNotification = findViewById(R.id.show_notification);
        showNotification.setOnClickListener(this);
        deleteNotification = findViewById(R.id.delete_notification);
        deleteNotification.setOnClickListener(this);
        //获取通知管理器
        manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_notification:
                //适配安卓8.0的消息渠道
                /*String channelID = "channelID";
                CharSequence channelName = "channelName";*/
                /*NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
                // 无通知音
                channel.setSound(null, Notification.AUDIO_ATTRIBUTES_DEFAULT);
                // 默认通知音
                channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);
                // 设置通知出现时的闪灯（如果 android 设备支持的话）
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                // 设置通知出现时的震动（如果 android 设备支持的话）
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200});
                // 创建渠道
                manager.createNotificationChannel(channel);*/

                /*Notification notify = new Notification.Builder(MainActivity.this,channelID)
                        .setContentTitle("hello world")
                        .setContentText("世界你好！")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setCustomContentView(remoteViews)
                        .setContentIntent(pendingIntent)
                        .build();
                notify.flags = Notification.FLAG_ONGOING_EVENT;
                manager.notify(1,notify);*/
                Log.d(TAG,"show-notify");
                Notification notification = new Notification();
                notification.icon = R.drawable.ic_launcher_background;
                notification.tickerText = "hello world";
                //notification.when = System.currentTimeMillis();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
                remoteViews.setImageViewResource(R.id.iv_notify, R.mipmap.ic_launcher);
                remoteViews.setTextViewText(R.id.tv_title,"hello world");
                remoteViews.setTextViewText(R.id.tv_content,"世界这么大，我想去看看！");
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                remoteViews.setTextViewText(R.id.tv_time,format.format(date));

                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0,
                        new Intent(this, SecondActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.iv_notify2, pendingIntent2);
                notification.contentView = remoteViews;
                notification.contentIntent = pendingIntent;
                manager.notify(2,notification);

                break;
            case R.id.delete_notification:
                crateNotification();
                break;
            default:
                //crateNotification();
                break;
        }
    }

    private void crateNotification() {
        Log.d(TAG,"crateNotification");
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannelGroup channelGroup = new NotificationChannelGroup("Group1", "Group1");
        manager.createNotificationChannelGroup(channelGroup);

        //创建通知渠道
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Log.d(TAG,"Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O");
            NotificationChannel notificationChannel = new NotificationChannel("TEST1", "TEST1", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setVibrationPattern(new long[]{ 0, 100, 100, 100 });
            notificationChannel.setGroup("Group1");
            manager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, SecondActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        remoteViews.setImageViewResource(R.id.iv_notify, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.tv_title,"hello world");
        remoteViews.setTextViewText(R.id.tv_content,"世界这么大，我想去看看！");
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        remoteViews.setTextViewText(R.id.tv_time,format.format(date));

        Notification notification = new NotificationCompat.Builder(this, "TEST1")
                //.setContentTitle("This is content title")
                //.setContentText("balabalabalabla")
                //.setWhen(System.currentTimeMillis())
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);

    }
}
