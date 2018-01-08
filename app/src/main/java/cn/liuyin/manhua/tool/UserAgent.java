package cn.liuyin.manhua.tool;

import android.webkit.WebSettings;

import java.util.ArrayList;
import java.util.Random;

import cn.liuyin.manhua.APP;

/**
 * Created by asus on 2017/12/29.
 */

public class UserAgent {
    ArrayList<String> ualists;

    public String getUA() {
        Random random = new Random();
        int s = random.nextInt(ualists.size());
        System.err.println(ualists.get(s));
        return ualists.get(s);
    }

    public UserAgent() {
        ualists = new ArrayList<>();
        ualists.add(WebSettings.getDefaultUserAgent(APP.getContext()));
        ualists.add("Mozilla/5.0 (iPhone 6sp; CPU iPhone OS 9_2 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 MQQBrowser/7.4.1 Mobile/13C75 Safari/8536.25 MttCustomUA/2 QBWebViewType/1 WKType/1");
        ualists.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_2 like Mac OS X) AppleWebKit/602.3.12 (KHTML, like Gecko) Mobile/14C92 MicroMessenger/6.5.9 NetType/WIFI Language/zh_CN");
        ualists.add("Mozilla/5.0 (Linux; U; Android 5.1; zh-cn; m1 metal Build/LMY47I) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/7.6 Mobile Safari/537.36");
        ualists.add("Mozilla/5.0 (Linux; Android 5.1.1; vivo X7 Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/48.0.2564.116 Mobile Safari/537.36 baiduboxapp/8.6.5 (Baidu; P1 5.1.1)");
        ualists.add("Mozilla/5.0 (Linux; Android 6.0; MP1512 Build/MRA58K) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/35.0.1916.138 Mobile Safari/537.36 T7/7.4 baiduboxapp/8.4 (Baidu; P1 6.0)");
        ualists.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Mobile/14E304 MicroMessenger/6.3.19 NetType/4G Language/zh_CN");
        ualists.add("Mozilla/5.0 (iPhone 6s; CPU iPhone OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 MQQBrowser/7.5.1 Mobile/13G36 Safari/8536.25 MttCustomUA/2 QBWebViewType/1 WKType/1");
        ualists.add("Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; HUAWEI MT7-TL00 Build/HuaweiMT7-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.3.8.909 Mobile Safari/537.36");
        ualists.add("Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; F5121 Build/34.0.A.1.247) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.5.1.944 Mobile Safari/537.36");

    }

}
