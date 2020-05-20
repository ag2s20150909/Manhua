package cn.liuyin.manhua.tool.okhttp;

import android.content.Context;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.liuyin.manhua.tool.FileTool;
import okhttp3.Dns;


public class OkHttpDns implements Dns {
    private static final Dns SYSTEM = Dns.SYSTEM;
    //HttpDnsService httpdns;//httpdns 解析服务
    private static OkHttpDns instance = null;

    private OkHttpDns(Context context) {
        //this.httpdns = HttpDns.getService(context, "account id");
    }

    public static OkHttpDns getInstance(Context context) {
        if (instance == null) {
            instance = new OkHttpDns(context);
        }
        return instance;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        //通过异步解析接口获取ip
        //FileTool.writeLog("dns log.txt","DNS:"+hostname);
        String ip;


        ip = DiskCache.getInstance().getString(hostname);

        HTTPDnsTool.getInstence().getQuad9Ip(hostname);
        //String ip = httpdns.getIpByHostAsync(hostname);
        if (ip != null) {
            //如果ip不为null，直接使用该ip进行网络请求
            FileTool.writeLog("dns log.txt", "HttpDNS:" + hostname + " IP:" + ip);
            String[] ips = ip.split(";");
            List<InetAddress> inetAddresses = new ArrayList<>();
            for (String s : ips) {
                inetAddresses.addAll(Arrays.asList(InetAddress.getAllByName(s)));
            }
            //List<InetAddress> inetAddresses = Arrays.asList();
            Log.e("OkHttpDns", "inetAddresses:" + inetAddresses);
            return inetAddresses;
        }
        //如果返回null，走系统DNS服务解析域名
//		FileTool.writeLog("dns log.txt","DNS:"+hostname);
        //HTTPDnsTool.getQuad9Ip(hostname);
        return Dns.SYSTEM.lookup(hostname);
    }
}
