package cn.jony.okhttpplus.lib.httpdns;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jony.okhttpplus.lib.httpdns.db.DNSCacheDatabaseHelper;
import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.NetworkManager;
import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveFactory;
import cn.jony.okhttpplus.lib.httpdns.strategy.HostResolveStrategy;
import okhttp3.OkHttpClient;

public enum DNSCache {
    Instance;

    public static final int UPDATE_CACHE_MSG = 1;
    public static final int CLEAR_CACHE_MSG = 2;

    public DNSCacheConfig config;
    private DNSCacheDatabaseHelper dbHelper;
    private NetworkManager networkManager;
    public OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(1, TimeUnit
            .SECONDS).readTimeout(1, TimeUnit.SECONDS).build();

    private Context context;
    private static int activityNum;

    private String strategyName = HostResolveStrategy.DEFAULT;
    private HostResolveStrategy strategy;

    private UpdateThread updateThread;

    public void init(Context context, DNSCacheConfig config) {
        init(context, config, HostResolveStrategy.DEFAULT);
    }

    public void init(Context context, DNSCacheConfig config, String strategyName) {
        this.context = context.getApplicationContext();
        this.dbHelper = new DNSCacheDatabaseHelper(context);
        this.networkManager = NetworkManager.CreateInstance(context);
        this.config = config;
        this.strategyName = strategyName;

        startUpdateTask();
        registerActivityNumListener();
    }

    public void init(Context context, DNSCacheConfig config, HostResolveStrategy strategy) {
        this.context = context.getApplicationContext();
        this.dbHelper = new DNSCacheDatabaseHelper(context);
        this.networkManager = NetworkManager.CreateInstance(context);
        this.config = config;
        this.strategy = strategy;

        startUpdateTask();
        registerActivityNumListener();
    }

    private void startUpdateTask() {
        updateThread = new UpdateThread();
        updateThread.start();
        updateThread.sendMessageDelay(UPDATE_CACHE_MSG, config.expireMillis);
    }

    private void registerActivityNumListener() {
        if (context instanceof Application) {
            ((Application) context).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {
                    activityNum++;
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    activityNum--;
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        }
    }

    public DNSCacheDatabaseHelper getDbHelper() {
        return dbHelper;
    }

    public void clear() {
        updateThread.sendMessageDelay(CLEAR_CACHE_MSG, config.expireMillis);
    }

    public void preLoadDNS(String... hostnames) {
        for (String hostname : hostnames) {
            try {
                lookup(hostname);
            } catch (UnknownHostException e) {
                Log.e("dnscache", Log.getStackTraceString(e));
            }
        }
    }

    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) throw new UnknownHostException("hostname == null");
        return getHostResolveStrategy().lookup(hostname);
    }

    private synchronized HostResolveStrategy getHostResolveStrategy() {
        if (strategy == null) {
            strategy = HostResolveFactory.getStrategy(strategyName);
        }
        return strategy;
    }

    public void onNetworkStatusChanged(NetworkInfo networkInfo) {
        if (networkInfo.isConnected()) {
            networkManager.init();
        }
    }

    public void updateIP(HostIP ip) {
        getHostResolveStrategy().update(ip);
    }

    public HostIP getIP(String sourceId, String targetId) {
        return dbHelper.getIPByID(sourceId, targetId);
    }

    private class UpdateThread extends Thread {
        public Handler handler;

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler(Looper.myLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == UPDATE_CACHE_MSG) {
                        if (isForeGround() && strategy != null && NetworkManager.getInstance().isNetOK()) {
                            strategy.update();
                        }

                        handler.sendMessageDelayed(handler.obtainMessage(UPDATE_CACHE_MSG), config
                                .expireMillis);

                    } else if (msg.what == CLEAR_CACHE_MSG && strategy != null) {
                        strategy.clear();
                    }
                }
            };
            Looper.loop();
        }

        public void sendMessageDelay(int what, long delay) {
            while (handler == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(16);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }

            handler.sendMessageDelayed(handler.obtainMessage(what), delay);
        }
    }

    public static boolean isForeGround() {
        return activityNum > 0;
    }
}
