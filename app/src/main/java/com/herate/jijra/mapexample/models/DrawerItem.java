package com.herate.jijra.mapexample.models;

/**
 * Created by jijra on 28.7.2015.
 */
public class DrawerItem {
    String ItemName;
    int imgResID;

    public DrawerItem(String itemName, int imgResID){
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }
    public String getItemName(){
        return ItemName;
    }
    public void setItemName(String itemName){
        ItemName = itemName;
    }
    public int getImgResID(){
        return imgResID;
    }
    public void setImgResID(int imgResID){
        this.imgResID = imgResID;
    }
}
