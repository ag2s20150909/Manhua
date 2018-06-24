package cn.liuyin.manhua.data.tool;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.liuyin.manhua.tool.FileTool;

public class BookShelf implements Serializable {
    public int sum;
    public ArrayList<Book> books;

    public BookShelf() {
        books = new ArrayList<>();
    }

    public static BookShelf sortBooks() {
        BookShelf shelf = getBookShelf();
        Comparator c = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                if (o1.count - o1.index > o2.count - o2.index)
                    return 1;
                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。
                    //		else return 0; //无效
                else return -1;
            }
        };
        Collections.sort(shelf.books, c);
        //shelf.books.sort(c);
        save(shelf);
        return shelf;
    }

    public static BookShelf sortBooksByUnRead() {
        BookShelf shelf = getBookShelf();
        Comparator c = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                if (o1.count - o1.index < o2.count - o2.index)
                    return 1;
                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。
                    //		else return 0; //无效
                else return -1;
            }
        };
        Collections.sort(shelf.books, c);
        //shelf.books.sort(c);
        save(shelf);
        return shelf;
    }

    public static BookShelf sortBooksByRead() {
        BookShelf shelf = getBookShelf();
        Comparator c = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                if (o1.lastRead < o2.lastRead)
                    return 1;
                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。
                    //		else return 0; //无效
                else return -1;
            }
        };
        Collections.sort(shelf.books, c);
        //shelf.books.sort(c);
        save(shelf);
        return shelf;
    }

    public static BookShelf sortBooksByUpdate() {
        BookShelf shelf = getBookShelf();
        Comparator c = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                if (o1.lastUpdateTime < o2.lastUpdateTime)
                    return 1;
                    //注意！！返回值必须是一对相反数，否则无效。jdk1.7以后就是这样。
                    //		else return 0; //无效
                else return -1;
            }
        };
        Collections.sort(shelf.books, c);
        save(shelf);
        return shelf;
    }


    public static BookShelf MoveTopByIndex(int index) {
        BookShelf shelf = getBookShelf();
        ArrayList<Book> copy = new ArrayList<>();
        for (int i = 0; i < shelf.books.size(); i++) {
            copy.add(i, shelf.books.get(i).clone());
        }
        shelf.books.set(0, copy.get(index));
        for (int i = 1; i <= index; i++) {
            shelf.books.set(i, copy.get(i - 1));
        }
        BookShelf.save(shelf);
        return shelf;
    }


    public static BookShelf MoveDownByIndex(int index) {

        BookShelf shelf = getBookShelf();
        ArrayList<Book> tbooks = new ArrayList<>(shelf.books);
        if (index < (shelf.books.size() - 1)) {
            tbooks.set(index + 1, shelf.books.get(index));
            tbooks.set(index, shelf.books.get(index + 1));
        }

        shelf.books = tbooks;
        //shelf.books.remove(index);
        BookShelf.save(shelf);
        return shelf;
    }

    public static BookShelf MoveUpByIndex(int index) {

        BookShelf shelf = getBookShelf();
        ArrayList<Book> tbooks = new ArrayList<>(shelf.books);
        if (index > 0) {
            tbooks.set(index - 1, shelf.books.get(index));
            tbooks.set(index, shelf.books.get(index - 1));

        }
        shelf.books = tbooks;
        //shelf.books.remove(index);
        BookShelf.save(shelf);
        return shelf;
    }

    public static BookShelf removeByIndex(int index) {
        Gson gson = new Gson();
        BookShelf shelf = getBookShelf();
        shelf.books.remove(index);
        BookShelf.save(shelf);
        return shelf;
    }

    public static Book getBookById(int bid) {
        BookShelf shelf = getBookShelf();
        for (Book book : shelf.books) {
            if (book.bookid == bid) {
                return book;
            }
        }
        return null;
    }


    public static BookShelf getBookShelf() {
        BookShelf shelf;
        Gson gson = new Gson();
        if (FileTool.has("config", "bookshelf.json")) {
            shelf = gson.fromJson(FileTool.readFile("config", "bookshelf.json"), BookShelf.class);
            shelf.sum = shelf.books.size();
            return shelf;
        } else {
            shelf = new BookShelf();
            FileTool.writeFiles("config", "bookshelf.json", gson.toJson(shelf, BookShelf.class));
            shelf.sum = shelf.books.size();
            return shelf;
        }
    }

    public static void updateBook(Book book) {
        //FileTool.writeError(System.currentTimeMillis()+"");
        book.lastRead = (int) (System.currentTimeMillis() / 1000);
        BookShelf shelf = getBookShelf();
        for (int i = 0; i < shelf.books.size(); i++) {
            Book b = shelf.books.get(i);
            if (b.bookid == book.bookid) {
                shelf.books.set(i, book);
                BookShelf.save(shelf);
                return;
            }
        }
        shelf.books.add(book);
        BookShelf.save(shelf);
        return;
    }

    public static void addBook(Book book) {
        //FileTool.writeError(System.currentTimeMillis()+"");
        book.lastRead = (int) (System.currentTimeMillis() / 1000);
        BookShelf shelf = getBookShelf();
        for (int i = 0; i < shelf.books.size(); i++) {
            Book b = shelf.books.get(i);
            if (b.bookid == book.bookid) {
                book.index = b.index;
                shelf.books.set(i, book);
                BookShelf.save(shelf);
                return;
            }
        }
        shelf.books.add(book);
        BookShelf.save(shelf);
        return;
    }

    public static void save(BookShelf shelf) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        if (shelf == null) {
            shelf = getBookShelf();
        }
        shelf.sum = shelf.books.size();
        FileTool.writeFiles("config", "bookshelf.json", gson.toJson(shelf, BookShelf.class));
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this, BookShelf.class);
    }


}
