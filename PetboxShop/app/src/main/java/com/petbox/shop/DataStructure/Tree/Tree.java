package com.petbox.shop.DataStructure.Tree;

import java.util.ArrayList;

/**
 * Created by petbox on 2015-10-16.
 */
public class Tree<T> {

    public int depth = 1;
    public Node<T> root;

    public void init(Node<T> root){
        setRoot(root);
    }

    public void  setRoot(Node<T> root){
        this.root = root;
    }

    public void add(Node<T> parent, Node<T> child){
            child.depth = parent.depth+1;
            parent.addChild(child);

            if(depth < parent.depth)
                depth = parent.depth;

    }

    public Node<T> search(String content){
        Node<T> node = new Node<T>();

        int search_depth = 0;

        switch(content.length()){
            case 6:
                search_depth = 1;
                break;
            case 9:
                search_depth = 2;
                break;
            case 12:
                search_depth = 3;
                break;
        }

        return node;
    }






}
