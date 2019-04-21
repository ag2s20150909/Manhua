package cn.jony.okhttpplus.lib.httpdns.strategy;


import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.net.networktype.InetAddressUtils;
import cn.jony.okhttpplus.lib.httpdns.util.EmptyUtil;

public abstract class AbsHostResolveStrategy implements HostResolveStrategy {
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (TextUtils.isEmpty(hostname)) throw new UnknownHostException("hostname == null");
        if (InetAddressUtils.isIPv4Address(hostname) || InetAddressUtils.isIPv6Address(hostname))
            return null;

        List<InetAddress> result = lookupInMemory(hostname);
        if (EmptyUtil.isCollectionEmpty(result)) {
            result = lookupInDB(hostname);
            if (EmptyUtil.isCollectionEmpty(result)) {
                result = lookupNet(hostname);
            }
        }
        return result;
    }

    public abstract List<InetAddress> lookupInMemory(String hostname);

    public abstract List<InetAddress> lookupInDB(String hostname);

    public abstract List<InetAddress> lookupNet(String hostname);


}
