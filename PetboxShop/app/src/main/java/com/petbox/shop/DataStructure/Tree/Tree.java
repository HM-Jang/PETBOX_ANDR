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




}
