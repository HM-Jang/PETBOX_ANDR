package com.petbox.shop.Adapter.Grid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.petbox.shop.GoodInfoActivity;
import com.petbox.shop.ImageDownloader;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-09-15.
 */
public class BestGoodGridAdapter extends BaseAdapter implements View.OnClickListener{

    Context mContext;
    ArrayList<BestGoodInfo> mItemList;
    LayoutInflater inflater;

    int mainColor = 0;

    public BestGoodGridAdapter(Context context){
        mContext = context;
    }

    public BestGoodGridAdapter(){}


    public BestGoodGridAdapter(Context context, ArrayList<BestGoodInfo> itemList){
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

            convertView = inflater.inflate(R.layout.grid_style_item, parent, false);
            holder = new ViewHolder();
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_grid_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_grid_name);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_grid_rate);
            holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_grid_origin_price);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_grid_price);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rb_grid);
            holder.tv_rate_person = (TextView) convertView.findViewById(R.id.tv_grid_rate_person);
            holder.tv_rate_per = (TextView) convertView.findViewById(R.id.tv_grid_rate_per);

            holder.ll_grid_item = (LinearLayout) convertView.findViewById(R.id.ll_grid_item);
            holder.ll_grid_item.setOnClickListener(this);

            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

            stars.getDrawable(2).setColorFilter(mainColor, PorterDuff.Mode.SRC_ATOP);
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        BestGoodInfo item = mItemList.get(position);

        //holder.iv_image.setImageResource(R.drawable.no_image);
        holder.tv_name.setText(item.name);

        String rete="";
        int rete_per = (int)(100- Math.ceil((Float.parseFloat(item.price) / Float.parseFloat(item.origin_price) * 100)));
        if(rete_per == 0){
            rete = "펫박스가";
            holder.tv_rate.setText(rete);
            holder.tv_rate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            holder.tv_rate_per.setVisibility(convertView.GONE);
        }else{
            rete = String.valueOf(rete_per);
            holder.tv_rate.setText(rete);
            holder.tv_rate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            holder.tv_rate_per.setVisibility(convertView.VISIBLE);
        }

        String urlcon = "";
        urlcon = "http://petbox.kr/shop/data/goods/" + item.imgUrl;

        ImageDownloader.download(urlcon, holder.iv_image); //이미지 다운로드 함수. thumbnail_detail url 의 이미지를 다운받아, 이미지뷰 에 노출
        int img_w = holder.iv_image.getWidth();

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) holder.iv_image.getLayoutParams();
        params.height = img_w;
        holder.iv_image.setLayoutParams(params);

        holder.iv_image.setScaleType(ImageView.ScaleType.FIT_XY); //이미지 사이즈에 크기 맞춤

        Log.e("chancedeal_adapter", "----------------imagedownload" + urlcon);

        //holder.iv_image.setImageBitmap(bm);
        holder.tv_origin_price.setText(item.origin_price + "원");
        Log.e("origin_price----", item.origin_price);
        holder.tv_price.setText(item.price + "원");

        int rating_person = item.rating_person;

        if(rating_person == 0){
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.tv_rate_person.setVisibility(View.INVISIBLE);
        }else {
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tv_rate_person.setVisibility(View.VISIBLE);

            holder.ratingBar.setRating(item.rating);
            holder.tv_rate_person.setText("(" + item.rating_person + ")");
        }

        holder.ll_grid_item.setTag(holder.ll_grid_item.getId() ,item.goodsno);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent goodsinfointent = new Intent(mContext, GoodInfoActivity.class);
        goodsinfointent.setFlags(goodsinfointent.FLAG_ACTIVITY_NEW_TASK);
        switch(id){
            case R.id.ll_grid_item:
                goodsinfointent.putExtra("goodsno",String.valueOf(v.getTag(id)));
                mContext.startActivity(goodsinfointent);
                break;
        }
    }

    public class ViewHolder{
        ImageView iv_image; //상품 이미지
        TextView tv_name ; // 상품 명
        TextView tv_rate; // 할인율
        TextView tv_rate_per; // 할인율
        TextView tv_origin_price; //원래 가격
        TextView tv_price; // 할인적용된 실제 판매 가격
        RatingBar ratingBar;    //레이팅바
        TextView tv_rate_person; // 점수준 사람
        LinearLayout ll_grid_item;
    }
}
