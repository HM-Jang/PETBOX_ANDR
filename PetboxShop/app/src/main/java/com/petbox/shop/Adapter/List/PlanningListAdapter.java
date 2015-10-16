package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petbox.shop.CtegoryGoodsActivity;
import com.petbox.shop.ImageDownloader;
import com.petbox.shop.Item.PlanningItemInfo;
import com.petbox.shop.R;
import com.petbox.shop.SearchGoodActivity;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-09-16.
 */

//기획전 페이지
public class PlanningListAdapter extends BaseAdapter implements View.OnClickListener{

    Context mContext;
    LayoutInflater inflater;
    ArrayList<PlanningItemInfo> mItemList;


    public PlanningListAdapter(){}

    public PlanningListAdapter(Context context){mContext = context;}

    public PlanningListAdapter(Context context, ArrayList<PlanningItemInfo> itemList){
        mContext = context;
        mItemList = itemList;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.list_style_planning, parent, false);
            holder = new ViewHolder();
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_list_plan_image);
            holder.ll_plan_list = (LinearLayout) convertView.findViewById(R.id.ll_plan_list);
            holder.ll_plan_list.setOnClickListener(this);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        PlanningItemInfo item = mItemList.get(position);
        String urlcon = "";
        urlcon = "http://petbox.kr/shop/data/skin/mera_ws/d_img/" + item.img;
        Log.e("urlcon",urlcon);
        ImageDownloader.download(urlcon, holder.iv_image);
        holder.iv_image.setScaleType(ImageView.ScaleType.FIT_XY); //이미지 사이즈에 크기 맞춤

        holder.ll_plan_list.setTag(holder.ll_plan_list.getId(), item.linkaddr);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String plan_data = "";
        Intent plan_intent = new Intent(mContext, CtegoryGoodsActivity.class);
        plan_intent.setFlags(plan_intent.FLAG_ACTIVITY_NEW_TASK);

        switch(id){
            case R.id.ll_plan_list :
                plan_data = ((String) v.getTag(id));
                Log.e("cate_num", String.valueOf(plan_data));
                plan_intent.putExtra("cate_num", plan_data);
                mContext.startActivity(plan_intent);

                break;
        }

    }

    public class ViewHolder{
        LinearLayout ll_plan_list;
        ImageView iv_image; //기획전 이미지
    }
}
