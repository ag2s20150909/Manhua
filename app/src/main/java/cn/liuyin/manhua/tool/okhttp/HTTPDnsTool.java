package cn.liuyin.manhua.tool.okhttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.liuyin.manhua.tool.HttpTool;

public class HTTPDnsTool {
    public static HTTPDnsTool mInstance;
    public ArrayList<String> mBlackList;
    private ExecutorService singleThreadExecutor;

    private HTTPDnsTool() {
        singleThreadExecutor = Executors.newFixedThreadPool(3);
        mBlackList = new ArrayList<>();
    }

    public static HTTPDnsTool getInstence() {
        if (mInstance == null) {
            mInstance = new HTTPDnsTool();
        }
        return mInstance;
    }

    public static void getCloudflareIP(String name) {
        String url = "https://cloudflare-dns.com/dns-query?type=A&name=" + name;


        // TODO: Implement this method
        String json = HttpTool.httpGet(url);
        try {
            StringBuilder ip = new StringBuilder();
            JSONArray ja = new JSONObject(json).getJSONArray("Answer");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo.getInt("type") == 1) {
                    ip.append(jo.getString("data")).append(";");
                }
            }
            if (ip.length() > 4) {
                ip = new StringBuilder(ip.substring(0, ip.lastIndexOf(";")));
//				FileTool.writeLog("httpdns.txt", "GETDNS:" + name + "IP:" + ip);

                DiskCache.getInstance().saveString(name, ip.toString(), 0);
            }
        } catch (Exception e) {

        }


    }

    public static void getTencentIP(String name) {
        //http://203.107.1.34/198121/d?host=api073nwc.zhuishushenqi.com
        String api = "http://119.29.29.29/d?dn=" + name;
        String ip = HttpTool.httpGet(api);
//		if(!(ip.isEmpty()|ip.startsWith("error:"))){
//			APP.getACache().put(name,ip,ACache.TIME_HOUR);
//		}

    }

    public void getQuad9Ip(final String name) {
        Runnable runable = new Runnable() {

            @Override
            public void run() {
                // TODO: Implement this method
                String url = "https://rubyfish.cn//dns-query?name=" + name;

                if (DiskCache.getInstance().getString(name) != null) {
                    return;
                }
                // TODO: Implement this method
                String json = HttpTool.httpGet(url);

                try {
                    StringBuilder ip = new StringBuilder();
                    String str = "";
                    JSONArray ja = new JSONObject(json).getJSONArray("Answer");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        if (jo.getInt("type") == 1) {
                            ip.append(jo.getString("data")).append(";");
                            //str=jo.getString("Expires");
                        }
                    }
                    if (ip.length() > 3) {
                        ip = new StringBuilder(ip.substring(0, ip.lastIndexOf(";")));
//							Date old=new Date(Date.parse(str));
//							Date now=new Date();
//							int time=(int) (old.getTime()-now.getTime())/1000;
                        DiskCache.getInstance().saveString(name, ip.toString(), 60 * 30);
                        //FileTool.writeLog("httpdns.txt","GETDNS:"+name+"IP:"+ip+"TIME:"+time);
                    }
                } catch (Exception e) {
//					FileTool.writeError(e);
                }
            }

        };

        singleThreadExecutor.execute(runable);


    }

}
