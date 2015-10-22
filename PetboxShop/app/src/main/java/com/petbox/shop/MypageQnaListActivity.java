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

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.CustomView.WebProgressDialog;
import com.petbox.shop.DB.Constants;

public class MypageQnaListActivity extends AppCompatActivity implements WebView.OnKeyListener {

    WebView webView;
    ImageView qna_back;

    Tracker mTracker;

    WebProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_qna_list);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        mTracker.setScreenName("마이페이지 - 나의 QnA");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        webView = (WebView) findViewById(R.id.wv_qna);
        webView.loadUrl(Constants.HTTP_URL_QNA);
        webView.getSettings().setJavaScriptEnabled(true);

        qna_back = (ImageView) findViewById(R.id.ibtn_good_info_back);

        progressDialog = WebProgressDialog.show(this, "", "", true, true, null);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url){
                webView.setVisibility(View.VISIBLE);
                if(progressDialog!=null) {
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

        qna_back.setOnClickListener(buttonListener);
    }

    @Override
    public void onStart(){
        super.onStart();
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
