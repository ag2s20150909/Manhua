package cn.liuyin.manhua.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.adapter.HomeAdapter;
import cn.liuyin.manhua.adapter.SearchAdapter;
import cn.liuyin.manhua.data.api.API;
import cn.liuyin.manhua.data.bean.ClassListBean;
import cn.liuyin.manhua.data.bean.RankingBean;
import cn.liuyin.manhua.data.bean.SearchResult;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.tool.FileTool;

public class HomeActivity extends BaseActivity {


    Gson gson;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        gson = new Gson();
        super.onCreate(savedInstanceState);
        LinearLayout l = new LinearLayout(this);
        //l.setBackgroundColor(Color.WHITE);
        l.setOrientation(LinearLayout.VERTICAL);
        HorizontalScrollView hs = new HorizontalScrollView(this);
        LinearLayout lz = new LinearLayout(this);
        lv = new ListView(this);
        try {

            HomeAdapter ui = new HomeAdapter(lz, this);
            ui.Build();
        } catch (Exception e) {
            FileTool.writeError(e);
        }

        hs.addView(lz);
        l.addView(hs);
        l.addView(lv);

        setContentView(l);
        getNew();

    }


    @Override
    protected void onStart() {

        super.onStart();

    }

    public void test() {
        new Thread(new Runnable() {

            @Override
            public void run() {


            }
        }).start();
    }

    public void getNew() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                API api = new API();

                try {

                    SearchResult d = new SearchResult();
                    RankingBean data = gson.fromJson(api.getRanking("newOnline", 1, 100), RankingBean.class);
                    d.add(data);
                    mHander.obtainMessage(1, d).sendToTarget();

                } catch (Exception e) {
                    FileTool.writeError(e);
                    mHander.obtainMessage(0, e.getLocalizedMessage()).sendToTarget();
                }

            }
        }).start();
    }

    public void getType(final int classId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                API api = new API();

                try {

                    SearchResult d = new SearchResult();
                    ClassListBean data = gson.fromJson(api.getCateDetail(classId, 1, 100), ClassListBean.class);
                    d.add(data);
                    mHander.obtainMessage(1, d).sendToTarget();

                } catch (Exception e) {
                    FileTool.writeError(e);
                    mHander.obtainMessage(0, e.getLocalizedMessage()).sendToTarget();
                }

            }
        }).start();
    }


    private void showList(final SearchResult data) {

        SearchAdapter adapter = new SearchAdapter(this, data);

        lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));
        lv.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "共有" + (data.results.size()) + "条结果", Toast.LENGTH_LONG).show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {


                try {
                    BookShelf.addBook(data.results.get(p3));
                    Intent i = new Intent(getApplicationContext(), ListActivity.class);
                    i.putExtra("url", data.results.get(p3).link);
                    i.putExtra("book", data.results.get(p3));
                    //Toast.makeText(getApplicationContext(), data.get(p3).get("link"), 1).show();
                    startActivity(i);
                } catch (Exception e) {
                    mHander.obtainMessage(0, e.getMessage()).sendToTarget();
                }
            }
        });


    }


    private Handler mHander = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message p1) {
            switch (p1.what) {
                case 0:
                    String msg = (String) p1.obj;
                    //tv.setText(msg);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    SearchResult ss = (SearchResult) p1.obj;
                    showList(ss);
                    break;
                case 2:
                    //ss=(String) p1.obj;
                    //Toast.makeText(MainActivity.this,ss,1).show();
                    break;

            }
            return false;
        }
    });


}
