package com.petbox.shop.Fragment.MyPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.petbox.shop.CartListWebView;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Delegate.MyPageDelegate;
import com.petbox.shop.Item.CouponInfo;
import com.petbox.shop.JsonParse;
import com.petbox.shop.MainActivity;
import com.petbox.shop.MypageCusomerActivity;
import com.petbox.shop.MypageMyReviewActivity;
import com.petbox.shop.MypageQnaListActivity;
import com.petbox.shop.R;
import com.petbox.shop.WishListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPageFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_email_address,tv_mypage_near_count,tv_mypage_point_count,tv_mypage_coupon_count;
    LinearLayout ll_mypage_near,ll_mypage_point,ll_mypage_coupon;
    LinearLayout ll_mypage_order,ll_mypage_sub_order,ll_mypage_wish,ll_mypage_1by1,ll_mypage_review,ll_mypage_inquiry,ll_mypage_center,ll_mypage_setting;

    MyPageDelegate delegate;


    String url = "";
    String params3 = "";
    String InsertDB = "";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPageFragment newInstance(MyPageDelegate delegate, String param2) {
        MyPageFragment fragment = new MyPageFragment();
        Bundle args = new Bundle();

        fragment.delegate = delegate;
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyPageFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_page, container, false);

        tv_email_address = (TextView)v.findViewById(R.id.tv_email_address);
        tv_mypage_near_count = (TextView)v.findViewById(R.id.tv_mypage_near_count);
        tv_mypage_point_count = (TextView)v.findViewById(R.id.tv_mypage_point_count);
        tv_mypage_coupon_count = (TextView)v.findViewById(R.id.tv_mypage_coupon_count);

        ll_mypage_near = (LinearLayout)v.findViewById(R.id.ll_mypage_near);
        ll_mypage_point = (LinearLayout)v.findViewById(R.id.ll_mypage_point);
        ll_mypage_coupon = (LinearLayout)v.findViewById(R.id.ll_mypage_coupon);
        ll_mypage_order = (LinearLayout)v.findViewById(R.id.ll_mypage_order);
        ll_mypage_wish = (LinearLayout)v.findViewById(R.id.ll_mypage_wish);
        ll_mypage_review = (LinearLayout)v.findViewById(R.id.ll_mypage_review);
        ll_mypage_inquiry = (LinearLayout)v.findViewById(R.id.ll_mypage_inquiry);
        ll_mypage_center = (LinearLayout)v.findViewById(R.id.ll_mypage_center);


        tv_email_address.setOnClickListener(this);
        tv_mypage_near_count.setOnClickListener(this);
        tv_mypage_point_count.setOnClickListener(this);

        ll_mypage_near.setOnClickListener(this);
        ll_mypage_point.setOnClickListener(this);
        ll_mypage_coupon.setOnClickListener(this);
        ll_mypage_order.setOnClickListener(this);

        ll_mypage_wish.setOnClickListener(this);

        ll_mypage_review.setOnClickListener(this);
        ll_mypage_inquiry.setOnClickListener(this);
        ll_mypage_center.setOnClickListener(this);
        // ll_mypage_setting.setOnClickListener(this);

        url = "http://petbox.kr/petboxjson/member_info.php";
        params3 = "?m_id="+ Constants.PREF_KEY_ID;
        params3 += "&mypage_info=" + 809;
        InsertDB = "mypage_coupon_list";



        String mypage_list;

        try{
            mypage_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url, params3, InsertDB).get();

            Log.e("order_list", mypage_list);
            JSONArray MypageArray = new JSONArray(mypage_list);
            JSONObject  Mypage_object = MypageArray.getJSONObject(0);

            Log.e("tv_mypage_near_count", Mypage_object.getString("order_count"));
            Log.e("tv_mypage_point_count",Mypage_object.getString("data_emoney"));
            Log.e("tv_mypage_coupon_count",Mypage_object.getString("coupon_ck"));

            tv_mypage_near_count.setText(Mypage_object.getString("order_count"));
            tv_mypage_point_count.setText(Mypage_object.getString("data_emoney"));
            tv_mypage_coupon_count.setText(Mypage_object.getString("coupon_ck"));
            tv_email_address.setText(Mypage_object.getString("data_email"));


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.e("Onclick", "----------------------" + id);

        Intent wish_list = new Intent(getActivity().getApplicationContext(), WishListActivity.class);
        Intent review_intnet = new Intent(getActivity().getApplicationContext(), MypageMyReviewActivity.class);
        Intent qna_intnet = new Intent(getActivity().getApplicationContext(), MypageQnaListActivity.class);
        Intent center_intent = new Intent(getActivity().getApplicationContext(), MypageCusomerActivity.class);

        switch(id) {

            case R.id.ll_mypage_near:
                Log.e("Onclick", "----------------------ll_mypage_near");
                delegate.setFragmentItem(1);
                // ((MainActivity) getActivity()).setMyPageOrderList();
                break;
            case R.id.ll_mypage_point:
                Log.e("Onclick", "----------------------ll_mypage_near");
                delegate.setFragmentItem(2);
                // ((MainActivity) getActivity()).setMyPageOrderList();
                break;
            case R.id.ll_mypage_coupon:
                Log.e("Onclick", "----------------------ll_mypage_near");
                delegate.setFragmentItem(3);
                // ((MainActivity) getActivity()).setMyPageOrderList();
                break;

            case R.id.ll_mypage_order:
                Log.e("Onclick", "----------------------ll_mypage_near");
                delegate.setFragmentItem(4);
                // ((MainActivity) getActivity()).setMyPageOrderList();

            case R.id.ll_mypage_wish:
                Log.e("Onclick", "----------------------ll_mypage_near");
                wish_list.putExtra("m_no", "1");
                startActivity(wish_list);
                // ((MainActivity) getActivity()).setMyPageOrderList();
                break;

            case R.id.ll_mypage_review:
                Log.e("Onclick", "----------------------ll_mypage_near");
                startActivity(review_intnet);
                // ((MainActivity) getActivity()).setMyPageOrderList();
                break;

            case R.id.ll_mypage_inquiry:
                Log.e("Onclick", "----------------------ll_mypage_near");
                startActivity(qna_intnet);
                // ((MainActivity) getActivity()).setMyPageOrderList();
                break;

            case R.id.ll_mypage_center:
                Log.e("Onclick", "----------------------ll_mypage_near");
                startActivity(center_intent);
                // ((MainActivity) getActivity()).setMyPageOrderList();
                break;

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
