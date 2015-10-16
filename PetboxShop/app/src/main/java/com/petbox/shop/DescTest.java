package com.petbox.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.petbox.shop.Item.PlanningItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DescTest extends AppCompatActivity {

    WebView wv_d;
    final String tag = "Hello World! - 1";
    final String mimeType = "text/html";
    final String encoding = "utf-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_test);

        String url="http://petbox.kr/petboxjson/json_test.php";
        String params = "";
        String InsertDB = "planning_list";
        String display_goods_list;

        String tag = "gogo";

        WebView wv_d = (WebView)findViewById(R.id.wv_desc);


        try {

            display_goods_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url,params,InsertDB).get();
            JSONArray display_goods_Array = new JSONArray(display_goods_list);

            for (int k = 0; k < display_goods_Array.length(); k++) {
                JSONObject ca_object = display_goods_Array.getJSONObject(k);
                tag += ca_object.getString("longdesc");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        wv_d.loadData(tag, mimeType, encoding);


    }
}
