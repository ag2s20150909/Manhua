package cn.liuyin.manhua.data.bean;

import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by asus on 2018/1/25.
 */

public class SearchBean {
    public Data data;

    /**
     * Data is the inner class of JavaBean
     */
    public class Data {
        public int totalPage;
        public int count;
        public ArrayList<Result> result;

        /**
         * Result is the inner class of Data
         */
        public class Result {
            public int bid;
            public int tclassId;
            public String tclass;
            public String bookTitle;
            public String author;
            public String state;
            public String cover;
            public String lastUpdateTitle;
            public int lastUpdateTime;
            public String introduction;
            public String keywords;
            public int hits;
            public int collectState;
        }

    }

    public boolean success;
    public String token;
    public String message;
    public int code;
    public String version;

    public SearchBean fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SearchBean.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, SearchBean.class);
    }
}