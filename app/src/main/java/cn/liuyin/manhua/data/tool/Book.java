package cn.liuyin.manhua.data.tool;


import com.google.gson.Gson;

import java.io.Serializable;

public class Book implements Serializable {
    public String link;
    public String name;
    public String img;
    public String author;
    public String type;
    public String newChapter;
    public String updateTime;
    public int lastRead;
    public int lastUpdateTime;
    public int bookid;
    public int count = 1;
    public int index = 1;


    @Override
    public Book clone() {
        return fromJson(toJson());
    }


    public Book fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Book.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Book.class);
    }


}
