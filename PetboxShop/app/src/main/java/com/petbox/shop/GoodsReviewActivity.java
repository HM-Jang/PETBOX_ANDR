package com.petbox.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.petbox.shop.DB.Constants;

public class GoodsReviewActivity extends AppCompatActivity implements WebView.OnKeyListener {

    WebView webView;
    String goodsno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_review);

        Intent intent = getIntent();
        goodsno = "?goodsno=" + intent.getStringExtra("goodsno");
        webView = (WebView) findViewById(R.id.wv_review);

        //webView.loadUrl(Constants.HTTP_URL_CART);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(Constants.HTTP_URL_GOODS_REVIEW + goodsno);
        webView.getSettings().setJavaScriptEnabled(true);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(event.getAction() != KeyEvent.ACTION_DOWN)
            return true;

        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();
            }else{
                this.onBackPressed();
            }
            return true;
        }
        return false;
    }
}
