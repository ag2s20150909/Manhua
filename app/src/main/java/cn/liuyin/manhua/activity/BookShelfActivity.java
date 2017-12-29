package cn.liuyin.manhua.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.adapter.BookShelfAdapter;
import cn.liuyin.manhua.data.BookMaker;
import cn.liuyin.manhua.data.JSONHeaper;
import cn.liuyin.manhua.tool.FileTool;

public class BookShelfActivity extends Activity {
    JSONArray array;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);

        lv = new ListView(this);
        setContentView(lv);
        try {
            array = new JSONArray(FileTool.readFile("", "index.json"));

            showList(array);
        } catch (JSONException e) {
            mHander.obtainMessage(0, e.getMessage()).sendToTarget();
        }
        //scanChanges();

    }

    @Override
    protected void onStart() {
        // TODO: Implement this method
        super.onStart();
        //scanChanges();
    }

    @Override
    protected void onRestart() {
        // TODO: Implement this method
        super.onRestart();
        update();
    }


    public void update() {
        try {

            array = new JSONArray(FileTool.readFile("", "index.json"));

            showList(array);
        } catch (JSONException e) {
            mHander.obtainMessage(0, e.getMessage()).sendToTarget();
        }
    }


    public void ChackUpdate() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                try {
                    long ms = System.currentTimeMillis();
                    if (FileTool.has("time.json")) {
                        JSONObject oo = new JSONObject(FileTool.readFile("", "time.json"));
                        ms = ms - oo.getLong("ms");
                    } else {
                        ms = 0;
                    }

                    for (int i = 0; i < array.length(); i++) {
                        String url = "http://m.pufei.net/manhua/" + array.getJSONObject(i).getInt("bookid") + "/";
                        BookMaker.getList(BookShelfActivity.this, url);
                    }
                    JSONObject O = new JSONObject();
                    O.put("ms", System.currentTimeMillis());
                    FileTool.writeFile("time.json", O.toString());


                    if (array != null) {
                        mHander.obtainMessage(1, array).sendToTarget();
                    } else {
                        mHander.obtainMessage(0, "error get list error").sendToTarget();
                    }


                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();
                }
            }
        }).start();
    }

    private Handler mHander = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message p1) {
            switch (p1.what) {
                case 0:
                    String msg = (String) p1.obj;

                    //tv.setText(msg);
                    Toast.makeText(BookShelfActivity.this, msg, Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    JSONArray ss = (JSONArray) p1.obj;
                    update();


                    break;
                case 2:

                    break;

            }
            return false;
        }
    });

    private void showList(JSONArray marray) {

        final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < marray.length(); i++) {
            try {
                HashMap<String, String> map = new HashMap<>();
                JSONObject temp = marray.getJSONObject(i);
                map.put("bookid", temp.getInt("bookid") + "");
                map.put("name", temp.getString("title"));
                map.put("unread", temp.getInt("unread") + "");
                data.add(map);
            } catch (JSONException e) {
                mHander.obtainMessage(0, e.getMessage());
            }
        }
        BookShelfAdapter adapter = new BookShelfAdapter(BookShelfActivity.this, data);
        lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                try {
                    Intent i = new Intent(getApplicationContext(), ListActivity.class);
                    String url = "http://m.pufei.net/manhua/" + data.get(p3).get("bookid") + "/";
                    i.putExtra("url", url);
                    startActivity(i);
                }
                catch (Exception e){
                    e.printStackTrace();
                    mHander.obtainMessage(0,e.getMessage()).sendToTarget();
                }
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                showSS(p3);
                return true;
            }
        });


    }


    public void showSS(final int index) {
        // 创建数据
        final int[] ids = JSONHeaper.getIds(array, "title");
        final String[] items = new String[]{"置顶", "上移", "下移", "删除", "更新"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setIcon(R.mipmap.ic_launcher).setTitle("对漫画的操作").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                // TODO: Implement this method
                Toast.makeText(getApplicationContext(), ids[index] + "", Toast.LENGTH_SHORT).show();
                switch (p2) {
                    case 3:
                        JSONHeaper.removeById(array, ids[index]);
                        FileTool.deleteDir(ids[index] + "");
                        break;
                    case 1:
                        if (index > 0) {
                            JSONHeaper.moveUpById(array, ids[index], 1);
                        }
                        break;
                    case 2:
                        if (index < (ids.length - 1)) {
                            JSONHeaper.moveDownById(array, ids[index], 1);
                        }

                        break;
                    case 0:
                        JSONHeaper.moveTopById(array, ids[index]);
                        break;
                    case 4:
                        ChackUpdate();
                        break;
                }

                try {
                    FileTool.writeFile("index.json", array.toString(4));
                    showList(array);
                } catch (JSONException e) {
                }
            }
        });

        builder.create().show();
    }


}
