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

import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.PopularSearchInfo;
import com.petbox.shop.MainActivity;
import com.petbox.shop.R;
import com.petbox.shop.SearchGoodActivity;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-09-16.
 */
public class PopularSearchListAdapter extends BaseAdapter implements  View.OnClickListener{

    Context mContext;
    ArrayList<PopularSearchInfo> mItemList;
    LayoutInflater inflater;

    public PopularSearchListAdapter(){}

    public PopularSearchListAdapter(Context context){
        mContext = context;
    }

    public PopularSearchListAdapter(Context context, ArrayList<PopularSearchInfo> itemList){
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

            convertView = inflater.inflate(R.layout.list_style_popular_search, parent, false);
            holder = new ViewHolder();
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_popular_circle);
            holder.tv_rank = (TextView) convertView.findViewById(R.id.tv_popular_rank);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_popular_title);
            holder.ll_search_data = (LinearLayout) convertView.findViewById(R.id.ll_search_data);

            holder.ll_search_data.setOnClickListener(this);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        PopularSearchInfo item = mItemList.get(position);

        //holder.iv_image.setImageResource(R.drawable.no_image);

        int rank = item.ranking;

        if(rank <= 3){  // 1,2,3 [3순위]
            holder.iv_image.setImageResource(R.drawable.ranking_circle_orange);
        }else{
            holder.iv_image.setImageResource(R.drawable.ranking_circle_gray);
        }

        holder.tv_rank.setText(""+item.ranking);
        holder.tv_title.setText(item.title);
        holder.ll_search_data.setTag(holder.ll_search_data.getId(), item.title);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();//클릭시 해당 이벤트로 이동
        String db_id = "";
       Intent Search_intent = new Intent(mContext, SearchGoodActivity.class);
        Search_intent.addFlags(Search_intent.FLAG_ACTIVITY_NEW_TASK);
        switch(id){
            case R.id.ll_search_data :
                db_id = ((String) v.getTag(id));
                Log.e("keyword", String.valueOf(db_id));
                Search_intent.putExtra("keyword", db_id);
                mContext.startActivity(Search_intent);

                break;

        }
    }

    public class ViewHolder{
        ImageView iv_image; // 원
        TextView tv_rank; // 순위
        TextView tv_title ; // 타이틀
        LinearLayout ll_search_data;
    }
}
