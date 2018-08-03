package com.example.android.mytasks.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.mytasks.Models.Task;
import com.example.android.mytasks.R;
import com.example.android.mytasks.TasksWidgetProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by SG on 7/31/2018.
 */

public class UpdateTaskService  extends IntentService {

    public static final String Widget_AddTask= "WidgetAddTask";;

    public static final String Widget_Action = "WidgetAction";;


    public static final String Widget_TaskUpdate = "TaskUpdate";;

    public static final String Extra_TaskName = "TaskName";
    public static final String Extra_TaskStatus = "TaskStatus";



    public static final String Extra_TaskID = "TaskID";
    public static final String Extra_ListID = "ListID";
    public static final String Extra_ListName = "ListName";




    public UpdateTaskService() {
        super("UpdateTaskService");
    }






    public static void startActionAddTask(Context context,ArrayList<Task> taskss,String ListId,String ListName) {

      Log.v("startActionAddTask","startActionAddTask");
        Intent intent = new Intent(context, UpdateTaskService.class);
        intent.setAction(Widget_AddTask);

        ArrayList<String> TaskName = new ArrayList<>();
        boolean[] TaskStatus = new boolean[taskss.size()];
        ArrayList<String> TaskID = new ArrayList<>();


        for(int i=0;i<taskss.size();i++)
        {

            TaskName.add(taskss.get(i).getName());
            TaskStatus[i] = taskss.get(i).isStatus();
            TaskID.add(taskss.get(i).getTaskID());
        }


        intent.putExtra(Extra_TaskName, TaskName);
        intent.putExtra(Extra_TaskStatus, TaskStatus);
        intent.putExtra(Extra_TaskID, TaskID);
        intent.putExtra(Extra_ListID, ListId);
        intent.putExtra(Extra_ListName, ListName);

        Log.v("UpdateTaskListname",ListName);
        context.startService(intent);
    }





    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v("onHandleIntent0000","onHandleIntent0000");

        if (intent != null) {




            if (intent.getAction().equals(Widget_AddTask))
            {  Log.v("onHandleIntent222","onHandleIntent 2222");
                ArrayList<String> TaskName = intent.getExtras().getStringArrayList(Extra_TaskName);
                boolean[] TaskStatusTmp = intent.getExtras().getBooleanArray(Extra_TaskStatus);

                ArrayList<String> TaskID = intent.getExtras().getStringArrayList(Extra_TaskID);
                String ListIDD = intent.getStringExtra(Extra_ListID);
                String ListNamee = intent.getStringExtra(Extra_ListName);
                HandleWidgetAddTaskAction(TaskName,TaskStatusTmp,TaskID,ListIDD,ListNamee);


            }


            if (intent.getAction().equals(Widget_TaskUpdate))
            {

                String TaskID = intent.getStringExtra("ItemTaskID");
                String ListID = intent.getStringExtra("ItemListID");
                boolean Status = intent.getBooleanExtra("ItemTaskStatus",false);
                String ListName = intent.getStringExtra("ItemListName");
                ChangeTaskStatus(TaskID,ListID,Status,ListName);
            }






        }

    }


public void ChangeTaskStatus(String TaskID,final String ListID,  boolean Status,final String ListName)
{
    Map<String,Object> map=new HashMap<>();
    map.put("Status",!Status);

    FirebaseDatabase.getInstance().getReference().child("TasksList").child(ListID).child(TaskID).updateChildren(map);
    Log.v("Widget_TaskUpdate","Service Runned");





    Query qqq = FirebaseDatabase.getInstance()
            .getReference()
            .child(getResources().getString(R.string.TasksList_Node)).child(ListID).orderByKey();




    qqq.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            ArrayList<Task> taskss = new ArrayList<>();
            for(DataSnapshot data : dataSnapshot.getChildren())
            {
                Task t =   data.getValue(Task.class);
                taskss.add(t);
            }



            ArrayList<String> TaskName = new ArrayList<>();
            boolean[] TaskStatus = new boolean[taskss.size()];
            ArrayList<String> TaskID = new ArrayList<>();



            for(int i=0;i<taskss.size();i++)
            {

                TaskName.add(taskss.get(i).getName());
                TaskStatus[i] = taskss.get(i).isStatus();
                TaskID.add(taskss.get(i).getTaskID());
            }



            HandleWidgetAddTaskAction(TaskName,TaskStatus,TaskID, ListID,ListName);





        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });





}

    private void HandleWidgetAddTaskAction(ArrayList<String>  TaskName,boolean[] TaskStatus,ArrayList<String> TaskID,String ListId,String ListName)
    {
        Log.v("HandleWidgetAd111","HandleWidgetAddTaskAction 2222");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, TasksWidgetProvider.class));


        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ListViewTasksWidget);
        //Now update all widgets
        TasksWidgetProvider.UpdateWidgets(this, appWidgetManager,appWidgetIds,TaskName,TaskStatus,TaskID,ListId,ListName);
    }









}
