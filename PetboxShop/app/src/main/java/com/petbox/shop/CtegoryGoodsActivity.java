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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.petbox.shop.Adapter.List.CategoryAdapter;
import com.petbox.shop.Adapter.List.CategoryListAdapter;
import com.petbox.shop.CustomView.SortDialog;
import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CtegoryGoodsActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spin_main, spin_sub;
    private ArrayAdapter<String> adapter;
    Context mContext;

    private ImageButton btn_sort;

    ListView listView;
    ListView c_listView;
    CategoryListAdapter listAdapter;
    ArrayList<CategoryGoodInfo> mItemList;

    SortDialog dialog;
    Dialog cdialog;
    CategoryAdapter categoryAdapter;
    ArrayList<CategoryInfo> cItemList;
    String cate_num;
    TextView text_t;


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String cate_num = "?category=" + intent.getStringExtra("cate_num");

        mItemList = goods_list(cate_num);
        listAdapter = new CategoryListAdapter(mContext, mItemList);
        listView.setAdapter(listAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctegory_goods);

        mContext =  getApplicationContext();

        dialog = new SortDialog(mContext);

        spin_main = (Spinner)findViewById(R.id.spin_category_goods_main);
        listView = (ListView)findViewById(R.id.lv_category_goods_list);

        String[] first_category = getResources().getStringArray(R.array.first_category);
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, first_category);
        spin_main.setAdapter(adapter);

        text_t = (TextView)findViewById(R.id.text_t);
        text_t.setOnClickListener(this);

        spin_sub = (Spinner)findViewById(R.id.spin_category_goods_sub);
        btn_sort = (ImageButton)findViewById(R.id.btn_category_goods_sort);
        btn_sort.setOnClickListener(this);

        cdialog = new Dialog(getApplicationContext());
        cdialog.setContentView(R.layout.dialog_category_list);
        c_listView = (ListView) cdialog.findViewById(R.id.lv_cate_dialog_list);

        cItemList = category_list(cate_num);
        categoryAdapter = new CategoryAdapter(mContext, cItemList);
        listView.setAdapter(categoryAdapter);

        cdialog.setTitle("List of likers");
        cdialog.setCanceledOnTouchOutside(true);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.text_t:
                if(!cdialog.isShowing())
                    cdialog.show();
                else
                    cdialog.dismiss();

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
}
