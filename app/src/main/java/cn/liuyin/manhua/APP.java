package cn.liuyin.manhua;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import cn.liuyin.manhua.tool.HttpTool;

/**
 * Created by asus on 2017/12/29.
 */

public class APP extends Application {
    @SuppressLint("StaticFieldLeak")
    static Context mContext;
    public static Context getContext(){return mContext;}
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpTool.getIPs();
            }
        }).start();
    }
}
