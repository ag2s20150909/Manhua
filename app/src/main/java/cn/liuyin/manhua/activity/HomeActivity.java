package cn.liuyin.manhua.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.DataManger;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.HttpTool;
import cn.liuyin.manhua.tool.UItool;

public class HomeActivity extends Activity {


    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

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

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
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
        checkPermission();

    }


    @Override
    protected void onStart() {
        // TODO: Implement this method
        super.onStart();

        DataManger.scanChanges();
        //初始化书架
        if (!FileTool.has("index.json")) {
            FileTool.writeFile("index.json", "[]");
        }

        //ComonTool.copy2System(this,cmd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*检查权限

     */


    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleGrantResults(requestCode, grantResults);

    }

    private void handleGrantResults(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 获得权限后执行xxx
                Toast.makeText(this, "获得读取权限成功", Toast.LENGTH_SHORT).show();
            } else {
                // Permission Denied 拒绝后xx的操作。
                Toast.makeText(this, "没有获得读取权限，应用可能无法使用", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    检查权限结束
     */





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
                    JSONArray data = new JSONArray();
                    for (Element item : lists) {
                        JSONObject temp = new JSONObject();
                        String nurl = item.select("a.red").attr("abs:href");
                        temp.put("link", nurl.substring(0, nurl.lastIndexOf("/") + 1));
                        temp.put("name", item.select("a.video").text());
                        temp.put("img", "");
                        temp.put("author", "缺少作者信息");
                        temp.put("type", "");
                        temp.put("new", item.select("a.red").text());
                        if (url.endsWith("update.html")) {
                            temp.put("time", item.select("span.red").text());
                        } else {
                            temp.put("time", "缺少时间信息");
                        }
                        data.put(temp);
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
                    JSONArray data = new JSONArray();
                    for (Element item : lists) {
                        JSONObject temp = new JSONObject();
                        temp.put("link", item.select("a").attr("abs:href"));
                        temp.put("name", item.select("h3").text());
                        temp.put("img", item.select("img").attr("data-src"));
                        temp.put("author", item.select("dd").get(0).text());
                        temp.put("type", item.select("dd").get(1).text());
                        temp.put("new", item.select("dd").get(2).text());
                        temp.put("time", item.select("dd").get(3).text());
                        data.put(temp);
                    }
                    FileTool.writeFile("mobile.html", doc.toString());

                    //FileTool.writeFile("update.html", doc.toString());
                    //FileTool.writeFile("pc.json", data.toString(4));
                    mHander.obtainMessage(1, data).sendToTarget();
                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();
                }
            }
        }).start();
    }


    private void showList(JSONArray array) {

        final ArrayList<HashMap<String, String>> data = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                HashMap<String, String> map = new HashMap<>();
                JSONObject temp = array.getJSONObject(i);
                map.put("link", temp.getString("link"));
                map.put("name", temp.getString("name"));
                map.put("author", temp.getString("author"));
                map.put("new", temp.getString("new"));
                map.put("time", temp.getString("time"));
                data.add(map);
            } catch (JSONException e) {
                mHander.obtainMessage(0, e.getMessage()).sendToTarget();
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.book_item,
                new String[]{"author", "name", "new", "time"},
                new int[]{R.id.author, R.id.name, R.id.new_c, R.id.time});
        lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));
        lv.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "共有" + (data.size()) + "条结果", Toast.LENGTH_LONG).show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {

                // TODO: Implement this method
                try {
                    Intent i = new Intent(getApplicationContext(), ListActivity.class);
                    i.putExtra("url", data.get(p3).get("link"));
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
                    JSONArray ss = (JSONArray) p1.obj;
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
