package cn.jony.okhttpplus.lib.httpdns.strategy;


import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.model.HostIP;
import cn.jony.okhttpplus.lib.httpdns.net.UrlHelper;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;

public class SyncHostResolveStrategy extends DefaultHostResolveStrategy {
    private static final String TAG = SyncHostResolveStrategy.class.getSimpleName();

    @Override
    public List<InetAddress> lookupNet(final String hostname) {
        final List<InetAddress> inetAddressList = new ArrayList<>();

        Call call = okHttpClient.newCall(new Request.Builder().url(UrlHelper.getDnsRequestUrl
                (hostname)).method("GET", null).cacheControl(CacheControl.FORCE_NETWORK).build
                ());
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                final BufferedSource source = response.body().source();
                final List<HostIP> ipList = new ArrayList<>();
                parseDnsPod(source, ipList, inetAddressList, hostname);

                updateCache(ipList, hostname);
            }
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return inetAddressList;
    }
}
