package com.petbox.shop.Fragment.MyPage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.petbox.shop.Adapter.List.CouponListAdapter;
import com.petbox.shop.Adapter.List.EmoneyListAdapter;
import com.petbox.shop.Adapter.List.OrderListAdapter;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Delegate.MyPageDelegate;
import com.petbox.shop.Item.CouponInfo;
import com.petbox.shop.Item.EmoneyInfo;
import com.petbox.shop.Item.OrderItemInfo;
import com.petbox.shop.JsonParse;
import com.petbox.shop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPageCouponList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPageCouponList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageCouponList extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView tv_mypage_coupon_extinc,tv_mypage_coupon;
    EditText et_coupon_insert1,et_coupon_insert2,et_coupon_insert3,et_coupon_insert4;

    Button bt_coupon_reg,bt_go_home;
    ListView lv_coupon_list;

    ScrollView coupon_scroll;

    CouponListAdapter listAdapter;
    ArrayList<CouponInfo> mItemList;

    String coupon_list;
    String url,params3,InsertDB;

    MyPageDelegate delegate;
    private PullToRefreshListView listView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPageCouponList.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPageCouponList newInstance(MyPageDelegate delegate, String param2) {
        MyPageCouponList fragment = new MyPageCouponList();
        Bundle args = new Bundle();
        fragment.delegate = delegate;
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyPageCouponList() {
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_page_coupon_list, container, false);

        Log.e("--coupon fragment--", "inda club ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        tv_mypage_coupon_extinc = (TextView) v.findViewById(R.id.tv_mypage_coupon_extinc);
        tv_mypage_coupon = (TextView) v.findViewById(R.id.tv_mypage_coupon);

        et_coupon_insert1 = (EditText) v.findViewById(R.id.et_coupon_insert1);
        et_coupon_insert2 = (EditText) v.findViewById(R.id.et_coupon_insert2);
        et_coupon_insert3 = (EditText) v.findViewById(R.id.et_coupon_insert3);
        et_coupon_insert4 = (EditText) v.findViewById(R.id.et_coupon_insert4);

        bt_coupon_reg = (Button) v.findViewById(R.id.bt_coupon_reg);
        bt_go_home = (Button) v.findViewById(R.id.bt_go_home);

        lv_coupon_list = (ListView) v.findViewById(R.id.lv_coupon_list);

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
            params3 = "?m_id="+ Constants.PREF_KEY_ID;
            params3 += "@mypage_info=" + 804;
            InsertDB = "mypage_coupon_list";
            cancoupon = 0;

            coupon_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url, params3, InsertDB).get();
            Log.e("order_list", coupon_list);
            JSONArray CouponListArray = new JSONArray(coupon_list);
            mItemList = new ArrayList<CouponInfo>();
            CouponInfo info[] = new CouponInfo[CouponListArray.length()];

            for (int k = 0; k < CouponListArray.length(); k++) {
                JSONObject Coupon_object = CouponListArray.getJSONObject(k);

                info[k] = new CouponInfo(sno, coupon_name, coupon_type, coupon_price, status, sdate, edate);

                info[k].sno = Coupon_object.getString("sno");
                info[k].coupon_name = Coupon_object.getString("coupon");
                info[k].coupon_type = Coupon_object.getString("coupon_type");
                info[k].coupon_price = Coupon_object.getString("coupon_price");
                info[k].status = Coupon_object.getString("cnt");
                info[k].sdate = Coupon_object.getString("sdate");
                info[k].edate = Coupon_object.getString("edate");

                if (Coupon_object.getInt("cnt") == 2) {
                    cancoupon++;
                    if(Coupon_object.getInt("remain_date") < 30){
                        edt_coupon++;
                    }
                }

                mItemList.add(info[k]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Cancoiupon", String.valueOf(cancoupon));
        tv_mypage_coupon.setText(String.valueOf(cancoupon));
        tv_mypage_coupon_extinc.setText(String.valueOf(edt_coupon));

        listAdapter = new CouponListAdapter(getContext(), mItemList);
        lv_coupon_list.setAdapter(listAdapter);

        // Inflate the layout for this fragment
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

    @Override
    public void onClick(View v) {
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
}