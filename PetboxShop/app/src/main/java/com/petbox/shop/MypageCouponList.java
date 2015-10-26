package com.petbox.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Adapter.List.CouponListAdapter;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Item.CouponInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MypageCouponList extends AppCompatActivity implements View.OnClickListener {

    TextView tv_mypage_coupon_extinc,tv_mypage_coupon;
    EditText et_coupon_insert1,et_coupon_insert2,et_coupon_insert3,et_coupon_insert4;

    Button bt_coupon_reg,bt_go_home;
    ListView lv_coupon_list;

    ScrollView coupon_scroll;

    CouponListAdapter listAdapter;
    ArrayList<CouponInfo> mItemList;

    String coupon_list;
    String url,params3,InsertDB;

    ImageView ibtn_good_info_back;

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_coupon_list);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        mTracker.setScreenName("마이페이지 - 쿠폰함");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        tv_mypage_coupon_extinc = (TextView) findViewById(R.id.tv_mypage_coupon_extinc);
        tv_mypage_coupon = (TextView) findViewById(R.id.tv_mypage_coupon);

        //et_coupon_insert1 = (EditText) v.findViewById(R.id.et_coupon_insert1);
        //et_coupon_insert2 = (EditText) v.findViewById(R.id.et_coupon_insert2);
        //et_coupon_insert3 = (EditText) v.findViewById(R.id.et_coupon_insert3);
        //et_coupon_insert4 = (EditText) v.findViewById(R.id.et_coupon_insert4);

        //bt_coupon_reg = (Button) v.findViewById(R.id.bt_coupon_reg);
        //bt_go_home = (Button) v.findViewById(R.id.bt_go_home);

        lv_coupon_list = (ListView) findViewById(R.id.lv_coupon_list);

        ibtn_good_info_back = (ImageView)findViewById(R.id.ibtn_good_info_back);
        ibtn_good_info_back.setOnClickListener(this);



        int cancoupon = 0;
        int edt_coupon = 0;
        String edt_coupon_mult="";

        try {
            String sno = "";
            String coupon_name = "";
            String coupon_type = "";
            String coupon_price = "";
            String status = "";
            String sdate = "";
            String edate = "";

            url = "http://petbox.kr/petboxjson/member_info.php";
            params3 = "?m_id="+ STPreferences.getString(Constants.PREF_KEY_ID);
            params3 += "&mypage_info=" + 804;
            InsertDB = "mypage_coupon_list";
            cancoupon = 0;

            coupon_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url, params3, InsertDB).get();
            Log.e("order_list", coupon_list);

            if(coupon_list.equals("null")){

            }else {
                mItemList = new ArrayList<CouponInfo>();
                JSONArray CouponListArray = new JSONArray(coupon_list);
                JSONObject Coupon_obs = CouponListArray.getJSONObject(0);
                JSONArray CouponListArrayobj = Coupon_obs.getJSONArray("goods");
                CouponInfo info[] = new CouponInfo[CouponListArray.length()];

                for (int k = 0; k < CouponListArray.length(); k++) {
                    JSONObject Coupon_object = CouponListArrayobj.getJSONObject(k);

                    info[k] = new CouponInfo(sno, coupon_name, coupon_type, coupon_price, status, sdate, edate);

                    info[k].sno = Coupon_object.getString("sno");
                    info[k].coupon_name = Coupon_object.getString("coupon");
                    info[k].coupon_type = Coupon_object.getString("coupontype");
                    info[k].coupon_price = Coupon_object.getString("price");
                    info[k].status = Coupon_object.getString("cnt");
                    info[k].sdate = Coupon_object.getString("sdate");
                    info[k].edate = Coupon_object.getString("edate");

                    if (Coupon_object.getInt("cnt") == 2) {
                        cancoupon++;
                        if (Coupon_object.getInt("remain_date") < 30) {
                            edt_coupon++;
                        }
                    }

                    mItemList.add(info[k]);
                }

                Log.e("Cancoiupon", String.valueOf(cancoupon));
                tv_mypage_coupon.setText(String.valueOf(cancoupon));
                tv_mypage_coupon_extinc.setText(String.valueOf(edt_coupon));

                listAdapter = new CouponListAdapter(getApplicationContext(), mItemList);
                lv_coupon_list.setAdapter(listAdapter);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.ibtn_good_info_back:
                finish();
                break;
        }
    }
}
