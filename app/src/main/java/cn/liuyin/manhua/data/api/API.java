package cn.liuyin.manhua.data.api;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.data.bean.ChaptersBean;
import cn.liuyin.manhua.data.bean.SearchBean;
import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.BookMaker;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.tool.FileTool;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class API {

    //OkHttpClient client;
    //private static final String host = "http://client.api.nrqu.net";
    public static String[] BASE_URLS = {
            "http://client.api.fffapi.com",
            "http://client.api.fffimage.com",
            "http://client.api.iixiaoming.com",
            "http://client.api.pocomic.com"};
    private static final String host = "http://client.api.fffimage.com";


    private API() {
    }


//    public static void CheckAppVersion() {
//        FormBody.Builder buider = new FormBody.Builder();
//        FormBody formBody = APIheper.getFormBuider(buider).build();//
//        String url = host + "/api/Sys/CheckAppVersion";
//
//        Request request = new Request.Builder().post(formBody).url(url).build();
//        try {
//            Response response = APP.getOkhttpClient().newCall(request).execute();
//            if (response.isSuccessful()) {
//
//                String version = new JSONObject(Objects.requireNonNull(response.body()).string()).getString("version");
//                System.err.println("APP_VerSion:" + Objects.requireNonNull(response.body()).string());
//                APP.getContext().getSharedPreferences("api", Context.MODE_PRIVATE).edit().putString("version", "3.1.5").apply();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String getHost(){

        Random rand = new Random();

        int randNumber =rand.nextInt(3 - 0 + 1) + 0;
        return BASE_URLS[randNumber];
    }

    /**
     * 获取分类
     *
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    public static String getRanking(String type, int page, int pageSize) {

        //rankType=newOnline&page=1
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("rankType", type);
        buider.add("page", page + "");
        buider.add("pageSize", pageSize + "");
        FormBody formBody = buider.build(); //APIheper.getFormBuider(buider).build();//
        String url = getHost() + "/api/book/ranking";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String json = new String(Objects.requireNonNull(response.body()).bytes(), StandardCharsets.UTF_8);
                FileTool.writeFile("debug.txt", json);
                System.err.println(json);
                return new JSONObject(Objects.requireNonNull(json)).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }

    public static String getCheckBook(BookShelf bookShelf) {
        //&pageSize=20&keyword
        StringBuilder requests = new StringBuilder();
        for (Book b : bookShelf.books) {
            ChaptersBean cs = BookMaker.getCatcheList(b);
            requests.append(b.bookid).append(";").append(Objects.requireNonNull(cs).data.list.get(0).cid).append(";").append(b.lastRead).append(",");
        }
        requests = new StringBuilder(requests.substring(0, requests.lastIndexOf(",")));
        System.err.println("APP_VerSion" + requests);
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("requests", requests.toString());
        FormBody formBody = buider.build();// APIheper.getFormBuider(buider).build();//
        String url = getHost() + "/api/book/read-history";


        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).getJSONObject("data").toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }


    public static String getCategory() {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//
        String url = getHost() + "/api/book/category";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }


    public static String getCateDetail(int class_id, int page, int pageSize) {
        //POST /api/book/cate-detail HTTP/1.1
        //&pageSize=20&sortType=popu&page=1&classId=638
        //sortType popu,update
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("classId", class_id + "");
        buider.add("sortType", "popu");
        buider.add("page", page + "");
        buider.add("pageSize", pageSize + "");

        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//
        String url = getHost() + "/api/book/cate-detail";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }


    public static String getRecom() {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//
        String url = getHost() + "/api/recom/index";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }


    ///api/book/contents
    public static String getContents(String bid, String cid) {
        //&bid=320&sortType=ASC
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("cid", cid).add("bid", bid);
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//
        String url = getHost() + "/api/book/contents";


        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getCachehttpClient().newCall(request).execute();
            if (response.isSuccessful()) {

                String json = new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
                System.out.println(json);
                return json;
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }


    }


    public static String getChapters(String bookid) {
        //&bid=320&sortType=ASC
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("sortType", "ASC").add("bid", bookid);
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url =getHost() + "/api/book/chapters";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }

    /**
     * 搜索漫画
     *
     * @param kw       搜索的关键字
     * @param page     搜索的页码
     * @param pageSize 页面尺寸
     * @return 返回API数据字符串
     */
    public static String search_1(String kw, int page, int pageSize) {
        //POST /api/book/search HTTP/1.1


        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("keyword", kw);
        buider.add("page", page + "");
        buider.add("pageSize", pageSize + "");
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//

        String url = getHost()+ "/api/book/search";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }

    public static String search(String bookid, int page) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("bid", bookid);
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = getHost() + "/api/book/cartoon-info";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }

    public static String getBookInfo(String bookid) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("bid", bookid);
        FormBody formBody = buider.build();// APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url =getHost() + "/api/book/cartoon-info";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }

    public static SearchBean search(String kw, int page, int pagesize) {
        Gson gson = new Gson();
        SearchBean result = new SearchBean();
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("pageSize", pagesize + "").add("keyword", kw).add("page", page + "");
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();
        String url = getHost()+ "/api/book/search";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                result = gson.fromJson(Objects.requireNonNull(response.body()).string(), SearchBean.class);
                return result;
            } else {
                result.code = 1;
                result.success = false;
                result.message = "nessage:" + response.message() + " \ncode:" + response.code();
                return result;
                //return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.code = 1;
            result.success = false;
            result.message = e.getLocalizedMessage();
            return result;
        }

    }

    public static String search(String kw) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("pageSize", "20").add("keyword", kw).add("page", "1");
        FormBody formBody = buider.build();//APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = getHost()  + "/api/book/search";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }

}
