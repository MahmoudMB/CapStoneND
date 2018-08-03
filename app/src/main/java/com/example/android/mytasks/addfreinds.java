package com.example.android.mytasks;

import android.net.Uri;
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
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.mytasks.Models.Task;
import com.example.android.mytasks.Models.User;
import com.example.android.mytasks.Utilis.FirebaseManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class addfreinds extends AppCompatActivity {


    @BindView(R.id.SearchFriend_EditText)
    EditText SearchFriend_EditText;




    @BindView(R.id.AddFreind_FriendImage)
    CircleImageView AddFreind_FriendImage;


    @BindView(R.id.AddFreind_FriendName)
    TextView AddFreind_FriendName;


    @BindView(R.id.AddFreind_FriendEmail)
    TextView AddFreind_FriendEmail;



    @BindView(R.id.AddFreinds_Search)
    TextView AddFreinds_Search;




    private RecyclerView recycleriew;
    FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfreinds);


        ButterKnife.bind(this);

        Toolbar mtoolbar = (Toolbar) findViewById(R.id.addFriends_toolbar);
        mtoolbar.setTitle("Friends");
        setSupportActionBar(mtoolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Friends");

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
            protected void onBindViewHolder(UserHolder holder, int position, final User model) {

                findViewById(R.id.NoFriends).setVisibility(View.GONE);
                final User Task = model;

                holder.Name.setText(model.getName());
                holder.Email.setText(model.getEmail());
                if (!TextUtils.isEmpty(model.getPhotoUrl()))
                holder.Photo.setImageURI(Uri.parse(model.getPhotoUrl()));


            holder.Status.setVisibility(View.GONE);

            }
        };


        recycleriew = (RecyclerView)findViewById(R.id.AddFreindsRecycler);
        recycleriew.setAdapter(adapter);
        recycleriew.setLayoutManager(new LinearLayoutManager(this));
















        SearchFriend_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (SearchFriend_EditText.getText().toString().isEmpty())
                    AddFreinds_Search.setVisibility(View.GONE);
                else if (!SearchFriend_EditText.getText().toString().isEmpty())
                    AddFreinds_Search.setVisibility(View.VISIBLE);


            }
        });







    }







    @OnClick(R.id.AddFreinds_Add) void AddFreinds_Add() {

      String Email =   ((TextView) findViewById(R.id.AddFreind_FriendEmail)).getText().toString();
Add(Email);

        findViewById(R.id.UserInformation).setVisibility(View.GONE);
        EditText searchText = findViewById(R.id.SearchFriend_EditText);
        searchText.getText().clear();

    }




    @OnClick(R.id.AddFreinds_Search) void AddFreinds_Search() {

        SearchForEmail();
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





    public void SearchForEmail()
    {

        EditText searchText = findViewById(R.id.SearchFriend_EditText);
        String Email = searchText.getText().toString();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("Users").orderByChild("Email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                if(!dataSnapshot.exists())
                {
                    findViewById(R.id.UserInformation).setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "There are no Users With This Email", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }

                if(dataSnapshot.exists())
                {


                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        String key = data.getKey();
                        if (!TextUtils.isEmpty(key)) {

                            User user = data.getValue(User.class);

                            Log.v("Email", user.getEmail() + " ");


                            findViewById(R.id.UserInformation).setVisibility(View.VISIBLE);


                            AddFreind_FriendEmail.setText(user.getEmail());
                            AddFreind_FriendName.setText(user.getName());
                            if (user.getPhotoUrl()!=null)
                            AddFreind_FriendImage.setImageURI(Uri.parse(user.getPhotoUrl()));
                        }

                    }



                }





                String Key =  dataSnapshot.getKey();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FireBase Error", "loadPost:onCancelled", databaseError.toException());
            }
        });



    }









    public void Add(String Email)
    {


        FirebaseDatabase.getInstance()
                .getReference()
                .child("Users").orderByChild("Email").equalTo(Email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String key=data.getKey();
                    if  (!TextUtils.isEmpty(key)){

                        User user = data.getValue(User.class);

                        Log.v("Email",user.getEmail()+" ");
                        AddNewFriendToUser(user);

                    }

                }



                String Key =  dataSnapshot.getKey();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FireBase Error", "loadPost:onCancelled", databaseError.toException());
            }
        });



    }






public void AddNewFriendToUser(User user)
{
    Map<String,Object> map=new HashMap<>();
    map.put("Uid",user.getUid());
    map.put("Email",user.getEmail());
    map.put("Name",user.getName());
    map.put("PhotoUrl",user.getPhotoUrl());
if (TextUtils.isEmpty(user.getPhotoUrl()))
    map.put("PhotoUrl"," ");
    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Friends").child(user.getUid()).setValue(map);
}







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.toolbar_items_addfreinds, menu);
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

        if (id == R.id.AddFreinds_Search) {
            return true;
        }


        return true;
    }



}
