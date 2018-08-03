package com.example.android.mytasks.Widget;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Measure;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.mytasks.Models.Task;
import com.example.android.mytasks.R;
import com.example.android.mytasks.TasksWidgetProvider;

import java.util.ArrayList;

/**
 * Created by SG on 7/31/2018.
 */

public class ListWidgetService  extends RemoteViewsService {

    public static final String Widget_Action = "WidgetAction";;
    public static final String Widget_TaskUpdate = "TaskUpdate";;

    public static final String Extra_TaskName = "TaskName";
    public static final String Extra_TaskStatus = "TaskStatus";
    public static final String Extra_TaskID = "TaskID";


    ArrayList<String> TaskName = new ArrayList<>();
    boolean[] TaskStatus;
    ArrayList<String> TaskID = new ArrayList<>();

    public static String ListID = "";
    public static String ListName = "";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }


    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context mContext;


        public ListRemoteViewsFactory(Context mContext) {
            this.mContext = mContext;
        }


        @Override
        public void onCreate() {

            Log.v("OnCreate","OnCreate");
        }

        @Override
        public void onDataSetChanged() {

            TaskName = TasksWidgetProvider.TaskName1;
            TaskStatus = TasksWidgetProvider.TaskStatus1;
            TaskID = TasksWidgetProvider.TaskID1;
           ListName = TasksWidgetProvider.ListName1;
            ListID = TasksWidgetProvider.ListID1;
        }

        @Override
        public void onDestroy() {


            TaskName=new ArrayList<>();
            TaskStatus = null;
            TaskID=new ArrayList<>();



        }

        @Override
        public int getCount() {

            return TaskName.size();

        }

        @Override
        public RemoteViews getViewAt(int position) {




            Log.v("GetView","GetViews");
            Log.v("111",TaskName.get(position)+" ");
            Log.v("111",TaskStatus[position]+" ");
            Log.v("111",TaskID.get(position)+" ");

            String currentTaskName = TaskName.get(position);
              boolean cuurentTaskStatus = TaskStatus[position];
            String CurrentTaskID = TaskID.get(position);




            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.tasks_items);

            views.setTextViewText(R.id.Task_Name,currentTaskName);

            if (cuurentTaskStatus)
            { views.setViewVisibility(R.id.AddTask_TaskStatus, View.VISIBLE);
                views.setImageViewResource(R.id.AddTask_TaskStatus,R.drawable.ic_round_check_circle_24px);
            }
            else if (!cuurentTaskStatus)
            { views.setViewVisibility(R.id.AddTask_TaskStatus, View.VISIBLE);
                views.setImageViewResource(R.id.AddTask_TaskStatus,R.drawable.ic_round_check_circle_outline_24px);
            }


            if (TextUtils.isEmpty(currentTaskName))
            {
                views.setViewVisibility(R.id.AddTask_TaskStatus, View.GONE);
            }

            Log.v("ListWidgetservice","Get Views");

            Bundle extras = new Bundle();



            extras.putString("ItemTaskID",CurrentTaskID);
         extras.putBoolean("ItemTaskStatus",cuurentTaskStatus);
        extras.putString("ItemListID",ListID);
            extras.putString("ItemListName",ListName);





            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            fillInIntent.setAction(Widget_TaskUpdate);
            views.setOnClickFillInIntent(R.id.WidgetRelativeLayout, fillInIntent);


            return views;


        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }



}
