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
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.bean.ContentBean;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.ShareUtils;


public class BookContentAdapter extends BaseAdapter {
    private Context mContext;
    private ContentBean mData;


    public BookContentAdapter(Context context, ContentBean data) {
        this.mData = data;
        this.mContext = context;
    }

    public void updateView(ContentBean data) {
        this.mData = data;
        this.notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        // TODO: Implement this method
        return mData.data.contents.size();
    }

    @Override
    public Object getItem(int p1) {
        // TODO: Implement this method
        return mData.data.contents.get(p1);
    }

    @Override
    public long getItemId(int p1) {
        // TODO: Implement this method
        return p1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContentBean.Data.Contents data = mData.data.contents.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_content_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.iv = convertView.findViewById(R.id.content_item_img);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();


        }

        Picasso.get().load(data.img).placeholder(R.drawable.ic_loading).noFade().into(viewHolder.iv);
        return convertView;
    }


    /**
     * 存储控件
     *
     * @author zhenyun
     */
    public class ViewHolder {
        public ImageView iv;
    }

    public void doOnLongClick(final ListView lv, final int index) {

        final String[] items = new String[]{"保存到相册", "分享给朋友", "保存长图"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setIcon(R.mipmap.ic_launcher).setTitle("对漫画的操作").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                // TODO: Implement this method
                //Toast.makeText(getApplicationContext(), bookshelf.books.get(index).bookid + "", Toast.LENGTH_SHORT).show();
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
                        saveLongImage(lv);
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


    public void saveLongImage(ListView lv) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Bitmap bitmap;
                    synchronized (this) {
                        bitmap = Picasso.get().load(mData.data.contents.get(0).img).get();
                        for (int i = 1; i < mData.data.contents.size(); i++) {
                            Bitmap b = Picasso.get().load(mData.data.contents.get(i).img).get();
                            //bitmap = BitmapTool.addBitmap(bitmap, b);

                            //Bitmap bitmap=Picasso.get().load(mData.data.contents.get(i).img).get();
                            //bitmap=BitmapTool.addBitmap(bitmap,Picasso.get().load(mData.data.contents.get(i).img).networkPolicy(NetworkPolicy.OFFLINE).get());
                            //FileTool.saveBitmap(mContext,i+".jpg",bitmap);


                        }
                    }


                    FileTool.saveBitmap(mContext, "long.jpg", bitmap);
                } catch (Exception e) {

                    FileTool.writeError(e.getLocalizedMessage());
                }

            }
        }).start();
    }


}
