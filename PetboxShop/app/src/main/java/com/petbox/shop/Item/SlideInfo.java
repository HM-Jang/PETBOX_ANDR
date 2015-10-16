package com.petbox.shop.Item;

/**
 * Created by 펫박스 on 2015-10-06.
 */
public class SlideInfo {

    public int sort = 0;
    public String title = "";
    public String type = "";
    public String type_data = "";
    public String banner_img = "";

    public SlideInfo(int _sort, String _title, String _type, String _type_data, String _banner_img) {
        sort = _sort;
        title = _title;
        type = _type;
        type_data = _type_data;
        banner_img = _banner_img;
    }

}
