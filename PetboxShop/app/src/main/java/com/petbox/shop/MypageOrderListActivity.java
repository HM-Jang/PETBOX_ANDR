package com.petbox.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.petbox.shop.Adapter.List.OrderListAdapter;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Item.OrderItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MypageOrderListActivity extends AppCompatActivity {

    //private OnFragmentInteractionListener mListener;

    ListView lv_mypage_order_list;
    LinearLayout ll_mypage_order_list;
    TextView tv_mypage_order_list_date,tv_mypage_order_list_ordernum,tv_mypage_order_list_ordernm;
    TextView tv_mypage_order_list_orderprice,tv_mypage_order_list_orderpg;
    TextView tv_mypage_order_list_status,tv_mypage_order_list_status_view;

    OrderListAdapter listAdapter;
    ArrayList<OrderItemInfo> mItemList;

    String order_list="";
    String url,params3,InsertDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_order_list);

        lv_mypage_order_list = (ListView)findViewById(R.id.lv_mypage_order_list);
        ll_mypage_order_list = (LinearLayout)findViewById(R.id.ll_mypage_order_list);

        tv_mypage_order_list_date = (TextView)findViewById(R.id.tv_mypage_order_list_date);
        tv_mypage_order_list_ordernum = (TextView)findViewById(R.id.tv_mypage_order_list_ordernum);
        tv_mypage_order_list_ordernm = (TextView)findViewById(R.id.tv_mypage_order_list_ordernm);
        tv_mypage_order_list_orderprice = (TextView)findViewById(R.id.tv_mypage_order_list_orderprice);
        tv_mypage_order_list_orderpg = (TextView)findViewById(R.id.tv_mypage_order_list_orderpg);
        tv_mypage_order_list_status = (TextView)findViewById(R.id.tv_mypage_order_list_status);
        tv_mypage_order_list_status_view = (TextView)findViewById(R.id.tv_mypage_order_list_status_view);

        try {
            String ordno = "";
            String nameOrder = "";
            String email = "";
            String phoneOrder = "";
            String mobileOrder = "";
            String nameReceiver = "";
            String phoneReceiver = "";
            String mobileReceiver = "";
            String zipcode = "";
            String address = "";
            String road_address = "";
            String settleprice = "";
            String prn_settleprice = "";
            String goodsprice = "";
            String deli_title = "";
            String deli_type = "";
            String deli_msg = "";
            String delivery = "";
            String coupon = "";
            String emoney = "";
            String reserve = "";
            String bankAccount = "";
            String bankSender = "";
            String deliveryno = "";
            String deliverycode = "";
            String m_no = "";
            String orddt = "";
            String uptdt_ = "";
            String str_step = "";
            String str_settlekind = "";
            String idx = "";
            String goodsnm = "";

            url = "http://petbox.kr/petboxjson/member_info.php";
            params3 = "?m_id="+  STPreferences.getString(Constants.PREF_KEY_ID);
            params3 += "&mypage_info="+805;
            InsertDB = "mypage_order_list";

            Log.e("Constants.PREF_KEY_ID", STPreferences.getString(Constants.PREF_KEY_ID));

            //order_list = new JsonParse.JsonLoadingTask().execute(url,params3).get();

            order_list = new JsonParse.JsonLoadingTask(getApplicationContext()).execute(url, params3,InsertDB).get();

            Log.e("order_list",order_list);
            if(order_list.equals("null")) {
            }else{

                JSONArray OrderListArray = new JSONArray(order_list);
                mItemList = new ArrayList<OrderItemInfo>();
                OrderItemInfo info[] = new OrderItemInfo[OrderListArray.length()];

                for (int k = 0; k < OrderListArray.length(); k++) {
                    JSONObject Order_object = OrderListArray.getJSONObject(k);

                    info[k] = new OrderItemInfo(ordno, nameOrder, email, phoneOrder, mobileOrder, nameReceiver, phoneReceiver, mobileReceiver, zipcode, address, road_address, settleprice, prn_settleprice, goodsprice, deli_title, deli_type, deli_msg, delivery, coupon, emoney, reserve, bankAccount, bankSender, deliveryno, deliverycode, m_no, orddt, uptdt_, str_step, str_settlekind, idx, goodsnm);

                    Log.e("orddt", Order_object.getString("orddt"));

                    info[k].orddt = Order_object.getString("orddt");
                    info[k].ordno = Order_object.getString("ordno");
                    info[k].goodsnm = Order_object.getString("goodsnm");
                    info[k].prn_settleprice = Order_object.getString("prn_settleprice");
                    info[k].str_settlekind = Order_object.getString("str_settlekind");
                    info[k].str_step = Order_object.getString("str_step");

                    info[k].nameOrder = "";
                    info[k].email = "";
                    info[k].phoneOrder = "";
                    info[k].mobileOrder = "";
                    info[k].nameReceiver = "";
                    info[k].phoneReceiver = "";
                    info[k].mobileReceiver = "";
                    info[k].zipcode = "";
                    info[k].address = "";
                    info[k].road_address = "";
                    info[k].settleprice = "";
                    info[k].goodsprice = "";
                    info[k].deli_title = "";
                    info[k].deli_type = "";
                    info[k].deli_msg = "";
                    info[k].delivery = "";
                    info[k].coupon = "";
                    info[k].emoney = "";
                    info[k].reserve = "";
                    info[k].bankAccount = "";
                    info[k].bankSender = "";
                    info[k].deliveryno = "";
                    info[k].deliverycode = "";
                    info[k].m_no = "";
                    info[k].uptdt_ = "";
                    info[k].idx = "";

                    mItemList.add(info[k]);

                }

                lv_mypage_order_list = (ListView) findViewById(R.id.lv_mypage_order_list);
                listAdapter = new OrderListAdapter(getApplicationContext(), mItemList);
                lv_mypage_order_list.setAdapter( listAdapter);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
