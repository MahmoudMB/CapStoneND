package com.example.android.mytasks;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.mytasks.Adapters.TaskAdapter;
import com.example.android.mytasks.Interfaces.FirebaseCallBacks;
import com.example.android.mytasks.Models.Task;
import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.Utilis.FirebaseManager;
import com.example.android.mytasks.Widget.UpdateTaskService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTask extends AppCompatActivity  implements FirebaseCallBacks {
    private RecyclerView recycleriew;
    private RecyclerView RecyclerCompletedTasks;
    TaskAdapter NotCompletedAdapter;
    TaskAdapter CompletedAdapter;
    ArrayList<Task> notCompletedTasks = new ArrayList<>();
    ArrayList<Task> completedTasks = new ArrayList<>();
    ChildEventListener notCompletedChildLis;
    ChildEventListener completedChildLis;
    ToDo todo;
    Query queryNotCompleted;
    Query queryCompleted;
    private static final String RECYCLER_LAYOUT1 = "RECYCLERLAYOUT1";
    private static final String RECYCLER_LAYOUT2 = "RECYCLERLAYOUT2";
    private static final String mPosition = "Position";
    private static final String ListCompletedTasks = "ListCompletedTasks";
    private static final String ListNotCompletedTasks = "ListNotCompletedTasks";


    private static final String mShowCompletedVisible = "ShowCompletedVisible";
    private static final String mShowCompletedNotVisible = "ShowCompletedNotVisible";

    private static final String mCompletedVisibiltiy = "CompletedVisibiltiy";

    private ScrollView mScrollView;
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
        todo = (ToDo) getIntent().getSerializableExtra(getResources().getString(R.string.MainScreen_TaskListIntent));
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle(todo.getName());
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (savedInstanceState!=null)
        {
            completedTasks = (ArrayList<Task>) savedInstanceState.getSerializable(ListCompletedTasks);
            notCompletedTasks = (ArrayList<Task>) savedInstanceState.getSerializable(ListNotCompletedTasks);
        }

        queryNotCompleted = FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).orderByChild(getResources().getString(R.string.Status_Node)).equalTo(false);


        notCompletedChildLis = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Task e = dataSnapshot.getValue(Task.class);
                if (!notCompletedTasks.contains(e))
                notCompletedTasks.add(e);
                NotCompletedAdapter.notifyDataSetChanged();
                Log.v("Add NotComplted", e.getName() + " ");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Task e = dataSnapshot.getValue(Task.class);
                notCompletedTasks.remove(e);
                NotCompletedAdapter.notifyDataSetChanged();

                Log.v("Removed Not Complted", e.getName() + " ");

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        completedChildLis = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Task e = dataSnapshot.getValue(Task.class);
                if (!completedTasks.contains(e))
                completedTasks.add(e);
                CompletedAdapter.notifyDataSetChanged();

                Log.v("Add Complted", e.getName() + " ");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Task e = dataSnapshot.getValue(Task.class);
                completedTasks.remove(e);
                CompletedAdapter.notifyDataSetChanged();

                Log.v("Removed Complted", e.getName() + " ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        NotCompletedAdapter = new TaskAdapter(this, notCompletedTasks, todo);

        CompletedAdapter = new TaskAdapter(this, completedTasks, todo);


        recycleriew = (RecyclerView) findViewById(R.id.AddTask_Recycler);
        recycleriew.setAdapter(NotCompletedAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycleriew.setLayoutManager(layoutManager);


        queryCompleted = FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).orderByChild(getResources().getString(R.string.Status_Node)).equalTo(true);


        RecyclerCompletedTasks = (RecyclerView) findViewById(R.id.AddTask_RecyclerCompletedTasks);
        RecyclerCompletedTasks.setAdapter(CompletedAdapter);
        RecyclerCompletedTasks.setLayoutManager(new LinearLayoutManager(this));

        mScrollView = findViewById(R.id.AddTask_ScrollView);
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


                if (RecyclerCompletedTasks.getVisibility() == View.VISIBLE) {
                    RecyclerCompletedTasks.setVisibility(View.GONE);
                    ShowComplated.setText(getResources().getString(R.string.ShowCompleted_addtask));
                } else if (RecyclerCompletedTasks.getVisibility() == View.GONE) {
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

       if (!TextUtils.isEmpty(t.trim())) {
           FirebaseManager.getInstance(this).createNewTask(t, todo.getListID());

       }
       else
           {
               Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.AddNewYask__Msg1), Snackbar.LENGTH_LONG);
               snackbar.show();
       }

       }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        queryNotCompleted.removeEventListener(notCompletedChildLis);
queryCompleted.removeEventListener(completedChildLis);
    }


    @Override
    protected void onStart() {
        super.onStart();

        queryNotCompleted.addChildEventListener(notCompletedChildLis);
        queryCompleted.addChildEventListener(completedChildLis);
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
            Intent i = new Intent(AddTask.this,PickMembers.class);
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

        outState.putSerializable(ListNotCompletedTasks,(Serializable) notCompletedTasks);
        outState.putSerializable(ListCompletedTasks,(Serializable) completedTasks);


        outState.putIntArray(mPosition,
                new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});



        outState.putInt(mCompletedVisibiltiy,RecyclerCompletedTasks.getVisibility());





}


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_LAYOUT1);
            recycleriew.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);


            savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_LAYOUT2);
            RecyclerCompletedTasks.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);


            if (savedInstanceState.getInt(mCompletedVisibiltiy) == View.VISIBLE) {
                RecyclerCompletedTasks.setVisibility(View.VISIBLE);
                ShowComplated.setText(getResources().getString(R.string.HideCompleted_addtask));

            } else if (savedInstanceState.getInt(mCompletedVisibiltiy) == View.GONE) {
                RecyclerCompletedTasks.setVisibility(View.GONE);
                ShowComplated.setText(getResources().getString(R.string.ShowCompleted_addtask));
            }

            final int[] position = savedInstanceState.getIntArray(mPosition);
            if(position != null)
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);
                    }
                });






        }



    }
}
