package com.example.alist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DBHelper extends SQLiteOpenHelper {
    static  DBHelper Instanse=null;
    static String Name="ToDoListDB";

    private DBHelper(Context context) {
        // конструктор суперкласса
        super(context, Name, null, 1);
        if(Instanse==null){
            Instanse=this;
            new ATask().setNextID(this.getNextID(this.getReadableDatabase(),"tasks"));
            new ADate().setNextID(this.getNextID(this.getReadableDatabase(),"dates"));
            ATask.ItemList=this.getDataToATaskList(this.getReadableDatabase(), null);
            ADate.ItemList=this.getDataToADateList(this.getReadableDatabase(), null);
        }
    }
    public static void setInstance(Context context){
        if(Instanse==null)
            Instanse=new DBHelper(context);
    }
    public static DBHelper getInstance(){
        return Instanse;
    }
    //SQLiteDatabase: /data/data/com.example.alist/databases/ToDoListDB
    void initDB(SQLiteDatabase SqlDB){
        SqlDB.execSQL(
                "create table tasks ("
                        + "id integer primary key,"
                        + "state integer not null default 1,"                       //1 - Complete, 2 - Fail, 3 - Hold
                        + "name text not null default 'Задача',"
                        + "start_datetime text not null default '2020-01-01 00:00:00',"
                        + "deadline_datetime text not null default '2020-01-01 01:00:00',"
                        + "really_finish_datetime text not null default '2020-01-01 00:30:00',"
                        + "description text not null default 'Описание задачи'"
                        + ");");
        /*SqlDB.execSQL(
                "insert into tasks(id, state, name, start_datetime, deadline_datetime, really_finish_datetime, description)"
                        +"values"
                        +"(1,"
                        +"1,"
                        +"'Задача по умолчанию',"
                        +"'2000-01-01 12:00:00',"
                        +"'2000-01-01 18:00:00',"
                        +"'2000-01-01 15:00:00',"
                        +"'Описание задачи по умолчанию'),"
                        +"(2,"
                        +"1,"
                        +"'Подзадача по умолчанию',"
                        +"'2000-01-01 13:00:00',"
                        +"'2000-01-01 14:00:00',"
                        +"'2000-01-01 13:30:00',"
                        +"'Описание подзадачи по умолчанию');");*/
        SqlDB.execSQL(
                "create table dates ("
                        + "id integer primary key,"
                        + "date text unique not null default '2020-01-01',"
                        + "consumed_water_ml integer not null default 0,"
                        + "consumed_kcals integer not null default 0,"
                        + "physical_activity_time text not null default '00:00:00',"
                        + "fresh_air_time text not null default '00:00:00',"
                        + "sleep_time text not null default '00:00:00',"
                        + "distraction_time text not null default '00:00:00'"
                        + ");");
        /*SqlDB.execSQL(
                "insert into dates(id, date, consumed_water_ml, consumed_kcals, physical_activity_time, fresh_air_time, sleep_time, distraction_time)"
                        +"values"
                        +"(1,"
                        +"'2000-01-01',"
                        +"1500,"
                        +"1200,"
                        +"'05:00:00',"
                        +"'06:00:00',"
                        +"'06:00:00',"
                        +"'02:00:00');");*/
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        initDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private int getNextID(SQLiteDatabase SqlDB, String tableName){
        String s[]={"MAX(id) as 'id'"};
        Cursor curs= SqlDB.query(tableName, s, null, null, null, null, null);
        if(curs.moveToFirst()){
            int idColInd = curs.getColumnIndex("id");
            return  curs.getInt(idColInd)+1;
        }
        return 1;
    }

    void removeFromTable(SQLiteDatabase SqlDB, String tableName, int id){
        String[] args={Integer.toString(id)};
        SqlDB.delete("tableName", "id=?", args);
        this.close();
    }
    void editATaskInDatabase(SQLiteDatabase SqlDB, ATask task){
        SqlDB.execSQL("update tasks set"
                +" state="+task.getStateToDB()+","
                +" name='"+task.getNameToDB()+"',"
                +" start_datetime='"+task.getStartDatetimeToDB()+"',"
                +" deadline_datetime='"+task.getFinishDatetimeToDB()+"',"
                +" really_finish_datetime='"+task.getReallyFinishDatetimeToDB()+"',"
                +" description='"+task.getDescriptionToDB()+"'"
                +" where id="+task.getIDToDB()+";");
    }
    void editADateInDatabase(SQLiteDatabase SqlDB, ADate date){
        SqlDB.execSQL("update dates set"
                +" date="+date.getDateToDB()+","
                +" consumed_water_ml='"+date.getConsumedWaterToDB()+"',"
                +" consumed_kcals='"+date.getConsumedKcalsToDB()+"',"
                +" physical_activity_time='"+date.getPhysActTimeToDB()+"',"
                +" fresh_air_time='"+date.getFreshAirTimeToDB()+"',"
                +" sleep_time='"+date.getSleepTimeToDB()+"',"
                +" distraction_time='"+date.getDistractionTimeToDB()+"',"
                +" where id="+date.getIDToDB()+";");
    }
    void addATaskToDB(SQLiteDatabase SqlDB, ATask task){
        ContentValues cv = new ContentValues();
        cv.put("id", task.getIDToDB());
        cv.put("state", task.getStateToDB());
        cv.put("name", task.getNameToDB());
        cv.put("start_datetime",task.getStartDatetimeToDB());
        cv.put("deadline_datetime", task.getFinishDatetimeToDB());
        cv.put("really_finish_datetime", task.getReallyFinishDatetimeToDB());
        cv.put("description", task.getDescriptionToDB());
        SqlDB.insert("tasks", null, cv);
    }
    void addADateToDB(SQLiteDatabase SqlDB, ADate date){
        ContentValues cv = new ContentValues();
        cv.put("id", date.getIDToDB());
        cv.put("date", date.getDateToDB());
        cv.put("consumed_water_ml", date.getConsumedWaterToDB());
        cv.put("consumed_kcals",date.getConsumedKcalsToDB());
        cv.put("physical_activity_time", date.getPhysActTimeToDB());
        cv.put("fresh_air_time", date.getFreshAirTimeToDB());
        cv.put("sleep_time", date.getSleepTimeToDB());
        cv.put("distraction_time", date.getDistractionTimeToDB());
        SqlDB.insert("dates", null, cv);
    }
    List<ATask> getDataToATaskList(SQLiteDatabase SqlDB, String whereStr){
        List<ATask> taskList=new ArrayList<>();
        Cursor curs = SqlDB.query("tasks", null, whereStr, null, null, null, "deadline_datetime desc");
        if (curs.moveToFirst()) {
            int idColInd = curs.getColumnIndex("id");
            int stateColInd = curs.getColumnIndex("state");
            int nameColInd = curs.getColumnIndex("name");
            int startDatetimeColInd = curs.getColumnIndex("start_datetime");
            int deadlineDatetimeColInd = curs.getColumnIndex("deadline_datetime");
            int reallyFinishDatetimeColInd = curs.getColumnIndex("really_finish_datetime");
            int descriptionColInd = curs.getColumnIndex("description");

            do {
                ATask task = new ATask();
                task.setIDFromDB(curs.getInt(idColInd));
                task.setStateFromDB(curs.getInt(stateColInd));
                task.setNameFromDB(curs.getString(nameColInd));
                task.setStartDatetimeFromDB(curs.getString(startDatetimeColInd));
                task.setFinishDatetimeFromDB(curs.getString(deadlineDatetimeColInd));
                task.setReallyFinishDatetimeFromDB(curs.getString(reallyFinishDatetimeColInd));
                task.setDescriptionFromDB(curs.getString(descriptionColInd));
                taskList.add(task);
            } while (curs.moveToNext());
        }
        curs.close();
        this.close();
        return taskList;
    }
    List<ADate> getDataToADateList(SQLiteDatabase SqlDB, String whereStr){
        List<ADate> dateList=new ArrayList<>();
        Cursor curs = SqlDB.query("dates", null, whereStr, null, null, null, "date desc");
        if (curs.moveToFirst()) {
            int idColInd = curs.getColumnIndex("id");
            int dateColInd = curs.getColumnIndex("date");
            int consumedWaterMlColInd = curs.getColumnIndex("consumed_water_ml");
            int consumedKcalsColInd = curs.getColumnIndex("consumed_kcals");
            int physicalActivityTimeColInd = curs.getColumnIndex("physical_activity_time");
            int freshAirTimeColInd = curs.getColumnIndex("fresh_air_time");
            int sleepTimeColInd = curs.getColumnIndex("sleep_time");
            int distractionTimeColInd = curs.getColumnIndex("distraction_time");

            do {
                ADate date = new ADate();
                date.setIDFromDB(curs.getInt(idColInd));
                date.setAvailabilityFromView(EAvailabilityType.Disabled);
                date.setDateFromDB(curs.getString(dateColInd));
                date.setConsumedWaterFromDB(curs.getInt(consumedWaterMlColInd));
                date.setConsumedKcalsFromDB(curs.getInt(consumedKcalsColInd));
                date.setPhysActTimeFromDB(curs.getString(physicalActivityTimeColInd));
                date.setFreshAirTimeFromDB(curs.getString(freshAirTimeColInd));
                date.setSleepTimeFromDB(curs.getString(sleepTimeColInd));
                date.setDistractionTimeFromDB(curs.getString(distractionTimeColInd));
                dateList.add(date);
            } while (curs.moveToNext());
        }
        curs.close();
        this.close();
        return dateList;
    }
    void clearDB(SQLiteDatabase SqlDB){
        SqlDB.delete("tasks_to_tasks", null, null);
        SqlDB.delete("tasks", null, null);
        SqlDB.delete("dates", null, null);
        close();
    }
}