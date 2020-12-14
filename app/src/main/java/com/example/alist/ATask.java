package com.example.alist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ATask extends AnItem{
    public
        static int nextID;
        static SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        static SimpleDateFormat SDFDate=new SimpleDateFormat("yyyy-MM-dd");
        static String DatetimeRegExp="\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
        static String TextRegExp="[\\da-zA-Zа-яёА-ЯЁ ,\\.\t\r\n]+";
        static List<ATask> ItemList;//new ArrayList<>(Arrays.asList(new ATask(), new ATask(), new ATask(), new ATask(), new ATask(), new ATask(), new ATask(), new ATask(),  new ATask(), new ATask()));

        ECompletionType State=ECompletionType.Hold;
        String Name="Задача";
        Date StartDatetime=new Date();//InitDatetime("2020-01-01 00:00:00");
        Date FinishDatetime=new Date();//InitDatetime("2020-01-02 00:00:00");
        Date ReallyFinishDatetime=InitDatetime("0001-01-01 00:00:00");
        String Description="Описание";


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
            ATask dest=(ATask)dst;
            dest.id=id;
            dest.State=State;
            dest.Name=Name;
            dest.StartDatetime=new Date(StartDatetime.getTime());
            dest.FinishDatetime=new Date(FinishDatetime.getTime());
            dest.ReallyFinishDatetime=new Date(ReallyFinishDatetime.getTime());
            dest.Description=Description;
        }
        static Date InitDatetime(String str){
            Date date;
            try{
                date=SDF.parse(str);
                return  date;
            }catch (Exception e){
                System.out.println(e);
            }
            return null;
        }

    //===================================Установить из View===================================
        boolean setStateFromView(String state){
            if(state.equals("выполнено")){
                State=ECompletionType.Complete;
            }else if(state.equals("провалено")){
                State=ECompletionType.Fail;
            }else if(state.equals("выполняется")){
                State=ECompletionType.Hold;
            }
            return true;
        }
        boolean setNameFromView(String name){
            if(name.matches(TextRegExp)) {
                Name = name;
                return true;
            }
            return false;
        }
        boolean setStartDatetimeFromView(String startDatetime){
            if(startDatetime.matches(DatetimeRegExp)) {
                try {
                    StartDatetime = SDF.parse(startDatetime);
                    return true;
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            return false;
        }
        boolean setFinishDatetimeFromView(String finishDatetime){
            if(finishDatetime.matches(DatetimeRegExp)) {
                try {
                    FinishDatetime = SDF.parse(finishDatetime);
                    return true;
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            return false;
        }
        boolean setReallyFinishDatetimeFromView(String reallyFinishDatetime){
            if(reallyFinishDatetime.equals("отсутствует")){
                ReallyFinishDatetime=InitDatetime("0001-01-01 00:00:00");
                return true;
            }
            if(reallyFinishDatetime.matches(DatetimeRegExp)) {
                try {
                    ReallyFinishDatetime = SDF.parse(reallyFinishDatetime);
                    return true;
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            return false;
        }
        boolean setDescriptionFromView(String description){
            if(description.matches(TextRegExp)) {
                Description = description;
                return true;
            }
            return false;
        }
    //===================================Установить из DBHelper===================================
    void setIDFromDB(int ID){
        id=ID;
    }
    void setStateFromDB(int state){
        if(state==1){
            State=ECompletionType.Complete;
        }else if(state==2){
            State=ECompletionType.Fail;
        }else if(state==3){
            State=ECompletionType.Hold;
        }
    }
    void setNameFromDB(String name){
        Name=name;
    }
    void setStartDatetimeFromDB(String startDatetime){
        try {
            StartDatetime = SDF.parse(startDatetime);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void setFinishDatetimeFromDB(String finishDatetime){
        try {
            FinishDatetime = SDF.parse(finishDatetime);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void setReallyFinishDatetimeFromDB(String reallyFinishDatetime){
        try {
            ReallyFinishDatetime = SDF.parse(reallyFinishDatetime);;
        }catch (Exception e){
            System.out.println(e);
        }
    }
    void setDescriptionFromDB(String description){
        Description = description;
    }
    //===================================Получить в View===================================
        ECompletionType getStateToView(){
            return State;
        }
        String getNameToView(){
            return Name;
        }
        String getStartDatetimeToView(){
            return SDF.format(StartDatetime);
        }
        String getFinishDatetimeToView(){
            return SDF.format(FinishDatetime);
        }
        String getReallyFinishDatetimeToView(){
            return SDF.format(ReallyFinishDatetime);
        }
        String getDescriptionToView(){
            return Description;
        }
        String getListItemCaptionToView(){
            return SDFDate.format(FinishDatetime)+"       "+Name;
        }
    //===================================Получить в DBHelper===================================
    int getIDToDB(){
        return id;
    }
    int getStateToDB(){
        if(State==ECompletionType.Complete){
            return 1;
        }else if(State==ECompletionType.Fail){
            return 2;
        }else if(State==ECompletionType.Hold){
            return 3;
        }
        return 0;
    }
    String getNameToDB(){
        return Name;
    }
    String getStartDatetimeToDB(){
        return SDF.format(StartDatetime);
    }
    String getFinishDatetimeToDB(){
        return SDF.format(FinishDatetime);
    }
    String getReallyFinishDatetimeToDB(){
        return SDF.format(ReallyFinishDatetime);
    }
    String getDescriptionToDB(){
        return Description;
    }

    boolean logicDataCheck(){
        boolean start_LE_finish=StartDatetime.getTime()<=FinishDatetime.getTime();
        boolean relFinish_GE_start=ReallyFinishDatetime.getTime()>=StartDatetime.getTime()||SDF.format(ReallyFinishDatetime).equals("0001-01-01 00:00:00");
        return start_LE_finish&&relFinish_GE_start;
    }
    void addToDB(){
        DBHelper dbH=DBHelper.getInstance();
        dbH.addATaskToDB(dbH.getWritableDatabase(), this);
    }
    void editInDB(){
        DBHelper dbH=DBHelper.getInstance();
        dbH.editATaskInDatabase(dbH.getWritableDatabase(), this);
    }
}
