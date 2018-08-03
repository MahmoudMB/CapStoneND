package com.example.android.mytasks;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.mytasks.Models.Task;
import com.example.android.mytasks.Widget.ListWidgetService;
import com.example.android.mytasks.Widget.UpdateTaskService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class TasksWidgetProvider extends AppWidgetProvider {

    public static final String Widget_Action = "WidgetAction";;
    public static final String Widget_Update = "android.appwidget.action.APPWIDGET_UPDATE";;

    public static final String Extra_TaskName = "TaskName";
    public static final String Extra_TaskStatus = "TaskStatus";
    public static final String Extra_TaskID = "TaskID";
    public static final String Extra_ListID = "ListID";

    public  static ArrayList<String> TaskName1 = new ArrayList<>();
    public  static boolean[] TaskStatus1;
    public  static ArrayList<String> TaskID1 = new ArrayList<>();

    public static ArrayList<Task> tasks = new ArrayList<>();
    public static String ListID1 = "";
    public static String ListName1 = "";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId,ArrayList<String>  TaskName,boolean[] TaskStatus,ArrayList<String> TaskID,String ListId,String ListName) {


        Log.v("Provider","updateAppWidget1");
        RemoteViews views = getListRemoteView(context);

        TaskStatus1 = TaskStatus;
        TaskName1 = TaskName;
        ListID1 = ListId;
        ListName1 = ListName;
        TaskID1 = TaskID;

        Log.v("ListName",ListName+" ");
        Log.v("ListName1",ListName1+" ");

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }




    private static RemoteViews getListRemoteView(Context context)
    {   Log.v("RemoteViews","RemoteViews ");

        RemoteViews views =new RemoteViews(context.getPackageName(),R.layout.tasks_widget_provider);
        Intent Rintent = new Intent(context,ListWidgetService.class);



      Intent appIntent = new Intent(context,UpdateTaskService.class);
      PendingIntent appPendingIntent = PendingIntent.getService(context,0,appIntent,PendingIntent.FLAG_UPDATE_CURRENT);
      views.setPendingIntentTemplate(R.id.ListViewTasksWidget,appPendingIntent);


        views.setRemoteAdapter(R.id.ListViewTasksWidget ,Rintent);

        return views;
    }





    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.v("On Update111","Called");
        Log.v("UpdateListID111",ListID1+" ");
        for (int appWidgetId : appWidgetIds) {
          //  updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.v("On Update","Called");
            Log.v("UpdateListID",ListID1+" ");

        }



        }


    public static void UpdateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,ArrayList<String>  TaskName,boolean[] TaskStatus,ArrayList<String> TaskID,String ListId,String ListName) {
        // There may be multiple widgets active, so update all of them

        Log.v("ListName2222",ListName+" ");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,TaskName,TaskStatus,TaskID,ListId,ListName);
        }



    }








    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



}

