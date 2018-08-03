package com.example.android.mytasks.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mytasks.Models.Task;
import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.R;
import com.example.android.mytasks.Utilis.FirebaseManager;
import com.example.android.mytasks.addtask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SG on 7/30/2018.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyviewHolder> {
    private final LayoutInflater inflator;
    private Context context;
  //  final private TasksListsAdapter.ListItemClickListener mOnClickListener;
    List<Task> data = Collections.emptyList();

ToDo todo;

    public TaskAdapter(Context context, List<Task> data,ToDo todo) {
        inflator =  LayoutInflater.from(context);
        this.data=data;
        this.context= context;
      //  mOnClickListener = listener;
         this.todo = todo;
    }


    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.tasks_items,parent,false);
        TaskAdapter.MyviewHolder holder = new TaskAdapter.MyviewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final TaskAdapter.MyviewHolder holder, int position) {

        final Task currentOrder = data.get(position);
        Log.v("ListID",todo.getListID()+" ");
        Log.v("TaskNam",currentOrder.getName()+" ");
        holder.TaskName.setText(currentOrder.getName());


        if (currentOrder.isStatus())
            holder.Status.setImageResource(R.drawable.ic_round_check_circle_24px);
        else if (!currentOrder.isStatus())
            holder.Status.setImageResource(R.drawable.ic_round_check_circle_outline_24px);



        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Log.v("Task name",currentOrder.getName()+" ");
                Log.v("Task id",currentOrder.getTaskID()+" ");
                Log.v("Task status",currentOrder.isStatus()+" ");



                Map<String,Object> map=new HashMap<>();
                if (currentOrder.isStatus())
                map.put("Status",false);
                else if (!currentOrder.isStatus())
                    map.put("Status",true);

                FirebaseDatabase.getInstance().getReference().child("TasksList").child(todo.getListID()).child(currentOrder.getTaskID()).updateChildren(map);


            }
        });
    }



    @Override
    public int getItemCount()
    {
        return data.size();
    }


    class MyviewHolder extends RecyclerView.ViewHolder {

        TextView TaskName;
        ImageView Status;



        public MyviewHolder(View itemView) {
            super(itemView);
          //  itemView.setOnClickListener(this);
            TaskName = (TextView)itemView.findViewById(R.id.Task_Name);
            Status = (ImageView)itemView.findViewById(R.id.AddTask_TaskStatus);
        }

/*
        @Override
        public void onClick(View v) {


            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
*/

    }



}
