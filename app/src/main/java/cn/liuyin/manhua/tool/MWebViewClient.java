package cn.liuyin.manhua.tool;


import android.graphics.Bitmap;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import cn.liuyin.manhua.activity.ReadActivity;

public class MWebViewClient extends WebViewClient {
    ReadActivity mActivity;

    public MWebViewClient(ReadActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO: Implement this method
        super.onPageStarted(view, url, favicon);
        //getClearAdDivJs(view);

    }


    @Override
    public void onPageCommitVisible(WebView view, String url) {
        // TODO: Implement this method
        super.onPageCommitVisible(view, url);
        ComonTool.removeAd(view);
        //main-bar
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO: Implement this method
        if (url.contains("pufei.net")) {
            Toast.makeText(mActivity, url, Toast.LENGTH_SHORT).show();
            //mActivity.getData(url);
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO: Implement this method
        super.onPageFinished(view, url);
        //Toast.makeText(view.getContext(),"ok"+urls,1).show();


        //view.loadUrl("javascript:alert('hhh');");
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        //footer.js
        // TODO: Implement this method
        if (url.contains("iymbl-app.js") || url.contains("header.js") || url.contains("/skin/middle.js") || url.contains("footer.js")) {
            return new WebResourceResponse(null, null, null);
        } else if (!(url.contains("pufei.net"))) {
            //urls+=url+"\n";
            return new WebResourceResponse(null, null, null);
        }
        return super.shouldInterceptRequest(view, url);
    }


}
