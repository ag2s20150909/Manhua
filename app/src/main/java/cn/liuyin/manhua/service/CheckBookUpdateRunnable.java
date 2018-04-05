package cn.liuyin.manhua.service;

import android.content.Context;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.data.tool.BookMaker;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.NetworkUtil;

/**
 * Created by asus on 2018/4/4.
 */

public class CheckBookUpdateRunnable implements Runnable {
    private MListener listener;
    private Context mContext;
    private BookShelf bookshelf;

    public CheckBookUpdateRunnable(Context context) {
        this.mContext = context;

        bookshelf = BookShelf.getBookShelf();
    }

    public void setListener(MListener l) {
        this.listener = l;
    }

    @Override
    public void run() {
        if (NetworkUtil.getNetWorkStates(APP.getContext()) != NetworkUtil.TYPE_NONE) {
            if (listener != null) {
                if (NetworkUtil.getNetWorkStates(APP.getContext()) == NetworkUtil.TYPE_MOBILE) {
                    listener.err("正在使用流量请注意");
                }

            }

            doUpdate();
        } else {
            if (listener != null) {
                listener.err("检查更新失败，没有网络连接。");
            }
        }

    }

    private void doUpdate() {
        try {
            if (listener != null) {
                listener.onStart();
            }

            for (int i = 0; i < bookshelf.books.size(); i++) {
                try {
                    BookMaker.getList(mContext, bookshelf.books.get(i));

                    bookshelf = BookShelf.getBookShelf();
                    if (listener != null) {
                        listener.doUpdate(bookshelf);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        if (NetworkUtil.getNetWorkStates(APP.getContext()) == NetworkUtil.TYPE_NONE) {
                            listener.err("网络没有连接，请检查网络连接。");
                        } else {
                            listener.err("【" + bookshelf.books.get(i).name + "】\n" + FileTool.getStackTrace(e));
                        }

                        //listener.err("【"+bookshelf.books.get(i).name+"】可能被下架，请稍候。\n"+FileTool.getStackTrace(e));
                    }
                }

            }
            if (listener != null) {
                listener.onFinished();
            }


        } catch (Exception e) {
            if (listener != null) {
                FileTool.writeError(e);
                listener.err(FileTool.getStackTrace(e));
            }
            //mHander.obtainMessage(0, "error" + FileTool.getStackTrace(e)).sendToTarget();
        }
    }

    public interface MListener {
        void onStart();

        void doUpdate(BookShelf bookshelf);

        void err(String err);

        void onFinished();
    }

}

