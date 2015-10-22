package com.petbox.shop.Fragment.Home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.petbox.shop.Adapter.Grid.BestGoodGridAdapter;
import com.petbox.shop.Adapter.Pager.BestGoodPagerAdapter;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.DB.DBConnector;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.SlideInfo;
import com.petbox.shop.JsonParse;
import com.petbox.shop.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PrimiumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrimiumFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public final int DEFAULT_INTERVAL = 5000;

    private ViewPager viewPager;

    PullToRefreshGridView gridView;
    ArrayList<BestGoodInfo> mItemList;
    BestGoodGridAdapter gridAdapter;
    PageIndicator mIndicator;

    PageIndicator indicator;
    BestGoodPagerAdapter bestGoodPagerAdapter;
    ArrayList<SlideInfo> slideList;
    CirclePageIndicator circlePageIndicator;
    String params_3,params_1;

    Button btn_dog, btn_cat;

    public int interval = DEFAULT_INTERVAL;

    Thread  timerThread;
    Handler handler;

    Boolean isRunning = true;

    int mainColor = 0;

    Tracker mTracker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrimiumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrimiumFragment newInstance(String param1, String param2) {
        PrimiumFragment fragment = new PrimiumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PrimiumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //System.out.println("SEO - PRIMIUM ++ ON START ++");

        //GoogleAnalytics.getInstance(getContext()).reportActivityStart(getActivity());

        /*
        params_1 = "?mdesign_no=18";
        params_3 = "?mdesign_no=22";

        slideList = home_slider(params_3);
        bestGoodPagerAdapter = new BestGoodPagerAdapter(getContext() ,slideList);

        if(viewPager != null){
            Log.e("onStart", "onStart -- viewPager null 아님");
            viewPager.setAdapter(bestGoodPagerAdapter);

            indicator = circlePageIndicator;
            indicator.setViewPager(viewPager);

            circlePageIndicator.setPageColor(0xFF6d6d6d);   // Normal 원 색상
            circlePageIndicator.setFillColor(mainColor);   //선택된 원 색상
            circlePageIndicator.setStrokeColor(0x00000000); //테두리 INVISIBLE
        }else{
            Log.e("onStart", "onStart -- viewPager null");
        }

        mItemList = goods_list(params_1);
        gridAdapter = new BestGoodGridAdapter(getContext(), mItemList);
        gridView.setAdapter(gridAdapter);
        */

    }

    @Override
    public void onStop(){
        super.onStop();
        //GoogleAnalytics.getInstance(getContext()).reportActivityStop(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*
        mTracker = ((PetboxApplication)getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName("프리미엄몰");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        */

        View v = inflater.inflate(R.layout.fragment_best_good, container, false);

        mainColor = getResources().getColor(R.color.colorPrimary);

        /***슬라이드**/
        View headerView = inflater.inflate(R.layout.custom_slide_image, null);
        viewPager = (ViewPager)headerView.findViewById(R.id.pager_best_good);

        circlePageIndicator = (CirclePageIndicator)headerView.findViewById(R.id.indicator_best_good);

        btn_dog = (Button) headerView.findViewById(R.id.btn_slide_dog);
        btn_dog.setOnClickListener(this);

        btn_cat = (Button) headerView.findViewById(R.id.btn_slide_cat);
        btn_cat.setOnClickListener(this);
        /***END 슬라이드 END**/

        gridView = (PullToRefreshGridView)v.findViewById(R.id.grid_best_good);
        gridView.addHeaderView(headerView);

        handler = new Handler(){
            public void handleMessage(Message msg){
                if (viewPager.getCurrentItem() == bestGoodPagerAdapter.getImages() - 1) {
                    viewPager.setCurrentItem(0, true);
                }else{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
                }
            }
        };


        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning){
                    try{
                        timerThread.sleep(3000);

                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    //isRunning = false;

                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }
        });

        params_1 = "?mdesign_no=18";
        params_3 = "?mdesign_no=22";

        slideList = home_slider(params_3);
        bestGoodPagerAdapter = new BestGoodPagerAdapter(getContext() ,slideList);

        if(viewPager != null){
            Log.e("onStart", "onStart -- viewPager null 아님");
            viewPager.setAdapter(bestGoodPagerAdapter);

            indicator = circlePageIndicator;
            indicator.setViewPager(viewPager);

            circlePageIndicator.setPageColor(0xFF6d6d6d);   // Normal 원 색상
            circlePageIndicator.setFillColor(mainColor);   //선택된 원 색상
            circlePageIndicator.setStrokeColor(0x00000000); //테두리 INVISIBLE
        }else{
            Log.e("onStart", "onStart -- viewPager null");
        }

        mItemList = goods_list(params_1);
        gridAdapter = new BestGoodGridAdapter(getContext(), mItemList);
        gridView.setAdapter(gridAdapter);

        timerThread.start();
        return  v;
    }

    public void initViewPager(){
        viewPager.setCurrentItem(0);
    }

    public void refreshGridView(ArrayList<BestGoodInfo> itemList){
        mItemList.clear();
        mItemList = goods_list(params_1);
        gridAdapter = new BestGoodGridAdapter(getContext(), mItemList);
        gridView.setAdapter(gridAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public ArrayList<BestGoodInfo> goods_list(String params){
        String url="http://petbox.kr/petboxjson/display_goods_list.php";
        String InsertDB = "display_goods_list";
        String display_goods_list;
        mItemList = new ArrayList<BestGoodInfo>();

        try {
            int sort = 0;
            String imgUrl = "";
            String name = "";
            String goodsno = "";
            String rate = "";
            String origin_price = "";
            String price = "";
            float rating = 0;
            int rating_person = 0;
            int icon = 0;

            display_goods_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url,params,InsertDB).get();
            JSONArray  display_goods_Array = new JSONArray(display_goods_list);
            JSONObject display_goods_obs = display_goods_Array.getJSONObject(0);
            JSONArray display_data_obj = display_goods_obs.getJSONArray("display_item");

            BestGoodInfo info[] = new BestGoodInfo[display_data_obj.length()];

            for (int k = 0; k < display_data_obj.length(); k++) {
                JSONObject ca_object = display_data_obj.getJSONObject(k);

                info[k] = new BestGoodInfo(sort,imgUrl,name,goodsno,rate,origin_price,price,rating,rating_person,icon);

                info[k].sort = Integer.parseInt(ca_object.getString("sort"));
                info[k].imgUrl = ca_object.getString("img_i");
                info[k].name = ca_object.getString("goodsnm");
                info[k].origin_price = ca_object.getString("goods_consumer");
                info[k].price = ca_object.getString("goods_price");
                info[k].goodsno = ca_object.getString("goodsno");
                info[k].rating = Float.parseFloat(ca_object.getString("point"));
                info[k].rating_person = Integer.parseInt(ca_object.getString("point_count"));
                if(ca_object.getString("icon") != null){
                    icon = Integer.parseInt(ca_object.getString("icon"));
                }else{
                    icon = 0;
                }
                info[k].icon = icon;

                mItemList.add(info[k]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mItemList;

    }

    public ArrayList<SlideInfo> home_slider(String params){
        String url="http://petbox.kr/petboxjson/display_slide_list.php";
        String InsertDB = "display_goods_list";
        String display_slide_list;
        slideList = new ArrayList<SlideInfo>();

        try {
            int sort = 0;
            String title ="";
            String type ="";
            String type_data ="";
            String banner_img = "";
            String down_path ="";

            display_slide_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url,params,InsertDB).get();
            JSONArray display_goods_Array = new JSONArray(display_slide_list);
            JSONObject display_goods_obs = display_goods_Array.getJSONObject(0);
            JSONArray display_data_obj = display_goods_obs.getJSONArray("display_slide");

            SlideInfo info[] = new SlideInfo[display_data_obj.length()];

            for (int k = 0; k < display_data_obj.length(); k++) {
                JSONObject sl_object = display_data_obj.getJSONObject(k);
                JSONArray banner = new JSONArray("[" + sl_object.getString("tpl_opt") + "]");
                JSONObject bn_object = banner.getJSONObject(0);
                JSONArray banner_img_p = new JSONArray("[" + bn_object.getString("banner_img") + "]");
                JSONObject bn_img_object = banner_img_p.getJSONObject(0);

                down_path = "http://petbox.kr/shop/data/m/upload_img/" + bn_img_object.getString(sl_object.getString("sort"));

                info[k] = new SlideInfo(sort,title,type,type_data,banner_img);

                info[k].sort = Integer.parseInt(sl_object.getString("sort"));
                info[k].title = sl_object.getString("title");
                info[k].type = sl_object.getString("type");
                info[k].type_data = sl_object.getString("type_data");
                info[k].banner_img = down_path;

                slideList.add(info[k]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return slideList;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btn_slide_dog:
                btn_cat.setBackgroundResource(R.drawable.home_cat_off);
                btn_dog.setBackgroundResource(R.drawable.home_dog_on);
                params_1 = "?mdesign_no=18";
                mItemList = goods_list(params_1);
                gridAdapter = new BestGoodGridAdapter(getContext(), mItemList);
                gridView.setAdapter(gridAdapter);
                break;

            case R.id.btn_slide_cat:
                btn_cat.setBackgroundResource(R.drawable.home_cat_on);
                btn_dog.setBackgroundResource(R.drawable.home_dog_off);
                params_1 = "?mdesign_no=19";
                mItemList = goods_list(params_1);
                gridAdapter = new BestGoodGridAdapter(getContext(), mItemList);
                gridView.setAdapter(gridAdapter);
                break;
        }
    }
}
