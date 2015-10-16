package com.petbox.shop.Item;

/**
 * Created by 펫박스 on 2015-10-12.
 */
public class CouponInfo {
    public String sno="";
    public String coupon_name="";
    public String coupon_type="";
    public String coupon_price="";
    public String status="";
    public String sdate="";
    public String edate="";

    public CouponInfo(String _sno, String _coupon_name,String _coupon_type, String _coupon_price, String _status,String _sdate, String _edate) {
        sno= _sno;
        coupon_name= _coupon_name;
        coupon_type= _coupon_type;
        coupon_price= _coupon_price;
        status= _status;
        sdate= _sdate;
        edate= _edate;

    }


}

