package com.petbox.shop;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.petbox.shop.Adapter.Pager.MyPagePagerAdapter;
import com.petbox.shop.Adapter.Pager.SearchPagerAdapter;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.CustomView.NonSwipeableViewPager;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Network.LoginManager;

public class MypageActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "MypageAct";
    public static int page_num = 0;


    private NonSwipeableViewPager mViewPager;
    private MyPagePagerAdapter myPagePagerAdapter;
    private FragmentManager fragmentManager;
    public static int menu_selected = 3;  // 0: 펫박스홈, 1: 카테고리, 2: 검색, 3: 마이페이지

    private Toolbar toolbar;
    ImageButton ibtn_home, ibtn_category, ibtn_search, ibtn_login, ibtn_mypage;
    ImageButton ibtn_back, ibtn_menu_cart;

    EditText edit_search;
    ImageView iv_logo, iv_search;

    int mainColor = 0;

    String bf="";

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        mTracker.setScreenName("마이페이지");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        Intent intent = getIntent();
        bf = intent.getStringExtra("activity");

        mainColor = getResources().getColor(R.color.colorPrimary);

        ibtn_back = (ImageButton)findViewById(R.id.ibtn_mypage_back);
        ibtn_back.setOnClickListener(this);

        ibtn_home = (ImageButton)findViewById(R.id.ibtn_home);
        ibtn_home.setOnClickListener(this);

        ibtn_menu_cart = (ImageButton)findViewById(R.id.ibtn_menu_cart);
        ibtn_menu_cart.setOnClickListener(this);

        ibtn_category = (ImageButton)findViewById(R.id.ibtn_category);
        ibtn_category.setOnClickListener(this);

        ibtn_search = (ImageButton)findViewById(R.id.ibtn_search);
        ibtn_search.setOnClickListener(this);

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);

        fragmentManager = getSupportFragmentManager();
        mViewPager.setOffscreenPageLimit(1);

        myPagePagerAdapter = new MyPagePagerAdapter(fragmentManager);

        mViewPager.setAdapter(myPagePagerAdapter);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent category_intnet = new Intent(this, CategoryActivity.class);
        switch(id) {

            case R.id.ibtn_menu_cart:
                Intent cart_intnet = new Intent(this, CartListWebView.class);
                startActivity(cart_intnet);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                overridePendingTransition(0, 0);
                //Toast.makeText(getApplicationContext(), "menu_cart", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ibtn_mypage_back:
                category_intnet.putExtra("activity", "mypage");
                startActivity(category_intnet);
                finish();
                overridePendingTransition(0, 0);

                break;
            case R.id.ibtn_home:
                //Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.ibtn_category:
                category_intnet.putExtra("activity", "mypage");
                startActivity(category_intnet);
                finish();
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "++ ON START ++");
        mTracker.setScreenName("마이페이지");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG, "++ ON STOP ++");
        //FlurryAgent.onEndSession(this);
        GoogleAnalytics.getInstance(this).reportActivityStop(this);

        Log.e("MyPageActivity", "===========================================================onStop");
    }

    @Override
    public void onDestroy(){
        //mFlurryAdInterstitial.destroy(); FLURRY
        super.onDestroy();
    }

    private void clearBackStack() {
        Log.e("MAinActivity", "===========================================================clearBackStack");
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for(int i=0; i< fragmentManager.getFragments().size(); i++){
            Log.i(TAG, i+"번째 Fragment REMOVE");
            fragmentTransaction.remove(fragmentManager.getFragments().get(i));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setFragmentItem(int fragmentItem) {
        mViewPager.setCurrentItem(fragmentItem);
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(0, 0);
    }

}
