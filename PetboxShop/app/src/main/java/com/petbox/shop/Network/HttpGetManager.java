package com.petbox.shop.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.petbox.shop.Delegate.HttpGetDelegate;
import com.petbox.shop.Delegate.HttpPostDelegate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by petbox on 2015-10-16.
 */
public class HttpGetManager {
    private static final String TAG = "HttpGetManager";

    private DefaultHttpClient httpClient;
    private HttpGetDelegate delegate;

    InputStream is;

    public HttpGetManager(){
        httpClient = new DefaultHttpClient();
    }

    int mode = 0;   // Default : Status

    public HttpGetManager(HttpGetDelegate delegate){
        httpClient = new DefaultHttpClient();
        this.delegate = delegate;
    }

    public void setDelegate(HttpGetDelegate delegate){
        this.delegate = delegate;
    }

    public void start(){
        HttpGetTask task = new HttpGetTask();
        task.execute();
    }

    class HttpGetTask extends AsyncTask<Void, Void, String> {



        @Override
        protected void onPreExecute(){
            if(delegate != null)
                delegate.prevRunningHttpGet();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String getURL = delegate.getUrl();
            List<NameValuePair> nameValuePairs = delegate.getNameValuePairs();

            String url = getURL + "?" + URLEncodedUtils.format(nameValuePairs, null);
            Log.i(TAG, "GET URL : " + url);
            HttpGet httpGet = new HttpGet(url);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                //result_code = response.getStatusLine().getStatusCode();
                is = entity.getContent();

            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();

            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = null;

                while((line = reader.readLine()) != null){
                    sb.append(line);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

            if(delegate != null)
                delegate.runningHttpGet();

            return sb.toString();
        }


        @Override
        protected  void onPostExecute(String result){
            if(delegate != null)
                delegate.afterRunningHttpGet(result);

            delegate = null;

            super.onPostExecute(result);
        }
    }
}
