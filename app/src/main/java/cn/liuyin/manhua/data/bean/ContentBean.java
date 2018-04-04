package cn.liuyin.manhua.data.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asus on 2018/1/25.
 */

public class ContentBean implements Serializable {
    public Data data;

    /**
     * Data is the inner class of ContentBean
     */
    public class Data {
        public int cid;
        public int bid;
        public String bookTitle;
        public int chapterPrice;
        public int vipMoney;
        public int giftMoney;
        public String title;
        public int isVip;
        public int imgTotal;
        public int preId;
        public int nextId;
        public int isAuto;
        public ArrayList<Contents> contents;

        /**
         * Contents is the inner class of Data
         */
        public class Contents {
            public String img;
            public int width;
            public int height;
            public int order;
        }

        public int isBuy;
    }

    public Boolean success;
    public String token;
    public String message;
    public int code;
    public String version;

    public static ContentBean fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ContentBean.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, ContentBean.class);
    }

}
