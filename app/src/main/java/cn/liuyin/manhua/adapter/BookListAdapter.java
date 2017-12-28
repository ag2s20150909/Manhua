package cn.liuyin.manhua.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.liuyin.manhua.R;

public class BookListAdapter extends BaseAdapter {
    Context mContext;
    int mIndex;
    ArrayList<HashMap<String, String>> mDatas;

    public BookListAdapter(Context context, ArrayList<HashMap<String, String>> datas, int index) {
        this.mContext = context;
        this.mDatas = datas;
        this.mIndex = index;

    }

    @Override
    public int getCount() {
        // TODO: Implement this method
        return this.mDatas.size();
    }

    @Override
    public Object getItem(int p1) {
        // TODO: Implement this method
        return mDatas.get(p1);
    }

    @Override
    public long getItemId(int p1) {
        // TODO: Implement this method
        return p1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> map = (HashMap<String, String>) getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_list_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_index = (TextView) convertView.findViewById(R.id.index);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_index.setText(map.get("index"));
        viewHolder.tv_name.setText(map.get("name"));
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

}
