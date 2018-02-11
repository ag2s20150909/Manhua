package cn.liuyin.manhua.data.bean;


import java.io.Serializable;
import java.util.ArrayList;

import cn.liuyin.manhua.data.tool.Book;

public class SearchResult implements Serializable {
    public ArrayList<Book> results;

    public SearchResult() {
        results = new ArrayList<>();
    }

    public void add(Book book) {
        String[] aa = book.link.split("/");
        if (!book.link.endsWith("/")) {
            book.link += "/";
        }
        book.bookid = Integer.parseInt(aa[aa.length - 1]);
        results.add(book);
    }
}
