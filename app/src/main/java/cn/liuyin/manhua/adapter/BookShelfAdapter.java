package cn.liuyin.manhua.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.data.tool.TimeTool;


public class BookShelfAdapter extends BaseAdapter {
    private Context mContext;

    private BookShelf mDatas;

    public BookShelfAdapter(Context context, BookShelf datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public void updateView(BookShelf data) {
        this.mDatas = data;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }

    @Override
    public int getCount() {

        return mDatas.books.size();
    }

    @Override
    public Object getItem(int p1) {
        return mDatas.books.get(p1);
    }

    @Override
    public long getItemId(int p1) {

        return p1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = mDatas.books.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_shelt_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_unread = convertView.findViewById(R.id.unread);
            viewHolder.tv_name = convertView.findViewById(R.id.name);
            viewHolder.tv_time = convertView.findViewById(R.id.time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        int unread = book.count - book.index;
        viewHolder.tv_unread.setText(String.valueOf(unread));
        viewHolder.tv_name.setText(book.name);
        viewHolder.tv_time.setText(TimeTool.getTimeString(book.lastUpdateTime));


        if (unread > 0) {
            viewHolder.tv_unread.setBackgroundResource(R.drawable.rand_background_accent);
        } else {
            viewHolder.tv_unread.setBackgroundResource(R.drawable.rand_background);
        }


        return convertView;

    }


    /**
     * 存储控件
     *
     * @author zhenyun
     */
    public class ViewHolder {
        TextView tv_unread;
        TextView tv_name;
        TextView tv_time;

    }


}
