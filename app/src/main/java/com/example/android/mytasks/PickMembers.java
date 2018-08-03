package com.example.android.mytasks;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.mytasks.Adapters.FriendsAdapter;
import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.Models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by SG on 7/29/2018.
 */

public class PickMembers extends AppCompatActivity implements FriendsAdapter.ListItemClickListener {
    private RecyclerView recycleriew;
    FirebaseRecyclerAdapter adapter;
    private RecyclerView RecyclerAllFriends;
    FriendsAdapter adapterAllFriends;
    List<User> SharedWith;
    List<User> AllFriends;
    ToDo todo;

    public PickMembers() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickmembers);
        ButterKnife.bind(this);
        SharedWith = new ArrayList<>();


        AllFriends = new ArrayList<>();


        todo =(ToDo) getIntent().getSerializableExtra(getResources().getString(R.string.MainScreen_TaskListIntent));




        //To Load All The Freinds User List
      //  if (!FirebaseAuth.getInstance().getCurrentUser().getUid().toString().equals(todo.getOwnerUID()))
            LoadShared();
SharedFreinds();

        //////////////////////

        /*
try{

    Thread.sleep(100);
}
catch (Exception ex){}
*/


        ////////// For List Of All Freinds
        adapterAllFriends= new FriendsAdapter(getApplicationContext(),AllFriends,this);

        RecyclerAllFriends = (RecyclerView)findViewById(R.id.PicFreinds_RecyclerAddNewShare);
        RecyclerAllFriends.setAdapter(adapterAllFriends);
        RecyclerAllFriends.setLayoutManager(new LinearLayoutManager(this));





        //      GetTasksFreinds();
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.PickFreinds_toolbar);
        mtoolbar.setTitle(getResources().getString(R.string.PickFreinds_Title));
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);






        if (AllFriends.isEmpty())
            findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.VISIBLE);

        else if (!AllFriends.isEmpty())
            findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.GONE);



        if (!FirebaseAuth.getInstance().getCurrentUser().getUid().toString().equals(todo.getOwnerUID())) {

            RecyclerAllFriends.setVisibility(View.GONE);
            findViewById(R.id.PickMemebers_NoOwnerText).setVisibility(View.VISIBLE);
            findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.GONE);
        }


        for (int i=0;i<SharedWith.size();i++)
        {
            if (AllFriends.contains(SharedWith.get(i)))
                AllFriends.remove(SharedWith.get(i));
        }




        adapterAllFriends.notifyDataSetChanged();
    }




    public void SharedFreinds(){

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).child(getResources().getString(R.string.Friends_Node));

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();



        adapter = new FirebaseRecyclerAdapter<User, UserHolder>(options) {
            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.friends_list_items, parent, false);

                return new UserHolder(view);
            }

            @Override
            protected void onBindViewHolder(final UserHolder holder, int position, User model) {

                final User user = model;
                holder.Name.setText(model.getName());
                holder.Email.setText(model.getEmail());
                if (!TextUtils.isEmpty(model.getPhotoUrl()))
                    holder.Photo.setImageURI(Uri.parse(model.getPhotoUrl()));




                holder.Status.setImageResource(R.drawable.ic_round_check_circle_24px);
                if (!SharedWith.contains(user))
                SharedWith.add(user);
                if (SharedWith.isEmpty())
                    findViewById(R.id.TextView_NoSharedFreindsExists).setVisibility(View.VISIBLE);
                else if (!SharedWith.isEmpty())
                    findViewById(R.id.TextView_NoSharedFreindsExists).setVisibility(View.GONE);


                if (AllFriends.contains(user)) {
                    AllFriends.remove(user);
                    adapterAllFriends.notifyDataSetChanged();
                }



                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(todo.getOwnerUID())) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUid()))
                            {

                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.PickFreinds_Msg1), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            else {
                                FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).child(getResources().getString(R.string.Friends_Node)).child(user.getUid()).setValue(null);
                                FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.Users_Node)).child(user.getUid()).child(getResources().getString(R.string.List_Node)).child(todo.getListID()).setValue(null);

                                if (!AllFriends.contains(user))
                                    AllFriends.add(user);


                                SharedWith.remove(user);
                                adapterAllFriends.notifyDataSetChanged();


                                if (SharedWith.isEmpty())
                                    findViewById(R.id.TextView_NoSharedFreindsExists).setVisibility(View.VISIBLE);
                                else if (!SharedWith.isEmpty())
                                    findViewById(R.id.TextView_NoSharedFreindsExists).setVisibility(View.GONE);


                                if (AllFriends.size()>1)
                                    findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.VISIBLE);

                                else if (AllFriends.size()<=1)
                                    findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.GONE);
                            }
                        }
                    });

                }


            }
        };

        recycleriew = (RecyclerView)findViewById(R.id.PicFreinds_Recycler);
        recycleriew.setAdapter(adapter);
        recycleriew.setLayoutManager(new LinearLayoutManager(this));

    }

    public void LoadShared()
    {


        FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).child(getResources().getString(R.string.Friends_Node)).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String key=data.getKey();
                    if  (!TextUtils.isEmpty(key))
                    {
                        User user = data.getValue(User.class);
                        SharedWith.add(user);
                    }
                }
                LoadAllFreinds();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FireBase Error", "loadPost:onCancelled", databaseError.toException());
            }
        });








    }

public void LoadAllFreinds(){

    FirebaseDatabase.getInstance()
            .getReference()
            .child(getResources().getString(R.string.Users_Node)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getResources().getString(R.string.Friends_Node)).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            for(DataSnapshot data: dataSnapshot.getChildren()){
                String key=data.getKey();
                if  (!TextUtils.isEmpty(key)){
                    User user = data.getValue(User.class);
                    if (!SharedWith.contains(user))
                    AllFriends.add(user);
                }




            }

            if (AllFriends.isEmpty()){
                findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.VISIBLE);
                //  Log.v("All FreindsSize11",AllFriends.size()+" ");
            }

            else if (!AllFriends.isEmpty())
                findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.GONE);
            adapterAllFriends.notifyDataSetChanged();


        }




        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("FireBase Error", "loadPost:onCancelled", databaseError.toException());
        }
    });



}
    @Override
    public void onListItemClick(int i) {


    Map<String, Object> map = new HashMap<>();
    map.put("Uid", AllFriends.get(i).getUid());
    map.put("Email", AllFriends.get(i).getEmail());
    map.put("Name", AllFriends.get(i).getName());
    map.put("PhotoUrl", AllFriends.get(i).getPhotoUrl());
    if (TextUtils.isEmpty(AllFriends.get(i).getPhotoUrl()))
        map.put("PhotoUrl", " ");
    FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.TasksList_Node)).child(todo.getListID()).child(getResources().getString(R.string.Friends_Node)).child(AllFriends.get(i).getUid()).setValue(map);


    Map<String, Object> mapList = new HashMap<>();
    mapList.put("Name", todo.getName());
    mapList.put("ListID", todo.getListID());
    mapList.put("OwnerUID", FirebaseAuth.getInstance().getCurrentUser().getUid());


    FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.Users_Node)).child(AllFriends.get(i).getUid()).child(getResources().getString(R.string.List_Node)).child(todo.getListID()).setValue(mapList);


    AllFriends.remove(AllFriends.get(i));

    adapterAllFriends.notifyDataSetChanged();


    if (AllFriends.isEmpty())
        findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.VISIBLE);

    else if (!AllFriends.isEmpty())
        findViewById(R.id.TextView_AllFreindsExists).setVisibility(View.GONE);



    }





    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.toolbar_items_pickfriends, menu);*/
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


        return true;
    }

}
