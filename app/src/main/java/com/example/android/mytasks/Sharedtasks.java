package com.example.android.mytasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.mytasks.Adapters.TasksListsAdapter;
import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.Models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Sharedtasks extends AppCompatActivity implements  TasksListsAdapter.ListItemClickListener{
    private RecyclerView recycleriew;
    TasksListsAdapter adapter;
    List<ToDo> ToDoLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedtasks);
        ButterKnife.bind(this);


        ToDoLists = new ArrayList<>();



         adapter  = new TasksListsAdapter(getApplicationContext(),ToDoLists,this);
        recycleriew = (RecyclerView)findViewById(R.id.SharedTasks_Tasks_List_Recycler);
        recycleriew.setAdapter(adapter);
        recycleriew.setLayoutManager(new LinearLayoutManager(this));


        Toolbar mtoolbar = (Toolbar) findViewById(R.id.SharedTasks_toolbar);
        mtoolbar.setTitle(getResources().getString(R.string.SharedTasks_ScreenTitle));

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LoadTasks();






    }
    public void LoadTasks()
    {


        FirebaseDatabase.getInstance()
                .getReference()
                .child((getResources().getString(R.string.Users_Node))).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getResources().getString(R.string.List_Node)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String key=data.getKey();
                    if  (!TextUtils.isEmpty(key)){

                        ToDo toDo = data.getValue(ToDo.class);
                if (!toDo.getOwnerUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        ToDoLists.add(toDo);
                    }
                }


                if (ToDoLists.isEmpty())
                {
                    findViewById(R.id.SharedTasks_NoLists).setVisibility(View.VISIBLE);
                }
                else if (!ToDoLists.isEmpty())
                {
                    findViewById(R.id.SharedTasks_NoLists).setVisibility(View.GONE);
                }

adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FireBase Error", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    @Override
    public void onListItemClick(int clickedItemIndex)
    {
        Intent i = new Intent(Sharedtasks.this,addtask.class);
        i.putExtra(getResources().getString(R.string.MainScreen_TaskListIntent),ToDoLists.get(clickedItemIndex));
        startActivity(i);
    }








    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        //Back Button to navigate back to the details screen
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
    return true;
    }
}
