package cn.liuyin.manhua.service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.data.bean.ContentBean;
import cn.liuyin.manhua.tool.ComonTool;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.NetworkUtil;
import okhttp3.Response;

import static cn.liuyin.manhua.tool.FileTool.writeImageToDisk;

/**
 * Created by asus on 2018/4/4.
 */

public class DownloadImgRunnable implements Runnable {
    private ContentBean mData;
    private int mIndex;

    public DownloadImgRunnable(ContentBean data, int index) {
        this.mData = data;
        this.mIndex = index;
    }

    @Override
    public void run() {
        // TODO: Implement this method
        if (mData.code == 0) {
            doGg(mData, mIndex);
        }

    }

    private void doGg(ContentBean data, int index) {
        File f = new File(FileTool.BASEPATH + "/img", data.data.bookTitle);
        if (!f.exists()) {
            f.mkdirs();
        }

        for (ContentBean.Data.Contents dl : data.data.contents) {
            try {
                String filename = ComonTool.getFixedFileName(data.data.bookTitle, index, dl.order);
                File file = new File(FileTool.BASEPATH + "/img/" + filename);
                if (!file.exists()) {
                    download(dl.img, filename);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void download(String url, String name) {

        okhttp3.Request request = new okhttp3.Request.Builder().get().url(url).header("Referer", "http://www.pufei.net/").build();
        try {
            if (NetworkUtil.getNetWorkStates(APP.getContext()) == NetworkUtil.TYPE_WIFI) {
                Response response = APP.getCachehttpClient().newCall(request).execute();
                if (response.isSuccessful()) {
                    writeImageToDisk(Objects.requireNonNull(response.body()).bytes(), name);
                }
            }
        } catch (IOException ignored) {

        }
    }


}

