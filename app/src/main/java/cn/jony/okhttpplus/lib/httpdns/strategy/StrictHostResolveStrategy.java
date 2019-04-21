package cn.jony.okhttpplus.lib.httpdns.strategy;


import android.util.Log;

import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;
import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.util.RealTimeThreadPool;

public class StrictHostResolveStrategy extends DefaultHostResolveStrategy {
    @Override
    public boolean isReliable(HostIP ip) {
        return super.isReliable(ip) && ip.hasUsed();
    }

    /**
     * 测速后依然只更新db，cache通过db更新
     *
     * @param ipList
     */
    @Override
    protected void doSpeedTest(final List<HostIP> ipList) {
        Log.d("strict", "do speed test");
        for (HostIP ip : ipList) {
            int rtt = speedTestManager.speedTest(ip.targetIP, ip.host);
            Log.d("strict", "[" + ip.targetIP + "] 'rtt is : " + rtt);
            ip.updateRtt(rtt, rtt > 0);
            cache.remove(new HostIP.Key(ip.host, ip.sourceIP));
        }

        RealTimeThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                DNSCache.Instance.getDbHelper().updateIpList(ipList);
            }
        });
    }
}
