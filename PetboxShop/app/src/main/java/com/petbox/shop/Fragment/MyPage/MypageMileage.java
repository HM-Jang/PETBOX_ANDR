package com.petbox.shop.Fragment.MyPage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import com.petbox.shop.Adapter.List.EmoneyListAdapter;
import com.petbox.shop.Adapter.List.OrderListAdapter;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.Delegate.MyPageDelegate;
import com.petbox.shop.Fragment.Home.IntegrationPlanningFragment;
import com.petbox.shop.Item.EmoneyInfo;
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
 * {@link MypageMileage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MypageMileage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MypageMileage extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

   // TextView tv_mypage_emoney_extinc,tv_mypage_emoney;
    TextView tv_mypage_emoney;
    ListView lv_emoney_list;
    TextView tv_emoney_date,tv_emoney_info,tv_emoney_plus_emoney,tv_emoney_minus_emoney;

    EmoneyListAdapter listAdapter;
    ArrayList<EmoneyInfo> mItemList;

    String emoney_list;
    String url,params3,InsertDB;

    MyPageDelegate delegate;
    int emoney_total = 0;
    int emoney_totalmult = 0;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment MypageMileage.
     */
    // TODO: Rename and change types and number of parameters
    public static MypageMileage newInstance(MyPageDelegate delegate, String param2) {
        MypageMileage fragment = new MypageMileage();
        Bundle args = new Bundle();
        fragment.delegate = delegate;
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MypageMileage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mypage_mileage, container, false);

        //tv_mypage_emoney_extinc = (TextView)v.findViewById(R.id.tv_mypage_emoney_extinc);
        tv_mypage_emoney = (TextView)v.findViewById(R.id.tv_mypage_emoney);

        lv_emoney_list = (ListView)v.findViewById(R.id.lv_emoney_list);
        tv_emoney_date = (TextView)v.findViewById(R.id.tv_emoney_date);
        tv_emoney_info = (TextView)v.findViewById(R.id.tv_emoney_info);
        tv_emoney_plus_emoney = (TextView)v.findViewById(R.id.tv_emoney_plus_emoney);
        tv_emoney_minus_emoney = (TextView)v.findViewById(R.id.tv_emoney_minus_emoney);



        try {
            String sort = "";
            String ordno = "";
            String emoney = "";
            String memo = "";
            String regdt = "";
            String regdts = "";
            String idx = "";

            url = "http://petbox.kr/petboxjson/member_info.php";
            params3 = "?m_id="+ Constants.PREF_KEY_ID;
            params3 += "?mypage_info="+803;
            InsertDB = "mypage_emoney_list";

            //order_list = new JsonParse.JsonLoadingTask().execute(url,params3).get();
            emoney_list = new JsonParse.JsonLoadingTask(getActivity().getApplicationContext()).execute(url, params3,InsertDB).get();
            Log.e("order_list", emoney_list);
            JSONArray EmoneyListArray = new JSONArray(emoney_list);
            mItemList = new ArrayList<EmoneyInfo>();
            EmoneyInfo info[] = new EmoneyInfo[EmoneyListArray.length()];

            for (int k = 0; k < EmoneyListArray.length(); k++) {
                JSONObject Emoney_object = EmoneyListArray.getJSONObject(k);

                info[k] = new EmoneyInfo(sort,ordno,emoney,memo,regdt,regdts,idx);

                info[k].sort = Emoney_object.getString("sno");
                info[k].ordno = Emoney_object.getString("ordno");
                info[k].emoney = Emoney_object.getString("emoney");
                info[k].memo = Emoney_object.getString("memo");
                info[k].regdt = Emoney_object.getString("regdt");
                info[k].regdts = Emoney_object.getString("regdts");
                info[k].idx = Emoney_object.getString("idx");


                if(Emoney_object.getString("emoney").contains("-")){
                    emoney_totalmult = Integer.parseInt(Emoney_object.getString("emoney").replace("-",""));
                    emoney_total -= emoney_totalmult;
                }else{
                    emoney_totalmult = Integer.parseInt(Emoney_object.getString("emoney").replace("-",""));
                    emoney_total += emoney_totalmult;
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
        Log.e("mItemList = ",  "// size : " + mItemList.size());

        tv_mypage_emoney.setText(emoney_total+"P");
        //lv_emoney_list = (ListView) v.findViewById(R.id.lv_emoney_list);
        listAdapter = new EmoneyListAdapter(getContext(), mItemList);
        lv_emoney_list.setAdapter(listAdapter);

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
