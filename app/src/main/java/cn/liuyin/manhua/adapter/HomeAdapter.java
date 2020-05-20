package cn.liuyin.manhua.adapter;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.LinkedList;

import cn.liuyin.manhua.activity.BookShelfActivity;
import cn.liuyin.manhua.activity.HomeActivity;
import cn.liuyin.manhua.activity.SearchActivity;
import cn.liuyin.manhua.data.bean.CategoryBean;
import cn.liuyin.manhua.tool.FileTool;

public class HomeAdapter {
    HomeActivity mActivity;
    LinkedList<Button> btns;
    ViewGroup mview;
    View.OnClickListener mlistener;

    public HomeAdapter(ViewGroup view, HomeActivity activity) {
        Gson gson = new Gson();
        CategoryBean data = gson.fromJson(FileTool.readAsset("Category.json"), CategoryBean.class);
        this.mview = view;
        this.mActivity = activity;
        this.mlistener = new MClick();
        btns = new LinkedList<>();
        addButton("书架", 0);
        addButton("搜索", 0);
        addButton("更新", 0);
        for (CategoryBean.Data.Items t : data.data.items) {
            addButton(t.className, t.classId);
        }

    }


    public void Build() {
        for (int i = 0; i < btns.size(); i++) {
            mview.addView(btns.get(i));
        }
    }

    public HomeAdapter addButton(String name, int classid) {
        Button btn = new Button(mActivity);
        btn.setText(name);
        btn.setId(btns.size());
        btn.setOnClickListener(mlistener);
        btn.setTag(classid);
        btns.add(btn);

        return HomeAdapter.this;
    }


    class MClick implements View.OnClickListener {

        @Override
        public void onClick(View p1) {
            switch (p1.getId()) {
                case 0:
                    mActivity.startActivity(new Intent(mActivity, BookShelfActivity.class));
                    break;
                case 1:
                    mActivity.startActivity(new Intent(mActivity, SearchActivity.class));
                    break;
                case 2:
                    mActivity.getNew();
                    break;
                default:
                    mActivity.getType((int) p1.getTag());
                    break;

            }


        }


    }
}

