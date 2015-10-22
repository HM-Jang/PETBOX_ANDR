package com.petbox.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.DB.Constants;

//고객센터
public class MypageCusomerActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView cusomer_back;
    Button btn_call, btn_email;

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_cusomer);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        mTracker.setScreenName("마이페이지 - 고객센터");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        btn_call = (Button) findViewById(R.id.btn_service_center_call);
        btn_call.setOnClickListener(this);

        btn_email = (Button) findViewById(R.id.btn_service_center_email);
        btn_email.setOnClickListener(this);

        cusomer_back = (ImageView)findViewById(R.id.ibtn_service_center_back);
        cusomer_back.setOnClickListener(this);

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
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btn_service_center_call:
                Intent intent = new Intent( Intent.ACTION_DIAL );
                intent.setData( Uri.parse("tel:07087621500") );
                startActivity(intent);
                break;

            case R.id.btn_service_center_email:
                Intent it = new Intent(Intent.ACTION_SEND);
                String[] mailaddr = {"hello@petbox.kr"};

                it.setType("plaine/text");
                it.putExtra(Intent.EXTRA_EMAIL, mailaddr);
                it.putExtra(Intent.EXTRA_SUBJECT, "[고객문의-앱] : ");
                it.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(it);

                break;

            case R.id.ibtn_service_center_back:
                finish();
                GoogleAnalytics.getInstance(this).reportActivityStop(this);
                break;

        }
    }
}
