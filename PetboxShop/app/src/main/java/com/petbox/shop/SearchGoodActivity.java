package com.petbox.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.petbox.shop.Adapter.List.GoodsListAdapter;
import com.petbox.shop.Item.BestGoodInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchGoodActivity extends AppCompatActivity implements View.OnClickListener {


    String Search_data;
    String Search_data_list;

    ArrayList<BestGoodInfo> mItemList;
    GoodsListAdapter listAdapter;

    ListView lv_search_goods_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_good);

        lv_search_goods_list = (ListView)findViewById(R.id.lv_search_goods_list);

        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword");

        String url="http://petbox.kr/petboxjson/search_goods_list.php";
        String params = "?keyword="+keyword;
        String InsertDB;

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

            InsertDB = "search_goods_data";

            Search_data_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url,params,InsertDB).get();

            JSONArray searchArray = new JSONArray(Search_data_list);
            JSONObject search_object = searchArray.getJSONObject(0);
            JSONArray search_data_obj = search_object.getJSONArray("search_data");

            mItemList = new ArrayList<BestGoodInfo>();
            BestGoodInfo info[] = new BestGoodInfo[search_data_obj.length()];

            for (int k = 0; k < 30; k++) {
                JSONObject sg_object = search_data_obj.getJSONObject(k);

                info[k] = new BestGoodInfo(sort,imgUrl,name,goodsno,rate,origin_price,price,rating,rating_person,icon);

                info[k].sort = Integer.parseInt(sg_object.getString("sort"));
                info[k].imgUrl = sg_object.getString("img_i");
                info[k].name = sg_object.getString("goodsnm");
                info[k].origin_price = sg_object.getString("goods_consumer");
                info[k].price = sg_object.getString("goods_price");
                info[k].goodsno = sg_object.getString("goodsno");
                info[k].rating = Float.parseFloat(sg_object.getString("point"));
                info[k].rating_person = Integer.parseInt(sg_object.getString("point_count"));
                if(sg_object.getString("icon") != null){
                    icon = Integer.parseInt(sg_object.getString("icon"));
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

        listAdapter = new GoodsListAdapter(getApplicationContext(), mItemList);
        lv_search_goods_list.setAdapter(listAdapter);

        /********
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         ********/
    }

    @Override
    public void onClick(View v) {

    }
}
