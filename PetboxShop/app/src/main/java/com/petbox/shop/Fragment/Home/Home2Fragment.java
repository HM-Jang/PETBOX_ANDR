package com.petbox.shop.Fragment.Home;


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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.petbox.shop.Adapter.List.GoodsListAdapter;
import com.petbox.shop.Adapter.Pager.BestGoodPagerAdapter;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.DB.DBConnector;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.SlideInfo;
import com.petbox.shop.JsonParse;
import com.petbox.shop.R;
import com.petbox.shop.Utility.Utility;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home2Fragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PullToRefreshScrollView scView;

    ViewPager viewPager;
    PageIndicator indicator;
    BestGoodPagerAdapter bestGoodPagerAdapter;
    ArrayList<SlideInfo> slideList;
    CirclePageIndicator circlePageIndicator;

    Thread timerThread;
    Handler handler;

    Boolean isRunning = true;

    LinearLayout linear_btn;
    Button btn_dog, btn_cat;

    ListView list_best, list_dc;
    ArrayList<BestGoodInfo> bestItemList;
    ArrayList<BestGoodInfo> dcItemList;

    GoodsListAdapter bestListAdapter;
    GoodsListAdapter dcListAdapter;


    int interval = Constants.AUTO_SLIDE_TIME;

    int mainColor = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String params_1,params_2,params_3;

    Tracker mTracker;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home2Fragment newInstance(String param1, String param2) {
        Home2Fragment fragment = new Home2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Home2Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        /*
        System.out.println("SEO - HOME2 ++ ON START ++");

        GoogleAnalytics.getInstance(getContext()).reportActivityStart(getActivity());

        params_1 = "?mdesign_no=1";
        params_2 = "?mdesign_no=14";
        params_3 = "?mdesign_no=5";

        slideList = home_slider(params_3);
        bestGoodPagerAdapter = new BestGoodPagerAdapter(getContext() ,slideList);
        viewPager.setAdapter(bestGoodPagerAdapter);

        indicator = circlePageIndicator;
        indicator.setViewPager(viewPager);

        circlePageIndicator.setPageColor(0xFF6d6d6d);   // Normal 원 색상
        circlePageIndicator.setFillColor(mainColor);   //선택된 원 색상
        circlePageIndicator.setStrokeColor(0x00000000); //테두리 INVISIBLE

        bestItemList = goods_list(params_1);
        bestListAdapter = new GoodsListAdapter(getContext(), bestItemList);
        list_best.setAdapter(bestListAdapter);
        Utility.setListViewHeightBasedOnChildren(list_best);

        bestItemList = goods_list(params_2);
        bestListAdapter = new GoodsListAdapter(getContext(), bestItemList);
        list_dc.setAdapter(bestListAdapter);
        Utility.setListViewHeightBasedOnChildren(list_dc);

        */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Home2Fragment", "-------------------------onCreate");


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onStop(){
        super.onStop();
        //GoogleAnalytics.getInstance(getContext()).reportActivityStop(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Home2Fragment", "-------------------------Home2Fragment");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mainColor = getResources().getColor(R.color.colorPrimary);
        list_best = (ListView)v.findViewById(R.id.list_home_best);
        list_dc = (ListView)v.findViewById(R.id.list_home_dc);



        /***슬라이드**/
        viewPager = (ViewPager)v.findViewById(R.id.pager_best_good);
        circlePageIndicator = (CirclePageIndicator)v.findViewById(R.id.indicator_best_good);
        /***END 슬라이드 END**/

        btn_dog = (Button)v.findViewById(R.id.btn_slide_dog);
        btn_dog.setOnClickListener(this);

        btn_cat = (Button)v.findViewById(R.id.btn_slide_cat);
        btn_cat.setOnClickListener(this);

        scView = (PullToRefreshScrollView) v.findViewById(R.id.sc_home);

        //int height = (getResources().getDimensionPixelSize(R.dimen.list))

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


        Log.e("Home2Fragment","%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%onStart");
        /** 시작한다 시작해 ! **/
        params_1 = "?mdesign_no=1";
        params_2 = "?mdesign_no=14";
        params_3 = "?mdesign_no=5";

        slideList = home_slider(params_3);
        bestGoodPagerAdapter = new BestGoodPagerAdapter(getContext() ,slideList);
        viewPager.setAdapter(bestGoodPagerAdapter);

        indicator = circlePageIndicator;
        indicator.setViewPager(viewPager);

        circlePageIndicator.setPageColor(0xFF6d6d6d);   // Normal 원 색상
        circlePageIndicator.setFillColor(mainColor);   //선택된 원 색상
        circlePageIndicator.setStrokeColor(0x00000000); //테두리 INVISIBLE

        bestItemList = goods_list(params_1);
        bestListAdapter = new GoodsListAdapter(getContext(), bestItemList);
        list_best.setAdapter(bestListAdapter);
        Utility.setListViewHeightBasedOnChildren(list_best);

        bestItemList = goods_list(params_2);
        bestListAdapter = new GoodsListAdapter(getContext(), bestItemList);
        list_dc.setAdapter(bestListAdapter);
        Utility.setListViewHeightBasedOnChildren(list_dc);
        /** 혼란하다 혼란해! **/

        timerThread.start();

        return v;
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
            JSONArray  display_goods_Array = new JSONArray(display_slide_list);
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

    public ArrayList<BestGoodInfo> goods_list(String params){
        String url="http://petbox.kr/petboxjson/display_goods_list.php";
        String InsertDB = "display_goods_list";
        String display_goods_list;
        bestItemList = new ArrayList<BestGoodInfo>();

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

                bestItemList.add(info[k]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bestItemList;

    }


    public void initViewPager(){
        viewPager.setCurrentItem(0);
    }

    public void refreshBestListView(ArrayList<BestGoodInfo> itemList){
        bestItemList.clear();
        bestItemList = itemList;

        bestListAdapter = new GoodsListAdapter(getActivity().getApplicationContext(), bestItemList);
        list_best.setAdapter(bestListAdapter);
    }

    public void refreshDcListView(ArrayList<BestGoodInfo> itemList){
        dcItemList.clear();
        dcItemList = itemList;

        dcListAdapter = new GoodsListAdapter(getActivity().getApplicationContext(), dcItemList);
        list_dc.setAdapter(dcListAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btn_slide_dog:
                Log.e("Home2Fragment", "-------------------------onClick");

                //mTracker.send(new HitBuilders.EventBuilder().setCategory("펫박스 홈").setAction("강아지 버튼 클릭").build());

                //Toast.makeText(getContext(), "GA TEST - 강아지 버튼", Toast.LENGTH_SHORT).show();

                params_1 = "?mdesign_no=1";
                params_2 = "?mdesign_no=14";
                btn_cat.setBackgroundResource(R.drawable.home_cat_off);
                btn_dog.setBackgroundResource(R.drawable.home_dog_on);

                bestItemList = goods_list(params_1);
                bestListAdapter = new GoodsListAdapter(getActivity().getApplicationContext(), bestItemList);
                list_best.setAdapter(bestListAdapter);
                Utility.setListViewHeightBasedOnChildren(list_best);

                bestItemList = goods_list(params_2);
                bestListAdapter = new GoodsListAdapter(getActivity().getApplicationContext(), bestItemList);
                list_dc.setAdapter(bestListAdapter);
                Utility.setListViewHeightBasedOnChildren(list_dc);
                break;

            case R.id.btn_slide_cat:

                params_1 = "?mdesign_no=2";
                params_2 = "?mdesign_no=15";
                btn_cat.setBackgroundResource(R.drawable.home_cat_on);
                btn_dog.setBackgroundResource(R.drawable.home_dog_off);

                bestItemList = goods_list(params_1);
                bestListAdapter = new GoodsListAdapter(getActivity().getApplicationContext(), bestItemList);
                list_best.setAdapter(bestListAdapter);
                Utility.setListViewHeightBasedOnChildren(list_best);

                bestItemList = goods_list(params_2);
                bestListAdapter = new GoodsListAdapter(getActivity().getApplicationContext(), bestItemList);
                list_dc.setAdapter(bestListAdapter);
                Utility.setListViewHeightBasedOnChildren(list_dc);

                Log.e("Home2Fragment", "-------------------------onClick");
                break;
        }
    }
}
