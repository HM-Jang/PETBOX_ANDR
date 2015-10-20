package com.petbox.shop;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.petbox.shop.DB.Constants;

public class CartListWebView extends AppCompatActivity implements WebView.OnKeyListener {

    WebView webView;

    /*151019_seo_start*/
    ImageView ibtn_good_info_back;
        /*151019_seo_end*/

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list_web_view);

            /*151019_seo_start*/
        ibtn_good_info_back = (ImageView)findViewById(R.id.ibtn_good_info_back);
        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이벤트 처리
                switch(v.getId()) {
                    case R.id.ibtn_good_info_back:
                        finish();
                        break;
                }
            }
        };
        ibtn_good_info_back.setOnClickListener(buttonListener);
            /*151019_seo_end*/

        webView = (WebView) findViewById(R.id.wv_cart);
        //webView.loadUrl(Constants.HTTP_URL_CART);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(Constants.HTTP_URL_CART);
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

