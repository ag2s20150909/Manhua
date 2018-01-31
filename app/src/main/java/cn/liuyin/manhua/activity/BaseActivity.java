package cn.liuyin.manhua.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        checkPermission();
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

}
