package com.petbox.shop.Fragment.MyPage;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.petbox.shop.Adapter.List.CategoryListAdapter;
import com.petbox.shop.Adapter.List.OrderListAdapter;
import com.petbox.shop.Delegate.MyPageDelegate;
import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.OrderItemInfo;
import com.petbox.shop.JsonParse;
import com.petbox.shop.MainActivity;
import com.petbox.shop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPageOrderList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPageOrderList#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyPageOrderList extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ListView lv_mypage_order_list;
    LinearLayout ll_mypage_order_list;
    TextView tv_mypage_order_list_date,tv_mypage_order_list_ordernum,tv_mypage_order_list_ordernm;
    TextView tv_mypage_order_list_orderprice,tv_mypage_order_list_orderpg;
    TextView tv_mypage_order_list_status,tv_mypage_order_list_status_view;

    OrderListAdapter listAdapter;
    ArrayList<OrderItemInfo> mItemList;

    String order_list;
    String url,params3,InsertDB;

    MyPageDelegate delegate;
    private PullToRefreshListView listView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPageOrderList.
     */

    // TODO: Rename and change types and number of parameters
    public static MyPageOrderList newInstance(MyPageDelegate delegate, String param2) {
        MyPageOrderList fragment = new MyPageOrderList();
        Bundle args = new Bundle();
        fragment.delegate = delegate;
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyPageOrderList() {
        // Required empty public
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_my_page_order_list, container, false);
        lv_mypage_order_list = (ListView)v.findViewById(R.id.lv_mypage_order_list);
        ll_mypage_order_list = (LinearLayout)v.findViewById(R.id.ll_mypage_order_list);

        tv_mypage_order_list_date = (TextView)v.findViewById(R.id.tv_mypage_order_list_date);
        tv_mypage_order_list_ordernum = (TextView)v.findViewById(R.id.tv_mypage_order_list_ordernum);
        tv_mypage_order_list_ordernm = (TextView)v.findViewById(R.id.tv_mypage_order_list_ordernm);
        tv_mypage_order_list_orderprice = (TextView)v.findViewById(R.id.tv_mypage_order_list_orderprice);
        tv_mypage_order_list_orderpg = (TextView)v.findViewById(R.id.tv_mypage_order_list_orderpg);
        tv_mypage_order_list_status = (TextView)v.findViewById(R.id.tv_mypage_order_list_status);
        tv_mypage_order_list_status_view = (TextView)v.findViewById(R.id.tv_mypage_order_list_status_view);

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
            params3 = "?m_no="+1;
            params3 += "&mypage_info="+805;

            InsertDB = "mypage_order_list";

            //order_list = new JsonParse.JsonLoadingTask().execute(url,params3).get();
            order_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url, params3,InsertDB).get();
            Log.e("order_list",order_list);
            JSONArray OrderListArray = new JSONArray(order_list);
            mItemList = new ArrayList<OrderItemInfo>();
            OrderItemInfo info[] = new OrderItemInfo[OrderListArray.length()];

            for (int k = 0; k < OrderListArray.length(); k++) {
                JSONObject Order_object = OrderListArray.getJSONObject(k);

                info[k] = new OrderItemInfo(ordno,nameOrder,email,phoneOrder,mobileOrder,nameReceiver,phoneReceiver,mobileReceiver,zipcode,address,road_address,settleprice,prn_settleprice,goodsprice,deli_title,deli_type,deli_msg,delivery,coupon,emoney,reserve,bankAccount,bankSender,deliveryno,deliverycode,m_no,orddt,uptdt_,str_step,str_settlekind,idx,goodsnm);

                Log.e("orddt",Order_object.getString("orddt"));

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


        } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    } catch (JSONException e) {
        e.printStackTrace();
    }
    Log.e("mItemList = ",  "// size : " + mItemList.size());

    lv_mypage_order_list = (ListView) v.findViewById(R.id.lv_mypage_order_list);
    listAdapter = new OrderListAdapter(getContext(), mItemList);
    lv_mypage_order_list.setAdapter( listAdapter);


    return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void refreshLIstView(ArrayList<OrderItemInfo> itemList){
        mItemList.clear();
        mItemList = itemList;

        listAdapter = new OrderListAdapter(getActivity().getApplicationContext(), mItemList);
        listView.setAdapter((ListAdapter) listAdapter);
    }


    public void onClick(View v) {
        int id = v.getId();//클릭시 해당 이벤트로 이동
        int db_id = 0;
        switch(id){

        }
    }

}
