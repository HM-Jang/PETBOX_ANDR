package com.petbox.shop.Adapter.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.petbox.shop.Delegate.MyPageDelegate;
import com.petbox.shop.Fragment.MyPage.MyPageCouponList;
import com.petbox.shop.Fragment.MyPage.MyPageFragment;
import com.petbox.shop.Fragment.MyPage.MyPageOrderList;
import com.petbox.shop.Fragment.MyPage.MypageMileage;

import java.util.Locale;

/**
 * Created by petbox on 2015-09-14.
 */
public class MyPagePagerAdapter extends FragmentStatePagerAdapter {

    private MyPageDelegate delegate;


    public MyPagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyPagePagerAdapter(FragmentManager fm, MyPageDelegate delegate){
        super(fm);
        this.delegate = delegate;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){

            case 0: //마이페이지
                return MyPageFragment.newInstance(delegate, "");

            case 1: //주문내역
                return MyPageOrderList.newInstance(delegate, "");

            case 2: //Emoney 내역
                return MypageMileage.newInstance(delegate, "");

            case 3: //Emoney 내역
                return MyPageCouponList.newInstance(delegate, "");

            case 4: //Emoney 내역
                return MyPageOrderList.newInstance(delegate, "");


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
        switch (position) {
            case 0:
                return "마이페이지";

        }
        return null;
    }
}
