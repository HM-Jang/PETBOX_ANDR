package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbox.shop.Item.CouponInfo;
import com.petbox.shop.Item.EmoneyInfo;
import com.petbox.shop.R;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by 펫박스 on 2015-10-12.
 */
public class CouponListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<CouponInfo> mItemList;
    LayoutInflater inflater;

    int mainColor = 0;

    public CouponListAdapter(){}

    public CouponListAdapter(Context context){mContext = context;}

    public CouponListAdapter(Context context, ArrayList<CouponInfo> itemList){
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

            convertView = inflater.inflate(R.layout.my_page_coupon_list_item, parent, false);
            holder = new ViewHolder();
            holder.iv_coupon_done = (ImageView) convertView.findViewById(R.id.iv_coupon_done);

            holder.tv_coupon_name = (TextView) convertView.findViewById(R.id.tv_coupon_name);
            holder.tv_coupon_info = (TextView) convertView.findViewById(R.id.tv_coupon_info);
            holder.tv_coupon_limit = (TextView) convertView.findViewById(R.id.tv_coupon_limit);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumIntegerDigits(5); //최대수 지정

        String coupon_name="";
        String coupon_info="";

        CouponInfo item = mItemList.get(position);

        Log.e("coupon_position",String.valueOf(position));

        coupon_name = "["+ nf.format(Integer.parseInt(item.coupon_price))+"]" + item.coupon_name;
        holder.tv_coupon_name.setText(coupon_name);
        Log.e("tv_coupon_name", coupon_name);
        Log.e("item.coupon_price", item.coupon_price);
        Log.e("item.coupon_name", item.coupon_name);
        Log.e("item.item.status", item.status);

        if(item.status == "1"){
            holder.iv_coupon_done.setImageResource(R.drawable.coupon_unser);
        }else{
            holder.iv_coupon_done.setImageResource(R.drawable.coupon_ser);
        }
        Log.e("--coupon fragment--","inda club ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        coupon_info = item.sdate + "~" + item.edate + " / " +nf.format(Integer.parseInt(item.coupon_price)) + "원 할인";
        Log.e("tv_coupon_info",coupon_info);


        holder.tv_coupon_info.setText(coupon_info);
        holder.tv_coupon_limit.setText(item.edate+"까지 사용 가능");
        Log.e("tv_coupon_limit",item.edate+"까지 사용 가능");
        return convertView;
    }

    class ViewHolder{
        ImageView iv_coupon_done;
        TextView tv_coupon_name;
        TextView tv_coupon_info;
        TextView tv_coupon_limit;
    }
}
