package cn.jony.okhttpplus.lib.okhttp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import cn.jony.okhttpplus.lib.httpdns.DNSCache;
import cn.jony.okhttpplus.lib.httpdns.util.EmptyUtil;
import okhttp3.Dns;


public final class HttpDNS implements Dns {
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) throw new UnknownHostException("hostname == null");
        List<InetAddress> addresses = DNSCache.Instance.lookup(hostname);
        return EmptyUtil.isCollectionEmpty(addresses) ? Arrays.asList(InetAddress.getAllByName
                (hostname)) : addresses;
    }
}
