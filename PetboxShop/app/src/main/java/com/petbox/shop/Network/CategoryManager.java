package com.petbox.shop.Network;

import com.petbox.shop.DB.Constants;
import com.petbox.shop.DataStructure.Tree.Node;
import com.petbox.shop.DataStructure.Tree.Tree;
import com.petbox.shop.Delegate.HttpGetDelegate;
import com.petbox.shop.Item.CategoryInfo;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petbox on 2015-10-16.
 */
public class CategoryManager implements HttpGetDelegate{

    private static final String TAG = "CategoryManager";

    public Tree<CategoryInfo> dog_tree;
    public Tree<CategoryInfo> cat_tree;

    int mode = 0; // 0: dog, 1: cat

    public CategoryManager(){
        init();
    }


    // Async로 애견 카테고리 목록 받은 후 애묘로로
   public void init(){
        dog_tree = new Tree<CategoryInfo>();
        Node<CategoryInfo> dog_root = new Node<CategoryInfo>();
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.name = "애견 카테고리";
        categoryInfo.category_num = "035";
        categoryInfo.sort = 0;

        dog_root.setData(categoryInfo);
        dog_tree.setRoot(dog_root);

        cat_tree = new Tree<CategoryInfo>();
        Node<CategoryInfo> cat_root = new Node<CategoryInfo>();

        categoryInfo = new CategoryInfo();
        categoryInfo.name = "애묘 카테고리";
        categoryInfo.category_num = "036";
        categoryInfo.sort = 0;

        cat_root.setData(categoryInfo);
        cat_tree.setRoot(cat_root);

        setDogTree();
        //setCatTree();
    }

    public Tree<CategoryInfo> getDogTree(){
        if(dog_tree == null) {
            dog_tree = new Tree<CategoryInfo>();
            setDogTree();
        }
        return dog_tree;
    }

    public Tree<CategoryInfo> getCatTree(){
        if(cat_tree == null) {
            cat_tree = new Tree<CategoryInfo>();
            setCatTree();
        }
        return cat_tree;
    }

    public void setDogTree(){
        mode = 0;
        HttpGetManager httpGetManager = new HttpGetManager(this);
        httpGetManager.start();
    }

    public void setCatTree(){
        mode = 1;
        HttpGetManager httpGetManager = new HttpGetManager(this);
        httpGetManager.start();
    }

    @Override
    public void prevRunningHttpGet() {

    }

    @Override
    public String getUrl() {
        return Constants.HTTP_URL_CATEGORY_LIST;
    }

    @Override
    public List<NameValuePair> getNameValuePairs() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        String param = "";

        if(mode ==0)
            param = "035";
        else
            param = "036";

        nameValuePairs.add(new BasicNameValuePair("category", param));

        return nameValuePairs;
    }

    @Override
    public void runningHttpGet() {

    }

    @Override
    public void afterRunningHttpGet(String jsonData) {
        try{
            //System.out.println(jsonData);

            JSONArray jsonArray = new JSONArray(jsonData);
            JSONObject main = jsonArray.getJSONObject(0);
            JSONArray category_data = main.getJSONArray("category_data");
            //JSONObject category_data = main.getJSONObject("category_data");

            ArrayList<CategoryInfo> arrCategory1 = new ArrayList<CategoryInfo>();   // 카테고리 글자 6
            ArrayList<CategoryInfo> arrCategory2 = new ArrayList<CategoryInfo>();   // 카테고리 글자 9
            ArrayList<CategoryInfo> arrCategory3 = new ArrayList<CategoryInfo>();   // 카테고리 글자 12

            for(int i=0; i< category_data.length(); i++){
                JSONObject obj = category_data.getJSONObject(i);

                CategoryInfo item = new CategoryInfo();

                item.name = obj.getString("catnm");
                item.category_num = obj.getString("category");
                item.sort  = obj.getInt("sort");

                switch(item.category_num.length()){
                    case 6:
                        arrCategory1.add(item);
                        //System.out.println("1차 : " + item.category_num + "// " + item.name);
                        break;

                    case 9:
                        arrCategory2.add(item);
                        //System.out.println("2차 : " + item.category_num + "// " + item.name);
                        break;

                    case 12:
                        arrCategory3.add(item);
                        //System.out.println("3차 : " + item.category_num + "// " + item.name);
                        break;
                }
            }

            //root(1차), Tree의 2차 Depth에 넣음.
            for(int i=0; i<arrCategory1.size(); i++){
                Node<CategoryInfo> item = new Node<CategoryInfo>();
                item.setData(arrCategory1.get(i));

                if(mode == 0)
                    dog_tree.add(dog_tree.root, item);
                if(mode == 1)
                    cat_tree.add(cat_tree.root, item);

                //System.out.println("1차 : " + item.getData().category_num);
                //ArrayList<ArrayList<CategoryInfo>> itemList = new ArrayList<ArrayList<CategoryInfo>>();
            }

            //root(1차), Tree의 3차 Depth에 넣음.
            for(int i=0; i<arrCategory2.size(); i++){
                Node<CategoryInfo> nodeItem = new Node<CategoryInfo>();
                nodeItem.setData(arrCategory2.get(i));
                CategoryInfo item = arrCategory2.get(i);

                ArrayList<Node<CategoryInfo>> arrList = null;

                if(mode ==0)
                    arrList = dog_tree.root.getChildList();
                else
                    arrList = cat_tree.root.getChildList();

                String split_num = item.category_num.substring(0,6);// 6자리

                //System.out.println("2차(SPLIT) : " + split_num + "// root.childList.size : " +  arrList.size());

                //Depth : 2의 항목들과 비교교
                for(int j=0; j< arrList.size(); j++){
                    CategoryInfo parentItem = arrList.get(j).getData();

                    //System.out.println("2차 : " + parentItem.category_num + " - " + split_num);

                    if(parentItem.category_num.equals(split_num)){
                        arrList.get(j).addChild(nodeItem);
                        //System.out.println("2차 : " + parentItem.category_num + " - " + item.name+"("+item.category_num+")");
                        break;
                    }
                }
            }


            for(int i=0; i<arrCategory3.size(); i++){
                Node<CategoryInfo> nodeItem = new Node<CategoryInfo>();
                nodeItem.setData(arrCategory3.get(i));
                CategoryInfo item = arrCategory3.get(i);

                String split_num = item.category_num.substring(0, 9);// 6자리

                ArrayList<Node<CategoryInfo>> rootChildList = null;

                if(mode == 0)
                    rootChildList = dog_tree.root.getChildList();
                else
                    rootChildList = cat_tree.root.getChildList();

                for(int j=0; j< rootChildList.size(); j++) {
                    ArrayList<Node<CategoryInfo>> arrList = rootChildList.get(j).getChildList();

                    for (int k = 0; k < arrList.size(); k++) {
                        CategoryInfo parentItem = arrList.get(k).getData();

                        if (parentItem.category_num.equals(split_num)) {
                            arrList.get(j).addChild(nodeItem);
                            System.out.println("3차 : " + parentItem.category_num + " - " + item.name + "(" + item.category_num + ")");
                            break;
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if(mode == 0)
            setCatTree();
    }
}
