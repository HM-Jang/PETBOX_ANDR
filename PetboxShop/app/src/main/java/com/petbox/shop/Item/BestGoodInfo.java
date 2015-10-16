package com.petbox.shop.Item;

/**
 * Created by petbox on 2015-09-15.
 */
public class BestGoodInfo {
    public int sort = 0;
    public String imgUrl = "";
    public String name = "";
    public String goodsno = "";
    public String rate = "";
    public String origin_price = "";
    public String price = "";
    public float rating = 0;
    public int rating_person = 0;
    public int icon = 0;

    public BestGoodInfo(){}

    public BestGoodInfo(int _sort, String _imgUrl, String _name, String _goodsno, String _rate, String _origin_price, String _price, float _rating, int _rating_person, int _icon) {
        sort = _sort;
        imgUrl = _imgUrl;
        name = _name;
        goodsno = _goodsno;
        rate = "";
        origin_price = _origin_price;
        price = _price;
        rating = _rating;
        rating_person = _rating_person;
        icon = _icon;
    }
}
