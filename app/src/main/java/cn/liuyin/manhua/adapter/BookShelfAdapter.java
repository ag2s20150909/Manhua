package cn.liuyin.manhua.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.BookShelf;


public class BookShelfAdapter extends BaseAdapter {
    Context mContext;

    BookShelf mDatas;

    public BookShelfAdapter(Context context, BookShelf datas) {
        this.mContext = context;
        this.mDatas = datas;
        //this.mIndex=index;
    }

    public void updateView(BookShelf data) {
        this.mDatas = data;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }

    @Override
    public int getCount() {
        // TODO: Implement this method
        return mDatas.books.size();
    }

    @Override
    public Object getItem(int p1) {
        // TODO: Implement this method
        return mDatas.books.get(p1);
    }

    @Override
    public long getItemId(int p1) {
        // TODO: Implement this method
        return p1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = mDatas.books.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_shelt_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_unread = convertView.findViewById(R.id.unread);
            viewHolder.tv_name = convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        int unread = book.count - book.index;
        viewHolder.tv_unread.setText(unread + "");
        viewHolder.tv_name.setText(book.name);

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

        public TextView tv_unread;
        public TextView tv_name;


    }


}
