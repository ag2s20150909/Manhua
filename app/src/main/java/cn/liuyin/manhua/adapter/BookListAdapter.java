package cn.liuyin.manhua.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.bean.ChaptersBean;


public class BookListAdapter extends BaseAdapter {
    Context mContext;
    int mIndex;
    ChaptersBean mDatas;

    public BookListAdapter(Context context, ChaptersBean datas, int index) {
        this.mContext = context;
        this.mDatas = datas;
        this.mIndex = index;

    }

    public void updateView(ChaptersBean data, int index) {
        this.mDatas = data;
        this.mIndex = index;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO: Implement this method
        return this.mDatas.data.list.size();
    }

    @Override
    public Object getItem(int p1) {
        // TODO: Implement this method
        return mDatas.data.list.get(p1);
    }

    @Override
    public long getItemId(int p1) {
        // TODO: Implement this method
        return p1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChaptersBean.Data.List data = mDatas.data.list.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_index = convertView.findViewById(R.id.index);
            viewHolder.tv_name = convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_index.setText((position + 1) + "");
        viewHolder.tv_name.setText(data.title);
        if (position + 1 == mIndex) {
            viewHolder.tv_index.setBackgroundResource(R.drawable.rand_background_accent);
        } else {
            viewHolder.tv_index.setBackgroundResource(R.drawable.rand_background);
        }


        return convertView;

    }


    /**
     * 存储控件
     *
     * @author zhenyun
     */
    public class ViewHolder {

        public TextView tv_index;
        public TextView tv_name;


    }


    public void doOnLongClick(final int index) {

        final String[] items = new String[]{"置顶", "上移", "下移", "删除", "更新"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        // 设置参数
        builder.setIcon(R.mipmap.ic_launcher).setTitle("对漫画的操作").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                // TODO: Implement this method
                //Toast.makeText(getApplicationContext(), bookshelf.books.get(index).bookid + "", Toast.LENGTH_SHORT).show();
                switch (p2) {

                }


            }
        });

        builder.create().show();
    }

}
