package com.petbox.shop.Network;

import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.petbox.shop.Adapter.List.PlanningListAdapter;
import com.petbox.shop.DB.Constants;
import com.petbox.shop.DataStructure.Tree.Node;
import com.petbox.shop.DataStructure.Tree.Tree;
import com.petbox.shop.Delegate.CategoryManagerDelegate;
import com.petbox.shop.Delegate.HttpGetDelegate;
import com.petbox.shop.Delegate.PlanningManagerDelegate;
import com.petbox.shop.Item.CategoryInfo;
import com.petbox.shop.Item.PlanningItemInfo;
import com.petbox.shop.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by petbox on 2015-10-19.
 */
public class PlanningManager {
    private static final String TAG = "PlanningManager";

    public static Tree<PlanningItemInfo> dog_tree;
    public static Tree<PlanningItemInfo> cat_tree;

    static PlanningManagerDelegate delegate;


    public static PlanningManager planningManager;

    public static PlanningManager getManager(){

        if(planningManager == null){
            planningManager = new PlanningManager();
        }
        return planningManager;
    }

    public static Tree<PlanningItemInfo> getDogTree(){
        if(dog_tree == null) {
            init();
        }
        return dog_tree;
    }

    public static Tree<PlanningItemInfo> getCatTree(){
        if(cat_tree == null) {
            init();
        }
        return cat_tree;
    }

    public PlanningManager(){
        init();
    }

    public PlanningManager(PlanningManagerDelegate delegate){
        this.delegate = delegate;
        init();
    }

    public static void  setDelegate(PlanningManagerDelegate delegate){
        planningManager.delegate = delegate;
    }

    public static ArrayList<PlanningItemInfo> convertArrayList(ArrayList<Node<PlanningItemInfo>> nodeList){
        ArrayList<PlanningItemInfo> arrList = new ArrayList<PlanningItemInfo>();

        for(int i=0; i< nodeList.size(); i++){
            Node<PlanningItemInfo> node = nodeList.get(i);
            PlanningItemInfo item = node.getData();

            System.out.println("CONVERT_ARRAY_LIST : " + item.name);

            arrList.add(item);
        }

        return arrList;
    }

    // Async로 애견 카테고리 목록 받은 후 애묘로로
    public static void init(){
        dog_tree = new Tree<PlanningItemInfo>();
        Node<PlanningItemInfo> dog_root = new Node<PlanningItemInfo>();
        PlanningItemInfo planningInfo = new PlanningItemInfo();
        planningInfo.linkaddr = "004028";
        planningInfo.loccd = "3";   //강아지
        planningInfo.img = "";
        dog_root.setData(planningInfo);
        dog_tree.setRoot(dog_root);

        cat_tree = new Tree<PlanningItemInfo>();
        Node<PlanningItemInfo> cat_root = new Node<PlanningItemInfo>();
        planningInfo = new PlanningItemInfo();
        planningInfo.linkaddr = "004029"; //고양이
        planningInfo.loccd = "4";   //고양이
        planningInfo.img = "";
        cat_root.setData(planningInfo);
        cat_tree.setRoot(cat_root);

        setPlanningList();
    }

    public static void setPlanningList(){
        HttpGetManager httpGetManager = new HttpGetManager(new PlanningHttpGetDelegate());
        httpGetManager.start();
    }

    // name 찾을것, mode : 강아지(0),고양이(1)
    public static Node<PlanningItemInfo> scan(String param, int mode){

        System.out.println("스캔 중.. ");
        ArrayList<Node<PlanningItemInfo>> rootChildList = null;

        if(mode == 0)
            rootChildList = dog_tree.root.getChildList();
        else
            rootChildList = cat_tree.root.getChildList();

        PlanningItemInfo item = new PlanningItemInfo();

        for(int i=0; i<rootChildList.size(); i++){
            ArrayList<Node<PlanningItemInfo>> child2List = rootChildList.get(i).getChildList();

            Node<PlanningItemInfo> node2 = rootChildList.get(i);
            item = node2.getData();

            if(item.linkaddr.equals(param))
                return node2;
        }

        return null;
    }

    public static class PlanningHttpGetDelegate implements HttpGetDelegate {

        @Override
        public void prevRunningHttpGet() {
            if (delegate != null)
                delegate.prevRunningPlanningManager();
        }

        @Override
        public String getUrl() {

            return Constants.HTTP_URL_PLANNING_LIST;

        }

        @Override
        public List<NameValuePair> getNameValuePairs() {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            //String param = "";

            //nameValuePairs.add(new BasicNameValuePair("category", param));

            return nameValuePairs;
        }

        @Override
        public void runningHttpGet() {
            if (delegate != null)
                delegate.runningHttpPlanningManager();
        }

        @Override
        public void afterRunningHttpGet(String jsonData) {
            try {
                System.out.println("JSON : " + jsonData);

                JSONArray display_goods_Array = new JSONArray(jsonData);


                for (int k = 0; k < display_goods_Array.length(); k++) {
                    JSONObject ca_object = display_goods_Array.getJSONObject(k);

                    String name = ca_object.getString("catnm");
                    String linkaddr = ca_object.getString("linkaddr");
                    String loccd = ca_object.getString("loccd");
                    String img = ca_object.getString("img");

                    // 3: 강아지
                    if (Integer.parseInt(loccd) == 3) {

                        if(ca_object.isNull("linkaddr"))
                            continue;

                        Node<PlanningItemInfo> node = new Node<PlanningItemInfo>();

                        PlanningItemInfo item = new PlanningItemInfo();

                        item.name = name;
                        item.linkaddr = linkaddr;
                        item.loccd = loccd;
                        item.img = img;

                        System.out.println("기획전 리스트(강아지) : "+ name + "// url : " + img);

                        node.setData(item);
                        dog_tree.add(dog_tree.root, node);

                    // 4: 고양이
                    } else {
                        if(ca_object.isNull("linkaddr")) {
                            System.out.println("기획전 리스트(고양이 NULL) : "+ name + "// url : " + img);
                            continue;

                        }
                        Node<PlanningItemInfo> node = new Node<PlanningItemInfo>();

                        PlanningItemInfo item = new PlanningItemInfo();
                        item.name = name;
                        item.linkaddr = linkaddr;
                        item.loccd = loccd;
                        item.img = img;

                        System.out.println("기획전 리스트(고양이) : "+ name + "// url : " + img);

                        node.setData(item);
                        cat_tree.add(cat_tree.root, node);
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            if(delegate != null)
                delegate.afterRunningPlanningManger();
        }
    }


}
