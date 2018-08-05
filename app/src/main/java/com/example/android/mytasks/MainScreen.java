package com.example.android.mytasks;

import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mytasks.Adapters.TasksListsAdapter;
import com.example.android.mytasks.Models.ToDo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainScreen extends AppCompatActivity implements TasksListsAdapter.ListItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recycleriew;
    TasksListsAdapter adapter;
    List<ToDo> ToDoLists;


    private DrawerLayout mdrwerlayout;
    private ActionBarDrawerToggle mtoggle;
    NavigationView navigationView;
    Query query;
    ChildEventListener lis;

    private static final String RECYCLER_LAYOUT = "RECYCLERLAYOUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);


        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            ButterKnife.bind(this);
            ToDoLists = new ArrayList<>();
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child(getResources().getString(R.string.Users_Node)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getResources().getString(R.string.List_Node));

            recycleriew = (RecyclerView) findViewById(R.id.Tasks_List_Recycler);
            if (savedInstanceState != null) {


                ToDoLists = (ArrayList<ToDo>) savedInstanceState.getSerializable(getResources().getString(R.string.MainScreen_ToDoLists));


                adapter = new TasksListsAdapter(getApplicationContext(), ToDoLists, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recycleriew.setLayoutManager(layoutManager);
                recycleriew.setAdapter(adapter);
                findViewById(R.id.MainScreen_NoLists).setVisibility(View.GONE);
            }
            else {

                ToDoLists = new ArrayList<>();

                adapter = new TasksListsAdapter(getApplicationContext(), ToDoLists, this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recycleriew.setLayoutManager(layoutManager);
                recycleriew.setAdapter(adapter);

            }

            lis = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    findViewById(R.id.MainScreen_NoLists).setVisibility(View.GONE);
                    if (!ToDoLists.contains(dataSnapshot.getValue(ToDo.class)) && ((dataSnapshot.getValue(ToDo.class)).getOwnerUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                        ToDoLists.add(dataSnapshot.getValue(ToDo.class));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };



            Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
            mtoolbar.setTitle(getResources().getString(R.string.app_name));
            setSupportActionBar(mtoolbar);


            navigationView = (NavigationView) findViewById(R.id.nav);

            navigationView.setNavigationItemSelectedListener(this);
////Header
//////////////
            View header = navigationView.getHeaderView(0);


            TextView name = (TextView) header.findViewById(R.id.Header_Name);
            ImageView photo = (ImageView) header.findViewById(R.id.Header_profile_img);


            name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
                photo.setImageURI(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
            }


            mdrwerlayout = (DrawerLayout) findViewById(R.id.MainScreen_drawer);
            mtoggle = new ActionBarDrawerToggle(this, mdrwerlayout, R.string.open, R.string.close);


            mdrwerlayout.addDrawerListener(mtoggle);
            mtoggle.syncState();


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }
        else{
            Intent i = new Intent(MainScreen.this,SigninActivity.class);
            startActivity(i);
            finish();

        }

    }






    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
            recycleriew.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getResources().getString(R.string.MainScreen_ToDoLists),(Serializable) ToDoLists);
        outState.putParcelable(RECYCLER_LAYOUT, recycleriew.getLayoutManager().onSaveInstanceState());
    }

    @OnClick(R.id.mainScreen_fab) void addNewList() {

        Intent i  = new Intent(MainScreen.this,AddNewList.class);
        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();
        query.addChildEventListener(lis);
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener(lis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Freind) {

            Intent i = new Intent(MainScreen.this,AddFriends.class);
            startActivity(i);
            //return true;
        }



        if (mtoggle.onOptionsItemSelected(item))
            return true;


        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            //return true;
        }






        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Friends_drawer) {

            Intent i = new Intent(MainScreen.this,AddFriends.class);
            startActivity(i);
            //return true;
        }

        if (item.getItemId() == R.id.LogOut_drawer) {
            //      query.removeEventListener(lis);
            FirebaseAuth.getInstance().signOut();
            finish();
            //return true;
        }

        if (item.getItemId() == R.id.SharedLists_drawer) {

            Intent i = new Intent(MainScreen.this,SharedTasks.class);
            startActivity(i);
            //return true;
        }


        return false;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        return true;
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent i = new Intent(MainScreen.this,AddTask.class);
        i.putExtra(getResources().getString(R.string.MainScreen_TaskListIntent),ToDoLists.get(clickedItemIndex));
        startActivity(i);
    }

}
