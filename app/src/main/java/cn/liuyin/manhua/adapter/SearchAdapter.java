package cn.liuyin.manhua.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.bean.SearchResult;
import cn.liuyin.manhua.data.tool.Book;

public class SearchAdapter extends BaseAdapter {

    private SearchResult mData;
    private Context mContext;

    public SearchAdapter(Context context, SearchResult data) {
        this.mData = data;
        this.mContext = context;
    }


    @Override
    public int getCount() {

        return mData.results.size();
    }

    @Override
    public Object getItem(int p1) {

        return mData.results.get(p1);
    }

    @Override
    public long getItemId(int p1) {

        return p1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = mData.results.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.name);
            viewHolder.tv_author = convertView.findViewById(R.id.author);
            viewHolder.tv_new = convertView.findViewById(R.id.new_c);
            viewHolder.tv_time = convertView.findViewById(R.id.time);
            viewHolder.iv_img=convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_name.setText(book.name);
        viewHolder.tv_author.setText(book.author);
        viewHolder.tv_new.setText(book.newChapter);
        viewHolder.tv_time.setText(book.updateTime);
        Picasso.get().load(book.img).placeholder(R.drawable.ic_loading).into(viewHolder.iv_img);


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
        TextView tv_name;
        TextView tv_author;
        TextView tv_new;
        TextView tv_time;
        ImageView iv_img;


    }

}
