package cn.liuyin.manhua.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.bean.SearchReslt;
import cn.liuyin.manhua.data.tool.Book;

public class SearchAdapter extends BaseAdapter {

    SearchReslt mData;
    Context mContext;

    public SearchAdapter(Context context, SearchReslt data) {
        this.mData = data;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        // TODO: Implement this method
        return mData.results.size();
    }

    @Override
    public Object getItem(int p1) {
        // TODO: Implement this method
        return mData.results.get(p1);
    }

    @Override
    public long getItemId(int p1) {
        // TODO: Implement this method
        return p1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = mData.results.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.name);
            viewHolder.tv_author = convertView.findViewById(R.id.author);
            viewHolder.tv_new = convertView.findViewById(R.id.new_c);
            viewHolder.tv_time = convertView.findViewById(R.id.time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_name.setText(book.name);
        viewHolder.tv_author.setText(book.author);
        viewHolder.tv_new.setText(book.newChapter);
        viewHolder.tv_time.setText(book.updateTime);


        return convertView;

    }

//	new String[]{"author", "name", "new", "time"},
//	new int[]{R.id.author, R.id.name, R.id.new_c, R.id.time});


    /**
     * 存储控件
     *
     * @author zhenyun
     */
    public class ViewHolder {
        public TextView tv_name;
        public TextView tv_author;
        public TextView tv_new;
        public TextView tv_time;


    }

}
