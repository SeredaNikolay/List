package com.example.alist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ADate extends AnItem{
    private
        static int nextID;
        static SimpleDateFormat SDFDate=new SimpleDateFormat("yyyy-MM-dd");
        static SimpleDateFormat SDFTime=new SimpleDateFormat("HH:mm:ss");
        static String DateRegExp="\\d{4}-\\d{2}-\\d{2}";
        static String TimeRegExp="\\d{2}:\\d{2}:\\d{2}";
        static String IntNotNegNumRegExp="[\\d]+";
        static List<ADate> ItemList;//new ArrayList<>(Arrays.asList(new ADate(), new ADate(), new ADate(), new ADate(), new ADate(), new ADate(), new ADate(), new ADate(), new ADate(), new ADate()));

        EAvailabilityType Availability=EAvailabilityType.Disabled;
        Date Date=new Date();//InitDate("0001-01-01");
        int ConsumedWater=0;
        int ConsumedKcals=0;
        Date PhysActTime=InitTime("00:00:00");
        Date FreshAirTime=InitTime("00:00:00");;
        Date SleepTime=InitTime("00:00:00");;
        Date DistractionTime=InitTime("00:00:00");

public
    void setNextID(int newNextID){
        nextID=newNextID;
    }
    int getNextID(){
        return nextID;
    }
    List<? extends AnItem> getItemListFromSub(){
        return ItemList;
    }
    void copyTo(AnItem dst){
        ADate dest=(ADate)dst;
        dest.id=id;
        dest.Date= new Date(Date.getTime());
        dest.ConsumedWater=ConsumedWater;
        dest.ConsumedKcals=ConsumedKcals;
        dest.PhysActTime=new Date(PhysActTime.getTime());
        dest.FreshAirTime=new Date(FreshAirTime.getTime());
        dest.SleepTime=new Date(SleepTime.getTime());
        dest.DistractionTime=new Date(DistractionTime.getTime());
    }
    static Date InitDate(String str){
        Date date;
        try{
            date=SDFDate.parse(str);
            return  date;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    static Date InitTime(String str){
        Date time;
        try{
            time=SDFTime.parse(str);
            return  time;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    //===================================Установить из View===================================
    boolean setAvailabilityFromView(EAvailabilityType availability){
        Availability=availability;
        return true;
    }
    boolean setDateFromView(String date){
        if(date.matches(DateRegExp)) {
            try {
                Date = SDFDate.parse(date);
                return true;
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return false;
    }
    boolean setConsumedWaterFromView(String consumedWater){
        if(consumedWater.matches(IntNotNegNumRegExp)) {
            ConsumedWater = Integer.parseInt(consumedWater);
            return true;
        }
        return false;
    }
    boolean setConsumedKcalsFromView(String consumedKcals){
        if(consumedKcals.matches(IntNotNegNumRegExp)) {
            ConsumedKcals = Integer.parseInt(consumedKcals);
            return true;
        }
        return false;
    }
    boolean setPhysActTimeFromView(String physActTime){
        if(physActTime.matches(TimeRegExp)) {
            try {
                PhysActTime = SDFTime.parse(physActTime);
                return true;
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return false;
    }
    boolean setFreshAirTimeFromView(String freshAirTime){
        if(freshAirTime.matches(TimeRegExp)) {
            try {
                FreshAirTime = SDFTime.parse(freshAirTime);
                return true;
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return false;
    }
    boolean setSleepTimeFromView(String sleepTime){
        if(sleepTime.matches(TimeRegExp)) {
            try {
                SleepTime = SDFTime.parse(sleepTime);
                return true;
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return false;
    }
    boolean setDistractionTimeFromView(String distractionTime){
        if(distractionTime.matches(TimeRegExp)) {
            try {
                DistractionTime = SDFTime.parse(distractionTime);
                return true;
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return false;
    }
    //===================================Установить из DBHelper===================================
    void setIDFromDB(int ID){
        id=ID;
    }
    void setAvailabilityFromDB(){
        Availability=EAvailabilityType.Disabled;
    }
    void setDateFromDB(String date){
        try {
            Date = SDFDate.parse(date);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void setConsumedWaterFromDB(int consumedWater){
        ConsumedWater = consumedWater;
    }
    void setConsumedKcalsFromDB(int consumedKcals){
        ConsumedKcals = consumedKcals;
    }
    void setPhysActTimeFromDB(String physActTime){
        try {
            PhysActTime = SDFTime.parse(physActTime);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void setFreshAirTimeFromDB(String freshAirTime){
        try {
            FreshAirTime = SDFTime.parse(freshAirTime);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void setSleepTimeFromDB(String sleepTime){
        try {
            SleepTime = SDFTime.parse(sleepTime);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void setDistractionTimeFromDB(String distractionTime){
        try {
            DistractionTime = SDFTime.parse(distractionTime);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    //===================================Получить в View===================================
    EAvailabilityType getAvailabilityToView(){
        return Availability;
    }
    String getDateToView(){
        return SDFDate.format(Date);
    }
    String getConsumedWaterToView(){
        return Integer.toString(ConsumedWater);
    }
    String getConsumedKcalsToView(){
        return Integer.toString(ConsumedKcals);
    }
    String getPhysActTimeToView(){
        return SDFTime.format(PhysActTime);
    }
    String getFreshAirTimeToView(){
        return SDFTime.format(FreshAirTime);
    }
    String getSleepTimeToView(){
        return SDFTime.format(SleepTime);
    }
    String getDistractionTimeToView(){
        return SDFTime.format(DistractionTime);
    }
    String getListItemCaptionToView(){
        return SDFDate.format(Date);
    }
    //===================================Получить в DBHelper===================================
    int getIDToDB(){
        return id;
    }
    String getDateToDB(){
        return SDFDate.format(Date);
    }
    int getConsumedWaterToDB(){
        return ConsumedWater;
    }
    int getConsumedKcalsToDB(){
        return ConsumedKcals;
    }
    String getPhysActTimeToDB(){
        return SDFTime.format(PhysActTime);
    }
    String getFreshAirTimeToDB(){
        return SDFTime.format(FreshAirTime);
    }
    String getSleepTimeToDB(){
        return SDFTime.format(SleepTime);
    }
    String getDistractionTimeToDB(){
        return SDFTime.format(DistractionTime);
    }
    boolean logicDataCheck(){
        for(ADate date: ItemList){
            if(date.Date.equals(this.Date)){
                return false;
            }
        }
        return true;
    }
    void addToDB(){
        DBHelper dbH=DBHelper.getInstance();
        dbH.addADateToDB(dbH.getWritableDatabase(), this);
    }
    void editInDB(){
        DBHelper dbH=DBHelper.getInstance();
        dbH.editADateInDatabase(dbH.getWritableDatabase(), this);
    }
}
