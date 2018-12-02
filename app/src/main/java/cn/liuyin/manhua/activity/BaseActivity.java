package cn.liuyin.manhua.activity;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import cn.liuyin.manhua.APP;
import cn.liuyin.manhua.data.tool.BookShelf;

public class BaseActivity extends Activity {
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        checkPermission();
        if (BookShelf.getBookShelf().sum > 80) {
            APP.showToast("书架最多可放80部，放多了存在bug,删除一些不看的书");
        }

    }

    @Override
    protected void onRestart() {
        if (BookShelf.getBookShelf().sum > 80) {
            APP.showToast("书架最多可放80部，放多了存在bug,删除一些不看的书");
        }
        super.onRestart();
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

}
