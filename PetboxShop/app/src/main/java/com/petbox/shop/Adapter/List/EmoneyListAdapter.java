package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.petbox.shop.Item.EmoneyInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by 펫박스 on 2015-10-12.
 */
public class EmoneyListAdapter extends BaseAdapter {


    Context mContext;
    ArrayList<EmoneyInfo> mItemList;
    LayoutInflater inflater;

    int mainColor = 0;

    public EmoneyListAdapter(){}

    public EmoneyListAdapter(Context context){mContext = context;}

    public EmoneyListAdapter(Context context, ArrayList<EmoneyInfo> itemList){
        mContext = context;
        mItemList = itemList;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainColor = mContext.getResources().getColor(R.color.colorPrimary);
    }
    @Override
    public int getCount() {
        return mItemList.size();
    }


    @Override
    public Object getItem(int position){
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.e("position", "position : " + position);

        if(convertView == null){

            convertView = inflater.inflate(R.layout.my_page_emoney_list_item, parent, false);
            holder = new ViewHolder();

            holder.tv_emoney_date = (TextView) convertView.findViewById(R.id.tv_emoney_date);
            holder.tv_emoney_info = (TextView) convertView.findViewById(R.id.tv_emoney_info);
            holder.tv_emoney_plus_emoney = (TextView) convertView.findViewById(R.id.tv_emoney_plus_emoney);
            holder.tv_emoney_minus_emoney = (TextView) convertView.findViewById(R.id.tv_emoney_minus_emoney);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        EmoneyInfo item = mItemList.get(position);

        holder.tv_emoney_date.setText(item.regdts);
        holder.tv_emoney_info.setText(item.memo);

        Log.e("position",String.valueOf(position));

        if(item.emoney.contains("-")){
            holder.tv_emoney_minus_emoney.setText(item.emoney);
            holder.tv_emoney_plus_emoney.setText("0");
        }else{
            holder.tv_emoney_plus_emoney.setText(item.emoney);
            holder.tv_emoney_minus_emoney.setText("0");
        }
        Log.e("position",item.emoney);
        return convertView;
    }

    class ViewHolder{
        TextView tv_emoney_date;
        TextView tv_emoney_info;
        TextView tv_emoney_plus_emoney;
        TextView tv_emoney_minus_emoney;
    }
}
