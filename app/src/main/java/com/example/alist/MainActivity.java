package com.example.alist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum EListType
{
    Task,
    Date
};

public class MainActivity extends AppCompatActivity {
private
    static MainActivity Instance;
    static int ListItemID;
    static String WhereStr=null;
    static EListType ListType=null;
    static Intent TaskIntent;
    static Intent DateIntent;
    static List<String> ListCaption=new ArrayList<>();
    static List<Integer> ListCaptionID=new ArrayList<>();
    static ArrayAdapter<String> Adapter;
    static ListView ItemView;
    static EditText SearchItem;
    OnClickListener buttonClickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            onButtonClick(v);
        }
    };
    OnItemClickListener listItemClickListener=new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
            onListItemClick(parent, itemClicked, position, id);
        }
    };
    private List<String> getItemsListLike(String str, List<String> strList){
        List<String> resList= new ArrayList<>();
        for(String s: strList) {
            if (s.contains(str))
                resList.add(s);
        }
        return resList;
    }
    private void onSearchButtonClicked() {
        List<String> lst;
        String searchText=((EditText)findViewById(R.id.SearchItem)).getText().toString();
        if(searchText.isEmpty()) {
            updateListType(ListType);
        }else{
            lst=getItemsListLike(searchText, ListCaption);
            updateAdapter(lst);
        }
    }
    private void onButtonClick(View v) {
        int btnID=v.getId();
        switch (btnID) {
            case R.id.SearchButton:
                if(SearchItem.getText().toString().isEmpty())
                    WhereStr=null;
                onSearchButtonClicked();
                break;
            case R.id.TaskButton:
                WhereStr=null;
                //updateListType(EListType.Task);
                setListType(EListType.Task, WhereStr);
                break;
            case R.id.CreateButton:
                //DBHelper.getInstance().clearDB(DBHelper.getInstance().getWritableDatabase());
                WhereStr=null;
                onCreateButtonClicked();
                setListType(ListType, WhereStr);
                break;
            case R.id.DateButton:
                WhereStr=null;
                //updateListType(EListType.Date);
                setListType(EListType.Date, WhereStr);
                break;
        }
    }
    private void setActivity(int itemID){
        ListItemID = itemID;
        if(ListType==EListType.Task){
            startActivity(TaskIntent);
        }else{
            startActivity(DateIntent);
        }
    }
    private void onCreateButtonClicked() {
        setActivity(0);
    }
    private void onListItemClick(AdapterView<?> parent, View itemClicked, int position, long id){
        setActivity(ListCaptionID.get(position));
    }

    static void setListType(EListType listType, String whereStr){
        DBHelper dbH=DBHelper.getInstance();
        if(listType==EListType.Task) {
            ATask.ItemList = dbH.getDataToATaskList(dbH.getReadableDatabase(), whereStr);
        }else {
            ADate.ItemList = dbH.getDataToADateList(dbH.getReadableDatabase(), whereStr);
        }
        getInstance().updateListType(listType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        findViewById(R.id.SearchButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.TaskButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.CreateButton).setOnClickListener(buttonClickListener);
        findViewById(R.id.DateButton).setOnClickListener(buttonClickListener);

        ItemView = findViewById(R.id.ItemsList);
        SearchItem=findViewById(R.id.SearchItem);
        ItemView.setOnItemClickListener(listItemClickListener);

        if(ListType==null) {
            DBHelper.setInstance(this);
            ListType=EListType.Task;
            TaskIntent = new Intent(this, TaskActivity.class);
            DateIntent = new Intent(this, DateActivity.class);
        }
        setInstance(this);
        setListType(ListType, WhereStr);
    }
    public static void setInstance(MainActivity instance) {
        Instance = instance;
    }
    public static MainActivity getInstance() {
        if(Instance==null){
            setInstance(new MainActivity());
        }
        return Instance;
    }
private
         void setButtonsEnabled(EListType curListType){
            if(curListType==EListType.Task) {
                findViewById(R.id.TaskButton).setEnabled(false);
                findViewById(R.id.DateButton).setEnabled(true);
            }else{
                findViewById(R.id.TaskButton).setEnabled(true);
                findViewById(R.id.DateButton).setEnabled(false);
            }
}
public
        void updateListCaption(){
            if(ListType==EListType.Task){
                ListCaption=AnItem.getItemListCaptions(new ATask());
                ListCaptionID=AnItem.getItemListCaptionsID(new ATask());
            }else if(ListType==EListType.Date){
                ListCaption=AnItem.getItemListCaptions(new ADate());
                ListCaptionID=AnItem.getItemListCaptionsID(new ADate());
            }
        }
        void updateAdapter(List<String> lst){
            Adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lst);
            ItemView.setAdapter(Adapter);
            Adapter.notifyDataSetChanged();
        }
        void updateListType(EListType lstType){
            ListType=lstType;
            updateListCaption();
            updateAdapter(ListCaption);
            setButtonsEnabled(lstType);
        }
}