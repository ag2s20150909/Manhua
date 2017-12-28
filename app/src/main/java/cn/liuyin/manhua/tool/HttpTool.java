package cn.liuyin.manhua.tool;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpTool {
    Context mContext;
    String UA = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36";
    String authenticity_token;
    String msg;
    OkHttpClient client;

    public HttpTool(Context context) {
        this.mContext = context;
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance(context))
                .build();
        //go();
    }


    public static String search(Context context, String kw) {
        try {
            kw = URLEncoder.encode(kw, "gb2312");
        } catch (UnsupportedEncodingException e) {
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance(context))
                .build();

        Request request = new Request.Builder().get().url("http://m.pufei.net/e/search/?searchget=1&tbname=mh&show=title,player,playadmin,bieming,pinyin&tempid=4&keyboard=" + kw).header("Referer", "http://ww.pufei.net/").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                //FileTool.writeFile("msg.txt",response.networkResponse().request().url().toString());
                return response.networkResponse().request().url().toString();
            } else {
                return "error:unknow error";
            }
        } catch (IOException e) {
            return "error" + e.getMessage();
        }
//        client.newCall(request).enqueue(new Callback() {
//				@Override
//				public void onFailure(Call call, IOException e) {
//					FileTool.writeFile("error.txt",e.getMessage());
//				}
//
//				@Override
//				public void onResponse(Call call, Response response) throws IOException {
//					//Log.d("google_lenve_fb", "onResponse: " + response.body().string().toString());
//					//msg=response.body().string();
//					
//					FileTool.writeFile("msg.txt",response.networkResponse().request().url().toString());
//				}
//			});


    }

    public static void search11(Context context, String kw) {
        try {
            kw = URLEncoder.encode(kw, "gb2312");
        } catch (UnsupportedEncodingException e) {
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance(context))
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("orderby", "1")
                .add("myorder", "1")
                .add("tbname", "mh")
                .add("tempid", "3")
                .add("show", "title,player,playadmin,bieming,pinyin")
                //.add("remember_me", "true")
                .add("keyboard", kw)
                .build();
        Request request = new Request.Builder().post(formBody).url("http://www.pufei.net/e/search/index.php").header("Referer", "http://ww.pufei.net/").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FileTool.writeFile("error.txt", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("google_lenve_fb", "onResponse: " + response.body().string().toString());
                //msg=response.body().string();
                FileTool.writeFile("msg.txt", response.body().string());
            }
        });


    }

    public void go() {
        Request request = new Request.Builder().url("https://github.com/login").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();
                Document parse = Jsoup.parse(resp);
                Elements select = parse.select("input[type=hidden]");
                Element element = select.get(1);
                authenticity_token = element.attr("value");
                FileTool.writeFile("test.txt", authenticity_token);
                //Toast.makeText(mContext,xsrf,1).show();
                //Message msg = mHandler.obtainMessage();
                //msg.what = 1;
                //msg.obj = xsrf;
                //Log.d("google_lenve_fb", "onResponse: xsrf:" + xsrf);
                //mHandler.sendMessage(msg);
            }
        });
    }

    public void run() {
        Request request = new Request.Builder().url("http://218.199.178.13/").removeHeader("User-Agent").addHeader("User-Agent", UA).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FileTool.writeFile("run.html", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();

                FileTool.writeFile("run.html", resp);
                //Toast.makeText(mContext,xsrf,1).show();
                //Message msg = mHandler.obtainMessage();
                //msg.what = 1;
                //msg.obj = xsrf;
                //Log.d("google_lenve_fb", "onResponse: xsrf:" + xsrf);
                //mHandler.sendMessage(msg);
            }
        });
    }

    //https://github.com/ag2s20150909?tab=repositories

    public void login(String email, String psw) {
        go();
        //xsrf="83724e847abec847a81f77aebf513de6";
        FormBody formBody = new FormBody.Builder()
                .add("utf8", "✓")
                .add("authenticity_token", authenticity_token)
                .add("password", psw)
                //.add("remember_me", "true")
                .add("login", email)
                .build();

        Request request = new Request.Builder().post(formBody).url("https://github.com/session").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                FileTool.writeFile("error.txt", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("google_lenve_fb", "onResponse: " + response.body().string().toString());
                //msg=response.body().string();
                FileTool.writeFile("msg.txt", response.body().string());
            }
        });

    }
}
