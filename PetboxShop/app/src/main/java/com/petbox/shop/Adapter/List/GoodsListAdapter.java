package com.petbox.shop.Adapter.List;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petbox.shop.CtegoryGoodsActivity;
import com.petbox.shop.DescTest;
import com.petbox.shop.GoodInfoActivity;
import com.petbox.shop.ImageDownloader;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.R;
import com.petbox.shop.Utility.Utility;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-10-05.
 */
public class GoodsListAdapter extends BaseAdapter implements View.OnClickListener{

    Context mContext;
    ArrayList<BestGoodInfo> mItemList;
    LayoutInflater inflater;

    int mainColor = 0;

    public GoodsListAdapter(){}

    public GoodsListAdapter(Context context){mContext = context;}

    public GoodsListAdapter(Context context, ArrayList<BestGoodInfo> itemList){
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

            convertView = inflater.inflate(R.layout.list_style_item, parent, false);
            holder = new ViewHolder();

            holder.relative_origin_price = (RelativeLayout) convertView.findViewById(R.id.relative_list_origin_price);
            holder.relative_ratingbar = (RelativeLayout) convertView.findViewById(R.id.relative_list_ratingbar);

            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_list_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_list_name);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_list_rate);
            holder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_list_orgin_price);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_list_price);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rb_list);
            holder.tv_rate_person = (TextView) convertView.findViewById(R.id.tv_list_rate_person);
            holder.tv_rate_per = (TextView) convertView.findViewById(R.id.tv_list_rate_per);

            holder.ll_list_item = (LinearLayout) convertView.findViewById(R.id.ll_list_item);
            holder.ll_list_item.setOnClickListener(this);

            holder.icon1 = (ImageView) convertView.findViewById(R.id.iv_list_icon1);
            holder.icon2 = (ImageView) convertView.findViewById(R.id.iv_list_icon2);
            holder.icon3 = (ImageView) convertView.findViewById(R.id.iv_list_icon3);

            /*
            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(mainColor, PorterDuff.Mode.SRC_ATOP);
            */
            convertView.setTag(holder);


        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        BestGoodInfo item = mItemList.get(position);
        //holder.iv_image.setImageResource(R.drawable.no_image);
        holder.tv_name.setText(item.name);

        String rete="";
        int rete_per = (int)(100- Math.ceil((Float.parseFloat(item.price) / Float.parseFloat(item.origin_price) * 100)));

        Log.e("rete_per",String.valueOf(rete_per));

        if(rete_per <= 0){
            rete = "펫박스가";
            holder.tv_rate.setText(rete);
            holder.tv_rate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            holder.tv_rate_per.setVisibility(convertView.GONE);
            holder.relative_origin_price.setVisibility(View.GONE);
        }else{
            rete = String.valueOf(rete_per);
            holder.tv_rate.setText(rete);
            holder.tv_rate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            holder.tv_rate_per.setVisibility(convertView.VISIBLE);
            holder.relative_origin_price.setVisibility(View.VISIBLE);
        }


        System.out.println("상품이름 : " + item.name + " // 아이콘 : "  + item.icon);

        String urlcon = "";
        urlcon = "http://petbox.kr/shop/data/goods/" + item.imgUrl;

        ImageDownloader.download(urlcon, holder.iv_image); //이미지 다운로드 함수. thumbnail_detail url 의 이미지를 다운받아, 이미지뷰 에 노출

        /*ViewGroup.LayoutParams params = holder.iv_image.getLayoutParams();
        int img_w = holder.iv_image.getWidth();
        params.height = img_w;
        holder.iv_image.setLayoutParams(params);*/

        holder.iv_image.setScaleType(ImageView.ScaleType.FIT_XY); //이미지 사이즈에 크기 맞춤


        Log.e("goodslist_adapter", "펫박스 홈(position : " + position +") // URL : " + urlcon);

        //holder.iv_image.setImageBitmap(bm);

        if(rete_per <= 0 ){
            holder.tv_origin_price.setVisibility(View.GONE);
        }else{
            holder.tv_origin_price.setVisibility(View.VISIBLE);
            holder.tv_origin_price.setText(Utility.replaceComma(""+item.origin_price) + "원");
        }

        holder.tv_price.setText(Utility.replaceComma(""+item.price) + "원");


        int rating_person = item.rating_person;

        if(rating_person == 0){
            holder.relative_ratingbar.setVisibility(View.INVISIBLE);
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.tv_rate_person.setVisibility(View.INVISIBLE);
        }else{
            holder.relative_ratingbar.setVisibility(View.VISIBLE);
            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.tv_rate_person.setVisibility(View.VISIBLE);

            holder.ratingBar.setRating(item.rating);
            //holder.ratingBar.setRating(4);
            holder.tv_rate_person.setText("(" + item.rating_person + ")");
        }


        holder.ll_list_item.setTag(holder.ll_list_item.getId(),item.goodsno);

        ArrayList<Integer> iconList = Utility.parseValidBinary(item.icon);

        int count = 0;

        holder.icon1.setVisibility(View.GONE);
        holder.icon2.setVisibility(View.GONE);
        holder.icon3.setVisibility(View.GONE);

        for(int i=1; i<=iconList.size(); i++){
            int id = mContext.getResources().getIdentifier("s_icon_"+iconList.get(i-1),"drawable" , "com.petbox.shop");

            System.out.println("아이콘 세팅");

            int num = iconList.get(i-1);

            if(num == 1)
                continue;

            count++;

            if(count==1){
                holder.icon1.setImageResource(id);
                holder.icon1.setVisibility(View.VISIBLE);
            }else if(count==2){
                holder.icon2.setImageResource(id);
                holder.icon2.setVisibility(View.VISIBLE);
            }else if(count==3){
                holder.icon3.setImageResource(id);
                holder.icon3.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent goodsinfointent = new Intent(mContext, GoodInfoActivity.class);
        goodsinfointent.setFlags(goodsinfointent.FLAG_ACTIVITY_NEW_TASK);

        switch(id){
            case R.id.ll_list_item :
                goodsinfointent.putExtra("goodsno",String.valueOf(v.getTag(id)));
                mContext.startActivity(goodsinfointent);
                break;
        }
    }

    class ViewHolder{
        LinearLayout ll_list_item;
        RelativeLayout relative_origin_price;   // 삭선 가격 텍스트뷰 들어있는 레이아웃
        RelativeLayout relative_ratingbar; // 레이팅바 들어있는 레이아웃

        ImageView iv_image; //상품 이미지
        TextView tv_name ; // 상품 명
        TextView tv_rate; // 할인율
        TextView tv_rate_per; // 할인율
        TextView tv_origin_price; //원래 가격
        TextView tv_price; // 할인적용된 실제 판매 가격
        RatingBar ratingBar;    //레이팅바
        TextView tv_rate_person; // 점수준 사람

        ImageView icon1;    // 아이콘1
        ImageView icon2;    // 아이콘2
        ImageView icon3;    // 아이콘3
    }
}
