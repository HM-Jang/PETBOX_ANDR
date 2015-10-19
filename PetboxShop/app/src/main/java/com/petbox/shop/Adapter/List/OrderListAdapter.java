package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.OrderItemInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-10-10.
 */
public class OrderListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<OrderItemInfo> mItemList;
    LayoutInflater inflater;

    int mainColor = 0;


    public OrderListAdapter(){}

    public OrderListAdapter(Context context){
        mContext = context;
    }

    public OrderListAdapter(Context context, ArrayList<OrderItemInfo> itemList){
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

            convertView = inflater.inflate(R.layout.my_page_order_list_item, parent, false);

            holder = new ViewHolder();
            holder.lv_mypage_order_list = (ListView)convertView.findViewById(R.id.lv_mypage_order_list);
            holder.ll_mypage_order_list = (LinearLayout)convertView.findViewById(R.id.ll_mypage_order_list);
            holder.tv_mypage_order_list_date = (TextView)convertView.findViewById(R.id.tv_mypage_order_list_date);
            holder.tv_mypage_order_list_ordernum = (TextView)convertView.findViewById(R.id.tv_mypage_order_list_ordernum);
            holder.tv_mypage_order_list_ordernm = (TextView)convertView.findViewById(R.id.tv_mypage_order_list_ordernm);
            holder.tv_mypage_order_list_orderprice = (TextView)convertView.findViewById(R.id.tv_mypage_order_list_orderprice);
            holder.tv_mypage_order_list_orderpg = (TextView)convertView.findViewById(R.id.tv_mypage_order_list_orderpg);
            holder.tv_mypage_order_list_status = (TextView)convertView.findViewById(R.id.tv_mypage_order_list_status);
            holder.tv_mypage_order_list_status_view = (TextView)convertView.findViewById(R.id.tv_mypage_order_list_status_view);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        OrderItemInfo item = mItemList.get(position);


        Log.e("position", String.valueOf(position) + "// mItemList.size : " + mItemList.size());
        Log.e("item.orddt",item.orddt);

        holder.tv_mypage_order_list_date.setText(item.orddt);
        Log.e("item.ordno", item.ordno);

        holder.tv_mypage_order_list_ordernum.setText(item.ordno);
        holder.tv_mypage_order_list_ordernm.setText(item.goodsnm);
        holder.tv_mypage_order_list_orderprice.setText(item.prn_settleprice);
        holder.tv_mypage_order_list_orderpg.setText(item.str_settlekind);
        holder.tv_mypage_order_list_status.setText(item.str_step);
        Log.e("item.str_step", item.str_step);
        //holder.tv_mypage_order_list_status_view.setTag(item.orddt);

        return convertView;
    }

    public class ViewHolder{
        ListView lv_mypage_order_list;
        LinearLayout ll_mypage_order_list;
        TextView tv_mypage_order_list_date,tv_mypage_order_list_ordernum,tv_mypage_order_list_ordernm;
        TextView tv_mypage_order_list_orderprice,tv_mypage_order_list_orderpg;
        TextView tv_mypage_order_list_status,tv_mypage_order_list_status_view;
    }


}