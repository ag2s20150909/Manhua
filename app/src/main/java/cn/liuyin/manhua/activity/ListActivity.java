package cn.liuyin.manhua.activity;

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



import cn.liuyin.manhua.R;
import cn.liuyin.manhua.adapter.BookListAdapter;
import cn.liuyin.manhua.data.bean.ChaptersBean;
import cn.liuyin.manhua.data.tool.Book;
import cn.liuyin.manhua.data.tool.BookMaker;

import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.tool.FileTool;

public class ListActivity extends BaseActivity {
    Book book;
    ChaptersBean data;
    BookListAdapter adapter;


    String url;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        book = (Book) getIntent().getSerializableExtra("book");

        setContentView(R.layout.list);
        lv = findViewById(R.id.mainListView1);
        url = getIntent().getStringExtra("url");
        if (FileTool.has("bid", book.bookid + ".json")) {
            data = BookMaker.getCatcheList(book);
            showList(data);
        }


        saveList();




    }

    @Override
    protected void onRestart() {

        super.onRestart();
        if (FileTool.has("bid", book.bookid + ".json")) {
            data = BookMaker.getCatcheList(book);
            showList(data);
        }
        //saveList();
    }


    @Override
    protected void onStart() {

        super.onStart();

    }


    @Override
    protected void onStop() {

        super.onStop();
        //System.gc();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }


    public void saveList() {
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    ChaptersBean data = BookMaker.getList(ListActivity.this, book);
                    mHander.obtainMessage(1, data).sendToTarget();


                } catch (Exception e) {
                    mHander.obtainMessage(0, "error" + e).sendToTarget();
                }
            }
        }).start();
    }

    private void showList(final ChaptersBean chapters) {
        if (chapters == null) {
            return;
        }
        if (chapters.code != 0) {
            mHander.obtainMessage(0, chapters.message).sendToTarget();
            return;
        }

        int possion;
        book = BookShelf.getBookById(book.bookid);
        possion = book != null ? book.index : 1;
        if (adapter == null) {
            adapter = new BookListAdapter(ListActivity.this, chapters, possion);
            //lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));
            lv.setAdapter(adapter);
        } else {
            adapter.updateView(chapters, possion);
        }

        lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));
        lv.setSelection(possion - 1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {

                try {
                    Intent i = new Intent(ListActivity.this, BookContentActivity.class);
                    i.putExtra("data", chapters);
                    i.putExtra("book", book);
                    i.putExtra("index", p3 + 1);

                    //Toast.makeText(ListActivity.this,data.get(p3).get("url"),1).show();
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
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
                    //System.err.println(msg);
                    //tv.setText(msg);
                    Toast.makeText(ListActivity.this, msg, Toast.LENGTH_SHORT).show();

                    break;
                case 1:
                    ChaptersBean ss = (ChaptersBean) p1.obj;

                    showList(ss);

                    break;
                case 2:

                    break;

            }
            return false;
        }
    });
}
