package com.petbox.shop.Delegate;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by petbox on 2015-10-17.
 */
public interface CategoryManagerDelegate {
    public void prevRunningCategoryManager();
    public void runningHttpCategoryManager();
    public void afterRunningCategoryManger();

}
