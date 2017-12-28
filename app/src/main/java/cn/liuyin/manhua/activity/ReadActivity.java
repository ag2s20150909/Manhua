package cn.liuyin.manhua.activity;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.liuyin.manhua.data.Book;
import cn.liuyin.manhua.data.BookMaker;
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
        InitArray();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏


        this.getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);

        wv = new WebView(this);
        wv.addJavascriptInterface(new Android(), "Android");
        setContentView(wv);
        wv.setWebViewClient(new MWebViewClient(this));
        wv.setWebChromeClient(new MWebChromeClient(this));
        ComonTool.enabeCache(wv);
        //wv.loadUrl(getIntent().getStringExtra("url"));
        getData(getIntent().getStringExtra("url"));
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
        } catch (JSONException e) {
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
        }
        BookMaker.makeHtml(wv, mbook, str);


    }


    class Android {
        @JavascriptInterface
        public void pre() {
            if (index > 1) {
                try {
                    String next = list.getJSONObject(index - 2).getString("url");
                    //wv.loadData(null,null,null);
                    getData(next);
                    index--;
                    wv.scrollTo(0, 0);
                } catch (JSONException e) {
                }
            } else {
                Toast.makeText(getApplicationContext(), "没有了😁", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void next() {
            if (index < list.length()) {
                try {
                    String next = list.getJSONObject(index).getString("url");
                    //wv.loadData(null,null,null);
                    getData(next);
                    index++;
                    wv.scrollTo(0, 0);
                } catch (JSONException e) {
                }
            } else {
                Toast.makeText(getApplicationContext(), "没有了😁", Toast.LENGTH_SHORT).show();
            }
            //
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }


    public void getData(final String url) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                try {

                    Book book = BookMaker.getBook(url);
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
                    //FileTool.writeFile("data.json", json);
                    break;

            }
            return false;
        }
    });
}
