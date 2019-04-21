package cn.jony.okhttpplus.lib.okhttp;


import java.io.IOException;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;
import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.InetAddressUtils;
import cn.jony.okhttpplus.lib.httpdns.net.networktype.NetworkManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DnsVisitNetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        Response response = chain.proceed(request);

        final String targetIP = chain.connection().socket().getInetAddress().getHostAddress();
        final boolean isSuc = response.isSuccessful();
        // TODO: 2017/3/8 这里的rtt可能并非一次rtt，这里为了简便使用请求时间来默认代替rtt
        final long rtt;
        if (NetworkManager.getInstance().isNetOK()) {
            rtt = System.currentTimeMillis() - startTime;
        } else {
            rtt = -1;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetworkManager.getInstance().isNetOK() &&
                        InetAddressUtils.isIPAddress(targetIP)) {
                    HostIP ip = DNSCache.Instance.getIP(NetworkManager.getInstance().ipAddress, targetIP);
                    if (ip != null) {
                        ip.updateRtt(rtt, isSuc);
                        ip.visitSinceSaved++;
                        DNSCache.Instance.updateIP(ip);
                    }
                }
            }
        }).start();
        return response;
    }
}
