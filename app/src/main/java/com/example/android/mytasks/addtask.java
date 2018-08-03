package com.example.android.mytasks;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.mytasks.Interfaces.FirebaseCallBacks;
import com.example.android.mytasks.Models.Task;
import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.Utilis.FirebaseManager;
import com.example.android.mytasks.Widget.UpdateTaskService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class addtask extends AppCompatActivity  implements FirebaseCallBacks {
    private RecyclerView recycleriew;
    private RecyclerView RecyclerCompletedTasks;
    FirebaseRecyclerAdapter NotCompletedAdapter;
    FirebaseRecyclerAdapter CompletedAdapter;
    List<Task> Tasks;
    ToDo todo;

    private static final String RECYCLER_LAYOUT1 = "RECYCLERLAYOUT1";
    private static final String RECYCLER_LAYOUT2 = "RECYCLERLAYOUT2";

    @BindView(R.id.addTask_AddNewTaskEditText)
    EditText AddNewTaskEditText;



    @BindView(R.id.addTask_fab)
    FloatingActionButton FloatAddBtn;



    @BindView(R.id.ShowComplated)
    Button ShowComplated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        ButterKnife.bind(this);
        todo =(ToDo) getIntent().getSerializableExtra(getResources().getString(R.string.MainScreen_TaskListIntent));
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle(todo.getName());
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).orderByChild(getResources().getString(R.string.Status_Node)).equalTo(false);

        FirebaseRecyclerOptions<Task> options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(query, Task.class)
                        .build();






        NotCompletedAdapter = new FirebaseRecyclerAdapter<Task, TaskHolder>(options) {
            @Override
            public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tasks_items, parent, false);

                return new TaskHolder(view);
            }



            @Override
            protected void onBindViewHolder(TaskHolder holder, int position, final Task model) {

                if (model.getTaskID()!=null){

                    final Task Task = model;
                    holder.TaskName.setText(model.getName());



                    if (model.isStatus())
                        holder.Status.setImageResource(R.drawable.ic_round_check_circle_24px);
                    else if (!model.isStatus())
                        holder.Status.setImageResource(R.drawable.ic_round_check_circle_outline_24px);



                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.isStatus())
                                FirebaseManager.getInstance(addtask.this).ChangeTaskStatus(model.getTaskID(),todo.getListID(),false);
                            else   if (!model.isStatus())
                                FirebaseManager.getInstance(addtask.this).ChangeTaskStatus(model.getTaskID(),todo.getListID(),true);

                        }
                    });
                }}
        };






        recycleriew = (RecyclerView)findViewById(R.id.AddTask_Recycler);
        recycleriew.setAdapter(NotCompletedAdapter);
        recycleriew.setLayoutManager(new LinearLayoutManager(this));











        query = FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).orderByChild(getResources().getString(R.string.Status_Node)).equalTo(true);

        options =
                new FirebaseRecyclerOptions.Builder<Task>()
                        .setQuery(query, Task.class)
                        .build();




        CompletedAdapter = new FirebaseRecyclerAdapter<Task, TaskHolder>(options) {
            @Override
            public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tasks_items, parent, false);

                return new TaskHolder(view);
            }



            @Override
            protected void onBindViewHolder(TaskHolder holder, int position, final Task model) {

                if (model.getTaskID()!=null){

                    final Task Task = model;
                    holder.TaskName.setText(model.getName());



                    if (model.isStatus())
                        holder.Status.setImageResource(R.drawable.ic_round_check_circle_24px);
                    else if (!model.isStatus())
                        holder.Status.setImageResource(R.drawable.ic_round_check_circle_outline_24px);



                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.isStatus())
                                FirebaseManager.getInstance(addtask.this).ChangeTaskStatus(model.getTaskID(),todo.getListID(),false);
                            else   if (!model.isStatus())
                                FirebaseManager.getInstance(addtask.this).ChangeTaskStatus(model.getTaskID(),todo.getListID(),true);

                        }
                    });
                }}
        };





        RecyclerCompletedTasks = (RecyclerView)findViewById(R.id.AddTask_RecyclerCompletedTasks);
        RecyclerCompletedTasks.setAdapter(CompletedAdapter);
        RecyclerCompletedTasks.setLayoutManager(new LinearLayoutManager(this));










        AddNewTaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (AddNewTaskEditText.getText().toString().isEmpty())
                    FloatAddBtn.setVisibility(View.GONE);
                else if (!AddNewTaskEditText.getText().toString().isEmpty())
                    FloatAddBtn.setVisibility(View.VISIBLE);


            }
        });







        ShowComplated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (RecyclerCompletedTasks.getVisibility()==View.VISIBLE) {
                    RecyclerCompletedTasks.setVisibility(View.GONE);
                    ShowComplated.setText(getResources().getString(R.string.ShowCompleted_addtask));
                }
                else if(RecyclerCompletedTasks.getVisibility()==View.GONE) {
                    RecyclerCompletedTasks.setVisibility(View.VISIBLE);
                    ShowComplated.setText(getResources().getString(R.string.HideCompleted_addtask));
                }

            }
        });




    }





    public void GetTasksForWidget()
    {

        Query qqq = FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).orderByKey();



        qqq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Task> taskss = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                  Task t =   data.getValue(Task.class);
                    taskss.add(t);
                }

                UpdateTaskService.startActionAddTask(getApplicationContext(),taskss,todo.getListID(),todo.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }











    @OnClick(R.id.addTask_fab) void addTask() {

        AddNewTask();
        AddNewTaskEditText.getText().clear();
    }




    public void AddNewTask()
    {
        String t = ((EditText)findViewById(R.id.addTask_AddNewTaskEditText)).getText().toString();

       if (!TextUtils.isEmpty(t)) {
           FirebaseManager.getInstance(this).createNewTask(t, todo.getListID());
       }
       else
           {
               Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.AddNewYask__Msg1), Snackbar.LENGTH_LONG);
               snackbar.show();
       }

       }









    @Override
    protected void onStart() {
        super.onStart();
        NotCompletedAdapter.startListening();
        CompletedAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotCompletedAdapter.stopListening();
        CompletedAdapter.startListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_items_addtask, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        //Back Button to navigate back to the details screen
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.addFreindToList) {
            Intent i = new Intent(addtask.this,PickMembers.class);
            i.putExtra(getResources().getString(R.string.MainScreen_TaskListIntent),todo);
            startActivity(i);
        }

        if (id == R.id.AddTowidget)
        {
            GetTasksForWidget();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "This List Has been Added To the Widget", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RECYCLER_LAYOUT1, recycleriew.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(RECYCLER_LAYOUT2, RecyclerCompletedTasks.getLayoutManager().onSaveInstanceState());
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            Log.v("Restore","Restore");
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_LAYOUT1);
            recycleriew.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

            Parcelable savedRecyclerLayoutState1 = savedInstanceState.getParcelable(RECYCLER_LAYOUT2);
            RecyclerCompletedTasks.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState1);

        }



    }
}
