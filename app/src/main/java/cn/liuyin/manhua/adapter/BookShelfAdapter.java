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

public class BookShelfAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<HashMap<String, String>> mDatas;

    public BookShelfAdapter(Context context, ArrayList<HashMap<String, String>> datas) {
        this.mContext = context;
        this.mDatas = datas;
        //this.mIndex=index;
    }

    @Override
    public int getCount() {
        // TODO: Implement this method
        return mDatas.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.book_shelt_item, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_unread = (TextView) convertView.findViewById(R.id.unread);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.tv_unread.setText(map.get("unread"));
        viewHolder.tv_name.setText(map.get("name"));
        int mIndex = Integer.parseInt(map.get("unread"));
        if (mIndex > 0) {
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
