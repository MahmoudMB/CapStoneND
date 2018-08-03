package com.example.android.mytasks;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SG on 7/26/2018.
 */

public class TaskHolder extends RecyclerView.ViewHolder {

    TextView TaskName;
    ImageView Status;



    public TaskHolder(View itemView)
    {
        super(itemView);
        TaskName = (TextView)itemView.findViewById(R.id.Task_Name);
        Status = (ImageView)itemView.findViewById(R.id.AddTask_TaskStatus);
    }




}
