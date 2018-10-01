package cn.liuyin.manhua.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.bean.ContentBean;
import cn.liuyin.manhua.service.DownloadImgRunnable;
import cn.liuyin.manhua.tool.ComonTool;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.ImgLoader;
import cn.liuyin.manhua.tool.ScreenUtil;
import cn.liuyin.manhua.tool.ShareUtils;
import cn.liuyin.view.DampingListView;


public class BookContentAdapter extends BaseAdapter {
    private DampingListView mLv;
    private Context mContext;
    private ContentBean mData;
    private int mIndex;
    ExecutorService mThreadPool;


    public BookContentAdapter(Context context, DampingListView lv) {
        this.mContext = context;
        this.mLv = lv;
        this.mThreadPool = Executors.newFixedThreadPool(3);

    }


    public void updateView(ContentBean data, int index) {
        this.mData = data;
        this.mIndex = index;
        mThreadPool.execute(new DownloadImgRunnable(data, index));
        this.notifyDataSetChanged();

    }


    @Override
    public int getCount() {

        return mData.data.contents.size();
    }

    @Override
    public Object getItem(int p1) {

        return mData.data.contents.get(p1);
    }

    @Override
    public long getItemId(int p1) {

        return p1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContentBean.Data.Contents data = mData.data.contents.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_content_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.iv = convertView.findViewById(R.id.content_item_img);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String filename = ComonTool.getFixedFileName(mData.data.bookTitle, mIndex, data.order);
        File file = new File(FileTool.BASEPATH + "/img", filename);
        if (file.exists()) {

            //Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //viewHolder.iv.setImageBitmap(bitmap);
            ImgLoader.getInstence().load(file, viewHolder.iv);
            //Picasso bug,不能读取本地图片 emmmmmmmmm.
            //Picasso.get().load(file).placeholder(R.drawable.ic_loading).error(R.mipmap.ic_launcher).noFade().into(viewHolder.iv);

        } else {
            Picasso.get().load(data.img).placeholder(R.drawable.ic_loading).noFade().into(viewHolder.iv);
        }

        return convertView;
    }


    /**
     * 存储控件
     *
     * @author zhenyun
     */
    public class ViewHolder {
        ImageView iv;
    }

    public void doOnLongClick(final int index) {

        final String[] items = new String[]{"保存到相册", "分享给朋友", "分享长图"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setIcon(R.drawable.ic_launcher).setTitle("对漫画的操作").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                switch (p2) {
                    case 0:
                        savaBitmap(index);
                        break;
                    case 1:
                        //saveLongImg(lv);
                        shareBitmap(index);
                        //shareImg();
                        break;
                    case 2:
                        saveList();
                        break;

                    default:
                        break;

                }


            }
        });

        builder.create().show();
    }

    private void savaBitmap(final int index) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    Bitmap bitmap = Picasso.get().load(mData.data.contents.get(index).img).get();
                    String fileName = mData.data.bookTitle + "_" + mData.data.title + "_" + (index + 1) + ".jpg";
                    FileTool.saveBitmap(mContext, fileName, bitmap);
                } catch (Exception e) {
                    FileTool.writeError(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void shareBitmap(final int index) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    Bitmap bitmap = Picasso.get().load(mData.data.contents.get(index).img).get();
                    String fileName = "share.jpg";
                    FileTool.saveBitmap(mContext, fileName, bitmap);
                    //shareImg();
                    String shareTitle = mData.data.bookTitle + "_" + mData.data.title + "[" + (index + 1) + "]" + "\n" + mData.data.contents.get(index).img;
                    ShareUtils.shareImageWithTitle(mContext, fileName, shareTitle);
                } catch (Exception e) {
                    FileTool.writeError(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void saveList() {
        //Toast.makeText(mContext,"生成长图中，请稍等一会。",Toast.LENGTH_LONG).show();
        Bitmap bitmap = ScreenUtil.shotListView(mLv);
        String fileName = "long.JPG";
        FileTool.saveBitmap(mContext, fileName, bitmap);
        String shareTitle = mData.data.bookTitle + "_" + mData.data.title;
        ShareUtils.shareImageWithTitle(mContext, fileName, shareTitle);
    }

    private void switchLight(int a) {

    }


}
