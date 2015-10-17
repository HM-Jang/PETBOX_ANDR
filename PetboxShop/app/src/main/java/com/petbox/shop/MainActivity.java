package com.petbox.shop;


import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import com.flurry.android.FlurryAgent;
import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdInterstitial;
import com.flurry.android.ads.FlurryAdInterstitialListener;
import com.petbox.shop.Adapter.Pager.CategoryPagerAdapter;
import com.petbox.shop.Adapter.Pager.HomePagerAdapter;
import com.petbox.shop.Adapter.Pager.MyPagePagerAdapter;
import com.petbox.shop.Adapter.Pager.SearchPagerAdapter;
import com.petbox.shop.CustomView.NonSwipeableViewPager;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.DB.DBConnector;
import com.petbox.shop.DataStructure.Tree.Tree;
import com.petbox.shop.Delegate.CategoryDelegate;
import com.petbox.shop.Delegate.MyPageDelegate;
import com.petbox.shop.Fragment.Category.CategoryFragment;
import com.petbox.shop.Fragment.Home.BestGoodFragment;
import com.petbox.shop.Fragment.Home.ChanceDealFragment;
import com.petbox.shop.Fragment.Home.EventFragment;
import com.petbox.shop.Fragment.Home.PlanningFragment;
import com.petbox.shop.Fragment.Home.PrimiumFragment;
import com.petbox.shop.Fragment.Home.RegularShippingFragment;
import com.petbox.shop.Fragment.MyPage.MyPageFragment;
import com.petbox.shop.Fragment.MyPage.MyPageOrderList;
import com.petbox.shop.Fragment.MyPage.MypageMileage;
import com.petbox.shop.Fragment.Search.PopularSearchFragment;
import com.petbox.shop.Fragment.Search.RecentSearchFragment;
import com.petbox.shop.Item.CategoryInfo;
import com.petbox.shop.Network.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, CategoryDelegate, MyPageDelegate {

    public static final String TAG = "MainAct";

    private TabLayout tabLayout;
    private NonSwipeableViewPager mViewPager;

    private HomePagerAdapter homePagerAdapter;
    private CategoryPagerAdapter categoryPagerAdapter;
    private SearchPagerAdapter searchPagerAdapter;
    private MyPagePagerAdapter myPagePagerAdapter;

    private FragmentManager fragmentManager;

    int menu_selected = 0;  // 0: 펫박스홈, 1: 카테고리, 2: 검색, 3: 마이페이지

    //SlidingTabLayout mSlidingTabLayout;

    private Toolbar toolbar;
    ImageButton ibtn_home, ibtn_category, ibtn_search, ibtn_login, ibtn_mypage;
    ImageButton ibtn_menu_wish, ibtn_menu_cart;

    EditText edit_search;
    ImageView iv_logo, iv_search;

    int mainColor = 0;

    String display_item_list;
    String category_list;

    String mode;
    String params1,params2,params3,params4;
    String InsertDB;
    SharedPreferences sp;


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "1097126896520";
    private String myResult;


    private GoogleCloudMessaging _gcm;
    private String _regId;



    /* FLURRY
    FlurryAdInterstitialListener interstitialListener = new FlurryAdInterstitialListener() {
        @Override
        public void onFetched(FlurryAdInterstitial flurryAdInterstitial) {
            flurryAdInterstitial.displayAd();
        }

        @Override
        public void onError(FlurryAdInterstitial flurryAdInterstitial, FlurryAdErrorType flurryAdErrorType, int i) {
            flurryAdInterstitial.destroy();
        }

        @Override
        public void onRendered(FlurryAdInterstitial flurryAdInterstitial) {

        }

        @Override
        public void onDisplay(FlurryAdInterstitial flurryAdInterstitial) {

        }

        @Override
        public void onClose(FlurryAdInterstitial flurryAdInterstitial) {

        }

        @Override
        public void onAppExit(FlurryAdInterstitial flurryAdInterstitial) {

        }

        @Override
        public void onClicked(FlurryAdInterstitial flurryAdInterstitial) {

        }

        @Override
        public void onVideoCompleted(FlurryAdInterstitial flurryAdInterstitial) {

        }
    };

    private FlurryAdInterstitial mFlurryAdInterstitial = null;
    private String mAdSpaceName = "INTERSTITIAL_ADSPACE";
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = new Intent(this, SplashActivity.class);
        startActivityForResult(i, Constants.REQ_SPLASH);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();

        long now = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        Date date1 = new Date(now);
        Date date2 = new Date();
        try {
            date2 = dateFormat.parse(sp.getString("PREF_UPDATE_DATE", "2014-01-01 01:01:01"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String strDate = dateFormat.format(date1);

        mode = "display_list";
        params1 = "http://petbox.kr/petboxjson/app_goods_list.php";
        params2 = sp.getString("PREF_UPDATE_DATE", "2014-01-01 01:01:01");
        params2 = "?update="+params2;

        params3 = "http://petbox.kr/petboxjson/category_list.php";
        params4 = "";


        //new DBConnector(this).deleteToCategoryList();
        /* FLURRY
        mFlurryAdInterstitial = new FlurryAdInterstitial(this, mAdSpaceName);
        mFlurryAdInterstitial.setListener(interstitialListener);
        */

        InsertDB = "category";
        //new JsonParse.JsonLoadingTask(getApplicationContext()).execute(params3, params4, InsertDB);

        if(date1.after(date2)) {
            InsertDB = "display_item_list";
            new JsonParse.JsonLoadingTask(getApplicationContext()).execute(params1, params2,InsertDB);
            Log.e("strDate_rere------", strDate);
            editor.putString("PREF_UPDATE_DATE", strDate);
            editor.commit();
        }

        mainColor = getResources().getColor(R.color.colorPrimary);

        //toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        //setSupportActionBar(toolbar);

        edit_search = (EditText)findViewById(R.id.edit_search);
        //edit_search.setFocusable(false);
        //edit_search.setClickable(true);

        edit_search.setOnClickListener(this);

        iv_search = (ImageView)findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);

        iv_logo = (ImageView)findViewById(R.id.iv_logo);

        ibtn_menu_wish = (ImageButton)findViewById(R.id.ibtn_menu_wish);
        ibtn_menu_wish.setOnClickListener(this);

        ibtn_menu_cart = (ImageButton)findViewById(R.id.ibtn_menu_cart);
        ibtn_menu_cart.setOnClickListener(this);

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

        edit_search.setMovementMethod(null);



        homePagerAdapter = new HomePagerAdapter(fragmentManager);
        categoryPagerAdapter = new CategoryPagerAdapter(fragmentManager, this);
        searchPagerAdapter = new SearchPagerAdapter(fragmentManager);
        myPagePagerAdapter = new MyPagePagerAdapter(fragmentManager, this);

        mViewPager.setAdapter(homePagerAdapter);
        tabLayout = (TabLayout)findViewById(R.id.slide_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        //tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.setSelectedTabIndicatorColor(mainColor);
        //tabLayout.setTabTextColors(0xff4e91ff, 0xff000000);
        tabLayout.setTabTextColors(0xff7c7c7c, 0xff000000);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setVisibleForTab(int viewMode){
        tabLayout.setVisibility(viewMode);
    }

    @Override
    public void onStart(){
        Log.i(TAG, "++ ON START ++");

        LoginManager.getHttpClient();

        if(LoginManager.getIsLogin()){
            ibtn_login.setVisibility(View.GONE);
            ibtn_mypage.setVisibility(View.VISIBLE);
        }
        super.onStart();
        //FlurryAgent.onStartSession(this, Constants.FLURRY_APIKEY);
        //mFlurryAdInterstitial.fetchAd(); FLURRY
    }

    @Override
    public void onStop(){
        Log.i(TAG, "++ ON STOP ++");
        //FlurryAgent.onEndSession(this);
        super.onStop();
    }

    @Override
    public void onDestroy(){
        //mFlurryAdInterstitial.destroy(); FLURRY
        super.onDestroy();
    }

    public void setHomePagerAdapter(){
        //clearBackStack();
        //Log.i("TAG", "MNGR[COUNT] : "+fragmentManager.getFragments().size());
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mViewPager.setSwipeEnabled(true);
        mViewPager.setAdapter(homePagerAdapter);
        mViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setCategoryPagerAdapter(){
        //clearBackStack();
        //Log.i("TAG", "MNGR[COUNT] : "+fragmentManager.getFragments().size());
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mViewPager.setSwipeEnabled(false);
        mViewPager.setAdapter(categoryPagerAdapter);
        mViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setSearchPagerAdapter(){
        //clearBackStack();
        //Log.i("TAG", "MNGR[COUNT] : "+fragmentManager.getFragments().size());
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mViewPager.setSwipeEnabled(true);
        mViewPager.setAdapter(searchPagerAdapter);
        mViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setMyPagePagerAdapter(){
        //clearBackStack();
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mViewPager.setSwipeEnabled(false);
        mViewPager.setAdapter(myPagePagerAdapter);

        mViewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private void clearBackStack() {
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.edit_search:

                // 검색창 short -> long
                if(iv_logo.getVisibility() == View.VISIBLE){
                    iv_logo.setVisibility(View.GONE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(edit_search, 0);


                   // edit_search.setFocusable(true);
                    //edit_search.setClickable(true);

                //검색창 long -> short
                }else if(iv_logo.getVisibility() == View.GONE){
                    iv_logo.setVisibility(View.VISIBLE);

                    edit_search.setMovementMethod(null);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                    //edit_search.setFocusable(false);
                    //edit_search.setClickable(true);
                    edit_search.setText("");
                }

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
                    intent.putExtra("keyword", edit_search.getText().toString());
                    startActivity(intent);

                    edit_search.setText("");

                    if(iv_logo.getVisibility() == View.GONE){
                        iv_logo.setVisibility(View.VISIBLE);

                        edit_search.setMovementMethod(null);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "검색란이 비어있습니다.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.ibtn_menu_wish:
                //setCategoryPagerAdapter();
                //Toast.makeText(getApplicationContext(), "찜하기 페이지 이동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, GoodInfoActivity.class);
                startActivity(intent);

                break;

            case R.id.ibtn_menu_cart:
                Intent cart_intnet = new Intent(MainActivity.this, CartListWebView.class);
                startActivity(cart_intnet);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //Toast.makeText(getApplicationContext(), "menu_cart", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ibtn_home:
                //Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                menu_selected = 0;

                if(menu_selected == 0 ){    // 홈
                    setHomePagerAdapter();
                    setVisibleForTab(View.VISIBLE);
                    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

                    ibtn_home.setImageResource(R.drawable.bot_home_on);
                    ibtn_category.setImageResource(R.drawable.bot_category_off);
                    ibtn_search.setImageResource(R.drawable.bot_search_off);

                    if(ibtn_mypage.getVisibility() == View.VISIBLE)
                        ibtn_mypage.setImageResource(R.drawable.bot_mypage_off);
                }

                break;

            case R.id.ibtn_category:
                //Toast.makeText(getApplicationContext(), "category", Toast.LENGTH_SHORT).show();
                menu_selected = 1;

                if(menu_selected == 1 ) {    // 카테고리
                    setCategoryPagerAdapter();
                    setVisibleForTab(View.GONE);

                    ibtn_home.setImageResource(R.drawable.bot_home_off);
                    ibtn_category.setImageResource(R.drawable.bot_category_on);
                    ibtn_search.setImageResource(R.drawable.bot_search_off);

                    if (ibtn_mypage.getVisibility() == View.VISIBLE)
                        ibtn_mypage.setImageResource(R.drawable.bot_mypage_off);
                }
                break;

            case R.id.ibtn_search:
                //Toast.makeText(getApplicationContext(), "search", Toast.LENGTH_SHORT).show();
                menu_selected = 2;

                if(menu_selected == 2 ) {    // 검색
                    setSearchPagerAdapter();
                    setVisibleForTab(View.VISIBLE);
                    tabLayout.setTabMode(TabLayout.MODE_FIXED);

                    ibtn_home.setImageResource(R.drawable.bot_home_off);
                    ibtn_category.setImageResource(R.drawable.bot_category_off);
                    ibtn_search.setImageResource(R.drawable.bot_search_on);

                    if (ibtn_mypage.getVisibility() == View.VISIBLE)
                        ibtn_mypage.setImageResource(R.drawable.bot_mypage_off);
                }

                break;

            case R.id.ibtn_login:
                Intent login_intnet = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(login_intnet, Constants.REQ_LOGIN);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_SHORT).show();
                break;

            case R.id.ibtn_mypage:
                menu_selected = 3;

                if(menu_selected == 3) {    // 마이페이지
                    //Toast.makeText(getApplicationContext(), "mypage", Toast.LENGTH_SHORT).show();
                    setMyPagePagerAdapter();
                    setVisibleForTab(View.GONE);

                    ibtn_home.setImageResource(R.drawable.bot_home_off);
                    ibtn_category.setImageResource(R.drawable.bot_category_off);
                    ibtn_search.setImageResource(R.drawable.bot_search_off);


                    if (ibtn_mypage.getVisibility() == View.VISIBLE)
                        ibtn_mypage.setImageResource(R.drawable.bot_mypage_on);
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case Constants.REQ_SPLASH:

                if(resultCode == Constants.RES_SPLASH_CANCEL){
                    finish();
                }
                break;

            case Constants.REQ_LOGIN:

                if(resultCode == Constants.RES_LOGIN_SUCCESS){
                    ibtn_login.setVisibility(View.GONE);
                    ibtn_mypage.setVisibility(View.VISIBLE);

                }

                break;
        }
    }

    @Override
    public void clickCategoryGoods(int select) {
        mViewPager.setCurrentItem(select);   // 카테고리 상품 화면
    }

    @Override
    public void backCategory() {
        mViewPager.setCurrentItem(0);   // 카테고리 화면
    }

    @Override
    public void setFragmentItem(int fragmentItem) {
        mViewPager.setCurrentItem(fragmentItem);
    }


    // google play service가 사용가능한가
    @SuppressLint("LongLogTag")
    private boolean checkPlayServices()
    {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                finish();
            }
            return false;
        }
        return true;
    }

    // registration  id를 가져온다.
    @SuppressLint("LongLogTag")
    private String getRegistrationId()
    {

        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
        if (TextUtils.isEmpty(registrationId))
        {
            //첫실행이라 볼 수 있음.
            Log.i("MainActivity.java | getRegistrationId", "|Registration not found.|");
            return "";
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion)
        {
            Log.i("MainActivity.java | getRegistrationId", "|App version changed.|");
            return "";
        }
        return registrationId;
    }

    // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
    private int getAppVersion()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // gcm 서버에 접속해서 registration id를 발급.
    private void registerInBackground()
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {

                String msg = "";
                try
                {
                    if (_gcm == null)
                    {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    _regId = _gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + _regId;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(_regId);
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                HttpPostData(_regId);
                Log.e("msg info--",_regId);
                return msg;

            }

            @SuppressLint("LongLogTag")
            @Override
            protected void onPostExecute(String msg)
            {
                // _textStatus = (TextView) findViewById(R.id.gcmmessge);

                Log.i("MainActivity.java | onPostExecute", "|" + msg + "|");
                // _textStatus.append(msg);
            }
        }.execute(null, null, null);
    }
    // registraion id를 preference에 저장한다.
    @SuppressLint("LongLogTag")
    private void storeRegistrationId(String regId)
    {
        int appVersion = getAppVersion();
        Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }


    public void HttpPostData(String reg_id) {

        try { //원래는 서버url 쓰라고 써있었음
            URL url = new URL("http://dd.innopolis.or.kr/gcm_msg/gcm_reg_insert.php");       // URL 설정
            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속

            //--------------------------
            //   전송 모드 설정 - 기본적인 설정이다
            //--------------------------

            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");


            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            StringBuffer buffer = new StringBuffer();
            buffer.append("reg_id").append("=").append(reg_id);                 // php 변수에 값 대
            Log.e("buffer", String.valueOf(buffer));
            Log.e("http.getOutputStream()", String.valueOf(http.getOutputStream()));

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");

            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str + "\n");
            }

            myResult = builder.toString();

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } // try
    } // HttpPostData
    /*
    @Override
    public void clickPlanning() {
        //Toast.makeText(this, "Main - Click Planning", Toast.LENGTH_SHORT).show();

        menu_selected = 0;

        if(menu_selected == 0 ){    // 홈
            setHomePagerAdapter();
            setVisibleForTab(View.VISIBLE);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            ibtn_home.setImageResource(R.drawable.bot_home_on);
            ibtn_category.setImageResource(R.drawable.bot_category_off);
            ibtn_search.setImageResource(R.drawable.bot_search_off);

            if(ibtn_mypage.getVisibility() == View.VISIBLE)
                ibtn_mypage.setImageResource(R.drawable.bot_mypage_off);
        }

        mViewPager.setCurrentItem(3);   //기획전

    }

    @Override
    public void clickPrimium() {
        //Toast.makeText(this, "Main - Click Primium", Toast.LENGTH_SHORT).show();

        menu_selected = 0;

        if(menu_selected == 0 ){    // 홈
            setHomePagerAdapter();
            setVisibleForTab(View.VISIBLE);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            ibtn_home.setImageResource(R.drawable.bot_home_on);
            ibtn_category.setImageResource(R.drawable.bot_category_off);
            ibtn_search.setImageResource(R.drawable.bot_search_off);

            if(ibtn_mypage.getVisibility() == View.VISIBLE)
                ibtn_mypage.setImageResource(R.drawable.bot_mypage_off);
        }
        mViewPager.setCurrentItem(4);   //프리미엄몰
    }

    @Override
    public void clickCategoryGoods() {
        categoryPagerAdapter.setMode(1);
    }
    */
}
