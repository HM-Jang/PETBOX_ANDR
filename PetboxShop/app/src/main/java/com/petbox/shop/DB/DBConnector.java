package com.petbox.shop.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.petbox.shop.Item.BestGoodInfo;
import com.petbox.shop.Item.CategoryInfo;
import com.petbox.shop.Item.RecentSearchInfo;
import com.petbox.shop.Item.SlideInfo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by petbox on 2015-09-21.
 */
public class DBConnector extends SQLiteOpenHelper {

    private static final String LOG = "DBConnetor";
    Context mContext;

    public DBConnector(Context context){
        super(context, Constants.DATABASE, null, Constants.DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // 최근 검색어
        db.execSQL("CREATE TABLE " + Constants.RECENT_SEARCH + " ("
                + Constants.RECENT_SEARCH_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.RECENT_SEARCH_TITLE + " TEXT,"
                + Constants.RECENT_SEARCH_DATE + " DATE"
                + ");");

        //진열 상품 리스트
        db.execSQL("CREATE TABLE " + Constants.DISPLAY_GOODS_LIST + " ("
                + Constants.DISPLAY_GOODS_LIST_NUM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.DISPLAY_GOODS_LIST_GOODSNO + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_DESIGN_NO + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_SORT + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_NM + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_THUMBNAIL + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_OPEN_M + " INTEGER,"
                + Constants.DISPLAY_GOODS_LIST_CONSUMER + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_PRICE + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_ICON + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_GRADE + " TEXT,"
                + Constants.DISPLAY_GOODS_LIST_GRADE_COUNT + " TEXT"
                + ");");

        //카테고리 리스트
        db.execSQL("CREATE TABLE " + Constants.CATEGORY_LIST + " ("
                + Constants.CATEGORY_NUM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.CATEGORY_CATEGORY + " TEXT,"
                + Constants.CATEGORY_NAME + " TEXT,"
                + Constants.CATEGORY_SORT + " TEXT"
                + ");");

        //슬라이드 리스트
        db.execSQL("CREATE TABLE " + Constants.DISPLAY_SLIDE_LIST + " ("
                + Constants.DISPLAY_SLIDE_NUM + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Constants.DISPLAY_SLIDE_DESIGN_NO + " TEXT,"
                + Constants.DISPLAY_SLIDE_SORT + " TEXT,"
                + Constants.DISPLAY_SLIDE_TEMP + " TEXT,"
                + Constants.DISPLAY_SLIDE_CHK + " TEXT,"
                + Constants.DISPLAY_SLIDE_TITLE + " TEXT,"
                + Constants.DISPLAY_SLIDE_TEMP_OPT + " TEST"
                + ");");

        Log.i(LOG, "++ DB Created ++");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // DB내 모든 테이블 레코드 정보 화면 출력
    public void showAllDB(String TABLE){

        SQLiteDatabase db = new DBConnector(mContext).getReadableDatabase();
        String query = "SELECT * FROM " + TABLE  + ";";

        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        if(c.getCount() >0){

            Log.i(LOG, " ++ Show All DB ++");

            int count =0;
            do{
                String log = "";

                for(int i=0; i<c.getColumnCount(); i++){
                    log += c.getColumnName(i);
                    log += " : ";

                    int type = c.getType(i);

                    switch(type){
                        case Cursor.FIELD_TYPE_INTEGER :
                            log += "(INT)" + c.getInt(i) + " ";
                            break;
                        case Cursor.FIELD_TYPE_STRING :
                            log += "(STR)" + c.getString(i) + " ";
                            break;
                        case Cursor.FIELD_TYPE_NULL :
                            log += "(NULL)" + " ";
                            break;
                        default :
                            log += "(ETC)" + " ";
                    }
                }
                count++;
                Log.i(LOG, "[" + count + "] //" + log);
            }while(c.moveToNext());
            Log.i(LOG, " ++ Show All DB : End ++");
        }else{
            Log.i(LOG, " ++ Show All DB : Empty ++");
        }
        db.close();
    }

    /* ### 최근검색어 START ####################################################### */

    //삽입
    public boolean insertToRecentSearch(String title, String date){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.RECENT_SEARCH_TITLE, title);
        values.put(Constants.RECENT_SEARCH_DATE, date);

        db.insert(Constants.RECENT_SEARCH, null, values);
        Log.i(LOG, "Insert " + title);

        db.close();

        return true;
    }

    //삭제
    public boolean deleteRecentSearchInfo(int rowId){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();

        String whereClause = Constants.RECENT_SEARCH_ROWID + "=?";  // Where절
        String[] whereArgs = new String[]{Integer.toString(rowId)};

        db.delete(Constants.RECENT_SEARCH, whereClause, whereArgs);

        db.close();
        return true;
    }



    //반환
    public ArrayList<RecentSearchInfo> returnFromRecentSearch(){
        SQLiteDatabase db = new DBConnector(mContext).getReadableDatabase();

        ArrayList<RecentSearchInfo> itemList = new ArrayList<RecentSearchInfo>();

        String query = "SELECT * FROM " + Constants.RECENT_SEARCH + " ORDER BY " + Constants.RECENT_SEARCH_ROWID + " desc";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() > 0){
            do{
                int id = c.getInt(c.getColumnIndex(Constants.RECENT_SEARCH_ROWID));
                String title = c.getString(c.getColumnIndex(Constants.RECENT_SEARCH_TITLE));
                String date = c.getString(c.getColumnIndex(Constants.RECENT_SEARCH_DATE));

                RecentSearchInfo item = new RecentSearchInfo(id, title, date);
                itemList.add(item);

            }while(c.moveToNext());
        }


        db.close();

        return itemList;
    }

    /* ### 최근검색어 END ####################################################### */

    /* ### 진열 상품 리스트 START ####################################################### */
    //삽입
    public boolean  insertToDisplayGoodsList(String goodsno, String design_no, String sort, String name, String thumbnail, int open, String consumer, String price, String icon, String grade, String grade_count ){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();

        Log.e("DB_insert", String.valueOf(goodsno));

        ContentValues values = new ContentValues();

        values.put(Constants.DISPLAY_GOODS_LIST_GOODSNO, goodsno);
        values.put(Constants.DISPLAY_GOODS_LIST_DESIGN_NO, design_no);
        values.put(Constants.DISPLAY_GOODS_LIST_SORT, sort);
        values.put(Constants.DISPLAY_GOODS_LIST_NM, name);
        values.put(Constants.DISPLAY_GOODS_LIST_THUMBNAIL, thumbnail);
        values.put(Constants.DISPLAY_GOODS_LIST_OPEN_M, open);
        values.put(Constants.DISPLAY_GOODS_LIST_CONSUMER, consumer);
        values.put(Constants.DISPLAY_GOODS_LIST_PRICE, price);

        values.put(Constants.DISPLAY_GOODS_LIST_ICON, icon);
        values.put(Constants.DISPLAY_GOODS_LIST_GRADE, grade);
        values.put(Constants.DISPLAY_GOODS_LIST_GRADE_COUNT, grade_count);

        db.insert(Constants.DISPLAY_GOODS_LIST, null, values);

        db.close();

        return true;
    }

    //삭제
    public boolean deleteDisplayGoodsList(int rowId){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();

        String whereClause = Constants.DISPLAY_GOODS_LIST_NUM + "=?";  // Where절
        String[] whereArgs = new String[]{Integer.toString(rowId)};

        db.delete(Constants.DISPLAY_GOODS_LIST, whereClause, whereArgs);

        db.close();
        return true;
    }

    //반환
    public ArrayList<BestGoodInfo> returnFromBestGoodInfo(String page_num){
        SQLiteDatabase db = new DBConnector(mContext).getReadableDatabase();

        ArrayList<BestGoodInfo> itemList = new ArrayList<BestGoodInfo>();

        String query = "SELECT * FROM " + Constants.DISPLAY_GOODS_LIST + " WHERE "+ Constants.DISPLAY_GOODS_LIST_DESIGN_NO + "= " +page_num +"  ORDER BY " + Constants.DISPLAY_GOODS_LIST_SORT + " desc";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() > 0){
            do{
                if(c.getInt(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_OPEN_M)) == 1 ){
                    int _sort = c.getInt(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_SORT));
                    String _imgUrl = c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_THUMBNAIL));
                    String _name = c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_NM));
                    String _goodsno = c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_GOODSNO));
                    String _origin_price = c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_CONSUMER));
                    String _price = c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_PRICE));
                    //String _rate = c.getString(c.getColumnIndex(Constants.RECENT_SEARCH_DATE));
                    //float rating = c.getString(c.getColumnIndex(Constants.RECENT_SEARCH_DATE));
                    //int rating_person = c.getString(c.getColumnIndex(Constants.RECENT_SEARCH_DATE));
                    String _rate = String.valueOf(c.getInt(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_SORT)));

                    int _icon =  Integer.parseInt(c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_ICON)));
                    float _rating =  Float.parseFloat(c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_GRADE)));
                    int _rating_person = Integer.parseInt(c.getString(c.getColumnIndex(Constants.DISPLAY_GOODS_LIST_GRADE_COUNT)));

                    BestGoodInfo item = new BestGoodInfo(_sort,_imgUrl,_name,_goodsno,_rate,_origin_price,_price,_rating,_rating_person,_icon);

                    itemList.add(item);
                }
            }while(c.moveToNext());
        }

        db.close();

        return itemList;
    }

    /* ### 상품 리스트 END ####################################################### */

    /* ### 슬라이드 리스트 START ####################################################### */
    //삽입
    public boolean  insertToDisplaySlideList(String design_no, String sort, String temp1, String chk, String title, String tpl_opt){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.DISPLAY_SLIDE_DESIGN_NO, design_no);
        values.put(Constants.DISPLAY_SLIDE_SORT, sort);
        values.put(Constants.DISPLAY_SLIDE_TEMP, temp1);
        values.put(Constants.DISPLAY_SLIDE_CHK, chk);
        values.put(Constants.DISPLAY_SLIDE_TITLE, title);
        values.put(Constants.DISPLAY_SLIDE_TEMP_OPT, tpl_opt);

        db.insert(Constants.DISPLAY_SLIDE_LIST, null, values);

        db.close();

        return true;
    }

    //삭제
    public boolean deleteDisplaySlideList(int rowId){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();

        String whereClause = Constants.DISPLAY_SLIDE_NUM + "=?";  // Where절
        String[] whereArgs = new String[]{Integer.toString(rowId)};

        db.delete(Constants.DISPLAY_SLIDE_LIST, whereClause, whereArgs);

        db.close();
        return true;
    }

    //슬라이드 갯수 로드
    public int SlideCount(int slide_no){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();
        int count = 0;
        String query = "SELECT * FROM " + Constants.DISPLAY_SLIDE_LIST + " WHERE "+ Constants.DISPLAY_SLIDE_DESIGN_NO +" = "+ slide_no;// Where절
        Log.e("query",query);
        Log.e("slide_no",String.valueOf(slide_no));
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        count = c.getCount();

        Log.e("slide_count",String.valueOf(count));

        db.close();
        return count;
    }

    //반환
    public ArrayList<SlideInfo> returnFromMainSlideInfo(){
        SQLiteDatabase db = new DBConnector(mContext).getReadableDatabase();

        ArrayList<SlideInfo> itemList = new ArrayList<SlideInfo>();

        String query = "SELECT * FROM " + Constants.DISPLAY_SLIDE_LIST + " WHERE "+ Constants.DISPLAY_SLIDE_DESIGN_NO +"= 5  ORDER BY " + Constants.DISPLAY_SLIDE_SORT + " desc";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() > 0){
            do{

                int _sort = c.getInt(c.getColumnIndex(Constants.DISPLAY_SLIDE_SORT));
                String _temp = c.getString(c.getColumnIndex(Constants.DISPLAY_SLIDE_TEMP));
                String _chk = c.getString(c.getColumnIndex(Constants.DISPLAY_SLIDE_CHK));
                String _title = c.getString(c.getColumnIndex(Constants.DISPLAY_SLIDE_TITLE));
                String _temp_opt = c.getString(c.getColumnIndex(Constants.DISPLAY_SLIDE_TEMP_OPT));

                SlideInfo item = new SlideInfo(_sort,_temp,_chk,_title,_temp_opt);

                itemList.add(item);

            }while(c.moveToNext());
        }


        db.close();

        return itemList;
    }

    /* ### 슬라이드 리스트 END ####################################################### */




    /* ### 카테고리 리스트 START ####################################################### */
    //삽입
    public boolean  insertToCategoryList(String category_num, String category_name, String sort){
        SQLiteDatabase db = new DBConnector(mContext).getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.e("insert category","insert category data");
        values.put(Constants.CATEGORY_CATEGORY, category_num);
        values.put(Constants.CATEGORY_NAME, category_name);
        values.put(Constants.CATEGORY_SORT, sort);

        db.insert(Constants.CATEGORY_LIST, null, values);
        db.close();

        return true;
    }

    //삭제
    public void deleteToCategoryList(){
        SQLiteDatabase db= new DBConnector(mContext).getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.CATEGORY_LIST + ";");
        db.close();
    }


    //반환
    public ArrayList<CategoryInfo> returnCategoryList(String cate_num){
        SQLiteDatabase db = new DBConnector(mContext).getReadableDatabase();
        String query="";
        ArrayList<CategoryInfo> itemList = new ArrayList<CategoryInfo>();

        if(cate_num != null)
        {
           query = "SELECT * FROM " + Constants.CATEGORY_LIST + " WHERE "+ Constants.CATEGORY_CATEGORY +" LIKE '"+cate_num+"%' ORDER BY " + Constants.CATEGORY_SORT + " desc";
        }else{
            query = "SELECT * FROM " + Constants.CATEGORY_LIST + " WHERE "+ Constants.CATEGORY_CATEGORY +" LIKE '"+cate_num+"%' ORDER BY " + Constants.CATEGORY_SORT + " desc";
        }
        Log.e("query",query);

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        if(c.getCount() > 0){
            do{

                int _sort = c.getInt(c.getColumnIndex(Constants.CATEGORY_SORT));
                String _category_num = c.getString(c.getColumnIndex(Constants.CATEGORY_CATEGORY));
                String _name = c.getString(c.getColumnIndex(Constants.CATEGORY_NAME));

                CategoryInfo item = new CategoryInfo(_sort,_category_num,_name);

                itemList.add(item);

            }while(c.moveToNext());
        }

        db.close();

        return itemList;
    }

    /* ### 카테고리 리스트 END ####################################################### */




}
