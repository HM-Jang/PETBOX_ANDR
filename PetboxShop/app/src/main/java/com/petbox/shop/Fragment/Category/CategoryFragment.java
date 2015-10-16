package com.petbox.shop.Fragment.Category;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.petbox.shop.CtegoryGoodsActivity;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.DataStructure.Tree.Node;
import com.petbox.shop.DataStructure.Tree.Tree;
import com.petbox.shop.Delegate.CategoryDelegate;
import com.petbox.shop.Delegate.HttpGetDelegate;
import com.petbox.shop.Delegate.HttpPostDelegate;
import com.petbox.shop.Item.CategoryInfo;
import com.petbox.shop.Network.CategoryManager;
import com.petbox.shop.Network.HttpGetManager;
import com.petbox.shop.Network.HttpPostManager;
import com.petbox.shop.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */

    private LinearLayout btn_dog_feed, btn_dog_snack, btn_dog_nutrients, btn_dog_medicine, btn_dog_hygienic, btn_dog_bath, btn_dog_house
            , btn_dog_fence, btn_dog_line, btn_dog_accesory, btn_dog_drinkers, btn_dog_beauty, btn_dog_toy;

    private LinearLayout btn_cat_feed, btn_cat_snack, btn_cat_nutrients, btn_cat_medicine, btn_cat_hygienic, btn_cat_bath, btn_cat_house
            , btn_cat_tower, btn_cat_maddaddabi, btn_cat_accesory, btn_cat_drinkers, btn_cat_beauty, btn_cat_toy;

    CategoryDelegate delegate;

    String param;

    CategoryManager categoryManager;

    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(CategoryDelegate delegate, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        fragment.delegate = delegate;

        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {
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
        categoryManager = new CategoryManager();

        View v = inflater.inflate(R.layout.fragment_category, container, false);

        btn_dog_feed = (LinearLayout) v.findViewById(R.id.btn_category_dog_feed);
        btn_dog_feed.setOnClickListener(this);

        btn_dog_snack = (LinearLayout) v.findViewById(R.id.btn_category_dog_snack);
        btn_dog_snack.setOnClickListener(this);

        btn_dog_nutrients = (LinearLayout) v.findViewById(R.id.btn_category_dog_nutrients);
        btn_dog_nutrients.setOnClickListener(this);

        btn_dog_medicine = (LinearLayout) v.findViewById(R.id.btn_category_dog_medicine);
        btn_dog_medicine.setOnClickListener(this);

        btn_dog_hygienic = (LinearLayout) v.findViewById(R.id.btn_category_dog_hygienic);
        btn_dog_hygienic.setOnClickListener(this);

        btn_dog_bath = (LinearLayout) v.findViewById(R.id.btn_category_dog_bath);
        btn_dog_bath.setOnClickListener(this);

        btn_dog_house = (LinearLayout) v.findViewById(R.id.btn_category_dog_house);
        btn_dog_house.setOnClickListener(this);

        btn_dog_fence = (LinearLayout) v.findViewById(R.id.btn_category_dog_fence);
        btn_dog_fence.setOnClickListener(this);

        btn_dog_line = (LinearLayout) v.findViewById(R.id.btn_category_dog_line);
        btn_dog_line.setOnClickListener(this);

        btn_dog_accesory = (LinearLayout) v.findViewById(R.id.btn_category_dog_accessory);
        btn_dog_accesory.setOnClickListener(this);

        btn_dog_drinkers = (LinearLayout) v.findViewById(R.id.btn_category_dog_drinkers);
        btn_dog_drinkers.setOnClickListener(this);

        btn_dog_beauty = (LinearLayout) v.findViewById(R.id.btn_category_dog_beauty);
        btn_dog_beauty.setOnClickListener(this);

        btn_dog_toy = (LinearLayout) v.findViewById(R.id.btn_category_dog_toy);
        btn_dog_toy.setOnClickListener(this);

        btn_cat_feed = (LinearLayout) v.findViewById(R.id.btn_category_cat_feed);
        btn_cat_feed.setOnClickListener(this);

        btn_cat_snack = (LinearLayout) v.findViewById(R.id.btn_category_cat_snack);
        btn_cat_snack.setOnClickListener(this);

        btn_cat_nutrients = (LinearLayout) v.findViewById(R.id.btn_category_cat_nutrients);
        btn_cat_nutrients.setOnClickListener(this);

        btn_cat_medicine = (LinearLayout) v.findViewById(R.id.btn_category_cat_medicine);
        btn_cat_medicine.setOnClickListener(this);

        btn_cat_hygienic = (LinearLayout) v.findViewById(R.id.btn_category_cat_hygienic);
        btn_cat_hygienic.setOnClickListener(this);

        btn_cat_bath = (LinearLayout) v.findViewById(R.id.btn_category_cat_bath);
        btn_cat_bath.setOnClickListener(this);

        btn_cat_house = (LinearLayout) v.findViewById(R.id.btn_category_cat_house);
        btn_cat_house.setOnClickListener(this);

        btn_cat_tower = (LinearLayout) v.findViewById(R.id.btn_category_cat_tower);
        btn_cat_tower.setOnClickListener(this);

        btn_cat_maddaddabi = (LinearLayout) v.findViewById(R.id.btn_category_cat_maddaddabi);
        btn_cat_maddaddabi.setOnClickListener(this);


        btn_cat_drinkers = (LinearLayout) v.findViewById(R.id.btn_category_cat_drinkers);
        btn_cat_drinkers.setOnClickListener(this);

        btn_cat_beauty = (LinearLayout) v.findViewById(R.id.btn_category_cat_beauty);
        btn_cat_beauty.setOnClickListener(this);

        btn_cat_toy = (LinearLayout) v.findViewById(R.id.btn_category_cat_toy);
        btn_cat_toy.setOnClickListener(this);

        /*
        linear_planning = (LinearLayout)v.findViewById(R.id.linear_category_planning);
        linear_planning.setOnClickListener(this);

        linear_primium = (LinearLayout)v.findViewById(R.id.linear_category_primium);
        linear_primium.setOnClickListener(this);

        linear_dog = (LinearLayout)v.findViewById(R.id.linear_category_dog);
        linear_dog.setOnClickListener(this);

        linear_cat = (LinearLayout)v.findViewById(R.id.linear_category_cat);
        linear_cat.setOnClickListener(this);
        */

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

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(),CtegoryGoodsActivity.class);
        int id = v.getId();

        switch(id){
            case R.id.btn_category_dog_feed:
                param = "035";
                //HttpGetManager httpGetManager = new HttpGetManager(this);
                //httpGetManager.start();

                //intent.putExtra("cate_num","035008");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_snack:
                param = "035";
                //intent.putExtra("cate_num","035009");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_nutrients:
                param = "035";
                //intent.putExtra("cate_num","035010");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_medicine:
                param = "035";
                //intent.putExtra("cate_num","035011");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_hygienic:
                param = "035";
                //intent.putExtra("cate_num","035012");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_bath:
                param = "035";
                //intent.putExtra("cate_num","035007");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_house:
                param = "035";
                //intent.putExtra("cate_num","035014");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_fence:
                param = "035";
                //intent.putExtra("cate_num","035015");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_line:
                param = "035";
                //intent.putExtra("cate_num","035029");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_accessory:
                param = "035";
                //intent.putExtra("cate_num","035018");
                //startActivity(intent);

            case R.id.btn_category_dog_drinkers:
                param = "035";
                //intent.putExtra("cate_num", "035032");
                //startActivity(intent);
                break;

            case R.id.btn_category_dog_beauty:
                param = "035";
                //intent.putExtra("cate_num","035033");
                //startActivity(intent);

            case R.id.btn_category_dog_toy:
                param = "035";
                //intent.putExtra("cate_num", "035034");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_feed:
                param = "036";
                //intent.putExtra("cate_num", "036005");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_snack:
                param = "036";
                //intent.putExtra("cate_num", "036004");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_nutrients:
                param = "036";
                //intent.putExtra("cate_num", "036003");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_medicine:
                param = "036";
                //intent.putExtra("cate_num", "036006");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_hygienic:
                param = "036";
                //intent.putExtra("cate_num", "036007");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_bath:
                param = "036";
                //intent.putExtra("cate_num", "036002");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_house:
                param = "036";
                //intent.putExtra("cate_num", "036014");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_tower:
                param = "036";
                //intent.putExtra("cate_num", "036008");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_maddaddabi:
                param = "036";
                //intent.putExtra("cate_num", "036010");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_drinkers:
                param = "036";
                //intent.putExtra("cate_num", "036013");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_beauty:
                param = "036";
                //intent.putExtra("cate_num", "036017");
                //startActivity(intent);
                break;

            case R.id.btn_category_cat_toy:
                param = "036";
                //intent.putExtra("cate_num", "036012");
                //startActivity(intent);
                break;
        }

        /*
        switch(id){
            case R.id.linear_category_planning:
                delegate.clickPlanning();
                //Toast.makeText(getContext(), "기획전", Toast.LENGTH_SHORT).show();
                break;

            case R.id.linear_category_primium:
                delegate.clickPrimium();
                //Toast.makeText(getContext(), "프리미엄몰", Toast.LENGTH_SHORT).show();
                break;

            case R.id.linear_category_dog:
                delegate.clickCategoryGoods();
                //Toast.makeText(getContext(), "애견", Toast.LENGTH_SHORT).show();
                break;

            case R.id.linear_category_cat:
                Toast.makeText(getContext(), "애묘", Toast.LENGTH_SHORT).show();
                break;
        }
        */
    }


}
