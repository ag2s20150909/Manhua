package cn.liuyin.manhua.tool;


import android.app.Activity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MWebChromeClient extends WebChromeClient {
    private Activity mActivity;

    public MWebChromeClient(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        // TODO: Implement this method
        super.onReceivedTitle(view, title);
        mActivity.setTitle(title);
    }

}
