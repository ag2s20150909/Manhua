package cn.liuyin.manhua.tool;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;


import cn.liuyin.manhua.APP;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpTool {
    Context mContext;
    String UA = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36";
    String authenticity_token;
    String msg;
    OkHttpClient client;
    public HttpTool(){
        this(APP.getContext());
    }
    public HttpTool(Context context) {
        this.mContext = context;
        client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance(context))
                .build();
    }

    public static String httpGet(String url){
        String ip="";
        int port=0;
        try {
            JSONArray ips=new JSONArray(FileTool.readFile("","ips.json"));

            int s=0;
            ip=ips.getJSONObject(s).getString("ip");
            port=Integer.parseInt(ips.getJSONObject(s).getString("port"));
        }
        catch (Exception e){
            e.printStackTrace();

        }
        System.err.println(ip+":"+port);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance(APP.getContext()))
                //.proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip,port)))
                .build();
        Request request = new Request.Builder().get().url(url)
                .header("Referer", "m.pufei.net")
                .header("User-Agent",new UserAgent().getUA())
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new String(response.body().bytes(),"gb2312");
            } else {
                return "error:"+response.message()+" errorcode:"+response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }


    public static void getIPs(){
        try {
            Document doc=Jsoup.connect("http://www.kuaidaili.com/free/intr/").get();
            Elements list=doc.select("#list").select("tr");
            JSONArray ips=new JSONArray();
            for(int i=0;i<list.size();i++){
                JSONObject dd=new JSONObject();
                dd.put("ip",list.get(i).select("td").get(0).text());
                dd.put("port",Integer.parseInt(list.get(i).select("td").get(1).text()));
                ips.put(dd);
            }
            FileTool.writeFile("ips.json",ips.toString(4));
        }
        catch (Exception e){

        }
    }





    public static String search(Context context, String kw) {
        try {
            kw = URLEncoder.encode(kw, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(CookieManager.getInstance(context))
                //.proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip,port)))
                .build();

        Request request = new Request.Builder().get().url("http://m.pufei.net/e/search/?searchget=1&tbname=mh&show=title,player,playadmin,bieming,pinyin&tempid=4&keyboard=" + kw).header("Referer", "http://ww.pufei.net/").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.networkResponse().request().url().toString();
            } else {
                return "error:"+response.message()+" errorcode:"+response.code();
            }
        } catch (IOException e) {
            return "error:" + e.getMessage();
        }


    }









}
