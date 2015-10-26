package com.petbox.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.CustomView.WebProgressDialog;
import com.petbox.shop.DB.Constants;

public class GoodsReviewActivity extends AppCompatActivity implements WebView.OnKeyListener {

    WebView webView;
    String goodsno;
    Tracker mTracker;

    WebProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_review);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();

        Intent intent = getIntent();
        goodsno = "goodsno=" + intent.getStringExtra("goodsno");
        webView = (WebView) findViewById(R.id.wv_review);
        webView.getSettings().setJavaScriptEnabled(true);

        progressDialog = WebProgressDialog.show(this, "", "", true, true, null);

        //webView.loadUrl(Constants.HTTP_URL_CART);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                if (progressDialog != null) {
                    progressDialog.dismiss();

                    /*
                    if(Build.VERSION.SDK_INT >= 21){
                        view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                        CookieManager cookieManager = CookieManager.getInstance();
                        cookieManager.setAcceptCookie(true);
                        cookieManager.setAcceptThirdPartyCookies(view, true);
                    }
                    */
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(Constants.HTTP_URL_GOODS_REVIEW + goodsno);


    }

    @Override
    public void onStart(){
        super.onStart();
        mTracker.setScreenName("상세상품 - 고객리뷰");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
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
