package com.petbox.shop.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.petbox.shop.Adapter.List.ListDialogAdapter;
import com.petbox.shop.Delegate.ClickDelegate;
import com.petbox.shop.R;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-10-17.
 */
public class ListDialog extends Dialog implements AdapterView.OnItemClickListener {

    ArrayList<String> mItemList;
    ListView listView;

    Context mContext;
    ClickDelegate delegate;
    String title;

    TextView tv_title;
    ListDialogAdapter adapter;

    int depth = 0; // 2: 2차, 3: 3차

    public ListDialog(Context context) {
        super(context);
        mContext = context;
    }

    public ListDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected ListDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public ListDialog(Context context, ClickDelegate delegate){
        super(context);
        mContext = context;
        this.delegate = delegate;
    }

    public ListDialog(Context context, String title, ArrayList<String> itemList, ClickDelegate delegate){
        super(context);
        mContext = context;
        this.delegate = delegate;
        this.title = title;
        mItemList = itemList;
    }

    public ListDialog(Context context, String title, ArrayList<String> itemList, ClickDelegate delegate, int depth){
        super(context);
        mContext = context;
        this.delegate = delegate;
        this.title = title;
        mItemList = itemList;
        this.depth = depth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_list);

        tv_title = (TextView)findViewById(R.id.tv_dialog_list_title);
        tv_title.setText(title);

        listView = (ListView) findViewById(R.id.list_dialog_list);
        listView.setOnItemClickListener(this);

        adapter = new ListDialogAdapter(mContext, mItemList);
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //depth 세팅이 안됬을 경우, 카테고리 상세상품 리스트가 아닐때
        if(depth == 0)
            delegate.click(position);
        else
            delegate.click(position, depth);
    }
}
