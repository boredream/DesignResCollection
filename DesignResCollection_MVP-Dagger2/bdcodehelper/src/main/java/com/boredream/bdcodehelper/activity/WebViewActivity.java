package com.boredream.bdcodehelper.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boredream.bdcodehelper.R;
import com.boredream.bdcodehelper.base.BoreBaseActivity;
import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;

public class WebViewActivity extends BoreBaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";

    private WebView webview;
    private String title;
    private TitleBuilder titleBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        title = getIntent().getStringExtra(EXTRA_TITLE);
        String url = getIntent().getStringExtra(EXTRA_URL);

        initView();

        showProgressDialog();
        webview.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        titleBuilder = initBackTitle(title);
        titleBuilder.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String url = webview.getUrl();
                if(!TextUtils.isEmpty(url)) {
                    AppUtils.copy2clipboard(WebViewActivity.this, url);
                    showToast("网址已经复制到剪贴板");
                }
                return false;
            }
        });

        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebClient());

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private class MyWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            titleBuilder.setTitleText(view.getTitle());
            dismissProgressDialog();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webview.loadUrl(url);
            return true;
        }
    }
}
