package com.wzh.fun.chat.activity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wzh.fun.R;
import com.wzh.fun.ui.activity.BaseActivity;

/**
 * author：Administrator on 2016/12/19 17:25
 * e-mail：wenzh@bbtree.com
 * description:文件说明
 * version:版本
 */
public class ImageLookActivity extends BaseActivity {
    private static String url = "";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);
        setBackView();
        setTitle("查看图片");
        url = getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview);
        // 启用javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/index.html");
        webView.setWebViewClient(new WebViewClientDemo());
    }
    private class WebViewClientDemo extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);// 当打开新链接时，使用当前的 WebView，不会使用系统其他浏览器
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //在这里执行你想调用的js函数
            webView.loadUrl("javascript:javacalljswith('" + ImageLookActivity.url + "')");
        }
    }
}
