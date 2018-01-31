package cn.liuyin.manhua.activity;



import android.app.Activity;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import cn.liuyin.manhua.R;

import cn.liuyin.manhua.adapter.SearchAdapter;
import cn.liuyin.manhua.data.bean.SearchReslt;
import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.HttpTool;
import cn.liuyin.manhua.tool.UItool;

public class HomeActivity extends BaseActivity {


    String cmd = "Du7JUm25x4";
    String update = "http://m.pufei.net/manhua/update.html";
    String paihang = "http://m.pufei.net/manhua/paihang.html";
    String rexue = "http://m.pufei.net/shaonianrexue/";
    String shaonvaiqing = "http://m.pufei.net/shaonvaiqing/";
    String wuxiagedou = "http://m.pufei.net/wuxiagedou/";
    String kehuan = "http://m.pufei.net/kehuan/";
    String jingjitiyu = "http://m.pufei.net/jingjitiyu/";
    String gaoxiaoxiju = "http://m.pufei.net/gaoxiaoxiju/";
    String danmeirensheng = "http://m.pufei.net/danmeirensheng/";
    String zhentantuili = "http://m.pufei.net/zhentantuili/";
    String kongbulingyi = "http://m.pufei.net/kongbulingyi/";
    String PClianzai = "http://www.pufei.net/manhua/lianzai.html";
    String PCupdate = "http://www.pufei.net/manhua/update.html";

    Gson gson;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        gson = new Gson();
        super.onCreate(savedInstanceState);
        LinearLayout l = new LinearLayout(this);
        //l.setBackgroundColor(Color.WHITE);
        l.setOrientation(LinearLayout.VERTICAL);
        HorizontalScrollView hs = new HorizontalScrollView(this);
        LinearLayout lz = new LinearLayout(this);
        lv = new ListView(this);
        UItool ui = new UItool(lz, new myonclick())
                .addButton("书架")
                .addButton("搜索")
                .addButton("今日更新")
                .addButton("漫画排行")
                .addButton("少年热血")
                .addButton("少女爱情")
                .addButton("武侠格斗")
                .addButton("科幻魔幻")
                .addButton("竞技体育")
                .addButton("搞笑喜剧")
                .addButton("耽美BL")
                .addButton("推理侦探")
                .addButton("恐怖灵异")
                .addButton("最近更新")
                .addButton("连载大全");

        ui.Build();

        hs.addView(lz);
        l.addView(hs);
        l.addView(lv);

        setContentView(l);
        getHtml(update);
        test();

    }


    @Override
    protected void onStart() {
        // TODO: Implement this method
        super.onStart();

        //BookShelf.sortByTime();
        //初始化书架


        //ComonTool.copy2System(this,cmd);
    }

    public void test() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                //BookShelf.sortByTime();
                // TODO: Implement this method
                //API api=new API(HomeActivity.this);

                //FileTool.writeFile("types.json",gson.toJson(api.search("女",1,50),SearchBean.class));
                //FileTool.writeFile("types.json",api.getTypes());
            }
        }).start();
    }


    public void getPcHtml(final String url) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                try {
                    //HttpTool.search1(getApplicationContext(), "狐");

                    String d= HttpTool.httpGet(url);
                    if (d.startsWith("error:")){
                        mHander.obtainMessage(0,d).sendToTarget();
                        return;
                    }
                    Document doc = Jsoup.parse(d,url);
                    doc.setBaseUri(update);
                    //allList
                    Elements lists;
                    if (url.endsWith("update.html")) {
                        lists = doc.select(".updateList").select("li");
                    } else {

                        lists = doc.select(".allList").select("li");
                    }
                    SearchReslt data = new SearchReslt();
                    for (Element item : lists) {
                        Book temp = new Book();
                        String nurl = item.select("a.red").attr("abs:href");
                        temp.link = nurl.substring(0, nurl.lastIndexOf("/") + 1);
                        temp.name = item.select("a.video").text();
                        temp.img = "";
                        temp.author = "";
                        temp.type = "";
                        temp.newChapter = item.select("a.red").text();
                        if (url.endsWith("update.html")) {
                            temp.updateTime = item.select("span.red").text();
                        } else {
                            temp.updateTime = "时间未知";
                        }
                        data.add(temp);
                    }


                    mHander.obtainMessage(1, data).sendToTarget();

                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();

                }
            }
        }).start();
    }


    public void getHtml(final String url) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                try {
                    //HttpTool.search1(getApplicationContext(), "狐");
                    HttpTool.httpGet("http:m.pufei.net");
                    String d= HttpTool.httpGet(url);
                    if (d.startsWith("error:")){
                        mHander.obtainMessage(0,d).sendToTarget();
                        return;
                    }
                    Document doc = Jsoup.parse(d,url);

                    Elements lists = doc.select(".cont-list").select("li");
                    SearchReslt data = new SearchReslt();
                    for (Element item : lists) {
                        Book temp = new Book();
                        temp.link = item.select("a").attr("abs:href");
                        temp.name = item.select("h3").text();
                        temp.img = item.select("img").attr("data-src");
                        temp.author = item.select("dd").get(0).text();
                        temp.type = item.select("dd").get(1).text();
                        temp.newChapter = item.select("dd").get(2).text();
                        temp.updateTime = item.select("dd").get(3).text();
                        data.add(temp);
                    }
                    FileTool.writeFile("mobile.html", gson.toJson(data, SearchReslt.class));

                    //FileTool.writeFile("update.html", doc.toString());
                    //FileTool.writeFile("pc.json", data.toString(4));
                    mHander.obtainMessage(1, data).sendToTarget();
                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();
                }
            }
        }).start();
    }


    private void showList(final SearchReslt data) {

        SearchAdapter adapter = new SearchAdapter(this, data);

        lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));
        lv.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "共有" + (data.results.size()) + "条结果", Toast.LENGTH_LONG).show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {

                // TODO: Implement this method
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
                    SearchReslt ss = (SearchReslt) p1.obj;
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


    class myonclick implements View.OnClickListener {

        @Override
        public void onClick(View p1) {
            switch (p1.getId()) {
                case 0:
                    startActivity(new Intent(getApplicationContext(), BookShelfActivity.class));
                    //Toast.makeText(getApplicationContext(), "该功能还没有完成😁", 1).show();
                    break;
                case 1:
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                    break;
                case 2:
                    getHtml(update);
                    break;
                case 3:
                    getHtml(paihang);
                    break;
                case 4:
                    getHtml(rexue);
                    break;
                case 5:
                    getHtml(shaonvaiqing);
                    break;
                case 6:
                    getHtml(wuxiagedou);
                    break;
                case 7:
                    getHtml(kehuan);
                    break;
                case 8:
                    getHtml(jingjitiyu);
                    break;
                case 9:
                    getHtml(gaoxiaoxiju);
                    break;
                case 10:
                    getHtml(danmeirensheng);
                    break;
                case 11:
                    getHtml(zhentantuili);
                    break;
                case 12:
                    getHtml(kongbulingyi);
                    break;
                case 13:
                    getPcHtml(PCupdate);
                    break;
                case 14:
                    getPcHtml(PClianzai);
                    break;


            }
        }


    }
}
