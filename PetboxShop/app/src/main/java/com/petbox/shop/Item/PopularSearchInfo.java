package com.petbox.shop.Item;

/**
 * Created by petbox on 2015-09-16.
 */
public class PopularSearchInfo {
    public int ranking = 999;  // 검색순위
    public String title = ""; // 타이틀
    public PopularSearchInfo(int _ranking, String _title) {
        ranking = _ranking;
        title = _title;
    }
}
