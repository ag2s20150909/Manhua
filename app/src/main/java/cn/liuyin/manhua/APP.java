package cn.liuyin.manhua;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.liuyin.manhua.data.api.API;
import cn.liuyin.manhua.data.api.ApiInterceptor;
import cn.liuyin.manhua.tool.CrashHandler;
import cn.liuyin.manhua.tool.okhttp.GzipRequestInterceptor;
import cn.liuyin.manhua.tool.okhttp.OkHttpDns;
import cn.liuyin.manhua.tool.okhttp.ShortCacheInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by asus on 2017/12/29.
 */

public class APP extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static okhttp3.OkHttpClient mClient;
    private static okhttp3.OkHttpClient mCacheClient;
    private ExecutorService fixdeThreadPool;

    public static Context getContext() {
        return mContext;
    }

    public static okhttp3.OkHttpClient getOkhttpClient() {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    .dns(OkHttpDns.getInstance(mContext))
                    //.addInterceptor(new GzipRequestInterceptor())
                    .addInterceptor(new ApiInterceptor())
                    .cache(new okhttp3.Cache(new File(mContext.getExternalCacheDir(), "okhttpcache"), 500 * 1024 * 1024))
                    .addNetworkInterceptor(new ShortCacheInterceptor())
                    .build();
        }
        return mClient;
    }

    public static okhttp3.OkHttpClient getCachehttpClient() {
        if (mCacheClient == null) {
            mCacheClient = new OkHttpClient.Builder()
                    .dns(OkHttpDns.getInstance(mContext))
                    //.addInterceptor(new GzipRequestInterceptor())
                    .addInterceptor(new ApiInterceptor())
                    .cache(new okhttp3.Cache(new File(mContext.getExternalCacheDir(), "okhttpcache"), 500 * 1024 * 1024))
                    .build();
        }
        return mCacheClient;
    }

    public static void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        fixdeThreadPool = Executors.newFixedThreadPool(1);
        CrashHandler.getInstance().init(this)
                .setOnCrashListener(new CrashHandler.OnCrashListener() {
                    @Override
                    public void onCrash(Context context, String errorMsg) {
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                })
                .setCrashSave()
                .setCrashSaveTargetFolder(Objects.requireNonNull(this.getExternalFilesDir("crash")).getAbsolutePath());
        File file = new File(mContext.getExternalCacheDir(), "okhttpcache");
        if (!file.exists()) {
            file.mkdirs();
        }

        okhttp3.Cache cache = new okhttp3.Cache(file, 1024 * 1024 * 500);
        okhttp3.OkHttpClient client = new OkHttpClient.Builder()
                .dns(OkHttpDns.getInstance(mContext))
                //.addInterceptor(new GzipRequestInterceptor())
                .cache(cache)
                .build();

        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(client);
        Picasso.Builder picassoBuilder = new Picasso.Builder(mContext);
        picassoBuilder.downloader(okHttp3Downloader).build();
        Picasso picasso = picassoBuilder.build();
        Picasso.setSingletonInstance(picasso);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                API.CheckAppVersion();
//            }
//        }).start();


    }


}
