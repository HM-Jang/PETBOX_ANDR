package com.petbox.shop;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.CustomView.WebProgressDialog;
import com.petbox.shop.DB.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class CartListWebView extends AppCompatActivity implements WebView.OnKeyListener {

    WebView webView;

    /*151019_seo_start*/
    ImageView ibtn_good_info_back;
        /*151019_seo_end*/

    Tracker mTracker;

    WebProgressDialog progressDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list_web_view);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        mTracker.setScreenName("장바구니");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
        webView.setOnKeyListener(this);
        //webView.loadUrl(Constants.HTTP_URL_CART);
        webView.getSettings().setJavaScriptEnabled(true);

        progressDialog = WebProgressDialog.show(this, "", "", true, true, null);

        webView.setWebViewClient(new PayWebViewClient());

        webView.loadUrl(Constants.HTTP_URL_CART);
        webView.getSettings().setJavaScriptEnabled(true);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }




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

    class PayWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            webView.setVisibility(View.VISIBLE);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {

            if ((url.startsWith("http://") || url.startsWith("https://")) && (url.contains("market.android.com") || url.contains("m.ahnlab.com/kr/site/download"))) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException e) {
                    return false;
                }
            } else if (url != null
                    && (url.contains("vguard") || url.contains("droidxantivirus") || url.contains("smhyundaiansimclick://")
                    || url.contains("smshinhanansimclick://") || url.contains("smshinhancardusim://") || url.contains("smartwall://") || url.contains("appfree://")
                    || url.contains("v3mobile") || url.endsWith(".apk") || url.contains("market://") || url.contains("ansimclick")
                    || url.contains("market://details?id=com.shcard.smartpay") || url.contains("shinhan-sr-ansimclick://"))) {
                Intent intent = null;
                // 인텐트 정합성 체크 : 2014 .01추가
                try {
                    intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    //Log.e("intent getScheme     +++===>", intent.getScheme());
                    // Log.e("intent getDataString +++===>", intent.getDataString());
                } catch (URISyntaxException ex) {
                    Log.e("Browser", "Bad URI " + url + ":" + ex.getMessage());
                    return false;
                }
                try {
                    boolean retval = true;
                    //chrome 버젼 방식 : 2014.01 추가
                    if (url.startsWith("intent")) { // chrome 버젼 방식
                        // 앱설치 체크를 합니다.
                        if (getPackageManager().resolveActivity(intent, 0) == null) {
                            String packagename = intent.getPackage();
                            if (packagename != null) {
                                Uri uri = Uri.parse("market://search?q=pname:" + packagename);
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                retval = true;
                            }
                        } else {
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setComponent(null);
                            try {
                                if (startActivityIfNeeded(intent, -1)) {
                                    retval = true;
                                }
                            } catch (ActivityNotFoundException ex) {
                                retval = false;
                            }
                        }
                    } else { // 구 방식
                        Uri uri = Uri.parse(url);
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        retval = true;
                    }
                    return retval;
                } catch (ActivityNotFoundException e) {
                    Log.e("error ===>", e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }else{
                //return super.shouldOverrideUrlLoading(view, url);
                return false;
            }
        }

            // 비동기 File Download 구현
        private void downloadFile(String mUrl) {
            new DownloadFileTask().execute(mUrl);
        }

        // AsyncTask<Params,Progress,Result>
        private class DownloadFileTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                URL myFileUrl = null;
                try {
                    myFileUrl = new URL(urls[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();

                    // 다운 받는 파일의 경로는 sdcard/ 에 저장되며 sdcard에 접근하려면 uses-permission에 android.permission.WRITE_EXTERNAL_STORAGE을 추가해야만 가능.
                    String mPath = "sdcard/v3mobile.apk";
                    FileOutputStream fos;
                    File f = new File(mPath);
                    if (f.createNewFile()) {
                        fos = new FileOutputStream(mPath);
                        int read;
                        while ((read = is.read()) != -1) {
                            fos.write(read);
                        }
                        fos.close();
                    }

                    return "v3mobile.apk";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String filename) {
                if (!"".equals(filename)) {
                    Toast.makeText(getApplicationContext(), "download complete", Toast.LENGTH_SHORT).show();

                    // 안드로이드 패키지 매니저를 사용한 어플리케이션 설치.
                    String filePath = "";

                    /*
                    if(Build.VERSION.SDK_INT > 18)
                        filePath = Environment.getExternalStorageDirectory() + "/" + filename;
                    else
                        filePath = Environment.getExternalStorageDirectory() + filename;
                    */

                    filePath = Environment.getExternalStorageDirectory() + "/" + filename;
                    System.out.println("APKFILE : " + filePath);

                    File apkFile = new File(filename);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    startActivity(intent);
                }
            }
        }
    }
}

