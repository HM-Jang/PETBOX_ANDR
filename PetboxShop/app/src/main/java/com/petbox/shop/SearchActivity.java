package com.petbox.shop;

import android.content.Intent;
import android.support.design.widget.TabLayout;
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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.petbox.shop.Adapter.Pager.CategoryPagerAdapter;
import com.petbox.shop.Adapter.Pager.HomePagerAdapter;
import com.petbox.shop.Adapter.Pager.MyPagePagerAdapter;
import com.petbox.shop.Adapter.Pager.SearchPagerAdapter;
import com.petbox.shop.CustomView.NonSwipeableViewPager;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.DB.DBConnector;
import com.petbox.shop.Network.LoginManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "SearchAct";
    public static int page_num = 0;

    private TabLayout tabLayout;
    private NonSwipeableViewPager mViewPager;
    private SearchPagerAdapter searchPagerAdapter;
    private FragmentManager fragmentManager;
    public static int menu_selected = 2;  // 0: 펫박스홈, 1: 카테고리, 2: 검색, 3: 마이페이지

    private Toolbar toolbar;
    ImageButton ibtn_home, ibtn_category, ibtn_search, ibtn_login, ibtn_mypage;
    ImageButton ibtn_menu_wish, ibtn_menu_cart;

    EditText edit_search;
    ImageView iv_logo, iv_search;

    int mainColor = 0;

    String bf="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        bf = intent.getStringExtra("activity");

        mainColor = getResources().getColor(R.color.colorPrimary);

        edit_search = (EditText)findViewById(R.id.edit_search);

        edit_search.setOnClickListener(this);

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

        searchPagerAdapter = new SearchPagerAdapter(fragmentManager);

        tabLayout = (TabLayout)findViewById(R.id.slide_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.setSelectedTabIndicatorColor(mainColor);
        //tabLayout.setTabTextColors(0xff4e91ff, 0xff000000);
        tabLayout.setTabTextColors(0xff7c7c7c, 0xff000000);

        mViewPager.setAdapter(searchPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        setVisibleForTab(View.VISIBLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int state) {
                Log.e("onPageSelected", String.valueOf(state));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled position", String.valueOf(position));
                Log.e("onPageScrolled positionOffset", String.valueOf(positionOffset));
                Log.e("onPageScrolled positionOffsetPixels", String.valueOf(positionOffsetPixels));
            }

            @Override
            public void onPageScrollStateChanged(int position) {
                Log.e("onPageScrollStateChanged position", String.valueOf(position));
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {
            case R.id.ibtn_home:
                //Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                    finish();
                break;
            case R.id.ibtn_category:
                Intent category_intnet = new Intent(this, CategoryActivity.class);
                category_intnet.putExtra("activity","search");
                startActivity(category_intnet);
                finish();
                break;
            case R.id.ibtn_login:
                Intent login_intnet = new Intent(this, LoginActivity.class);
                startActivityForResult(login_intnet, Constants.REQ_LOGIN);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.ibtn_mypage:

                Intent mypage_intnet = new Intent(this, MypageActivity.class);
                mypage_intnet.putExtra("activity", "search");
                startActivity(mypage_intnet);
                finish();

                break;

            case R.id.iv_search:
                String searchContent = edit_search.getText().toString();

                if(!searchContent.isEmpty()){   //검색란이 비어있지 않을 때
                    //edit_search.setText("");
                    SimpleDateFormat format = new SimpleDateFormat("MM-dd");
                    Date currentTime = new Date ( );

                    String today = format.format(currentTime);
                    new DBConnector(getApplicationContext()).insertToRecentSearch(searchContent, today);

                    Intent intent = new Intent(this, SearchGoodActivity.class);
                    intent.putExtra("keyword",edit_search.getText().toString());
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "검색란이 비어있습니다.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    public void setVisibleForTab(int viewMode) {
        tabLayout.setVisibility(viewMode);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "++ ON START ++");

        Log.e("MAinActivity", "===========================================================onStart");
        LoginManager.getHttpClient();

        if(LoginManager.getIsLogin()){
            ibtn_login.setVisibility(View.GONE);
            ibtn_mypage.setVisibility(View.VISIBLE);
        }

        //FlurryAgent.onStartSession(this, Constants.FLURRY_APIKEY);
        //mFlurryAdInterstitial.fetchAd(); FLURRY
    }

    @Override
    public void onStop(){
        Log.i(TAG, "++ ON STOP ++");
        //FlurryAgent.onEndSession(this);
        super.onStop();
        Log.e("MAinActivity", "===========================================================onStop");
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


}
