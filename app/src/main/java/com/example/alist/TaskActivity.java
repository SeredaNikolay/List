package com.example.alist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

enum ECompletionType
{
    Complete,
    Fail,
    Hold
};
public class TaskActivity extends AppCompatActivity {

    static private ECompletionType CompletionType=null;
    private void setWidgetsEnabled(boolean enabled){
        findViewById(R.id.CompleteButton).setEnabled(enabled);
        findViewById(R.id.FailButton).setEnabled(enabled);
        findViewById(R.id.SaveButton).setEnabled(enabled);
        findViewById(R.id.EditName).setEnabled(enabled);
        findViewById(R.id.EditStart).setEnabled(enabled);
        findViewById(R.id.EditFinish).setEnabled(enabled);
        findViewById(R.id.EditDescription).setEnabled(enabled);
    }
    private void setEnabled(ECompletionType curCompType) {
        if (curCompType == ECompletionType.Complete || curCompType == ECompletionType.Fail) {
            Date datetime=new Date();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            setReallyFinishDatetime(sdf.format(datetime));
            setWidgetsEnabled(false);
        } else {
            setWidgetsEnabled(true);
        }
    }

    private void setStateViewText(ECompletionType compType) {
        TextView tv=findViewById(R.id.StateView);
        if(compType==ECompletionType.Complete){
            tv.setText("Состояние: выполнено");
        }else if(compType==ECompletionType.Fail){
            tv.setText("Состояние: провалено");
        }else if(compType==ECompletionType.Hold){
            tv.setText("Состояние: выполняется");
        }
    }

    OnClickListener clickListener= new OnClickListener() {
        @Override
        public void onClick(View v) {
            onButtonClick(v);
        }
    };
    private void finishTask(String state, ECompletionType compType){
        boolean isOk;
        ATask task=(ATask)AnItem.getItemByID(new ATask(), MainActivity.ListItemID);
        if(task==null){
            task=new ATask();
        }
        MainActivity ma=MainActivity.getInstance();
        task.setStateFromView(state);
        setState(compType);
        if(MainActivity.ListItemID==0){
            isOk=addATask(task);
        }else{
            isOk=editATask(task);
        }
        if(!isOk){
            task.setStateFromView("выполняется");
            setState(ECompletionType.Hold);
        }
        ma.updateListType(ma.ListType);
    }
    private void saveTask(){
        MainActivity ma=MainActivity.getInstance();
        ATask task=(ATask)AnItem.getItemByID(new ATask(), MainActivity.ListItemID);
        if(task==null){
            task=new ATask();
        }
        if(MainActivity.ListItemID==0){
            addATask(task);
        }else{
            editATask(task);
        }
        ma.updateListType(ma.ListType);
    }
    private void onSaveButtonClicked() {
        saveTask();
    }
    private void onCompleteButtonClicked() {
        finishTask("выполнено", ECompletionType.Complete);
    }
    private void onFailButtonClicked() {
        finishTask("провалено", ECompletionType.Fail);
    }
    private void onDatesButton(){
        ATask task=(ATask)AnItem.getItemByID(new ATask(), MainActivity.ListItemID);
        if(task!=null) {
            String[] dates={task.getStartDatetimeToView(), task.getFinishDatetimeToDB()};
            dates[0]=dates[0].substring(0, dates[0].indexOf(' '));
            dates[1]=dates[1].substring(0, dates[1].indexOf(' '));
            MainActivity.ListType=EListType.Date;
            MainActivity.WhereStr="date between '" + dates[0] + "' and '" + dates[1]+"'";
            startActivity(new Intent(this, MainActivity.class));
        }else{
            Toast.makeText(this, "Необходимо сохранить запись", Toast.LENGTH_SHORT).show();
        }
    }
    private void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.CompleteButton:
                onCompleteButtonClicked();
                break;
            case R.id.SaveButton:
                onSaveButtonClicked();
                break;
            case R.id.FailButton:
                onFailButtonClicked();
                break;
            case R.id.DatesButton:
                onDatesButton();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity);

        findViewById(R.id.CompleteButton).setOnClickListener(clickListener);
        findViewById(R.id.SaveButton).setOnClickListener(clickListener);
        findViewById(R.id.FailButton).setOnClickListener(clickListener);
        findViewById(R.id.DatesButton).setOnClickListener(clickListener);

        ATask task=(ATask)AnItem.getItemByID(new ATask(), MainActivity.ListItemID);
        if(task==null){
            task=new ATask();
        }
        setFieldsFromATask(task);

    }
    //===================================Установить===================================
public
    void setState(ECompletionType compType){
        CompletionType=compType;
        setEnabled(compType);
        setStateViewText(compType);
    }
    void setTaskName(String taskName){
        ((EditText)findViewById(R.id.EditName)).setText(taskName);
    }
    void setStartDatetime(String datetime){
        ((EditText)findViewById(R.id.EditStart)).setText(datetime);
    }
    void setFinishDatetime(String datetime){
        ((EditText)findViewById(R.id.EditFinish)).setText(datetime);
    }
    void setReallyFinishDatetime(String datetime){
        TextView tv=findViewById(R.id.ReallyFinishCaptionView);
        if(datetime.equals("0001-01-01 00:00:00")) {
            tv.setText("Окончание выполнения задачи: отсутствует");
        }else {
            tv.setText("Окончание выполнения задачи: "+datetime);
        }
    }
    void setDescription(String description) {
        ((EditText)findViewById(R.id.EditDescription)).setText(description);
    }
    void setFieldsFromATask(ATask task){
        setState(task.getStateToView());
        setTaskName(task.getNameToView());
        setStartDatetime(task.getStartDatetimeToView());
        setFinishDatetime(task.getFinishDatetimeToView());
        setReallyFinishDatetime(task.getReallyFinishDatetimeToView());
        setDescription(task.getDescriptionToView());
        //Toast.makeText(this, Integer.toString(task.getIDToDB()), Toast.LENGTH_SHORT).show();
    }
    boolean isCorrectData(boolean dontShow, String msgText){
        if(!dontShow)
            Toast.makeText(this, msgText, Toast.LENGTH_SHORT).show();
        return dontShow;
    }
    boolean trySetFieldsToATask(ATask task){
        boolean correctData=true;
        ATask reserveTask=new ATask();
        task.copyTo(reserveTask);
        correctData&=isCorrectData(task.setStateFromView(getState()), "Состояние указано неверно");
        correctData&=isCorrectData(task.setNameFromView(getTaskName()), "Название указано неверно");
        correctData&=isCorrectData(task.setStartDatetimeFromView(getStartDatetime()), "Время начала указано неверно");
        correctData&=isCorrectData(task.setFinishDatetimeFromView(getFinishDatetime()), "Дедлайн указан неверно");
        correctData&=isCorrectData(task.setReallyFinishDatetimeFromView(getReallyFinishDatetime()), "Время окончания указано неверно");
        correctData&=isCorrectData(task.setDescriptionFromView(getDescription()), "Описание указано неверно");
        correctData&=isCorrectData(task.logicDataCheck(), "Введены нереальные данные");
        if(!correctData){
            reserveTask.copyTo(task);
        }
        return correctData;
    }
    boolean addATask(ATask task){
        boolean isStated;
        task.setTmpIDFromView(new ATask());
        isStated = trySetFieldsToATask(task);
        if(isStated){
            task.setReallyIDFromView();
            List<ATask> taskList = (List<ATask>) AnItem.getItemList(new ATask());
            taskList.add(task);
            task.addToDB();
            MainActivity.ListItemID = task.getIDToView();
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }
        return isStated;
    }
    boolean editATask(ATask task){
        boolean isStated;
        isStated=trySetFieldsToATask(task);
        if(isStated){
            task.editInDB();
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }
        return isStated;
    }
    //===================================Получить===================================
    String getState(){
        String data=((TextView)findViewById(R.id.StateView)).getText().toString();
        return data.substring(data.indexOf(':')+2);
    }
    String getTaskName(){
        return ((EditText)findViewById(R.id.EditName)).getText().toString();
    }
    String getStartDatetime(){
        return ((EditText)findViewById(R.id.EditStart)).getText().toString();
    }
    String getFinishDatetime(){
        return ((EditText)findViewById(R.id.EditFinish)).getText().toString();
    }
    String getReallyFinishDatetime(){
        String data=((TextView)findViewById(R.id.ReallyFinishCaptionView)).getText().toString();
        return data.substring(data.indexOf(':')+2);
    }
    String getDescription(){
        return ((TextView)findViewById(R.id.EditDescription)).getText().toString();
    }
}
