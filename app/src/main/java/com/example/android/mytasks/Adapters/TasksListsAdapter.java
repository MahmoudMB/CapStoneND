package com.example.android.mytasks.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by SG on 7/25/2018.
 */

    public class TasksListsAdapter extends RecyclerView.Adapter<TasksListsAdapter.MyviewHolder> {

        private final LayoutInflater inflator;
        private Context context;
        final private ListItemClickListener mOnClickListener;
        List<ToDo> data = Collections.emptyList();

        int orderid;

        public interface ListItemClickListener {
            void onListItemClick(int clickedItemIndex);
        }


        public TasksListsAdapter(Context context,List<ToDo> data,ListItemClickListener listener) {
            inflator =  LayoutInflater.from(context);
            this.data=data;
            mOnClickListener = listener;
            this.context= context;
        }

        /*
    public TasksListsAdapter(List<ToDo> data,Context context) {
        inflator =  LayoutInflater.from(context);
        this.data=data;

        this.context= context;
    }*/
        @Override
        public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.tasks_lists_items,parent,false);
            MyviewHolder holder = new  MyviewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyviewHolder holder, int position) {

            final ToDo currentOrder = data.get(position);

            holder.ToDoListName.setText(currentOrder.getName());
        }


        @Override
        public int getItemCount()
        {
            return data.size();
        }




        class MyviewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

            TextView ToDoListName;
        //    TextView Desription;




            public MyviewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                ToDoListName = (TextView)itemView.findViewById(R.id.tasks_lists_Name);
                //Desription = (TextView)itemView.findViewById(R.id.tasks_lists_Description);
            }


            @Override
            public void onClick(View v) {

                int clickedPosition = getAdapterPosition();
                mOnClickListener.onListItemClick(clickedPosition);


            }



        }

    }

