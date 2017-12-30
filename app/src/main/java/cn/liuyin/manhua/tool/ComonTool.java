package cn.liuyin.manhua.tool;


import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComonTool {


    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static void enabeCache(WebView myWebView) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//设置渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
// 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
//开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = myWebView.getContext().getFilesDir().getAbsolutePath() + "APP_CACAHE_DIRNAME";
//设置数据库缓存路径
        webSettings.setDatabasePath(cacheDirPath);
//设置  Application Caches 缓存目录
        webSettings.setAppCachePath(cacheDirPath);
//开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        //webSettings.setBlockNetworkImage(true);
        webSettings.setLoadWithOverviewMode(true);
//设置WebView支持JavaScript
        webSettings.setJavaScriptEnabled(true);
//设置可以访问文件
        webSettings.setAllowFileAccess(true);
//设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


    }

    public static void removeAd(WebView webview) {


        ArrayList<String> list = new ArrayList<>();
        list.add("#down_fixed_link");
        list.add(".header");
        list.add(".main-nav");
        list.add("footer");
        StringBuilder js = new StringBuilder("(function(){");
        String[] adDivs = list.toArray(new String[list.size()]);
        for (int i = 0; i < adDivs.length; i++) {

            js.append("" + "try{" + "var AD").append(i).append("= document.querySelectorAll('").append(adDivs[i]).append("');").append("for(var j=0;j<AD").append(i).append(".length;j++){").append(
                    //"var adDiv" + i + "j=AD" + i + "[j];" +
                    "if(AD").append(i).append("[j] != null){").append("AD").append(i).append("[j].innerHTML='';").append("AD").append(i).append("[j].parentNode.removeChild(AD").append(i).append("[j]);}").append("}").append("}").append("catch(error){console.log(error.toString());}");
        }
        js.append("})();");
        webview.loadUrl("javascript:" + js.toString());
        //return js.toString();
    }


    public static void copy2System(Context context, String cmd) {
        ClipboardManager clipboard = (ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", cmd);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "复制成功。", Toast.LENGTH_LONG).show();
    }
}
