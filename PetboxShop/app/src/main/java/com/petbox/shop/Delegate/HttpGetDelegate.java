package com.petbox.shop.Delegate;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by petbox on 2015-10-16.
 */
public interface HttpGetDelegate {
    public void prevRunningHttpGet();
    public String getUrl();
    public List<NameValuePair> getNameValuePairs();
    public void runningHttpGet();
    public void afterRunningHttpGet(String jsonData);
}
