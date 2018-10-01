package cn.liuyin.manhua.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.R;
import okhttp3.OkHttpClient;

public class DownloadService extends Service {
    private NotificationManager notificationManager;
    public Notification.Builder builder;
    Context mContext;
    OkHttpClient client;
    ExecutorService fixdeThreadPool;

    public DownloadService() {
        mContext = APP.getContext();
        fixdeThreadPool = Executors.newFixedThreadPool(5);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(mContext);
        client = APP.getCachehttpClient();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("action") != null && intent.getStringExtra("action").endsWith("download")) {
            go(intent.getIntExtra("bid", 0));
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void go(int bid) {
        fixdeThreadPool.execute(new DownloadRunnable(this, bid));

    }


    public void notifyMsg(String title, int all, int now, int bid) {
        int progress;
        if (now >= all) {
            progress = 100;
        } else {
            progress = now * 100 / all;
        }

        builder.setSmallIcon(R.drawable.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)).setContentTitle(title);
        if (progress >= 0 && progress < 100) {
            //下载进行中
            builder.setContentText("正在下载:" + title + progress + "%");
            builder.setProgress(100, progress, false);
        } else {
            builder.setContentText("下载成功");
            //mBuilder.setContentIntent();通知意图点击操作
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Notification mNotification = builder.build();
        notificationManager.notify(bid, mNotification);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
