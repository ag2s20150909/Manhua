package cn.liuyin.manhua.data.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by asus on 2018/1/25.
 */

public class ContentBean implements Serializable {
    public Data data;
    public Boolean success;
    public String token;
    public String message;
    public int code;
    public String version;

    public static ContentBean fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ContentBean.class);
    }

    public ContentBean fix() {
        for (int i = 0; i < this.data.contents.size(); i++) {
            String tag1 = "https://manhua.qpic.cn";
            Data.Contents d = this.data.contents.get(i);
            if (d.img.contains(tag1)) {
                d.img = d.img.substring(d.img.indexOf(tag1));
                this.data.contents.set(i, d);
            }
        }
        return this;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, ContentBean.class);
    }

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
        public int isBuy;

        /**
         * Contents is the inner class of Data
         */
        public class Contents {
            public String img;
            public int width;
            public int height;
            public int order;
        }
    }

}
