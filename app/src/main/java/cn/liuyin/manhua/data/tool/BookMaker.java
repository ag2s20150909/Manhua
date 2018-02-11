package cn.liuyin.manhua.data.tool;


import android.content.Context;

import com.google.gson.Gson;


import cn.liuyin.manhua.data.api.API;
import cn.liuyin.manhua.data.bean.ChaptersBean;
import cn.liuyin.manhua.data.bean.ContentBean;

import cn.liuyin.manhua.tool.FileTool;


public class BookMaker {

    public static ContentBean getContent(int bid, int cid) {

        Gson gson = new Gson();
        API api = new API();
        if (FileTool.has("chapter", bid + "_" + cid + ".json")) {
            String json = FileTool.readFile("chapter", bid + "_" + cid + ".json");
            return gson.fromJson(json, ContentBean.class);
        } else {
            String json = api.getContents(bid + "", cid + "");
            if (json.startsWith("error:")) {
                ContentBean c = new ContentBean();
                c.code = 1;
                c.success = false;
                c.message = json;
                return c;

            }
            return gson.fromJson(json, ContentBean.class);
        }
    }

    public static ChaptersBean getCatcheList(Book book) {
        Gson gson = new Gson();
        if (FileTool.has("bid", book.bookid + ".json")) {
            return gson.fromJson(FileTool.readFile("bid", book.bookid + ".json"), ChaptersBean.class);
        } else {
            return null;
        }
    }

    public static ChaptersBean getList(Context context, Book book) {
        API api = new API(context);
        Gson gson = new Gson();
        ChaptersBean data = gson.fromJson(api.getChapters(book.bookid + ""), ChaptersBean.class);
        book.count = data.data.list.size();
        book.lastUpdateTime = data.data.lastUpdateTime;
        BookShelf.addBook(book);
        for (int i = 0; i < data.data.list.size(); i++) {
            data.data.list.get(i).index = i + 1;
        }
        if (data.code == 0) {
            FileTool.writeFiles("bid", book.bookid + ".json", gson.toJson(data, ChaptersBean.class));
        }

        return data;


    }


}
