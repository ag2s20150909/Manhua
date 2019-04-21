package cn.jony.okhttpplus.lib.httpdns.net;


public class UrlHelper {
    private static final String DNS_POS_HOST = "http://119.29.29.29/";

    public static String getDnsRequestUrl(String host) {
        return DNS_POS_HOST + "d?dn=" + host + "&ttl=1";
    }
}
