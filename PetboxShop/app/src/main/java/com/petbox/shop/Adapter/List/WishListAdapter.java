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
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.WishInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by 펫박스 on 2015-10-14.
 */
public class WishListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<WishInfo> mItemList;
    LayoutInflater inflater;
    int mainColor = 0;

    public WishListAdapter(){}

    public WishListAdapter(Context context){mContext = context;}

    public WishListAdapter(Context context, ArrayList<WishInfo> itemList){
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
        Log.e("position", "position : " + position);

        if(convertView == null){

            convertView = inflater.inflate(R.layout.list_style_wish_list, parent, false);
            holder = new ViewHolder();

            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_list_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_list_name);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_list_rate);
            holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_list_orgin_price);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_list_price);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rb_list);
            holder.tv_rate_person = (TextView) convertView.findViewById(R.id.tv_list_rate_person);
            holder.tv_rate_per = (TextView) convertView.findViewById(R.id.tv_list_rate_per);
            holder.tv_list_opt = (TextView) convertView.findViewById(R.id.tv_list_opt);

            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

            stars.getDrawable(2).setColorFilter(mainColor, PorterDuff.Mode.SRC_ATOP);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        WishInfo item = mItemList.get(position);
        //holder.iv_image.setImageResource(R.drawable.no_image);
        holder.tv_name.setText(item.goodsnm);

        String rete="";

        int rete_per = (int)(100- Math.ceil((Float.parseFloat(item.goods_price) / Float.parseFloat(item.goods_consumer) * 100)));

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

        String opt = "";
        if(item.opt1 != ""){
            if(item.opt2 != ""){
                opt = item.opt1 + "\n" + item.opt2;
            }else {
                opt = item.opt1;
            }
        }
        holder.tv_list_opt.setText(opt);

        String urlcon = "";
        urlcon = "http://petbox.kr/shop/data/goods/" + item.img_i;

        ImageDownloader.download(urlcon, holder.iv_image); //이미지 다운로드 함수. thumbnail_detail url 의 이미지를 다운받아, 이미지뷰 에 노출

        holder.iv_image.setScaleType(ImageView.ScaleType.FIT_XY); //이미지 사이즈에 크기 맞춤


        Log.e("goodslist_adapter", "----------------imagedownload" + urlcon);

        //holder.iv_image.setImageBitmap(bm);

        if(rete_per == 0){
            holder.tv_origin_price.setVisibility(View.INVISIBLE);
            holder.tv_price.setText(item.goods_consumer + "원");
        }else{
            holder.tv_origin_price.setVisibility(View.VISIBLE);
            holder.tv_origin_price.setText(item.goods_consumer + "원");
            holder.tv_price.setText(item.goods_price + "원");
        }

        int rating_person = Integer.parseInt(item.point_count);

        if(rating_person == 0){
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.tv_rate_person.setVisibility(View.INVISIBLE);
        }else{
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tv_rate_person.setVisibility(View.VISIBLE);

            holder.ratingBar.setRating(Float.parseFloat(item.point));
            holder.tv_rate_person.setText("(" + item.point_count + ")");
        }

        return convertView;
    }

    class ViewHolder{
        ImageView iv_image; //상품 이미지
        TextView tv_name ; // 상품 명
        TextView tv_rate; // 할인율
        TextView tv_rate_per; // 할인율
        TextView tv_origin_price; //원래 가격
        TextView tv_price; // 할인적용된 실제 판매 가격
        RatingBar ratingBar;    //레이팅바
        TextView tv_rate_person; // 점수준 사람
        TextView tv_list_opt;
    }
}
