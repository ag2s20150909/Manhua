package cn.liuyin.manhua.tool;

import android.content.Context;
import android.webkit.WebSettings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import cn.liuyin.manhua.APP;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpTool {
    Context mContext;

    OkHttpClient client;

    public HttpTool() {
        this(APP.getContext());
    }

    public HttpTool(Context context) {

        this.mContext = context;
        client = new OkHttpClient.Builder()
                .build();
    }

    public static String httpGet(String url) {


        OkHttpClient.Builder buider = new OkHttpClient.Builder();


        OkHttpClient client = buider.build();
        Request.Builder requestbuilder = new Request.Builder().get().url(url);
        requestbuilder.header("Referer", "m.pufei.net");

        requestbuilder.header("User-Agent", WebSettings.getDefaultUserAgent(APP.getContext()));

        Request request = requestbuilder.build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new String(Objects.requireNonNull(response.body()).bytes(), "gb2312");
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

    public static String search(Context context, String kw) {
        try {
            kw = URLEncoder.encode(kw, "gb2312");
        } catch (UnsupportedEncodingException e) {
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder().get().url("http://m.pufei.net/e/search/?searchget=1&tbname=mh&show=title,player,playadmin,bieming,pinyin&tempid=4&keyboard=" + kw).header("Referer", "http://ww.pufei.net/").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return Objects.requireNonNull(response.networkResponse()).request().url().toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (IOException e) {
            return "error:" + e.getMessage();
        }


    }


}
