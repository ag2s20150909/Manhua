package cn.liuyin.manhua.activity;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.adapter.BookContentAdapter;
import cn.liuyin.manhua.data.bean.ChaptersBean;
import cn.liuyin.manhua.data.bean.ContentBean;
import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.BookMaker;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.tool.FileTool;
import cn.liuyin.view.DampingListView;

public class BookContentActivity extends BaseActivity {
    Context mContext;
    ChaptersBean chapters;
    Book book;
    int index;

    DampingListView lv;
    TextView tv;
    BookContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
            this.mContext = BookContentActivity.this;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_content);
            this.getWindow().getDecorView().setBackgroundColor(Color.DKGRAY);
            chapters = (ChaptersBean) getIntent().getSerializableExtra("data");
            book = (Book) getIntent().getSerializableExtra("book");
            index = getIntent().getIntExtra("index", 1);
            lv = findViewById(R.id.activity_content_lv);

            tv = findViewById(R.id.activity_content_tv);
            getData(index);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void showData(final ContentBean data) {
        adapter = new BookContentAdapter(this, data);
        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
                adapter.doOnLongClick(p3);
                return true;
            }
        });
        tv.setText(data.data.title);
        book.index = index;
        BookShelf.addBook(book);


        lv.setOnBounceCallBack(new DampingListView.BounceCallBack() {

            @Override
            public void onOverScrollUp() {
                if (index > 1) {
                    index--;
                    getData(index);

                } else {
                    mHander.obtainMessage(0, "前面没有了😁").sendToTarget();
                }
            }

            @Override
            public void onOverScrollDown() {
                if (index < chapters.data.list.size()) {
                    index++;
                    getData(index);

                } else {
                    mHander.obtainMessage(0, "下面没有了😁").sendToTarget();
                }
            }
        });
    }

    public void getData(final int index) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    Gson gson = new Gson();
                    //API api=new API(BookContentActivity.this);
                    ContentBean data = BookMaker.getContent(chapters.data.list.get(index - 1).bid, chapters.data.list.get(index - 1).cid);
                    //=gson.fromJson(api.getContents(chapters.data.list.get(index-1).bid+"",chapters.data.list.get(index-1).cid+""),ContentBean.class);
                    String msg = gson.toJson(data, ContentBean.class);
                    FileTool.writeFile("debug.txt", msg);
                    mHander.obtainMessage(2, data).sendToTarget();
                } catch (Exception e) {
                    FileTool.writeError(e.getMessage());
                    mHander.obtainMessage(0, e.getMessage()).sendToTarget();
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
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    // mbook = (Book) p1.obj;
                    //runJs(mbook.code);
                    break;
                case 2:
                    ContentBean data = (ContentBean) p1.obj;
                    showData(data);
                    //FileTool.writeFile("data.json", json);
                    break;

            }
            return false;
        }
    });
}
