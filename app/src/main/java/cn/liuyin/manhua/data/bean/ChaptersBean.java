package cn.liuyin.manhua.data.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by asus on 2018/1/25.
 */

public class ChaptersBean implements Serializable {
    public Data data;

    /**
     * Data is the inner class of ChaptersBean
     */
    public class Data implements Serializable {
        public int count;
        int now;
        int unread;
        public int lastUpdateTime;
        public String sortType;
        public String state;
        public ArrayList<List> list;

        /**
         * List is the inner class of Data
         */
        public class List implements Serializable {
            public int index = 0;
            public int cid;
            public int bid;
            public String title;
            public boolean isSaved = false;
            public int isVip;
            public int preId;
            public int nextId;
        }

    }

    public Boolean success;
    public String token;
    public String message;
    public int code;
    public String version;


    public static ChaptersBean fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ChaptersBean.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, ChaptersBean.class);
    }
}
