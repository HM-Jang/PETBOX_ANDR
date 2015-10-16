package com.petbox.shop.DataStructure.Tree;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-10-16.
 */
public class Node <T> {

    public int depth;
    public T data;
    public ArrayList<Node<T>> childList;
    public Node<T> parent;


    public void setData(T data)
    {
        this.data = data;
        childList = new ArrayList<Node<T>>();
    }

    public T getData(){
        return data;
    }

    public void addChild(Node<T> child){
        child.parent = this;
        childList.add(child);
    }

    public void addChild(int num, Node<T> child){
        child.parent = this;
        childList.add(num, child);
    }

    /*
    public void addChild(Node<T> child, int parent_depth){

        if(childList == null){
            childList = new ArrayList<Node<T>>();
        }

        child.depth = parent_depth+1;
        childList.add(child);
    }

    public void addChild(int num, Node<T> child , int parent_depth){

        if(childList == null){
            childList = new ArrayList<Node<T>>();
        }

        child.depth = parent_depth+1;
        childList.add(num, child);
    }
    */
    public ArrayList<Node<T>> getChildList(){
        return childList;
    }

}
