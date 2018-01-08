package cn.liuyin.manhua.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import cn.liuyin.manhua.R;

public class SettingActivity extends Activity {
    Switch proxy, handproxy, useragent, cookie;
    Button setProxy;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_setting);
        proxy = findViewById(R.id.proxy);
        handproxy = findViewById(R.id.hand_proxy);
        useragent = findViewById(R.id.user_agent);
        cookie = findViewById(R.id.cookie);
        setProxy = findViewById(R.id.set_porxy);

        proxy.setChecked(preferences.getBoolean("proxy", false));
        handproxy.setChecked(preferences.getBoolean("handproxy", false));
        useragent.setChecked(preferences.getBoolean("useragent", true));
        cookie.setChecked(preferences.getBoolean("cookie", true));


        proxy.setOnCheckedChangeListener(new onCheck());
        handproxy.setOnCheckedChangeListener(new onCheck());
        useragent.setOnCheckedChangeListener(new onCheck());
        cookie.setOnCheckedChangeListener(new onCheck());
        setProxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setHandPoxy();

            }
        });
        setProxy.setClickable(preferences.getBoolean("handproxy", false));


    }


    public void setHandPoxy() {

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(p);
        TextView address_t = new TextView(this);
        address_t.setLayoutParams(p);
        address_t.setText("代理地址：");
        root.addView(address_t);
        final EditText address_i = new EditText(this);
        address_i.setText(preferences.getString("proxy_url", "127.0.0.1"));
        address_i.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        address_i.setLayoutParams(p);
        root.addView(address_i);

        TextView port_t = new TextView(this);
        port_t.setText("端口：");
        port_t.setLayoutParams(p);
        root.addView(port_t);
        final EditText port_i = new EditText(this);
        port_i.setText(preferences.getInt("proxy_port", 0) + "");
        port_i.setInputType(InputType.TYPE_CLASS_NUMBER);
        port_i.setLayoutParams(p);
        root.addView(port_i);

        AlertDialog.Builder ad1 = new AlertDialog.Builder(this);
        ad1.setTitle("设置HTTP代理");
        ad1.setIcon(android.R.drawable.ic_dialog_info);
        ad1.setView(root);
        ad1.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                preferences.edit().putString("proxy_url", address_i.getText().toString()).apply();
                preferences.edit().putInt("proxy_port", Integer.parseInt(port_i.getText().toString())).apply();


            }
        });
        ad1.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }


    public class onCheck implements CompoundButton.OnCheckedChangeListener {

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.proxy:
                    preferences.edit().putBoolean("proxy", isChecked).apply();
                    break;
                case R.id.user_agent:
                    preferences.edit().putBoolean("useragent", isChecked).apply();
                    break;
                case R.id.cookie:
                    preferences.edit().putBoolean("cookie", isChecked).apply();
                    break;
                case R.id.hand_proxy:
                    preferences.edit().putBoolean("handproxy", isChecked).apply();
                    setProxy.setClickable(isChecked);
                    break;
            }
        }
    }
}
