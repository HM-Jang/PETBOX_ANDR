package com.petbox.shop.Fragment.Category;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.petbox.shop.Adapter.Grid.BestGoodGridAdapter;
import com.petbox.shop.Adapter.List.CategoryListAdapter;
import com.petbox.shop.Adapter.List.ChanceDealListAdapter;
import com.petbox.shop.Adapter.List.GoodsListAdapter;
import com.petbox.shop.Adapter.Pager.BestGoodPagerAdapter;
import com.petbox.shop.CustomView.SortDialog;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.DB.DBConnector;
import com.petbox.shop.Delegate.CategoryDelegate;
import com.petbox.shop.Delegate.SortDelegate;
import com.petbox.shop.ImageDownloader;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.CategoryInfo;
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
 * Use the {@link CategoryGoodsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryGoodsFragment extends Fragment implements View.OnClickListener, SortDelegate {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryGoodsFragment.
     */

    int mainColor = 0;

    private Spinner spin_main, spin_sub;
    private ArrayAdapter<String> adapter;

    private ImageButton btn_sort;

    PageIndicator indicator;
    BestGoodPagerAdapter bestGoodPagerAdapter;
    ArrayList<SlideInfo> slideList;
    CirclePageIndicator circlePageIndicator;
    String params_3,params_1;
    private ViewPager viewPager;


    ListView listView;
    CategoryListAdapter listAdapter;
    ArrayList<CategoryGoodInfo> mItemList;


    CategoryDelegate delegate;

    public int interval = Constants.AUTO_SLIDE_TIME;

    Thread  timerThread;
    Handler handler;

    Boolean isRunning = true;

    LinearLayout linear_slide_btn;  //헤더의 강아지 고양이 버튼묶음

    ArrayList<CategoryInfo> category_list;

    SortDialog  dialog;

    /*카테고리 넘버 035:애견 , 036:애묘*/
    String cate_num;
    String InsertDB;

    // TODO: Rename and change types and number of parameters
    public static CategoryGoodsFragment newInstance(CategoryDelegate _delegate, String param2) {
        CategoryGoodsFragment fragment = new CategoryGoodsFragment();
        Bundle args = new Bundle();

        fragment.delegate = _delegate;
        fragment.cate_num = param2;
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public CategoryGoodsFragment() {
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

        String params_1 = "?category="+cate_num;
        Log.e("cate_num",cate_num);
        params_3 = "?mdesign_no=5";

        /**슬라이드 시작**/

        /*slideList = home_slider(params_3);
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
        }*/
        /**END 슬라이드 END**/



        mItemList = goods_list(params_1);
        listAdapter = new CategoryListAdapter(getContext(), mItemList);
        listView.setAdapter(listAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category_goods, container, false);


        dialog = new SortDialog(getContext(), this);

        spin_main = (Spinner)v.findViewById(R.id.spin_category_goods_main);
        listView = (ListView)v.findViewById(R.id.list_category_goods);


        String[] first_category = getResources().getStringArray(R.array.first_category);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, first_category);
        spin_main.setAdapter(adapter);

        spin_sub = (Spinner)v.findViewById(R.id.spin_category_goods_sub);
        btn_sort = (ImageButton)v.findViewById(R.id.btn_category_goods_sort);
        btn_sort.setOnClickListener(this);

        mainColor = getResources().getColor(R.color.colorPrimary);

        /**슬라이드 시작**/
        /*
         View headerView = inflater.inflate(R.layout.custom_slide_image, null);circlePageIndicator = (CirclePageIndicator)headerView.findViewById(R.id.indicator_best_good);
        viewPager = (ViewPager)headerView.findViewById(R.id.pager_best_good);

        linear_slide_btn = (LinearLayout) headerView.findViewById(R.id.linear_slide_btn);
        linear_slide_btn.setVisibility(View.GONE);

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

        timerThread.start();*/
        /**END 슬라이드 END**/



        return v;
    }

    public void initViewPager(){
        viewPager.setCurrentItem(0);
    }

    public void refreshLIstView(ArrayList<CategoryGoodInfo> itemList){
        mItemList.clear();
        mItemList = itemList;

        listAdapter = new CategoryListAdapter(getActivity().getApplicationContext(), mItemList);
        listView.setAdapter((ListAdapter) listAdapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btn_category_goods_sort:

                if(!dialog.isShowing())
                    dialog.show();
                else
                    dialog.dismiss();

                //Toast.makeText(getContext(), "정렬 버튼", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void sort(int mode) {
        switch(mode){
            case 0: //낮은 가격순
                btn_sort.setImageResource(R.drawable.category_sort_low_price);
                Toast.makeText(getContext(), "낮은 가격순", Toast.LENGTH_SHORT).show();
                break;

            case 1: //높은 가격순
                btn_sort.setImageResource(R.drawable.category_sort_high_price);
                Toast.makeText(getContext(), "높은 가격순", Toast.LENGTH_SHORT).show();
                break;

            case 2: //최신 상품순
                btn_sort.setImageResource(R.drawable.category_sort_recent);
                Toast.makeText(getContext(), "최신 상품순", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public ArrayList<CategoryGoodInfo> goods_list(String params){
        String url="http://petbox.kr/petboxjson/category_goods_list.php";
        String InsertDB = "category_goods_list";
        String display_goods_list;
        mItemList = new ArrayList<CategoryGoodInfo>();

        try {
            String category = "";
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
            Log.e("catenum", display_goods_list);

            JSONArray  display_goods_Array = new JSONArray(display_goods_list);
            JSONObject display_goods_obs = display_goods_Array.getJSONObject(0);
            JSONArray display_data_obj = display_goods_obs.getJSONArray("category_data");

            CategoryGoodInfo info[] = new CategoryGoodInfo[display_data_obj.length()];

            for (int k = 0; k < display_data_obj.length(); k++) {
                JSONObject ca_object = display_data_obj.getJSONObject(k);

                info[k] = new CategoryGoodInfo(category,imgUrl,name,goodsno,rate,origin_price,price,rating,rating_person,icon);

                info[k].category_num = ca_object.getString("category");
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

}
