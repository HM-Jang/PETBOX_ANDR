package com.petbox.shop.Fragment.Home;


import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.petbox.shop.Adapter.Grid.BestGoodGridAdapter;
import com.petbox.shop.Adapter.Pager.BestGoodPagerAdapter;
import com.petbox.shop.DB.DBConnector;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public final int DEFAULT_INTERVAL = 5000;

    private ViewPager viewPager;

    PullToRefreshGridView gridView;
    ArrayList<BestGoodInfo> mItemList;
    BestGoodGridAdapter gridAdapter;
    PageIndicator mIndicator;

    Button btn_dog, btn_cat;    // 강아지, 고양이 버튼

    BestGoodPagerAdapter bestGoodPagerAdapter;

    public int interval = DEFAULT_INTERVAL;

    Thread  timerThread;
    Handler handler;

    Boolean isRunning = true;

    int mainColor = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
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
        Log.e("HomeFragment", "-------------------------HomeFragment");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_best_good, container, false);

        mainColor = getResources().getColor(R.color.colorPrimary);

        mItemList = new ArrayList<BestGoodInfo>();
        mItemList = new DBConnector(getContext()).returnFromBestGoodInfo("6");

        View headerView = inflater.inflate(R.layout.custom_slide_image, null);
        viewPager = (ViewPager)headerView.findViewById(R.id.pager_best_good);

        bestGoodPagerAdapter = new BestGoodPagerAdapter(getContext(),"5");
        viewPager.setAdapter(bestGoodPagerAdapter);

        //viewPager.setCurrentItem(3);

        CirclePageIndicator circlePageIndicator = (CirclePageIndicator)headerView.findViewById(R.id.indicator_best_good);
        mIndicator = circlePageIndicator;
        mIndicator.setViewPager(viewPager);

        circlePageIndicator.setPageColor(0xFF6d6d6d);   // Normal 원 색상
        circlePageIndicator.setFillColor(mainColor);   //선택된 원 색상
        circlePageIndicator.setStrokeColor(0x00000000); //테두리 INVISIBLE

        btn_dog = (Button)headerView.findViewById(R.id.btn_slide_dog);
        btn_dog.setOnClickListener(this);

        btn_cat = (Button)headerView.findViewById(R.id.btn_slide_cat);
        btn_cat.setOnClickListener(this);

        gridView = (PullToRefreshGridView)v.findViewById(R.id.grid_best_good);
        gridAdapter = new BestGoodGridAdapter(getActivity().getApplicationContext(), mItemList);
        gridView.addHeaderView(headerView);
        gridView.setAdapter(gridAdapter);

        handler = new Handler(){
            public void handleMessage(Message msg){
                if (viewPager.getCurrentItem() == bestGoodPagerAdapter.getImages() - 1) {
                    viewPager.setCurrentItem(0, true);
                }else{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1, true);
                }
            }
        };


        timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning){
                    try{
                        timerThread.sleep(3000);

                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    //isRunning = false;

                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }
        });

        timerThread.start();

        return v;
    }

    public void initViewPager(){
        viewPager.setCurrentItem(0);
    }

    public void refreshGridView(ArrayList<BestGoodInfo> itemList){
        mItemList.clear();
        mItemList = itemList;

        gridAdapter = new BestGoodGridAdapter(getActivity().getApplicationContext(), mItemList);
        gridView.setAdapter(gridAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btn_slide_dog:
                Toast.makeText(getContext(), "강아지", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_slide_cat:
                Toast.makeText(getContext(), "고양이", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
