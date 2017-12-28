package cn.liuyin.manhua.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import cn.liuyin.manhua.tool.HttpTool;

public class SearchActivity extends Activity {
    EditText et;
    Button btn;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
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
                Toast.makeText(getApplicationContext(), "88", 1).show();
                if (!et.getText().toString().isEmpty()) {
                    search(et.getText().toString());

                }
            }
        });


    }

    public void showEnptyView() {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        emptyView.setText("对不起，没有找到哭∏_∏");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) lv.getParent()).addView(emptyView);
        lv.setEmptyView(emptyView);
    }


    public void search(final String kw) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                String u = HttpTool.search(getApplicationContext(), kw);
                getHtml(u);
            }
        }).start();
    }

    public void getHtml(final String url) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                try {

                    Document doc = Jsoup.connect(url).get();
                    Elements lists = doc.select(".cont-list").select("li");
                    mHander.obtainMessage(0, "搜索到" + lists.size() + "条结果").sendToTarget();
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

                    //FileTool.writeFile("update.html",doc.toString());
                    mHander.obtainMessage(1, data).sendToTarget();
                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();
                }
            }
        }).start();
    }

    private void showList(JSONArray array) {

        final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < array.length(); i++) {
            try {
                HashMap<String, String> map = new HashMap<>();
                JSONObject temp = array.getJSONObject(i);
                map.put("link", temp.getString("link") + "/");
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

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                Intent i = new Intent(getApplicationContext(), ListActivity.class);
                i.putExtra("url", data.get(p3).get("link"));
                Toast.makeText(getApplicationContext(), data.get(p3).get("link"), 1).show();
                startActivity(i);
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
                    Toast.makeText(getApplicationContext(), msg, 1).show();

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

}
