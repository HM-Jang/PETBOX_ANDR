package com.petbox.shop.Item;

/**
 * Created by 펫박스 on 2015-10-10.
 */
public class OrderItemInfo {
    public String ordno = "";
    public String nameOrder = "";
    public String email = "";
    public String phoneOrder = "";
    public String mobileOrder = "";
    public String nameReceiver = "";
    public String phoneReceiver = "";
    public String mobileReceiver = "";
    public String zipcode = "";
    public String address = "";
    public String road_address = "";
    public String settleprice = "";
    public String prn_settleprice = "";
    public String goodsprice = "";
    public String deli_title = "";
    public String deli_type = "";
    public String deli_msg = "";
    public String delivery = "";
    public String coupon = "";
    public String emoney = "";
    public String reserve = "";
    public String bankAccount = "";
    public String bankSender = "";
    public String deliveryno = "";
    public String deliverycode = "";
    public String m_no = "";
    public String orddt = "";
    public String uptdt_ = "";
    public String str_step = "";
    public String str_settlekind = "";
    public String idx = "";
    public String goodsnm = "";

    public OrderItemInfo(String _ordno, String _nameOrder, String _email, String _phoneOrder, String _mobileOrder, String _nameReceiver,
                         String _phoneReceiver, String _mobileReceiver, String _zipcode, String _address, String _road_address, String _settleprice,
                         String _prn_settleprice, String _goodsprice, String _deli_title, String _deli_type, String _deli_msg, String _delivery, String _coupon, String _emoney,
                         String _reserve, String _bankAccount, String _bankSender, String _deliveryno, String _deliverycode, String _m_no, String _orddt, String _uptdt_, String _str_step, String _str_settlekind,String _idx, String _goodsnm) {

        ordno = _ordno;
        nameOrder = _nameOrder;
        email = _email;
        phoneOrder = _phoneOrder;
        mobileOrder = _mobileOrder;
        nameReceiver = _nameReceiver;
        phoneReceiver = _phoneReceiver;
        mobileReceiver = _mobileReceiver;
        zipcode = _zipcode;
        address = _address;
        road_address = _road_address;
        settleprice = _settleprice;
        prn_settleprice = _prn_settleprice;
        goodsprice = _goodsprice;
        deli_title = _deli_title;
        deli_type = _deli_type;
        deli_msg = _deli_msg;
        delivery = _delivery;
        coupon = _coupon;
        emoney = _emoney;
        reserve = _reserve;
        bankAccount = _bankAccount;
        bankSender = _bankSender;
        deliveryno = _deliveryno;
        deliverycode = _deliverycode;
        m_no = _m_no;
        orddt = _orddt;
        uptdt_ = _uptdt_;
        str_step = _str_step;
        str_settlekind = _str_settlekind;
        idx = _idx;
        goodsnm = _goodsnm;
    }
}
