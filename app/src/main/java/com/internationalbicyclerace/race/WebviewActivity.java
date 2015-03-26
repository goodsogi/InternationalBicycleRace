package com.internationalbicyclerace.race;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.internationalbicyclerace.IBRConstants;
import com.internationalbicyclerace.R;
import com.pluslibrary.utils.PlusLogger;


public class WebviewActivity extends Activity {
    private Intent mIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mIntent = getIntent();
        String url = (String) mIntent.getExtras().get(IBRConstants.KEY_URL);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                PlusLogger.doIt("facebook url: " + url);

                return false;
            }
        });
        webView.loadUrl(url);
    }
}
