package cn.liuyin.manhua.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.api.API;
import cn.liuyin.manhua.data.bean.ChaptersBean;
import cn.liuyin.manhua.data.bean.ContentBean;
import cn.liuyin.manhua.tool.FileTool;

/**
 * Created by asus on 2018/4/4.
 */

public class DownloadRunnable implements Runnable {
    private ExecutorService fixedThreadPool;
    private int mId;
    private DownloadService mService;

    DownloadRunnable(DownloadService service, int id) {
        this.mId = id;
        this.mService = service;
        fixedThreadPool = Executors.newFixedThreadPool(1);
    }

    private static ContentBean getContent(int bid, int cid) {

        if (FileTool.has("chapter", bid + "_" + cid + ".json")) {
            String json = FileTool.readFile("chapter", bid + "_" + cid + ".json");
            return ContentBean.fromJson(json);
        } else {
            String json = API.getContents(bid + "", cid + "");
            if (json.startsWith("error:")) {
                ContentBean c = new ContentBean();
                c.code = 1;
                c.success = false;
                c.message = json;
                return c;

            }
            return ContentBean.fromJson(json);
        }
    }

    @Override
    public void run() {
        // TODO: Implement this method
        ChaptersBean d = ChaptersBean.fromJson(API.getChapters(mId + ""));
        downloadMh(d);
    }

    private void downloadMh(ChaptersBean data) {
        mService.builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentInfo("下载中...")
                .setContentTitle("正在下载");

        int i = 1;


        for (ChaptersBean.Data.List d : data.data.list) {
            ContentBean c = getContent(d.bid, d.cid);
            //doGg(c,i);
            fixedThreadPool.execute(new DownloadImgRunnable(c, i));
            i++;
            mService.notifyMsg(c.data.bookTitle, data.data.list.size(), i, d.bid);


        }
        //mService.notifyMsg(c.data.bookTitle,data.data.list.size(),i,d.bid);
    }


}

