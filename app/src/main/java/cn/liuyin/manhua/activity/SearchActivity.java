package cn.liuyin.manhua.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.adapter.SearchAdapter;
import cn.liuyin.manhua.data.api.API;
import cn.liuyin.manhua.data.bean.SearchBean;
import cn.liuyin.manhua.data.bean.SearchResult;
import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.tool.HttpTool;

public class SearchActivity extends BaseActivity {
    EditText et;
    Button btn;
    ListView lv;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams ep = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        ep.weight = 1;

        LinearLayout root = new LinearLayout(this);
        LinearLayout head = new LinearLayout(this);
        et = new EditText(this);
        et.setSingleLine(true);
        btn = new Button(this);
        lv = new ListView(this);
        root.setOrientation(LinearLayout.VERTICAL);
        head.setOrientation(LinearLayout.HORIZONTAL);
        head.setLayoutParams(hp);
        lv.setLayoutParams(hp);
        et.setLayoutParams(ep);
        btn.setText("搜索");
        root.addView(head);
        root.addView(lv);
        head.addView(et);
        head.addView(btn);
        setContentView(root);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {
                //Toast.makeText(getApplicationContext(), "88", 1).show();
                if (!et.getText().toString().isEmpty()) {
                    search(et.getText().toString());

                }
            }
        });


    }

    public void search(final String kw) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Gson gson = new Gson();
                SearchResult data = new SearchResult();
                SearchBean d = gson.fromJson(new API().search_1(kw, 1, 100), SearchBean.class);
                data.add(d);
                mHander.obtainMessage(1, data).sendToTarget();
                // String u = HttpTool.search(getApplicationContext(), kw);
                // getHtml(u);
            }
        }).start();
    }


//    public void search(final String kw) {
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//
//                String u = HttpTool.search(getApplicationContext(), kw);
//                getHtml(u);
//            }
//        }).start();
//    }

    public void getHtml(final String url) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    String d = HttpTool.httpGet(url);
                    if (d.startsWith("error:")) {
                        mHander.obtainMessage(0, d).sendToTarget();
                    } else {
                        Document doc = Jsoup.parse(d, url);
                        //FileTool.writeFile("doc.html",doc.html());
                        Elements lists = doc.select(".cont-list").select("li");
                        mHander.obtainMessage(0, "搜索到" + lists.size() + "条结果").sendToTarget();
                        SearchResult data = new SearchResult();
                        for (Element item : lists) {
                            Book temp = new Book();

                            temp.link = item.select("a").attr("abs:href") + "/";
                            temp.name = item.select("h3").text();
                            temp.img = item.select("img").attr("data-src");
                            temp.author = item.select("dd").get(0).text();
                            temp.type = item.select("dd").get(1).text();
                            temp.newChapter = item.select("dd").get(2).text();
                            temp.updateTime = item.select("dd").get(3).text();
                            data.add(temp);
                        }

                        //FileTool.writeFile("update.html",doc.toString());
                        mHander.obtainMessage(1, data).sendToTarget();
                    }
                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();
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

}
