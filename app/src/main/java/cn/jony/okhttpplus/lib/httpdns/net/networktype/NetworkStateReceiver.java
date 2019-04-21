package cn.jony.okhttpplus.lib.httpdns.net.networktype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;

public class NetworkStateReceiver extends BroadcastReceiver {
    public String TAG = "TAG_NET";

    public static NetworkInfo getActiveNetwork(Context context) {
        if (context == null)
            return null;
        ConnectivityManager mConnMgr = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);

        if (mConnMgr == null)
            return null;

        NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo(); // 获取活动网络连接信息
        return aActiveInfo;
    }

    public static void register(Context context, Callback callback) {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        context.registerReceiver(networkStateReceiver, mFilter);
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = getActiveNetwork(context);
            if (networkInfo != null) {

                // 刷新网络环境
                if (NetworkManager.getInstance() != null) {
                    NetworkManager.getInstance().init();
                    DNSCache.Instance.onNetworkStatusChanged(networkInfo);
                }
            }
        }
    }

    public interface Callback {
        void onNetworkStatusChanged(NetworkInfo networkInfo);
    }
}
