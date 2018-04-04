package cn.liuyin.manhua.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.data.bean.ContentBean;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.NetworkUtil;
import okhttp3.Response;

/**
 * Created by asus on 2018/4/4.
 */

public class DownloadImgRunnable implements Runnable {
    ContentBean mData;
    int mIndex;

    public DownloadImgRunnable(ContentBean data, int index) {
        this.mData = data;
        this.mIndex = index;
    }

    @Override
    public void run() {
        // TODO: Implement this method
        doGg(mData, mIndex);
    }

    public void doGg(ContentBean data, int index) {
        File f = new File(FileTool.BASEPATH + "/img", data.data.bookTitle);
        if (!f.exists()) {
            f.mkdirs();
        }
        for (ContentBean.Data.Contents dl : data.data.contents) {
            //dl.img;
            try {
                String filename = data.data.bookTitle + "/[" + data.data.bookTitle + "][c" + index + "][p" + dl.order + "].jpg";
                File file = new File(FileTool.BASEPATH + "/img/" + filename);
                if (file.exists()) {

                } else {
                    download(dl.img, filename);
                }

            } catch (Exception e) {
            }

        }
    }

    public void download(String url, String name) {

        okhttp3.Request request = new okhttp3.Request.Builder().get().url(url).header("Referer", "http://ww.pufei.net/").build();
        try {
            if (NetworkUtil.getNetWorkStates(APP.getContext()) == NetworkUtil.TYPE_WIFI) {
                Response response = APP.getCachehttpClient().newCall(request).execute();
                if (response.isSuccessful()) {
                    //Bitmap bitmap = BitmapFactory.decodeByteArray(response.body().bytes(), 0, response.body().bytes().length);
                    writeImageToDisk(response.body().bytes(), name);
                } else {

                }
            }
        } catch (IOException e) {

        }
    }

    /**
     * 将图片写入到磁盘
     *
     * @param img      图片数据流
     * @param fileName 文件保存时的名称
     */
    public static void writeImageToDisk(byte[] img, String fileName) {
        try {
            File file = new File(FileTool.BASEPATH + "/img/" + fileName);
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

