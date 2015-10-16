package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.petbox.shop.ImageDownloader;
import com.petbox.shop.Item.CategoryGoodInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by 펫박스 on 2015-10-10.
 */
public class CategoryListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<CategoryGoodInfo> mItemList;
    LayoutInflater inflater;

    int mainColor = 0;


    public CategoryListAdapter(){}

    public CategoryListAdapter(Context context){
        mContext = context;
    }

    public CategoryListAdapter(Context context, ArrayList<CategoryGoodInfo> itemList){
        mContext = context;
        mItemList = itemList;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainColor = mContext.getResources().getColor(R.color.colorPrimary);
    }


    public int getCount() {
        return mItemList.size();
    }

    public Object getItem(int position) {
        return mItemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){

            convertView = inflater.inflate(R.layout.list_style_item, parent, false);
            holder = new ViewHolder();
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_list_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_list_name);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_list_rate);
            holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_list_orgin_price);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_list_price);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rb_list);
            holder.tv_rate_person = (TextView) convertView.findViewById(R.id.tv_list_rate_person);
            holder.tv_rate_per = (TextView) convertView.findViewById(R.id.tv_list_rate_per);
            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

            stars.getDrawable(2).setColorFilter(mainColor, PorterDuff.Mode.SRC_ATOP);
            convertView.setTag(holder);

            //convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryGoodInfo item = mItemList.get(position);

        //holder.iv_image.setImageResource(R.drawable.no_image);
        holder.tv_name.setText(item.name);

        String rete="";
        int rete_per = (int)(100- Math.ceil((Float.parseFloat(item.price) / Float.parseFloat(item.origin_price) * 100)));
        Log.e("rete_per", String.valueOf(rete_per));

        if(rete_per == 0){
            rete = "펫박스가";
            holder.tv_rate.setText(rete);
            holder.tv_rate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            holder.tv_rate_per.setVisibility(convertView.INVISIBLE);
        }else{
            rete = String.valueOf(rete_per);
            holder.tv_rate.setText(rete);
            holder.tv_rate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            holder.tv_rate_per.setVisibility(convertView.VISIBLE);
        }

        String urlcon = "";
        urlcon = "http://petbox.kr/shop/data/goods/" + item.imgUrl;

        ImageDownloader.download(urlcon, holder.iv_image); //이미지 다운로드 함수. thumbnail_detail url 의 이미지를 다운받아, 이미지뷰 에 노출
        holder.iv_image.setScaleType(ImageView.ScaleType.FIT_XY); //이미지 사이즈에 크기 맞춤

        Log.e("chancedeal_adapter", "----------------imagedownload" + urlcon);

        if(rete_per == 0){
            holder.tv_origin_price.setVisibility(View.INVISIBLE);
            holder.tv_price.setText(item.origin_price + "원");
        }else{
            holder.tv_origin_price.setVisibility(View.VISIBLE);
            holder.tv_origin_price.setText(item.origin_price + "원");
            holder.tv_price.setText(item.price + "원");
        }

        int rating_person = item.rating_person;

        if(rating_person == 0){
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.tv_rate_person.setVisibility(View.INVISIBLE);
        }else{
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tv_rate_person.setVisibility(View.VISIBLE);

            holder.ratingBar.setRating(item.rating);
            holder.tv_rate_person.setText("(" + item.rating_person + ")");
        }

        return convertView;
    }

    public class ViewHolder{
        ImageView iv_image; //상품 이미지
        TextView tv_name ; // 상품 명
        TextView tv_rate_per; // 할인율
        TextView tv_rate; // 할인율
        TextView tv_origin_price; //원래 가격
        TextView tv_price; // 할인적용된 실제 판매 가격
        RatingBar ratingBar;    //레이팅바
        TextView tv_rate_person; // 점수준 사람
    }

}
