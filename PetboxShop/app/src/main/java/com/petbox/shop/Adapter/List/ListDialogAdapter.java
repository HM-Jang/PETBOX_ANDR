package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.petbox.shop.CustomView.CustomNumberPicker;
import com.petbox.shop.Item.CartItemInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-10-17.
 */
public class ListDialogAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> mItemList;
    LayoutInflater inflater;

    public ListDialogAdapter(){}

    public ListDialogAdapter(Context context){
        mContext = context;
    }

    public ListDialogAdapter(Context context, ArrayList<String> itemList){
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

            convertView = inflater.inflate(R.layout.list_style_dialog_string, parent, false);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_list_dialog_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String item = mItemList.get(position);

        holder.tv_name.setText(item);

        return convertView;
    }

    class ViewHolder{
        TextView tv_name;
    }
}
