package cn.liuyin.manhua.tool;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComonTool {

    //url
    private static final String ENCODE = "utf-8";
    //md5
    protected static char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

    /**
     * 返回能被一般文件浏览器正确排序的文件名
     *
     * @param name    漫画的名称
     * @param chapter 章节的索引
     * @param index   图片的索引
     * @return
     */
    public static String getFixedFileName(String name, int chapter, int index) {
        DecimalFormat df1 = new DecimalFormat("0000");
        DecimalFormat df2 = new DecimalFormat("00");
        return name + "/[" + name + "][" + df1.format(chapter) + "][" + df2.format(index) + "].jpg";
    }

    public static String getURLDecoderString(String paramString) {
        String str = "";
        if (paramString == null) {
            return "";
        }
        try {
            paramString = URLDecoder.decode(paramString, "utf-8");
            return paramString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getURLEncoderString(String paramString) {
        String str = "";
        if (paramString == null) {
            return "";
        }
        try {
            paramString = URLEncoder.encode(paramString, "utf-8");
            return paramString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void appendHexPair(byte paramByte, StringBuffer paramStringBuffer) {
        char c1 = hexDigits[((paramByte & 0xF0) >> 4)];
        char c2 = hexDigits[(paramByte & 0xF)];
        paramStringBuffer.append(c1);
        paramStringBuffer.append(c2);
    }

    private static String bufferToHex(byte[] paramArrayOfByte) {
        return bufferToHex(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    private static String bufferToHex(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        StringBuffer localStringBuffer = new StringBuffer(paramInt2 * 2);
        int i = paramInt1;
        while (i < paramInt1 + paramInt2) {
            appendHexPair(paramArrayOfByte[i], localStringBuffer);
            i += 1;
        }
        return localStringBuffer.toString();
    }

    public static boolean checkPassword(String paramString1, String paramString2) {
        return getMD5String(paramString1).equals(paramString2);
    }

    /**
     * 返回文件的md5值
     *
     * @param path 要加密的文件的路径
     * @return 文件的md5值
     */
    public static String getFileMD5String(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            //获取MD5加密器
            MessageDigest md = MessageDigest.getInstance("md5");
            //类似读取文件
            byte[] bytes = new byte[10240];//一次读取写入10k
            int len;
            while ((len = fis.read(bytes)) != -1) {//从原目的地读取数据
                //把数据写到md加密器，类比fos.write(bytes, 0, len);
                md.update(bytes, 0, len);
            }
            //读完整个文件数据，并写到md加密器中
            byte[] digest = md.digest();//完成加密，得到md5值，但是是byte类型的。还要做最后的转换
            for (byte b : digest) {//遍历字节，把每个字节拼接起来
                //把每个字节转换成16进制数
                int d = b & 0xff;//只保留后两位数
                String herString = Integer.toHexString(d);//把int类型数据转为16进制字符串表示
                //如果只有一位，则在前面补0.让其也是两位
                if (herString.length() == 1) {//字节高4位为0
                    herString = "0" + herString;//拼接字符串，拼成两位表示
                }
                sb.append(herString);
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }

    /**
     * 对传递过来的字符串进行md5加密
     *
     * @param str 待加密的字符串
     * @return 字符串Md5加密后的结果
     */
    public static String getMD5String(String str) {
        StringBuilder sb = new StringBuilder();//字符串容器
        try {
            //获取md5加密器.public static MessageDigest getInstance(String algorithm)返回实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();//把要加密的字符串转换成字节数组
            byte[] digest = md.digest(bytes);//使用指定的 【byte 数组】对摘要进行最后更新，然后完成摘要计算。即完成md5的加密

            for (byte b : digest) {
                //把每个字节转换成16进制数
                int d = b & 0xff;//只保留后两位数
                String herString = Integer.toHexString(d);//把int类型数据转为16进制字符串表示
                //如果只有一位，则在前面补0.让其也是两位
                if (herString.length() == 1) {//字节高4位为0
                    herString = "0" + herString;//拼接字符串，拼成两位表示
                }
                sb.append(herString);
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sb.toString();
    }

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
                return info.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void enabeCache(WebView myWebView) {
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//设置渲染的优先级
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
// 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
//开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        String cacheDirPath = myWebView.getContext().getFilesDir().getAbsolutePath() + "APP_CACAHE_DIRNAME";
//设置数据库缓存路径
        //webSettings.setDatabasePath(cacheDirPath);
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
        String[] adDivs = list.toArray(new String[0]);
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
        Objects.requireNonNull(clipboard).setPrimaryClip(clip);
        Toast.makeText(context, "复制成功。", Toast.LENGTH_LONG).show();
    }
}
