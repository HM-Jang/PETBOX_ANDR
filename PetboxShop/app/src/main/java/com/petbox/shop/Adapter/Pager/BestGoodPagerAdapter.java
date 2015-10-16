package com.petbox.shop.Adapter.Pager;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.petbox.shop.DB.DBConnector;
import com.petbox.shop.ImageDownloader;
import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.SlideInfo;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-09-15.
 */
public class BestGoodPagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<SlideInfo> mItemList;
    LayoutInflater inflater;

    private Bitmap[] images;

    /*
    private int[] images = new int[]{
            R.drawable.test1,
            R.drawable.test2,
            R.drawable.test3,
            R.drawable.test4,
            R.drawable.test5,
            R.drawable.test6
    };*/

    public BestGoodPagerAdapter(){}

    public int getImages(){
        return images.length;

    }

    public BestGoodPagerAdapter(Context context, ArrayList<SlideInfo> itemList){
        mContext = context;
        ImageView imageView = new ImageView(mContext);

        mItemList = itemList;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.e(" mItemList.size()",String.valueOf(mItemList.size()));

        images = new Bitmap[mItemList.size()];
        for(int k =0; k<mItemList.size(); k++){
            SlideInfo item = mItemList.get(k);
            ImageDownloader.download(item.banner_img, imageView);
        }
    }


    @Override
    public int getCount() {

        return images.length;
    }


    public BestGoodPagerAdapter(Context context, String num){
        mContext = context;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        ImageView imageView = new ImageView(mContext);

        SlideInfo item = mItemList.get(position);

        ImageDownloader.download(item.banner_img, imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        final int pos = position;

        imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, pos + "번째, 이미지 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        ((ViewPager) container).addView(imageView, 0);
        Log.e("BestGoodPagerAdapter","-------------------------instantiateItem");

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ((ViewPager) container).removeView((ImageView)object);
    }

}
