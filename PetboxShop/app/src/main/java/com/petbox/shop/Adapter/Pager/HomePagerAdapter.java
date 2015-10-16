package com.petbox.shop.Adapter.Pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;

import com.petbox.shop.Fragment.Category.CategoryGoodsFragment;
import com.petbox.shop.Fragment.Home.BestGoodFragment;
import com.petbox.shop.Fragment.Home.ChanceDealFragment;
import com.petbox.shop.Fragment.Home.EventFragment;
import com.petbox.shop.Fragment.Home.Home2Fragment;
import com.petbox.shop.Fragment.Home.HomeFragment;
import com.petbox.shop.Fragment.Home.IntegrationPlanningFragment;
import com.petbox.shop.Fragment.Home.PlanningFragment;
import com.petbox.shop.Fragment.Home.PrimiumFragment;
import com.petbox.shop.Fragment.Home.RegularShippingFragment;
import com.petbox.shop.Fragment.MyPage.MyPageOrderList;
import com.petbox.shop.Item.SlideInfo;


import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by petbox on 2015-09-14.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }


    @Override
    public Fragment getItem(int position) {
        Log.e("HomePagerAdapter", "-------------------------instantiateItem");
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){

            case 0: // 펫박스 홈
                return Home2Fragment.newInstance("", "");

            case 1: //베스트상품
                return BestGoodFragment.newInstance("","");

            case 2: //찬스딜
                return ChanceDealFragment.newInstance("","");
                //return CategoryGoodsFragment.newInstance(delegate,"");

            case 3: //기획전
                return PlanningFragment.newInstance("","");

            case 4: //프리미엄몰
                return PrimiumFragment.newInstance("","");
                //return MyPageOrderList.newInstance("","");

        }

        return null;

        //  return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        Log.e("HomePagerAdapter", "-------------------------instantiateItem");
        switch (position) {
            case 0:
                return "펫박스홈";
            case 1:
                return "베스트상품";
            case 2:
                return "찬스딜";
            case 3:
                return "기획전";
            case 4:
                return "프리미엄몰";
        }
        return null;
    }
}
