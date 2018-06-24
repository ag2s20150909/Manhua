package cn.liuyin.manhua.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.liuyin.manhua.tool.FileTool;

@SuppressLint("Registered")
public class BaseActivity extends Activity {
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private final int MASK_RATE = 150;
    private WindowManager mWindowManager = null;
    private View mNightView = null;
    private WindowManager.LayoutParams mNightViewParam;
    private boolean mIsAddedView;
    SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        checkPermission();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //注册监听器
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        initNightView();

        mIsAddedView = false;
        mNightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(listener);
        if (mIsAddedView) {

            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
            mNightView = null;
        }
        super.onDestroy();
    }

    public void ChangeToDay() {

        //APP.isNightMode = false;
        mNightView.setBackgroundResource(android.R.color.transparent);
    }


    public boolean isNight() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int k = Integer.parseInt(hour);
        return (k >= 0 && k < 6) || (k >= 18 && k < 24);
    }

    public void ChangeToNight() {
        //APP.isNightMode = true;
        initNightView();
        mNightView.setBackgroundColor(Color.argb(MASK_RATE, 0, 0, 0));
    }

    /**
     * wait a time until the onresume finish
     */
    public void recreateOnResume() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recreate();
            }
        }, 100);
    }

    private void initNightView() {
        if (mIsAddedView == true)
            return;
        mNightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView, mNightViewParam);
        mIsAddedView = true;
    }

    public void setLight(Activity context, int brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }

    SensorEventListener listener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //当传感器精度发生变化时
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            //当传感器监测到的数值发生变化时
            float value = event.values[0];
            if (FileTool.has("night.dat")) {
                if (value <= 5.0) {
                    ChangeToNight();
                } else {
                    ChangeToDay();
                }
            }
            //setTitle(""+event.values[0]+"/"+event.values[1]+"/"+event.values[2]+"@"+event.values.length);
            //tv.setText(""+value);
            //Toast.makeText(BaseActivity.this,""+value+"",1).show();
            //light.setText("当前亮度 " + value + " lx(勒克斯)");
        }

    };




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
