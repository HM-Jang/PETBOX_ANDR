package com.petbox.shop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Adapter.List.CategoryAdapter;
import com.petbox.shop.Adapter.List.CategoryListAdapter;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.CustomView.ListDialog;
import com.petbox.shop.CustomView.SortDialog;
import com.petbox.shop.DataStructure.Tree.Node;
import com.petbox.shop.Delegate.CategoryManagerDelegate;
import com.petbox.shop.Delegate.ClickDelegate;
import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.CategoryInfo;
import com.petbox.shop.Item.PlanningItemInfo;
import com.petbox.shop.Network.CategoryManager;
import com.petbox.shop.Network.PlanningManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CtegoryGoodsActivity extends AppCompatActivity implements View.OnClickListener, ClickDelegate {

    private RelativeLayout relative_main, relative_sub;
    private TextView tv_main, tv_sub;
    private ArrayAdapter<String> adapter;
    Context mContext;

    private ImageButton btn_sort;

    ListView listView;
    ListView c_listView;
    CategoryListAdapter listAdapter;
    ArrayList<CategoryGoodInfo> mItemList;

    ListDialog listDialog;
    SortDialog dialog;

    //Dialog cdialog;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryInfo> cItemList;
    String cate_num;
    TextView tv_title;  // 카테고리 xx

    String param = "";
    String category_name = "";
    int category_mode = 0; // 0: 강아지, 1: 고양이
    int where = 0; // 0 : 카테고리메뉴 -> 카테고리 상세상품, 1: 기획전 -> 카테고리 상세상품
    Node<CategoryInfo> selected_node;
    Node<PlanningItemInfo> selected_node_planning;

    ArrayList<Node<CategoryInfo>> category1List;
    ArrayList<Node<CategoryInfo>> category2List;

    ArrayList<Node<PlanningItemInfo>> planningList;

    Tracker mTracker;

    ImageView iv_back;
    //CategoryManager categoryManager;

    @Override
    protected void onStart() {
        super.onStart();
        mTracker.setScreenName("카테고리 상품 리스트");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        //Intent intent = getIntent();
        //String cate_num = "?category=" + intent.getStringExtra("cate_num");

        /*
        String cate_num = "?category=" + param;

        mItemList = goods_list(cate_num);
        listAdapter = new CategoryListAdapter(this, mItemList);
        listView.setAdapter(listAdapter);
        */

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctegory_goods);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();

        /*
        mTracker.setScreenName("카테고리 상품 리스트");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        */

        where = getIntent().getIntExtra("where", 0);
        param = getIntent().getStringExtra("cate_num");
        category_mode = getIntent().getIntExtra("cate_mode", 0);

        ArrayList<String> arrList = new ArrayList<String>();

        System.out.println("WHERE : "+where);

        //카테고리

        //if(where == 0)
          //  category_name = getIntent().getStringExtra("cate_name");

        //CategoryManager.setDelegate(this);  // 싱글톤 카테고리 매니저 세팅

        iv_back = (ImageView)findViewById(R.id.iv_category_good_back);
        iv_back.setOnClickListener(this);

        mContext =  getApplicationContext();

        dialog = new SortDialog(mContext);

        relative_main = (RelativeLayout)findViewById(R.id.relative_category_goods_category1);
        //relative_main.setOnClickListener(this);

        relative_sub = (RelativeLayout) findViewById(R.id.relative_category_goods_category2);
        //relative_sub.setOnClickListener(this);

        tv_main = (TextView)findViewById(R.id.tv_category_goods_main);
        tv_main.setOnClickListener(this);
        tv_sub = (TextView)findViewById(R.id.tv_category_goods_sub);
        tv_sub.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.lv_category_goods_list);
        tv_title = (TextView) findViewById(R.id.tv_category_goods_title);
         //text_t = (TextView)findViewById(R.id.text_t);
        //text_t.setOnClickListener(this);
        
        btn_sort = (ImageButton)findViewById(R.id.btn_category_goods_sort);
        btn_sort.setOnClickListener(this);

        if(where == 0){
            System.out.println("CTegoryGoodsActivity > PARAM : " + param);

            selected_node = CategoryManager.scan(param, category_mode, 1);   // category_num으로 노드 검색
            category_name = selected_node.getData().name;

            System.out.println("MODE : " + category_mode + "// NAME : " + category_name);

            category1List = selected_node.getChildList();
            category2List = new ArrayList<Node<CategoryInfo>>();

            if(category1List.isEmpty()){
                relative_sub.setVisibility(View.INVISIBLE);
                //tv_sub.setVisibility(View.INVISIBLE);
            }
            for(int i=0; i< category1List.size(); i++){
                arrList.add(category1List.get(i).getData().name);
            }
        }else{  //기획전
            System.out.println("CTegoryGoodsActivity > PARAM : " + param);

            selected_node_planning = PlanningManager.scan(param, category_mode);   // category_num으로 노드 검색
            category_name = selected_node_planning.getData().name;

            relative_sub.setVisibility(View.INVISIBLE); // 2차가 없으므로

            planningList = selected_node_planning.parent.getChildList();

            /*
            for(int i=0; i< planningList.size(); i++) {
                arrList.add(planningList.get(i).getData().linkaddr);
            }
            */
        }

        tv_title.setText(category_name);


        String cate_num = "?category=" + param;

        mItemList = goods_list(cate_num);
        listAdapter = new CategoryListAdapter(this, mItemList);
        listView.setAdapter(listAdapter);
       /*
        cdialog = new Dialog(getApplicationContext());
        cdialog.setContentView(R.layout.dialog_category_list);
        */
        //c_listView = (ListView) cdialog.findViewById(R.id.lv_cate_dialog_list);

        /*
        cItemList = category_list(cate_num);
        categoryAdapter = new CategoryAdapter(mContext, cItemList);
        listView.setAdapter(categoryAdapter);
        */

        //cdialog.setTitle("List of likers");
        //cdialog.setCanceledOnTouchOutside(true);

    }

    public ArrayList<String> convertArrayList(ArrayList<Node<CategoryInfo>> itemList){
        ArrayList<String> arr = new ArrayList<String>();

        for(int i=0; i<itemList.size(); i++){
            String name = itemList.get(i).getData().name;

            arr.add(name);
        }

        return arr;
    }

    public ArrayList<String> convertArrayList2(ArrayList<Node<PlanningItemInfo>> itemList){
        ArrayList<String> arr = new ArrayList<String>();

        for(int i=0; i<itemList.size(); i++){
            String name = itemList.get(i).getData().name;

            arr.add(name);
        }

        return arr;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.iv_category_good_back:
                finish();
                break;

            case R.id.tv_category_goods_main:
                if(where == 0){ //카테고리
                    listDialog = new ListDialog(this, category_name, convertArrayList(category1List), this, 2);
                    listDialog.show();
                }else{  // 기획전
                    listDialog = new ListDialog(this, category_name, convertArrayList2(planningList), this, 0);
                    listDialog.show();
                }

                break;

            case R.id.tv_category_goods_sub:
                listDialog = new ListDialog(this, category_name, convertArrayList(category2List), this, 3);
                listDialog.show();
                break;

            case R.id.btn_category_goods_sort:

                if(!dialog.isShowing())
                    dialog.show();
                else
                    dialog.dismiss();

                //Toast.makeText(getContext(), "정렬 버튼", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void sort(int mode) {
        switch(mode){
            case 0: //낮은 가격순
                btn_sort.setImageResource(R.drawable.category_sort_low_price);
                Toast.makeText(getApplicationContext(), "낮은 가격순", Toast.LENGTH_SHORT).show();
                break;

            case 1: //높은 가격순
                btn_sort.setImageResource(R.drawable.category_sort_high_price);
                Toast.makeText(getApplicationContext(), "높은 가격순", Toast.LENGTH_SHORT).show();
                break;

            case 2: //최신 상품순
                btn_sort.setImageResource(R.drawable.category_sort_recent);
                Toast.makeText(getApplicationContext(), "최신 상품순", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*
    public ArrayList<CategoryInfo> category_list(String params){
        String url="http://petbox.kr/petboxjson/category_list.php";
        String params1 = "?category=" + params;
        String InsertDB = "category_list";
        String category_list;
        cItemList = new ArrayList<CategoryInfo>();

        int sort = 0;
        String category_num = "";
        String name = "";

        try {
            category_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url,params1,InsertDB).get();
            JSONArray display_goods_Array = new JSONArray(category_list);
            JSONObject display_goods_obs = display_goods_Array.getJSONObject(0);
            JSONArray display_data_obj = display_goods_obs.getJSONArray("category_data");

            CategoryInfo info[] = new CategoryInfo[display_data_obj.length()];

            for (int k = 0; k < display_data_obj.length(); k++) {
                JSONObject ca_object = display_data_obj.getJSONObject(k);

                info[k] = new CategoryInfo(sort, category_num, name);

                info[k].sort = ca_object.getInt("sort");
                info[k].category_num = ca_object.getString("category_num");
                info[k].name = ca_object.getString("name");

                cItemList.add(info[k]);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cItemList;
    }
    */

    public ArrayList<CategoryGoodInfo> addItem(JSONArray display_data_obj,int start,int end){
        mItemList = new ArrayList<CategoryGoodInfo>();
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

        CategoryGoodInfo info[] = new CategoryGoodInfo[10];
        if(end > display_data_obj.length()){
            end =  display_data_obj.length();
        }
        try {
            for (int k = start; k < end; k++) {
                JSONObject ca_object = display_data_obj.getJSONObject(k);

                info[k] = new CategoryGoodInfo(category, imgUrl, name, goodsno, rate, origin_price, price, rating, rating_person, icon);

                info[k].category_num = ca_object.getString("category");
                info[k].imgUrl = ca_object.getString("img_i");
                info[k].name = ca_object.getString("goodsnm");
                info[k].origin_price = ca_object.getString("goods_consumer");
                info[k].price = ca_object.getString("goods_price");
                info[k].goodsno = ca_object.getString("goodsno");
                info[k].rating = Float.parseFloat(ca_object.getString("point"));
                info[k].rating_person = Integer.parseInt(ca_object.getString("point_count"));
                if (ca_object.getString("icon") != null) {
                    icon = Integer.parseInt(ca_object.getString("icon"));
                } else {
                    icon = 0;
                }
                info[k].icon = icon;
                mItemList.add(info[k]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mItemList;
    }

    public ArrayList<CategoryGoodInfo> goods_list(String params){
        String url="http://petbox.kr/petboxjson/category_goods_list.php";
        String InsertDB = "category_goods_list";
        String display_goods_list;
        mItemList = new ArrayList<CategoryGoodInfo>();

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

        try {

            display_goods_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url,params,InsertDB).get();
            JSONArray display_goods_Array = new JSONArray(display_goods_list);
            JSONObject display_goods_obs = display_goods_Array.getJSONObject(0);
            JSONArray display_data_obj = display_goods_obs.getJSONArray("category_data");

            CategoryGoodInfo info[] = new CategoryGoodInfo[display_data_obj.length()];

            for (int k = 0; k < display_data_obj.length(); k++) {
                JSONObject ca_object = display_data_obj.getJSONObject(k);

                info[k] = new CategoryGoodInfo(category, imgUrl, name, goodsno, rate, origin_price, price, rating, rating_person, icon);

                info[k].category_num = ca_object.getString("category");
                info[k].imgUrl = ca_object.getString("img_i");
                info[k].name = ca_object.getString("goodsnm");
                info[k].origin_price = ca_object.getString("goods_consumer");
                info[k].price = ca_object.getString("goods_price");
                info[k].goodsno = ca_object.getString("goodsno");
                info[k].rating = Float.parseFloat(ca_object.getString("point"));
                info[k].rating_person = Integer.parseInt(ca_object.getString("point_count"));
                if (ca_object.getString("icon") != null) {
                    icon = Integer.parseInt(ca_object.getString("icon"));
                } else {
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



    @Override
    public void click(int position) {
        if(where == 1){ //기획전 리스트 클릭
            String name = planningList.get(position).getData().name;
            tv_main.setText(name);

            cate_num = "?category=" + planningList.get(position).getData().linkaddr;    // linkaddr = category_num

            mItemList = goods_list(cate_num);
            listAdapter = new CategoryListAdapter(mContext, mItemList);
            listView.setAdapter(listAdapter);

            listDialog.dismiss();
        }
    }

    @Override
    public void click(int position, int depth) {

        // 2차카테고리(첫번째 스피너)일경우
        if(depth == 2){
            String name = category1List.get(position).getData().name;
            tv_main.setText(name);

            cate_num = "?category=" + category1List.get(position).getData().category_num;

            category2List = category1List.get(position).getChildList();

            System.out.println("cate_num : "+ cate_num +"// category2List.size() : " + category2List.size());

            if(category2List.size() > 0) {
                relative_sub.setVisibility(View.VISIBLE);
                //tv_sub.setVisibility(View.VISIBLE);
                tv_sub.setText("선택");
            }else {
                relative_sub.setVisibility(View.INVISIBLE);
                tv_sub.setText("선택");
                //tv_sub.setVisibility(View.INVISIBLE);
            }
        /*
        cItemList = category_list(cate_num);
        categoryAdapter = new CategoryAdapter(mContext, cItemList);
        listView.setAdapter(categoryAdapter);
        */

        }else if(depth == 3){
            String name = category2List.get(position).getData().name;
            tv_sub.setText(name);

            cate_num = "?category=" + category2List.get(position).getData().category_num;
        }

        mItemList = goods_list(cate_num);
        listAdapter = new CategoryListAdapter(mContext, mItemList);
        listView.setAdapter(listAdapter);

        listDialog.dismiss();
    }
}
