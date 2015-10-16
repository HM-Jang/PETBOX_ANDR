package com.petbox.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.petbox.shop.Adapter.List.CategoryListAdapter;
import com.petbox.shop.Adapter.List.WishListAdapter;
import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.WishInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class WishListActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    WishListAdapter listAdapter;
    ArrayList<WishInfo> mItemList;

    Context mContext;


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String m_no = "?m_no=" + intent.getStringExtra("m_no");

        mItemList = goods_list(m_no);
        listAdapter = new WishListAdapter(mContext, mItemList);
        listView.setAdapter(listAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext =  getApplicationContext();

        listView = (ListView)findViewById(R.id.lv_wish_list);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    public ArrayList<WishInfo> goods_list(String params){
        String url="http://petbox.kr/petboxjson/member_wish_list.php";
        String InsertDB = "wish_list";
        String display_goods_list;
        mItemList = new ArrayList<WishInfo>();

        String goodsno="";
        String opt1="";
        String opt2="";
        String addopt="";
        String img_i="";
        String goodsnm="";
        String goods_consumer="";
        String goods_price="";
        String icon="";
        String point="";
        String point_count="";

        try {

            display_goods_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url,params,InsertDB).get();
            JSONArray display_goods_Array = new JSONArray(display_goods_list);
            JSONObject display_goods_obs = display_goods_Array.getJSONObject(0);
            JSONArray display_data_obj = display_goods_obs.getJSONArray("wish_list");

            WishInfo info[] = new WishInfo[display_data_obj.length()];

            for (int k = 0; k < display_data_obj.length(); k++) {
                JSONObject ca_object = display_data_obj.getJSONObject(k);

                info[k] = new WishInfo(goodsno, opt1, opt2, addopt, img_i, goodsnm, goods_consumer, goods_price, icon, point, point_count);

                info[k].goodsno = ca_object.getString("goodsno");
                info[k].opt1 = ca_object.getString("opt1");
                info[k].opt2 = ca_object.getString("opt2");
                info[k].addopt = ca_object.getString("addopt");
                info[k].img_i = ca_object.getString("img_i");
                info[k].goods_price = ca_object.getString("goods_price");
                info[k].goods_consumer = ca_object.getString("goods_consumer");
                info[k].goodsnm = ca_object.getString("goodsnm");
                info[k].point = ca_object.getString("point");
                info[k].point_count = ca_object.getString("point_count");

                if (ca_object.getString("icon") != null) {
                    icon = ca_object.getString("icon");
                } else {
                    icon = "0";
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
