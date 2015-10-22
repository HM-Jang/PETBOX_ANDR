package com.petbox.shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Network.LoginManager;

public class MypageAppSettingActivity extends AppCompatActivity implements View.OnClickListener {

    String m_id="";
    TextView tv_id,tv_nowver,tv_newver;
    CheckBox ch_push;
    Button btn_logout;
    SharedPreferences sp;
    PackageInfo info;

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_app_setting);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        mTracker.setScreenName("마이페이지 - 앱 설정");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_nowver = (TextView) findViewById(R.id.tv_nowver);

        ch_push = (CheckBox) findViewById(R.id.ch_push);
        ch_push.setOnClickListener(this);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("PUSH", false)) {
            ch_push.setChecked(true);
        }

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        m_id = STPreferences.getString(Constants.PREF_KEY_ID);
        //STPreferences.getint(Constants.PUSH);

        if (m_id != null) {
            tv_id.setText(m_id);
        }

        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tv_nowver.setText((CharSequence) info);
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
    public void onClick(View v) {
        int id = v.getId();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        switch(id){
            case R.id.ch_push:
                if(ch_push.isChecked()){
                    editor.putBoolean("PUSH", true);
                }else if(!ch_push.isChecked()){
                    editor.putBoolean("PUSH", false);
                }
                editor.commit();
                break;

            case R.id.btn_logout:
                CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.setAcceptCookie(true);
                cookieManager.removeSessionCookie();
                cookieSyncManager.sync();
                LoginManager.setIsLogin(false);

                STPreferences.putString(Constants.PREF_KEY_COOKIE, null);
                STPreferences.putString(Constants.PREF_KEY_ID, null);
                STPreferences.putString(Constants.PREF_KEY_PASSWORD, null);
                STPreferences.putString(Constants.PREF_KEY_AUTO_LOGIN, "false");

                Log.e("RES_REGIST_LOGOUT",String.valueOf(Constants.RES_LOGIN_LOGOUT));
                setResult(Constants.RES_LOGIN_LOGOUT);

                this.finish();
                break;
        }

    }
}
