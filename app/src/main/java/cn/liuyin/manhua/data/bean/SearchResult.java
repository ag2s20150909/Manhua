package cn.liuyin.manhua.data.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.TimeTool;

public class SearchResult implements Serializable {
    public boolean success = true;
    public String message = "ok";
    public ArrayList<Book> results;

    public SearchResult() {
        results = new ArrayList<>();
    }

    public void add(Book book) {

        if (book.bookid == 0) {
            String[] aa = book.link.split("/");
            if (!book.link.endsWith("/")) {
                book.link += "/";
            }
            book.bookid = Integer.parseInt(aa[aa.length - 1]);
        }
        results.add(book);
    }

    //ClassListBean
    public void add(ClassListBean d) {
        ClassListBean.Data items = d.data;
        for (ClassListBean.Data.Items it : items.items) {
            Book b = new Book();

            b.updateTime = TimeTool.getTimeString(it.lastUpdateTime);
            b.name = it.bookTitle;
            b.author = it.author;
            b.bookid = it.bid;
            b.img = it.cover;
            b.newChapter = it.lastUpdateTitle;
            b.lastUpdateTime = it.lastUpdateTime;
            b.type = it.keywords;
            this.add(b);
        }
    }

    public void add(SearchBean d) {
        List<SearchBean.Data.Result> items = d.data.result;
        for (SearchBean.Data.Result it : items) {
            Book b = new Book();
            b.updateTime = TimeTool.getTimeString(it.lastUpdateTime);
            b.name = it.bookTitle;
            b.author = it.author;
            b.bookid = it.bid;
            b.img = it.cover;
            b.newChapter = it.lastUpdateTitle;
            b.lastUpdateTime = it.lastUpdateTime;
            b.type = it.tclass;
            this.add(b);
        }
    }

    public void add(RankingBean d) {
        RankingBean.Data items = d.data;
        for (RankingBean.Data.Items it : items.items) {
            Book b = new Book();

            b.updateTime = TimeTool.getTimeString(it.lastUpdateTime);
            b.name = it.bookTitle;
            b.author = it.author;
            b.bookid = it.bid;
            b.img = it.cover;
            b.newChapter = it.lastUpdateTitle;
            b.lastUpdateTime = it.lastUpdateTime;
            b.type = it.keywords;
            this.add(b);
        }
    }

}
