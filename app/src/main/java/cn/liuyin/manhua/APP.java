package cn.liuyin.manhua;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.liuyin.manhua.tool.FileTool;
import okhttp3.OkHttpClient;

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
        File file = new File(FileTool.BASEPATH, "cache");
        if (!file.exists()) {
            file.mkdirs();
        }

        okhttp3.Cache cache = new okhttp3.Cache(file, 1024 * 1024 * 50);
        okhttp3.OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(client);
        Picasso.Builder picassoBuilder = new Picasso.Builder(mContext);
        picassoBuilder.downloader(okHttp3Downloader).build();
        Picasso picasso = picassoBuilder.build();
        Picasso.setSingletonInstance(picasso);

    }
}
