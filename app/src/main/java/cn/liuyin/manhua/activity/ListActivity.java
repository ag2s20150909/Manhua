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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.adapter.BookListAdapter;
import cn.liuyin.manhua.data.BookMaker;
import cn.liuyin.manhua.data.DataManger;
import cn.liuyin.manhua.data.JSONHeaper;
import cn.liuyin.manhua.tool.FileTool;

public class ListActivity extends Activity {
    String _BookId;
    JSONArray array;
    String url;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        array = new JSONArray();
        setContentView(R.layout.list);
        lv = (ListView) findViewById(R.id.mainListView1);
        url = getIntent().getStringExtra("url");
        saveList(url);

        String idtag = "manhua/";
        _BookId = url.substring(url.indexOf(idtag) + idtag.length(), url.lastIndexOf("/"));
        System.out.println("<<<"+_BookId);

        try {
            if (FileTool.has(_BookId+"/index.json")){
                array = new JSONArray(FileTool.readFile(_BookId, "index.json"));
                showList(array);
            }
            else {
                saveList(url);
            }

        } catch (JSONException e) {
            mHander.obtainMessage(0,e.getMessage());
            e.printStackTrace();
        }



    }

    @Override
    protected void onStart() {
        // TODO: Implement this method

        DataManger.scanChanges();
        super.onStart();
        if (this.array != null) {
            showList(array);
        } else {
            try {
                array = new JSONArray(FileTool.readFile(_BookId, "index.json"));
                showList(array);
            } catch (JSONException e) {
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO: Implement this method
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onRestoreInstanceState(savedInstanceState);
        if (this.array != null) {
            showList(array);
        } else {
            try {
                array = new JSONArray(FileTool.readFile(_BookId, "index.json"));
                showList(array);
            } catch (JSONException e) {
            }
        }
    }

    @Override
    protected void onStop() {
        // TODO: Implement this method
        super.onStop();
        //System.gc();
    }

    @Override
    protected void onDestroy() {
        // TODO: Implement this method
        super.onDestroy();

    }


    public void saveList(final String url) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                try {

                    JSONArray marray = BookMaker.getList(ListActivity.this, url);
                    ddR();
                    if (marray == null||marray.length()==0) {
                        mHander.obtainMessage(0, "error get list error").sendToTarget();
                    } else {
                        mHander.obtainMessage(1, marray).sendToTarget();
                    }

                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();
                }
            }
        }).start();
    }

    public void ddR() throws JSONException {
        if (!FileTool.has(_BookId + "/config.json")) {
            JSONObject O = new JSONObject();
            if (FileTool.has("index.json")) {
                JSONArray indexjson = new JSONArray(FileTool.readFile("", "index.json"));
                JSONObject O2 = JSONHeaper.getByid(indexjson, Integer.parseInt(_BookId));
                O.put("index", O2.getInt("possion"));
            } else {
                O.put("index", 1);
            }
            FileTool.writeFiles(_BookId + "", "config.json", O.toString());
        }
    }

    private void showList(JSONArray array) {
        int possion = 1;
        try {
            JSONObject O = new JSONObject();
            if (FileTool.has(_BookId+"/config.json")) {
                O= new JSONObject(FileTool.readFile(_BookId, "config.json"));
                possion = O.getInt("index");
            }
            else {
                O.put("index",1);

            }
            FileTool.writeFiles(_BookId,"config.json",O.toString());


        } catch (Exception e) {
            e.printStackTrace();
            possion=1;
        }


        final ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < array.length(); i++) {
            try {
                HashMap<String, String> map = new HashMap<>();
                JSONObject temp = array.getJSONObject(i);
                map.put("index", temp.getString("index"));
                map.put("name", temp.getString("name"));
                map.put("url", temp.getString("url"));
                data.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        //SimpleAdapter adapter=new SimpleAdapter(ListActivity.this,data,R.layout.book_list_item,new String[]{"index","name"},new int[]{R.id.index,R.id.name});
        BookListAdapter adapter = new BookListAdapter(ListActivity.this, data, possion);
        lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));
        lv.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(),"还有"+(data.size()-possion)+"章未读",Toast.LENGTH_LONG).show();
        lv.setSelection(possion - 1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                try {
                    Intent i = new Intent(ListActivity.this, ReadActivity.class);
                    i.putExtra("url", data.get(p3).get("url"));
                    i.putExtra("index", data.get(p3).get("index"));
                    i.putExtra("bookid", _BookId);
                    //Toast.makeText(ListActivity.this,data.get(p3).get("url"),1).show();
                    startActivity(i);
                }catch (Exception e){
                    mHander.obtainMessage(0,e.getMessage());
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
                    System.err.println(msg);
                    //tv.setText(msg);
                    Toast.makeText(ListActivity.this, msg, Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    JSONArray ss = (JSONArray) p1.obj;
                    array = ss;
                    showList(ss);

                    break;
                case 2:

                    break;

            }
            return false;
        }
    });
}
