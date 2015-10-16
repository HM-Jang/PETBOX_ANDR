package com.petbox.shop.Item;

/**
 * Created by 펫박스 on 2015-10-12.
 */
public class EmoneyInfo {
    public String sort = "";
    public String ordno = "";
    public String emoney = "";
    public String memo = "";
    public String regdt = "";
    public String regdts = "";
    public String idx = "";

    public EmoneyInfo(String _sort,String _ordno, String _emoney, String _memo, String _regdt, String _regdts, String _idx) {
        sort = _sort;
        ordno = _ordno;
        emoney = _emoney;
        memo = _memo;
        regdt = _regdt;
        regdts = _regdts;
        idx = _idx;
    }
}