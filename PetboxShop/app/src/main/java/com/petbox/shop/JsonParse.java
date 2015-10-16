package com.petbox.shop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.petbox.shop.DB.DBConnector;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by 펫박스 on 2015-10-06.
 */
public class JsonParse{

    private static String getJsonText(String[] params,Context context) {
        String line = "";
        String url_standard = params[0];
        String url_mult;

        try {
            if( params[0] != null){
                Log.e("params[0]", params[1]);

                url_mult = params[1].replace(" ", "%20");

                Log.e("loat url",url_standard + url_mult);

                line = getStringFromUrl(url_standard + url_mult);
            }else{
                line = getStringFromUrl("http://petbox.kr/petboxjson/app_goods_list.php");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("json_data", line);

        if(params[2] == "category"){
            category(line,context);
        }else if(params[2] == "display_item_list"){
            display_item_list(line,context);
        }

        return line;
    } // getJsonText

    // getStringFromUrl : 주어진 URL 페이지를 문자열로 얻는다.
    public static String getStringFromUrl(String url) throws UnsupportedEncodingException {

        // 입력스트림을 "UTF-8"
        BufferedReader br = new BufferedReader(new InputStreamReader(getInputStreamFromUrl(url), "UTF-8"));

        StringBuffer sb = new StringBuffer();

        try {
            // 라인 단위로 읽은 데이터를 임시 저장한 문자열 변수 line
            String line = null;

            // 라인 단위로 데이터를 읽어서 StringBuffer 에 저장한다.
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    } // getStringFromUrl

    /**
     *  getInputStreamFromUrl : 주어진 URL 에 대한 입력 스트림(InputStream)을 얻는다.
     */
    public static InputStream getInputStreamFromUrl(String url) {
        InputStream contentStream = null;
        try {
            // HttpClient 를 사용해서 주어진 URL에 대한 입력 스트림을 얻는다.
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            contentStream = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentStream;
    } // getInputStreamFromUrl

    /**
     *	스레드에서 향상된 AsyncTask 를 이용하여
     * UI 처리 및 Background 작업 등을 하나의 클래스에서 작업 할 수 있도록 지원해준다.
     */
    public static class JsonLoadingTask extends AsyncTask<String, Void, String> {

        private Context mContext;
        String Jsondata;
        public JsonLoadingTask(Context context){
            mContext = context;
        }

        @Override
        protected String doInBackground(String... strs) {
            Jsondata = getJsonText(strs,mContext);
            return Jsondata;
        } // doInBackground : 백그라운드 작업을 진행한다.

        @Override
        protected void onPostExecute(String result) {
            Log.e("Start download image", "-----------------------------------gogo");

        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.
    } // JsonLoadingTask


    public static void category(String category, Context context){
        String category_list = category;
        JSONArray categoryArray = null;
        try {
            categoryArray = new JSONArray(category_list);
            JSONObject cate_object = categoryArray.getJSONObject(0);
            JSONArray category_data = cate_object.getJSONArray("category_data");
            for (int k = 0; k < category_data.length(); k++) {
                JSONObject ca_object = category_data.getJSONObject(k);
                new DBConnector(context).insertToCategoryList(ca_object.getString("category"), ca_object.getString("catnm"), ca_object.getString("sort"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void display_item_list(String display_list,Context context){
        String display_item_list = display_list;
        Log.e("display_item_list", display_item_list);
        String down_path,file_name;

        JSONArray countriesArray = null;
        try {
            countriesArray = new JSONArray(display_item_list);
            Log.e("countriesArray", String.valueOf(countriesArray.length()));

            JSONObject object = countriesArray.getJSONObject(0);
            JSONArray display_item = object.getJSONArray("display_item");

            Log.e("countriesArray", String.valueOf(display_item.length()));


            for (int k = 0; k < display_item.length(); k++) {
                JSONObject di_object = display_item.getJSONObject(k);
                Log.e("object-type", String.valueOf(di_object.getString("goodsno")));
                Log.e("object-type", String.valueOf(k));

                Log.e("DB_insert", "do insert");
               new DBConnector(context).insertToDisplayGoodsList(di_object.getString("goodsno"), di_object.getString("mdesign_no"), di_object.getString("sort"), di_object.getString("goodsnm"), di_object.getString("img_i"), di_object.getInt("open_mobile"), di_object.getString("goods_consumer"), di_object.getString("goods_price"), di_object.getString("icon"), di_object.getString("point"), di_object.getString("point_count"));
            }

            JSONArray display_slide = object.getJSONArray("display_slide");

            Log.e("display_slide.length()", String.valueOf(display_slide.length()));

            for (int l = 0; l < display_slide.length(); l++) {
                JSONObject sl_object = display_slide.getJSONObject(l);

                JSONArray banner = new JSONArray("[" + sl_object.getString("tpl_opt") + "]");

                JSONObject bn_object = banner.getJSONObject(0);
                Log.e("banner_num", bn_object.getString("banner_num"));

                JSONArray banner_img = new JSONArray("[" + bn_object.getString("banner_img") + "]");

                Log.e("bn_img_object", String.valueOf(l));
                JSONObject bn_img_object = banner_img.getJSONObject(0);
                Log.e("bn_img_object", bn_img_object.getString(sl_object.getString("sort")));

                down_path = "http://petbox.kr/shop/data/m/upload_img/" + bn_img_object.getString(sl_object.getString("sort"));
                file_name = "slide_" + sl_object.getString("mdesign_no") + "_" + sl_object.getString("sort");

                if(new DBConnector(context).SlideCount(5) != 0){
                    Log.e("slide","삭제하려함");
                    ImageDownloader.DeleteBitmapToFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.petbox.shop/files/", "slide_" + sl_object.getString("mdesign_no") + "_" + sl_object.getString("sort"));
                    Log.e("slide", "삭제완료 함");
                }
                new DBConnector(context).insertToDisplaySlideList(sl_object.getString("mdesign_no"), sl_object.getString("sort"), sl_object.getString("type"), sl_object.getString("type_data"), sl_object.getString("title"), bn_img_object.getString(sl_object.getString("sort")));
                Log.e("slide", "DB에 넣었음");

                ImageDownloader.SaveBitmapToFileCache(down_path,file_name);
                Log.e("slide_path", Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.petbox.shop/files/" + "slide_" + sl_object.getString("mdesign_no") + "_" + sl_object.getString("sort"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
