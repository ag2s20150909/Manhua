package cn.liuyin.manhua.data.api;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Objects;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.data.bean.SearchBean;
import cn.liuyin.manhua.tool.FileTool;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class API {

    //OkHttpClient client;
    private static final String host = "http://client.pf.iymbl.com";


    private API() {
    }


    //点赞
//    public static String dianzan(int score) {
//
//        FormBody.Builder buider = new FormBody.Builder();
//        buider.add("score", score + "");
//
//        FormBody formBody = buider.build();
//        String url = "http://dianzan.myqcloud.com" + "/rank.php";
//
//        Request request = new Request.Builder().post(formBody).url(url).build();
//        try {
//            Response response = APP.getOkhttpClient().newCall(request).execute();
//            if (response.isSuccessful()) {
//                return new JSONObject(response.body().string()).toString(4);
//            } else {
//                return "error:" + response.message() + " errorcode:" + response.code();
//            }
//        } catch (Exception e) {
//            return "error:" + e.getMessage();
//        }
//
//    }


    public static String getRanking(String type, int page, int pageSize) {

        //rankType=newOnline&page=1
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("rankType", type);
        buider.add("page", page + "");
        buider.add("pageSize", pageSize + "");
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        String url = host + "/api/book/ranking";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }


    public static String getCategory() {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        String url = host + "/api/book/category";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
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

        FormBody formBody = APIheper.getFormBuider(buider).build();//
        String url = host + "/api/book/cate-detail";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }


    public static String getRecom() {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        String url = host + "/api/recom/index";

        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString(4);
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }


    ///api/book/contents
    public static String getContents(String bid, String cid) {
        //&bid=320&sortType=ASC
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("cid", cid).add("bid", bid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        String url = host + "/api/book/contents";


        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getCachehttpClient().newCall(request).execute();
            if (response.isSuccessful()) {

                String json = new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
                System.out.println(json);
                FileTool.writeFiles("chapter", bid + "_" + cid + ".json", json);
                return json;
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }


    }


    public static String getChapters(String bookid) {
        //&bid=320&sortType=ASC
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("sortType", "ASC").add("bid", bookid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/chapters";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

    public static String search_1(String kw, int page, int pageSize) {
        //POST /api/book/search HTTP/1.1


        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("keyword", kw);
        buider.add("page", page + "");
        buider.add("pageSize", pageSize + "");
        FormBody formBody = APIheper.getFormBuider(buider).build();//

        String url = host + "/api/book/search";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

    public static String search(String bookid, int page) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("bid", bookid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/cartoon-info";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

    public static String getBookInfo(String bookid) {
        //&pageSize=20&keyword
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("bid", bookid);
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/cartoon-info";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

    public static SearchBean search(String kw, int page, int pagesize) {
        Gson gson = new Gson();
        SearchBean result = new SearchBean();
        FormBody.Builder buider = new FormBody.Builder();
        buider.add("pageSize", pagesize + "").add("keyword", kw).add("page", page + "");
        FormBody formBody = APIheper.getFormBuider(buider).build();
        String url = host + "/api/book/search";
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
        FormBody formBody = APIheper.getFormBuider(buider).build();//
        //String url=host + "/api/recom/index";
        String url = host + "/api/book/search";
        Request request = new Request.Builder().post(formBody).url(url).build();
        try {
            Response response = APP.getOkhttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                return new JSONObject(Objects.requireNonNull(response.body()).string()).toString();
            } else {
                return "error:" + response.message() + " errorcode:" + response.code();
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }

    }

}