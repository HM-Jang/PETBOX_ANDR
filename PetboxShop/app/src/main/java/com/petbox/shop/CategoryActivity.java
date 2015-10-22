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
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Adapter.Pager.CategoryPagerAdapter;
import com.petbox.shop.Adapter.Pager.MyPagePagerAdapter;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.CustomView.NonSwipeableViewPager;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Network.LoginManager;

import java.util.Calendar;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{

    public static final String TAG = "CategoryAct";
    public static int page_num = 0;


    private NonSwipeableViewPager mViewPager;
    private CategoryPagerAdapter categoryPagerAdapter;
    private FragmentManager fragmentManager;
    public static int menu_selected = 1;  // 0: 펫박스홈, 1: 카테고리, 2: 검색, 3: 마이페이지

    private Toolbar toolbar;
    ImageButton ibtn_home, ibtn_category, ibtn_search, ibtn_login, ibtn_mypage;
    ImageButton ibtn_menu_wish, ibtn_menu_cart;

    EditText edit_search;
    ImageView iv_logo, iv_search;

    int mainColor = 0;

    String bf="";

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        /*
        mTracker.setScreenName("전체 카테고리");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        */

        Intent intent = getIntent();
        bf = intent.getStringExtra("activity");

        mainColor = getResources().getColor(R.color.colorPrimary);

        edit_search = (EditText)findViewById(R.id.edit_search);

        edit_search.setOnClickListener(this);

        iv_logo = (ImageView)findViewById(R.id.iv_logo);
        iv_logo.setOnClickListener(this);

        ibtn_menu_cart = (ImageButton)findViewById(R.id.ibtn_menu_cart);
        ibtn_menu_cart.setOnClickListener(this);

        iv_search = (ImageView)findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);

        ibtn_home = (ImageButton)findViewById(R.id.ibtn_home);
        ibtn_home.setOnClickListener(this);

        ibtn_category = (ImageButton)findViewById(R.id.ibtn_category);
        ibtn_category.setOnClickListener(this);

        ibtn_search = (ImageButton)findViewById(R.id.ibtn_search);
        ibtn_search.setOnClickListener(this);

        ibtn_login = (ImageButton)findViewById(R.id.ibtn_login);
        ibtn_login.setOnClickListener(this);

        ibtn_mypage = (ImageButton)findViewById(R.id.ibtn_mypage);
        ibtn_mypage.setOnClickListener(this);

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);

        fragmentManager = getSupportFragmentManager();
        mViewPager.setOffscreenPageLimit(1);

        categoryPagerAdapter = new CategoryPagerAdapter(fragmentManager);

        mViewPager.setAdapter(categoryPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.iv_logo:
                finish();
                overridePendingTransition(0, 0);
                break;

            case R.id.ibtn_home:
                //Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                break;

            case R.id.ibtn_menu_cart:
                if(!LoginManager.getIsLogin()){ // 현재 로그인x
                    Intent cart_intnet = new Intent(CategoryActivity.this, LoginActivity.class);
                    startActivity(cart_intnet);
                    overridePendingTransition(0,0);
                }else{  // 현재 로그인 o
                    Intent cart_intnet = new Intent(CategoryActivity.this, CartListWebView.class);
                    startActivity(cart_intnet);
                    overridePendingTransition(0,0);
                }
                //Toast.makeText(getApplicationContext(), "menu_cart", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ibtn_category:
                //Intent category_intnet = new Intent(this, CategoryActivity.class);
                //category_intnet.putExtra("activity","search");
                //startActivity(category_intnet);
                finish();
                overridePendingTransition(0, 0);
                break;

            case R.id.ibtn_login:
                Intent login_intnet = new Intent(this, LoginActivity.class);
                startActivityForResult(login_intnet, Constants.REQ_LOGIN);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                break;

            case R.id.ibtn_search:
                //Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
                Intent search_intnet = new Intent(this, SearchActivity.class);
                startActivity(search_intnet);
                finish();
                overridePendingTransition(0, 0);
                break;

            case R.id.ibtn_mypage:
                Intent mypage_intnet = new Intent(this, MypageActivity.class);
                mypage_intnet.putExtra("activity", "search");
                startActivity(mypage_intnet);
                finish();
                overridePendingTransition(0, 0);

                break;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "++ ON START ++");
        mTracker.setScreenName("전체 카테고리");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

        Log.e("CateogoryActivity", "===========================================================onStart");
        LoginManager.getHttpClient();

        if(LoginManager.getIsLogin()){
            ibtn_login.setVisibility(View.GONE);
            ibtn_mypage.setVisibility(View.VISIBLE);
        }else{
            ibtn_login.setVisibility(View.VISIBLE);
            ibtn_mypage.setVisibility(View.GONE);
        }
        //FlurryAgent.onStartSession(this, Constants.FLURRY_APIKEY);
        //mFlurryAdInterstitial.fetchAd(); FLURRY
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG, "++ ON STOP ++");
        //FlurryAgent.onEndSession(this);
        GoogleAnalytics.getInstance(this).reportActivityStop(this);


        Log.e("CategoryActivity", "===========================================================onStop");
    }

    @Override
    public void onDestroy(){
        //mFlurryAdInterstitial.destroy(); FLURRY
        super.onDestroy();
    }

    private void clearBackStack() {
        Log.e("CategoryActivity", "===========================================================clearBackStack");
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for(int i=0; i< fragmentManager.getFragments().size(); i++){
            Log.i(TAG, i+"번째 Fragment REMOVE");
            fragmentTransaction.remove(fragmentManager.getFragments().get(i));
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(0, 0);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();

        switch(position){
            case 0:
                mTracker.setScreenName("전체 카테고리");
                System.out.println("VIEWPAGER - 전체 카테고리");
                break;
        }

        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
