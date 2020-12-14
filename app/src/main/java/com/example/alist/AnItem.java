package com.example.alist;

import android.app.LauncherActivity;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public abstract class AnItem{
protected
    int id=0;

public
    static List<? extends AnItem> getItemList(AnItem anItem){
        return anItem.getItemListFromSub();
    }
    static AnItem getItemByID(AnItem anItem, int id){
        for(AnItem item: getItemList(anItem)){
            if(item.id==id)
                return item;
        }
        return null;
    }
    static boolean isUniqueID(AnItem anItem, int id){
        return getItemByID(anItem, id)==null;
    }
    boolean setTmpIDFromView(AnItem anItem) {
        if(isUniqueID(anItem,0)) {
            id = 0;
            return true;
        }
        return false;
    }
    static void clearTMPRecord(AnItem anItem){
        for(AnItem item: getItemList(anItem)){
            if(item.id==0) {
                getItemList(anItem).remove(item);
                break;
            }
        }
    }
    void setReallyIDFromView(){
        id=getNextID();
        setNextID(id+1);
    }

public
    static List<String> getItemListCaptions(AnItem anItem){
        List<String> ItemListCaption=new ArrayList<>();
        for(AnItem item: getItemList(anItem)){
            ItemListCaption.add(item.getListItemCaptionToView());
        }
        return ItemListCaption;
    }
    static List<Integer> getItemListCaptionsID(AnItem anItem){
        List<Integer> ItemListCaptionsID=new ArrayList<>();
        for(AnItem item: getItemList(anItem)){
            ItemListCaptionsID.add(item.getIDToView());
        }
        return ItemListCaptionsID;
    }
    Integer getIDToView(){
        return id;
    }

    abstract int getNextID();
    abstract void setNextID(int newNextID);

    abstract String getListItemCaptionToView();
    abstract void copyTo(AnItem dst);
    abstract boolean logicDataCheck();

    abstract List<? extends AnItem> getItemListFromSub();
}
