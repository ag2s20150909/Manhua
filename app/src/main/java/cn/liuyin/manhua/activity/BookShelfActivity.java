package cn.liuyin.manhua.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.liuyin.manhua.R;
import cn.liuyin.manhua.adapter.BookShelfAdapter;
import cn.liuyin.manhua.data.tool.BookShelf;
import cn.liuyin.manhua.service.CheckBookUpdateRunnable;
import cn.liuyin.manhua.service.DownloadService;
import cn.liuyin.manhua.tool.NetworkUtil;


public class BookShelfActivity extends BaseActivity {
    ExecutorService fixedThreadPool;
    BookShelf bookshelf;
    BookShelfAdapter adapter;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        fixedThreadPool = Executors.newFixedThreadPool(1);
        lv = new ListView(this);
        setContentView(lv);
        bookshelf = BookShelf.getBookShelf();
        //adapter = new BookShelfAdapter(this, bookshelf);
        showList(bookshelf, true);
        if (NetworkUtil.getNetWorkStates(this) == NetworkUtil.TYPE_WIFI) {
            CheckUpdate();
        }
        //scanChanges();

    }

    @Override
    protected void onStart() {

        super.onStart();
        //BookShelf.sortBooks();
        bookshelf = BookShelf.getBookShelf();
        showList(bookshelf, false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    public void CheckUpdate() {
        CheckBookUpdateRunnable runnable = new CheckBookUpdateRunnable(this);
        runnable.setListener(new CheckBookUpdateRunnable.MListener() {

            @Override
            public void onStart() {
                // TODO: Implement this method
                mHander.obtainMessage(0, "开始检查更新").sendToTarget();
            }

            @Override
            public void doUpdate(BookShelf bookshelf) {
                // TODO: Implement this method
                mHander.obtainMessage(1, bookshelf).sendToTarget();


            }

            @Override
            public void err(String err) {
                // TODO: Implement this method
                mHander.obtainMessage(0, err).sendToTarget();
            }

            @Override
            public void onFinished() {
                // TODO: Implement this method
                mHander.obtainMessage(0, "检查更新完成").sendToTarget();
            }
        });
        fixedThreadPool.execute(runnable
        );
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
                    bookshelf = (BookShelf) p1.obj;
                    adapter.updateView(bookshelf);
                    break;
                case 2:

                    break;

            }
            return false;
        }
    });

    private void showList(final BookShelf data, boolean reload) {

        if (reload) {
            adapter = new BookShelfAdapter(BookShelfActivity.this, data);
            lv.setAdapter(adapter);
        } else {
            adapter.updateView(data);
        }
        // lv.setLayoutAnimation(new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.list_animation), 0.5f));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {

                try {
                    Intent i = new Intent(getApplicationContext(), ListActivity.class);
                    i.putExtra("url", data.books.get(p3).link);
                    i.putExtra("book", data.books.get(p3));
                    BookShelf.updateBook(data.books.get(p3));

                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHander.obtainMessage(0, e.getMessage()).sendToTarget();
                }
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4) {
                showSS(p3);
                return true;
            }
        });


    }


    public void showSS(final int index) {

        final String[] items = new String[]{"置顶漫画", "上移漫画", "下移漫画", "删除漫画", "检查更新", "更新时间排序", "阅读时间排序", "未读章节排序", "下载漫画", "清除缓存"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setIcon(R.drawable.ic_launcher).setTitle("对漫画的操作").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                Toast.makeText(getApplicationContext(), bookshelf.books.get(index).bookid + "", Toast.LENGTH_SHORT).show();
                switch (p2) {
                    case 3:
                        //bookshelf=BookShelf.sortBooks();
                        bookshelf = BookShelf.removeByIndex(index);
                        //FileTool.deleteDir(ids[index] + "");
                        break;
                    case 1:
                        if (index > 0) {
                            bookshelf = BookShelf.MoveUpByIndex(index);
                        }
                        break;
                    case 2:
                        if (index < (bookshelf.books.size() - 1)) {
                            bookshelf = BookShelf.MoveDownByIndex(index);
                        }

                        break;
                    case 0:

                        bookshelf = BookShelf.MoveTopByIndex(index);
                        break;
                    case 4:
                        CheckUpdate();
                        break;
                    case 5:
                        bookshelf = BookShelf.sortBooksByUpdate();
                        break;
                    case 6:
                        bookshelf = BookShelf.sortBooksByRead();
                        break;
                    case 7:
                        bookshelf = BookShelf.sortBooksByUnRead();
                        break;
                    case 8:
                        Intent i = new Intent(BookShelfActivity.this, DownloadService.class);
                        i.putExtra("action", "download");
                        i.putExtra("bid", bookshelf.books.get(index).bookid);
                        startService(i);
                    case 9:

                        break;


                }
                showList(bookshelf, false);


            }
        });

        builder.create().show();
    }

}
