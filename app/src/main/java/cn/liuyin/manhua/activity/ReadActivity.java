package cn.liuyin.manhua.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.data.Book;
import cn.liuyin.manhua.data.BookMaker;
import cn.liuyin.manhua.data.JSONHeaper;
import cn.liuyin.manhua.tool.ComonTool;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.manhua.tool.JSFunction;
import cn.liuyin.manhua.tool.MWebChromeClient;
import cn.liuyin.manhua.tool.MWebViewClient;

public class ReadActivity extends Activity {
    String _BookId;
    Book mbook;
    JSONArray list;
    int index;


    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        _BookId = getIntent().getStringExtra("bookid");

        //index=getIntent().getIntExtra("index",1);
        InitArray();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏


        this.getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);

        wv = new WebView(this);
        wv.addJavascriptInterface(new Android(), "Android");
        setContentView(wv);
        wv.setWebViewClient(new MWebViewClient(this));
        wv.setWebChromeClient(new MWebChromeClient(this));
        wv.setOnLongClickListener(new onLongClick());
        ComonTool.enabeCache(wv);
        //wv.loadUrl(getIntent().getStringExtra("url"));


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO: Implement this method
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
        outState.putString("bookid", _BookId);
        wv.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onRestoreInstanceState(savedInstanceState);
        index = savedInstanceState.getInt("index");
        _BookId = savedInstanceState.getString("bookid");
        wv.restoreState(savedInstanceState);
    }


    public void InitArray() {
        String jstr = FileTool.readFile(_BookId, "index.json");
        index = Integer.parseInt(getIntent().getStringExtra("index"));
        try {
            list = new JSONArray(jstr);
            getData(list, _BookId, index);
        } catch (JSONException e) {
            e.printStackTrace();
            mHander.obtainMessage(0, e.getMessage()).sendToTarget();
        }
    }

    public void runJs(String js) {
        js = JSFunction.decode(js);
        wv.evaluateJavascript(js, new ValueCallback<String>() {

            @Override
            public void onReceiveValue(String p1) {
                // TODO: Implement this method
                mHander.obtainMessage(2, p1).sendToTarget();
            }
        });
    }

    public void showData(String str) {
        mbook.index = index;
        try {
            JSONObject O = new JSONObject();
            O.put("index", mbook.index);
            Toast.makeText(getApplicationContext(), "还有" + (list.length() - O.getInt("index")) + "章未读", Toast.LENGTH_LONG).show();
            FileTool.writeFiles(mbook.bookid + "", "config.json", O.toString());
        } catch (JSONException e) {
            mHander.obtainMessage(0, e.getMessage()).sendToTarget();
            e.printStackTrace();
        }
        BookMaker.makeHtml(wv, mbook, str);


    }


    private class onLongClick implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            showSS();
            return false;
        }
    }

    public void showSS() {
        // 创建数据

        final String[] items = new String[]{"重试", "下一章", "上一章"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setIcon(R.mipmap.ic_launcher).setTitle("对漫画的操作").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                // TODO: Implement this method
                switch (p2) {
                    case 0:
                        GoThis();
                        break;
                    case 1:
                        goNext();
                        break;
                    case 2:
                        goPre();
                        break;
                }


            }
        });

        builder.create().show();
    }


    public void GoThis() {

        try {
            int next = list.getJSONObject(index - 1).getInt("index");
            getData(list, _BookId, next);
            wv.scrollTo(0, 0);
        } catch (JSONException e) {
            e.printStackTrace();
            mHander.obtainMessage(0, e.getMessage()).sendToTarget();
        }

    }

    public void goPre() {
        if (index > 1) {
            try {
                int next = list.getJSONObject(index - 2).getInt("index");

                //wv.loadData(null,null,null);
                getData(list, _BookId, next);
                index--;
                wv.scrollTo(0, 0);
            } catch (JSONException e) {
                e.printStackTrace();
                mHander.obtainMessage(0, e.getMessage()).sendToTarget();
            }
        } else {
            Toast.makeText(getApplicationContext(), "没有了😁", Toast.LENGTH_SHORT).show();
        }
    }

    public void goNext() {
        if (index < list.length()) {
            try {
                int next = list.getJSONObject(index).getInt("index");
                //wv.loadData(null,null,null);
                getData(list, _BookId, next);
                index++;
                wv.scrollTo(0, 0);
            } catch (JSONException e) {
                mHander.obtainMessage(0, e.getMessage()).sendToTarget();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "没有了😁", Toast.LENGTH_SHORT).show();
        }
    }


    class Android {
        @JavascriptInterface
        public void pre() {
            goPre();
        }

        @JavascriptInterface
        public void next() {
            goNext();
            //
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }


    public void getData(final JSONArray dd, final String bookid, final int index) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                try {

                    Book book = BookMaker.getBook(dd, bookid, index);
                    if (book != null) {
                        mHander.obtainMessage(1, book).sendToTarget();
                    } else {
                        mHander.obtainMessage(0, "error:get book failed.").sendToTarget();
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
                    Toast.makeText(ReadActivity.this, msg, Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    mbook = (Book) p1.obj;
                    runJs(mbook.code);
                    break;
                case 2:
                    String json = (String) p1.obj;
                    showData(json);
                    break;

            }
            return false;
        }
    });
}
