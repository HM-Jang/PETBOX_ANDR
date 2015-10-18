package com.petbox.shop.Fragment.Category;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        categoryManager = CategoryManager.getManager();

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

        Node<CategoryInfo> node = new Node<CategoryInfo>();

        switch(id){
            case R.id.btn_category_dog_feed:
                 //HttpGetManager httpGetManager = new HttpGetManager(this);
                //httpGetManager.start();

                node = categoryManager.scan("애견 사료", 0);
                param = node.getData().category_num;

                intent.putExtra("cate_num",param);
                String cate_name = node.getData().name;

                intent.putExtra("cate_name", cate_name);
                intent.putExtra("cate_mode", 0);

                System.out.println("cate_name  : " + cate_name + "// cate_num : " + param);


                startActivity(intent);
                break;

            case R.id.btn_category_dog_snack:
                //param = "035";
                node = categoryManager.scan("간식", 0);
                param = node.getData().category_num;

                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_nutrients:
                //param = "035";
                node = categoryManager.scan("영양제", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_medicine:
                param = "035";
                node = categoryManager.scan("의약부외품", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_hygienic:
                node = categoryManager.scan("위생/배변", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_bath:
                node = categoryManager.scan("목욕용품", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_house:
                node = categoryManager.scan("하우스/가구", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_fence:
                node = categoryManager.scan("이동장/철장", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_line:
                node = categoryManager.scan("줄/이름표", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_accessory:
                node = categoryManager.scan("패션/액세서리/팬시", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);

            case R.id.btn_category_dog_drinkers:
                node = categoryManager.scan("급식기/급수기", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_dog_beauty:
                node = categoryManager.scan("미용용품", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);

            case R.id.btn_category_dog_toy:
                node = categoryManager.scan("장난감/훈련용품", 0);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 0);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_feed:
                node = categoryManager.scan("고양이 사료", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_snack:
                node = categoryManager.scan("간식/캔", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_nutrients:
                node = categoryManager.scan("영양제", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_medicine:
                node = categoryManager.scan("의약부외품", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_hygienic:
                node = categoryManager.scan("위생/배변", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_bath:
                node = categoryManager.scan("목욕용품", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_house:
                node = categoryManager.scan("이동장/하우스", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_tower:
                node = categoryManager.scan("캣타워/스크래쳐", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_maddaddabi:
                node = categoryManager.scan("캣닙/마따따비", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);;
                break;

            case R.id.btn_category_cat_drinkers:
                node = categoryManager.scan("급식기/급수기", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_beauty:
                node = categoryManager.scan("미용용품", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
                break;

            case R.id.btn_category_cat_toy:
                node = categoryManager.scan("장난감", 1);
                param = node.getData().category_num;
                intent.putExtra("cate_num",param);
                intent.putExtra("cate_name", node.getData().name);
                intent.putExtra("cate_mode", 1);
                startActivity(intent);
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
