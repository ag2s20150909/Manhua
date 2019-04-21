package cn.jony.okhttpplus.lib.httpdns.strategy;


import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;
import cn.jony.okhttpplus.lib.httpdns.DNSCacheConfig;
import cn.jony.okhttpplus.lib.httpdns.db.DNSCacheDatabaseHelper;
import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.net.UrlHelper;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.NetworkManager;
import cn.jony.okhttpplus.lib.httpdns.speedtest.SpeedTestManager;
import cn.jony.okhttpplus.lib.httpdns.util.EmptyUtil;
import cn.jony.okhttpplus.lib.httpdns.util.RealTimeThreadPool;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;

public class DefaultHostResolveStrategy extends AbsHostResolveStrategy {
    static final int MAX_FAIL_NUM = 2;
    static final double MAX_FAIL_PERCENT = 0.95;
    static final int VISIT_THRESHOLD = 10;

    private static final String TAG = DefaultHostResolveStrategy.class.getSimpleName();

    protected final LruCache<HostIP.Key, List<HostIP>> cache;
    protected final OkHttpClient okHttpClient;
    protected final SpeedTestManager speedTestManager;

    public DefaultHostResolveStrategy() {
        cache = new LruCache<HostIP.Key, List<HostIP>>(DNSCache.Instance.config.maxCacheSize) {
            @Override
            protected int sizeOf(HostIP.Key key, List<HostIP> value) {
                return EmptyUtil.isCollectionEmpty(value) ? 1 : value.size();
            }
        };
        okHttpClient = DNSCache.Instance.okHttpClient;
        speedTestManager = new SpeedTestManager();
    }

    @Override
    public boolean isReliable(HostIP ip) {
        DNSCacheConfig config = DNSCache.Instance.config;
        return !TextUtils.isEmpty(ip.targetIP) && ip.getWorkMillis() < config.expireMillis
                && ip.rtt <= config.maxRtt && ip.ttl <= config.maxTtl &&
                allowFail(ip);
    }

    private boolean allowFail(HostIP ip) {
        return ip.getFailNum() < MAX_FAIL_NUM || ip.getFailPercent() < MAX_FAIL_PERCENT;
    }


    @Override
    public List<InetAddress> lookupInMemory(String hostname) {
        HostIP.Key key = new HostIP.Key(hostname, NetworkManager.getInstance().ipAddress);
        List<HostIP> list = cache.get(key);
        if (!EmptyUtil.isCollectionEmpty(list) && list.size() > 1) {
            filterAndRemove(list);
            Collections.sort(list, IP_COMPARATOR);
        }

        return ipList2Addresses(list);
    }

    protected void filterAndRemove(List<HostIP> ipList) {
        if (ipList == null)
            return;

        Iterator<HostIP> it = ipList.iterator();
        while (it.hasNext()) {
            HostIP ip = it.next();
            if (!isReliable(ip)) {
                it.remove();
            }
        }
    }

    protected static List<InetAddress> ipList2Addresses(List<HostIP> ipList) {
        if (!EmptyUtil.isCollectionEmpty(ipList)) {
            List<InetAddress> addresses = new ArrayList<>();
            for (HostIP ip : ipList) {
                try {
                    addresses.add(InetAddress.getByName(ip.targetIP));
                } catch (UnknownHostException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }

            }
            return addresses;
        }

        return null;
    }

    @Override
    public List<InetAddress> lookupInDB(String hostname) {
        final String sourceIP = NetworkManager.getInstance().ipAddress;
        List<HostIP> list = DNSCache.Instance.getDbHelper().getIPByHostAndSourceIP(hostname, sourceIP);
        filterAndRemove(list);

        if (!EmptyUtil.isCollectionEmpty(list)) {
            cache.put(new HostIP.Key(hostname, sourceIP), list);
        }
        return ipList2Addresses(list);
    }

    @Override
    public List<InetAddress> lookupNet(final String hostname) {
        final List<InetAddress> inetAddressList = new ArrayList<>();

        Call call = okHttpClient.newCall(new Request.Builder().url(UrlHelper.getDnsRequestUrl
                (hostname)).method("GET", null).cacheControl(CacheControl.FORCE_NETWORK).build
                ());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final BufferedSource source = response.body().source();
                    final List<HostIP> ipList = new ArrayList<>();
                    parseDnsPod(source, ipList, inetAddressList, hostname);

                    updateCache(ipList, hostname);

                    doSpeedTest(ipList);
                }
            }
        });

        return inetAddressList;
    }

    protected void parseDnsPod(BufferedSource source, List<HostIP> ipList, List<InetAddress>
            inetAddressList, String hostname) throws IOException {
        String line = source.readUtf8Line();
        if (!TextUtils.isEmpty(line)) {
            String[] tmp = line.split(",");
            if (tmp.length == 2) {
                String targetIPs = tmp[0];

                int ttl = -1;
                try {
                    ttl = Integer.parseInt(tmp[1]);
                } catch (NumberFormatException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }


                if (!TextUtils.isEmpty(targetIPs)) {
                    String[] ipArr = targetIPs.split(";");
                    for (String targetIP : ipArr) {
                        inetAddressList.add(InetAddress.getByName(targetIP));

                        HostIP.Builder builder = new HostIP.Builder()
                                .sourceIP(NetworkManager.getInstance().ipAddress)
                                .targetIP(targetIP).operator(NetworkManager.getInstance().operatorTypeStr)
                                .saveMillis(System.currentTimeMillis()).host(hostname);
                        if (ttl > 0) {
                            builder.ttl(ttl);
                        }

                        ipList.add(builder.build());
                    }
                }
            }
        }
    }

    protected void updateCache(final List<HostIP> ipList, final String hostname) {
        if (!EmptyUtil.isCollectionEmpty(ipList)) {
            RealTimeThreadPool.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    cache.put(new HostIP.Key(hostname, NetworkManager.getInstance().ipAddress), ipList);
                    DNSCache.Instance.getDbHelper().addIPList(ipList);
                }
            });
        }
    }

    protected void doSpeedTest(final List<HostIP> ipList) {
    }

    /**
     * 更新方法:对每个ip进行测速，根据缓存事件和访问次数判断是否更新缓存事件；最后将表中所有数据都删除，然后再依次增加 <br />
     * 只更新db，不更新cache；因为cache如果未命中会通过db获取数据来更新。
     */
    @Override
    public void update() {
        DNSCacheDatabaseHelper dbHelper = DNSCache.Instance.getDbHelper();
        List<HostIP> ipList = dbHelper.getAllIP();
        Iterator<HostIP> ipIterator = ipList.iterator();
        while (ipIterator.hasNext()) {
            HostIP ip = ipIterator.next();
            int rtt = speedTestManager.speedTest(ip.targetIP, ip.host);
            ip.updateRtt(rtt, rtt > 0);

            if (ip.visitSinceSaved >= VISIT_THRESHOLD) {
                ip.visitSinceSaved = 0;
                ip.saveMillis = System.currentTimeMillis();
            }

            if (!isReliable(ip)) {
                ipIterator.remove();
            }
        }

        cache.evictAll();
        dbHelper.clear();
        dbHelper.addIPList(ipList);
    }

    @Override
    public void update(HostIP ip) {
        DNSCache.Instance.getDbHelper().updateIp(ip);
        String sourceIP = TextUtils.isEmpty(ip.sourceIP) ? NetworkManager.getInstance().ipAddress : ip.sourceIP;
        List<HostIP> ipList = cache.get(new HostIP.Key(ip.host, sourceIP));
        for (HostIP ip1 : ipList) {
            if (ip1.equals(ip)) {
                ip1.setSucNum(ip.getSucNum());
                ip1.setFailNum(ip.getFailNum());
                ip1.rtt = ip.rtt;
                ip1.visitSinceSaved = ip.visitSinceSaved;
            }
        }
    }

    @Override
    public void clear() {
        cache.evictAll();
        DNSCache.Instance.getDbHelper().clear();
    }

    private static final int FAIL_WEIGHT = 200;
    private static final int RTT_WEIGHT = 2;
    private static final int ttl_WEIGHT = 1;

    protected static final Comparator<HostIP> IP_COMPARATOR = new Comparator<HostIP>() {
        @Override
        public int compare(HostIP o1, HostIP o2) {
            long weight1 = o1.getFailNum() * FAIL_WEIGHT + o1.rtt * RTT_WEIGHT + o1.ttl * ttl_WEIGHT;
            long weight2 = o2.getFailNum() * FAIL_WEIGHT + o2.rtt * RTT_WEIGHT + o2.ttl * ttl_WEIGHT;
            return weight1 > weight2 ? 1 : weight1 == weight2 ? 0 : -1;
        }
    };
}
