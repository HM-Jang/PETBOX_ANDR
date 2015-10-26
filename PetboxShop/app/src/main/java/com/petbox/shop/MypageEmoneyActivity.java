package com.petbox.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.petbox.shop.Adapter.List.EmoneyListAdapter;
import com.petbox.shop.Application.PetboxApplication;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Item.EmoneyInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MypageEmoneyActivity extends AppCompatActivity implements View.OnClickListener {


    // TextView tv_mypage_emoney_extinc,tv_mypage_emoney;
    TextView tv_mypage_emoney;
    ListView lv_emoney_list;
    TextView tv_emoney_date,tv_emoney_info,tv_emoney_plus_emoney,tv_emoney_minus_emoney;

    EmoneyListAdapter listAdapter;
    ArrayList<EmoneyInfo> mItemList;

    String emoney_list;
    String url,params3,InsertDB;

    int emoney_total = 0;
    int emoney_totalmult = 0;

    ImageView ibtn_good_info_back;

    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_emoney);

        mTracker = ((PetboxApplication)this.getApplication()).getDefaultTracker();
        mTracker.setScreenName("마이페이지 - 나의포인트");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        //tv_mypage_emoney_extinc = (TextView)v.findViewById(R.id.tv_mypage_emoney_extinc);
        tv_mypage_emoney = (TextView)findViewById(R.id.tv_mypage_emoney);

        lv_emoney_list = (ListView)findViewById(R.id.lv_emoney_list);
        tv_emoney_date = (TextView)findViewById(R.id.tv_emoney_date);
        tv_emoney_info = (TextView)findViewById(R.id.tv_emoney_info);
        tv_emoney_plus_emoney = (TextView)findViewById(R.id.tv_emoney_plus_emoney);
        tv_emoney_minus_emoney = (TextView)findViewById(R.id.tv_emoney_minus_emoney);

        ibtn_good_info_back = (ImageView)findViewById(R.id.ibtn_good_info_back);
        ibtn_good_info_back.setOnClickListener(this);

        try {
            String sort = "";
            String ordno = "";
            String emoney = "";
            String memo = "";
            String regdt = "";
            String regdts = "";
            String idx = "";

            url = "http://petbox.kr/petboxjson/member_info.php";
            params3 = "?m_id="+ STPreferences.getString(Constants.PREF_KEY_ID);
            params3 += "&mypage_info="+803;
            InsertDB = "mypage_emoney_list";

            //order_list = new JsonParse.JsonLoadingTask().execute(url,params3).get();
            emoney_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url, params3,InsertDB).get();
            Log.e("order_list", emoney_list);
            if(emoney_list.equals("")){

            }else {
                JSONArray EmoneyListArray = new JSONArray(emoney_list);
                mItemList = new ArrayList<EmoneyInfo>();
                EmoneyInfo info[] = new EmoneyInfo[EmoneyListArray.length()];

                for (int k = 0; k < EmoneyListArray.length(); k++) {
                    JSONObject Emoney_object = EmoneyListArray.getJSONObject(k);

                    info[k] = new EmoneyInfo(sort, ordno, emoney, memo, regdt, regdts, idx);

                    info[k].sort = Emoney_object.getString("sno");
                    info[k].ordno = Emoney_object.getString("ordno");
                    info[k].emoney = Emoney_object.getString("emoney");
                    info[k].memo = Emoney_object.getString("memo");
                    info[k].regdt = Emoney_object.getString("regdt");
                    info[k].regdts = Emoney_object.getString("regdts");
                    info[k].idx = Emoney_object.getString("idx");


                    if (Emoney_object.getString("emoney").contains("-")) {
                        emoney_totalmult = Integer.parseInt(Emoney_object.getString("emoney").replace("-", ""));
                        emoney_total -= emoney_totalmult;
                    } else {
                        emoney_totalmult = Integer.parseInt(Emoney_object.getString("emoney").replace("-", ""));
                        emoney_total += emoney_totalmult;
                    }

                    mItemList.add(info[k]);

                }

                tv_mypage_emoney.setText(emoney_total+"P");
                //lv_emoney_list = (ListView) v.findViewById(R.id.lv_emoney_list);
                listAdapter = new EmoneyListAdapter(getApplicationContext(), mItemList);
                lv_emoney_list.setAdapter(listAdapter);
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
