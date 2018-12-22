package cn.liuyin.manhua.data.tool;


import android.content.Context;

import com.google.gson.Gson;


import java.io.File;

import cn.liuyin.manhua.data.api.API;
import cn.liuyin.manhua.data.bean.ChaptersBean;
import cn.liuyin.manhua.data.bean.CheckBookBean;
import cn.liuyin.manhua.data.bean.ContentBean;

import cn.liuyin.manhua.tool.FileTool;


public class BookMaker {

    public static CheckBookBean getCheckBook(BookShelf bookShelf) {
        Gson gson = new Gson();
        String json = API.getCheckBook(bookShelf);
        System.err.println("APP_VerSion" + json);
        CheckBookBean data = gson.fromJson(json, CheckBookBean.class);
        return data;
    }

    public static ContentBean getContent(int bid, int cid) {

        Gson gson = new Gson();
        if (FileTool.has("chapter", bid + "_" + cid + ".json")) {
            String json = FileTool.readFile("chapter", bid + "_" + cid + ".json");
            return gson.fromJson(json, ContentBean.class).fix();
        } else {
            String json = API.getContents(bid + "", cid + "");
            if (json.startsWith("error:")) {
                ContentBean c = new ContentBean();
                c.code = 1;
                c.success = false;
                c.message = json;
                return c;

            }
            ContentBean data = gson.fromJson(json, ContentBean.class);
            if (data.success && data.code == 0) {
                FileTool.writeFiles("chapter", bid + "_" + cid + ".json", json);
            }


            return data;
        }
    }

    public static ChaptersBean getCatcheList(Book book) {
        Gson gson = new Gson();
        if (FileTool.has("bid", book.bookid + ".json")) {
            ChaptersBean data = gson.fromJson(FileTool.readFile("bid", book.bookid + ".json"), ChaptersBean.class);
            for (int i = 0; i < data.data.list.size(); i++) {
                ChaptersBean.Data.List d = data.data.list.get(i);
                data.data.list.get(i).index = i + 1;
                data.data.list.get(i).isSaved = isChapterSaved(i + 1, d.bid, d.cid);
            }
            return data;
        } else {
            return null;
        }
    }

    public static ChaptersBean getList(Context context, Book book) {
        Gson gson = new Gson();
        ChaptersBean data = gson.fromJson(API.getChapters(book.bookid + ""), ChaptersBean.class);
        book.count = data.data.list.size();
        book.lastUpdateTime = data.data.lastUpdateTime;
        BookShelf.updateBook(book);
        for (int i = 0; i < data.data.list.size(); i++) {
            ChaptersBean.Data.List d = data.data.list.get(i);
            data.data.list.get(i).index = i + 1;
            data.data.list.get(i).isSaved = isChapterSaved(i + 1, d.bid, d.cid);
        }
        if (data.code == 0 && data.success) {
            FileTool.writeFiles("bid", book.bookid + ".json", gson.toJson(data, ChaptersBean.class));
        }

        return data;


    }


    private static boolean isChapterSaved(int index, int bid, int cid) {
        File f = new File(FileTool.BASEPATH + "/chapter", bid + "_" + cid + ".json");
        //            ContentBean d = getContent(bid, cid);
//            for (int i = 0; i < d.data.contents.size(); i++) {
//                String filename = ComonTool.getFixedFileName(d.data.bookTitle, index, d.data.contents.get(i).order);
//
//                File f1 = new File(FileTool.BASEPATH + "/img", filename);
//                if (!f1.exists()) {
//                    ///System.err.println(filename + ":false");
//                    return false;
//                }
//            }
//System.err.println(f.getName() + ":false");
        return f.exists();


    }


}
