package com.example.android.mytasks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by SG on 7/25/2018.
 */

public class ToDoHolder extends RecyclerView.ViewHolder {


    TextView ToDoListName;

    public ToDoHolder(View itemView) {
        super(itemView);
        ToDoListName = (TextView)itemView.findViewById(R.id.tasks_lists_Name);

    }








}
