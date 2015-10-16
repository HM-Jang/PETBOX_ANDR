package com.petbox.shop.Fragment.Home;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.petbox.shop.Adapter.Grid.BestGoodGridAdapter;
import com.petbox.shop.Adapter.List.PlanningListAdapter;
import com.petbox.shop.Adapter.Pager.BestGoodPagerAdapter;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.PlanningItemInfo;
import com.petbox.shop.JsonParse;
import com.petbox.shop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PlanningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanningFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int SELECT_DOG = 0;
    private static final int SELECT_CAT = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn_dog, btn_cat;
    PullToRefreshListView listView;
    PlanningListAdapter listAdapter_dog;
    PlanningListAdapter listAdapter_cat;

    ArrayList<PlanningItemInfo> mitemList;
    PlanningListAdapter listAdapter;

    ArrayList<PlanningItemInfo> mItemList_dog;
    ArrayList<PlanningItemInfo> mItemList_cat;

    int selected = SELECT_DOG;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanningFragment newInstance(String param1, String param2) {
        PlanningFragment fragment = new PlanningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlanningFragment() {
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
        Log.e("PlanningFragment", "-------------------------PlanningFragment");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_planning, container, false);

        btn_dog = (Button) v.findViewById(R.id.btn_planning_dog);
        btn_dog.setOnClickListener(this);

        btn_cat = (Button) v.findViewById(R.id.btn_planning_cat);
        btn_cat.setOnClickListener(this);

        String url="http://petbox.kr/petboxjson/special_offer_list.php";
        String params = "";
        String InsertDB = "planning_list";
        String display_goods_list;
        mitemList = new ArrayList<PlanningItemInfo>();
        mItemList_dog = new ArrayList<PlanningItemInfo>();
        mItemList_cat = new ArrayList<PlanningItemInfo>();

        try {
            String linkaddr = "";
            String loccd = "";
            String img = "";

            display_goods_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url,params,InsertDB).get();
            JSONArray display_goods_Array = new JSONArray(display_goods_list);
            PlanningItemInfo info[] = new PlanningItemInfo[display_goods_Array.length()];
            PlanningItemInfo info_c[] = new PlanningItemInfo[display_goods_Array.length()];

            int dog_count = 0;
            int cat_count = 0;

            for (int k = 0; k < display_goods_Array.length(); k++) {
                JSONObject ca_object = display_goods_Array.getJSONObject(k);

                if(Integer.parseInt(ca_object.getString("loccd")) == 3){
                    info[dog_count] = new PlanningItemInfo(linkaddr,loccd,img);
                    info[dog_count].linkaddr = ca_object.getString("linkaddr");
                    info[dog_count].loccd = ca_object.getString("loccd");
                    info[dog_count].img = ca_object.getString("img");
                    Log.e("loccd",ca_object.getString("loccd"));
                    mItemList_dog.add(info[dog_count]);
                    dog_count++;
                }else{
                    info_c[cat_count] = new PlanningItemInfo(linkaddr,loccd,img);
                    info_c[cat_count].linkaddr = ca_object.getString("linkaddr");
                    info_c[cat_count].loccd = ca_object.getString("loccd");
                    info_c[cat_count].img = ca_object.getString("img");
                    mItemList_cat.add(info_c[cat_count]);
                    cat_count++;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mitemList = goods_list();
        listAdapter_dog = new PlanningListAdapter(getContext() ,mItemList_dog);
        listAdapter_cat = new PlanningListAdapter(getContext() ,mItemList_cat);

        listView = (PullToRefreshListView) v.findViewById(R.id.list_planning);

        listView.setAdapter(listAdapter_dog);

        return v;
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

    public void setAdapter(int selected){
        if(selected == SELECT_DOG){
            listView.setAdapter(listAdapter_dog);

        }else if(selected == SELECT_CAT){
            listView.setAdapter(listAdapter_cat);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id){
            case R.id.btn_planning_dog :
                btn_cat.setBackgroundResource(R.drawable.home_cat_off);
                btn_dog.setBackgroundResource(R.drawable.home_dog_on);
                selected = SELECT_DOG;
                setAdapter(selected);
                break;

            case R.id.btn_planning_cat :
                btn_cat.setBackgroundResource(R.drawable.home_cat_on);
                btn_dog.setBackgroundResource(R.drawable.home_dog_off);
                selected = SELECT_CAT;
                setAdapter(selected);
                break;
        }
    }

    public ArrayList<PlanningItemInfo> goods_list(){
        String url="http://petbox.kr/petboxjson/special_offer_list.php";
        String params = "";
        String InsertDB = "planning_list";
        String display_goods_list;
        mitemList = new ArrayList<PlanningItemInfo>();

        try {
            String linkaddr = "";
            String loccd = "";
            String img = "";

            display_goods_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url,params,InsertDB).get();
            JSONArray display_goods_Array = new JSONArray(display_goods_list);
            PlanningItemInfo info[] = new PlanningItemInfo[display_goods_Array.length()];

            for (int k = 0; k < display_goods_Array.length(); k++) {
                JSONObject ca_object = display_goods_Array.getJSONObject(k);

                info[k] = new PlanningItemInfo(linkaddr,loccd,img);
                info[k].linkaddr = ca_object.getString("linkaddr");
                info[k].loccd = ca_object.getString("loccd");
                info[k].img = ca_object.getString("img");

                mitemList.add(info[k]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mitemList;
    }
}
