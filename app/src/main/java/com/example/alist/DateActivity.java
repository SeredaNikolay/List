package com.example.alist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

enum EAvailabilityType{
    Enabled,
    Disabled
}

public class DateActivity extends AppCompatActivity {
    static EAvailabilityType AvailabilityType=null;

    private void setWidgetsEnabled(boolean enabled){
        findViewById(R.id.EditDate).setEnabled(enabled);
        findViewById(R.id.EditConsumedWater).setEnabled(enabled);
        findViewById(R.id.EditConsumedKcals).setEnabled(enabled);
        findViewById(R.id.EditPhysActTime).setEnabled(enabled);
        findViewById(R.id.EditFreshAirTime).setEnabled(enabled);
        findViewById(R.id.EditSleepTime).setEnabled(enabled);
        findViewById(R.id.EditDistractionTime).setEnabled(enabled);
        findViewById(R.id.RecordDateButton).setEnabled(enabled);
    }

    private void setEnabled(EAvailabilityType availability){
        if(availability==EAvailabilityType.Enabled) {
            setWidgetsEnabled(true);
        }else{
            setWidgetsEnabled(false);
        }
    }

    private void setAvailabilityType(EAvailabilityType availability){
        AvailabilityType=availability;
        setEnabled(availability);
    }

    OnClickListener clickListener= new OnClickListener() {
        @Override
        public void onClick(View v) {
            onButtonClick(v);
        }
    };

    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.RecordDateButton:
                onSaveButtonClicked();
                break;
            case R.id.TasksButton:
                onTasksButtonClicked();
                break;
        }
    }
    private void saveDate(){
        ADate date= new ADate();
        MainActivity ma=MainActivity.getInstance();
        date.setAvailabilityFromView(EAvailabilityType.Disabled);
        setAvailability(EAvailabilityType.Disabled);
        if(!addADate(date)){
            date.setAvailabilityFromView(EAvailabilityType.Enabled);
            setAvailability(EAvailabilityType.Enabled);
        }
        ma.updateListType(ma.ListType);
    }
    void onTasksButtonClicked(){
        ADate date=(ADate)AnItem.getItemByID(new ADate(), MainActivity.ListItemID);
        if(date!=null) {
            MainActivity.WhereStr="('"+date.getDateToDB()+"' between start_datetime and deadline_datetime) or (start_datetime like '"+date.getDateToDB()+"%');";
            MainActivity.ListType=EListType.Task;
            startActivity(new Intent(this, MainActivity.class));
        }else{
            Toast.makeText(this, "Необходимо сохранить запись", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSaveButtonClicked() {
        saveDate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_activity);

        findViewById(R.id.RecordDateButton).setOnClickListener(clickListener);
        findViewById(R.id.TasksButton).setOnClickListener(clickListener);

        if(AvailabilityType==null)
            AvailabilityType=EAvailabilityType.Enabled;
        setAvailabilityType(AvailabilityType);

        ADate date=(ADate)AnItem.getItemByID(new ADate(), MainActivity.ListItemID);
        if(date==null){
            date=new ADate();
            date.setAvailabilityFromView(EAvailabilityType.Enabled);
        }
        setFieldsFromADate(date);
    }
    //===================================Установить===================================
    public void setAvailability(EAvailabilityType avType){
        AvailabilityType=avType;
        setEnabled(avType);
    }
    public void setDate(String date){
        ((EditText)findViewById(R.id.EditDate)).setText(date);
    }
    public void setConsumedWater(String consumedWater){
        ((EditText)findViewById(R.id.EditConsumedWater)).setText(consumedWater);
    }
    public void setConsumedKcals(String consumedKcals){
        ((EditText)findViewById(R.id.EditConsumedKcals)).setText(consumedKcals);
    }
    public void setPhysActTime(String physActTime){
        ((EditText)findViewById(R.id.EditPhysActTime)).setText(physActTime);
    }
    public void setFreshAirTime(String freshAirTime) {
        ((EditText)findViewById(R.id.EditFreshAirTime)).setText(freshAirTime);
    }
    public void setSleepTime(String sleepTime) {
        ((EditText)findViewById(R.id.EditSleepTime)).setText(sleepTime);
    }
    public void setDistractionTime(String distractionTime) {
        ((EditText)findViewById(R.id.EditDistractionTime)).setText(distractionTime);
    }

    boolean isCorrectData(boolean dontShow, String msgText){
        if(!dontShow)
            Toast.makeText(this, msgText, Toast.LENGTH_SHORT).show();
        return dontShow;
    }
    void setFieldsFromADate(ADate date){
        setAvailability(date.getAvailabilityToView());
        setDate(date.getDateToView());
        setConsumedWater(date.getConsumedWaterToView());
        setConsumedKcals(date.getConsumedKcalsToView());
        setPhysActTime(date.getPhysActTimeToView());
        setFreshAirTime(date.getFreshAirTimeToView());
        setSleepTime(date.getSleepTimeToView());
        setDistractionTime(date.getDistractionTimeToView());
    }

    boolean trySetFieldsToADate(ADate date){
        boolean correctData=true;
        ADate reserveDate=new ADate();
        date.copyTo(reserveDate);
        correctData&=isCorrectData(date.setAvailabilityFromView(getAvailability()), "Доступность указана неверно");
        correctData&=isCorrectData(date.setDateFromView(getDate()), "Дата указана неверно");
        correctData&=isCorrectData(date.setConsumedWaterFromView(getConsumedWater()), "Вода указана неверно");
        correctData&=isCorrectData(date.setConsumedKcalsFromView(getConsumedKcals()), "Калории указаны неверно");
        correctData&=isCorrectData(date.setPhysActTimeFromView(getPhysActTime()), "Физическая активность указана неверно");
        correctData&=isCorrectData(date.setFreshAirTimeFromView(getFreshAirTime()), "Свежий воздух указан неверно");
        correctData&=isCorrectData(date.setSleepTimeFromView(getSleepTime()), "Сон указан неверно");
        correctData&=isCorrectData(date.setDistractionTimeFromView(getDistractionTime()), "Отвлечения указаны неверно");
        correctData&=isCorrectData(date.logicDataCheck(), "Такая дата уже существует");
        if(!correctData){
            reserveDate.copyTo(date);
        }
        return correctData;
    }
    boolean addADate(ADate date){
        boolean isStated;
        isStated = trySetFieldsToADate(date);
        if(isStated){
            date.setReallyIDFromView();
            List<ADate> dateList = (List<ADate>) AnItem.getItemList(new ADate());
            dateList.add(date);
            date.addToDB();
            MainActivity.ListItemID = date.getIDToView();
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }
        return isStated;
    }
    //===================================Получить===================================
    public EAvailabilityType getAvailability(){
        return AvailabilityType;
    }
    public String getDate(){
        return ((EditText)findViewById(R.id.EditDate)).getText().toString();
    }
    public String getConsumedWater(){
        return ((EditText)findViewById(R.id.EditConsumedWater)).getText().toString();
    }
    public String getConsumedKcals(){
        return ((EditText)findViewById(R.id.EditConsumedKcals)).getText().toString();
    }
    public String getPhysActTime(){
        return ((EditText)findViewById(R.id.EditPhysActTime)).getText().toString();
    }
    public String getFreshAirTime(){
        return ((EditText)findViewById(R.id.EditFreshAirTime)).getText().toString();
    }
    public String getSleepTime(){
        return ((EditText)findViewById(R.id.EditSleepTime)).getText().toString();
    }
    public String getDistractionTime(){
        return ((EditText)findViewById(R.id.EditDistractionTime)).getText().toString();
    }

    public static void removeTmpTask(){
        AnItem.clearTMPRecord(new ADate());
    }
}