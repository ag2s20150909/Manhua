package cn.liuyin.manhua.data.api;


import android.content.Context;

import com.google.gson.Gson;


import org.json.JSONObject;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.data.bean.SearchBean;
import cn.liuyin.manhua.tool.FileTool;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class API {
    Context mContext;
    OkHttpClient client;
    private final String host = "http://client.pf.iymbl.com";


    public API() {
        this(APP.getContext());

    }

    public API(Context context) {
        this.mContext = context;
        client = new OkHttpClient.Builder()
                //.cookieJar(CookieManager.getInstance(context))
                .build();
        //checkUpdate();
    }


    ///api/book/contents
    public String getContents(String bid, String cid) {
        //&bid=320&sortType=ASC
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("cid", cid).add("bid", bid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        String url = host + "/api/book/contents";


        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {

                String json = new JSONObject(response.body().string()).toString();
                FileTool.writeFiles("chapter", bid + "_" + cid + ".json", json);
                return json;
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }


    }


    public String getChapters(String bookid) {
        //&bid=320&sortType=ASC
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("sortType", "ASC").add("bid", bookid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/chapters";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }


    public String search(String bookid, int page) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("bid", bookid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/cartoon-info";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

    public String getBookInfo(String bookid) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("bid", bookid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/cartoon-info";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

    public SearchBean search(String kw, int page, int pagesize) {
        Gson gson = new Gson();
        SearchBean result = new SearchBean();
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("pageSize", pagesize + "").add("keyword", kw).add("page", page + "");
        FormBody formBody = APIheper.getFormBuider(buider).build();
        String url = host + "/api/book/search";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = gson.fromJson(response.body().string(), SearchBean.class);
                return result;
            } else {
                result.code = 1;
                result.success = false;
                result.message = "nessage:" + response.message() + " \ncode:" + response.code();
                return result;
                //return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            result.code = 1;
            result.success = false;
            result.message = e.getLocalizedMessage();
            return result;
        }

    }

    public String search(String kw) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("pageSize", "20").add("keyword", kw).add("page", "1");
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/search";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

}