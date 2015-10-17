package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.Item.CategoryInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by 펫박스 on 2015-10-15.
 */
public class CategoryAdapter extends BaseAdapter implements View.OnClickListener {
    Context mContext;
    ArrayList<CategoryInfo> mItemList;
    LayoutInflater inflater;

    int mainColor = 0;


    public int getCount() {
        return mItemList.size();
    }

    public Object getItem(int position) {
        return mItemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public CategoryAdapter(Context context, ArrayList<CategoryInfo> itemList){
        mContext = context;
        mItemList = itemList;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainColor = mContext.getResources().getColor(R.color.colorPrimary);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {

            convertView = inflater.inflate(R.layout.dialog_list_item, parent, false);
            holder = new ViewHolder();
            holder.tv_cate_name = (TextView) convertView.findViewById(R.id.tv_cate_name);
            holder.ll_cate_list = (LinearLayout) convertView.findViewById(R.id.ll_cate_list);

            holder.ll_cate_list.setOnClickListener(this);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryInfo item = mItemList.get(position);

        holder.tv_cate_name.setText(item.name);
        holder.ll_cate_list.setTag(holder.ll_cate_list.getId(),item.category_num);

        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    public class ViewHolder{
        TextView  tv_cate_name; // 상품 명
        LinearLayout ll_cate_list;
    }

}
