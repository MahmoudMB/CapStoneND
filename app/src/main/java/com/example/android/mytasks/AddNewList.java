package com.example.android.mytasks;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.android.mytasks.Interfaces.FirebaseCallBacks;
import com.example.android.mytasks.Models.ToDo;
import com.example.android.mytasks.Models.User;
import com.example.android.mytasks.Utilis.FirebaseManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewList extends AppCompatActivity implements FirebaseCallBacks {


    private RecyclerView recycleriew;
    FirebaseRecyclerAdapter adapter;
    List<User> freindsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewlist);
        ButterKnife.bind(this);


        freindsList = new ArrayList<>();


        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child(getResources().getString(R.string.Users_Node)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getResources().getString(R.string.Friends_Node));

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


                if (freindsList.contains(user)){

                    holder.Status.setImageResource(R.drawable.ic_round_check_circle_24px);
                    holder.Status.setTag("ic_round_check_circle_24px");

                }
                else {
                    holder.Status.setImageResource(R.drawable.ic_round_check_circle_outline_24px);

                    holder.Status.setTag("ic_round_check_circle_outline_24px");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (holder.Status.getTag().equals("ic_round_check_circle_outline_24px"))
                        {

                            holder.Status.setImageResource(R.drawable.ic_round_check_circle_24px);
                            freindsList.add(user);
                            holder.Status.setTag("ic_round_check_circle_24px");

                        }
                        else
                        {


                            holder.Status.setImageResource(R.drawable.ic_round_check_circle_outline_24px);
                            freindsList.remove(user);
                            holder.Status.setTag("ic_round_check_circle_outline_24px");

                        }


                    }
                });

            }
        };



        recycleriew = (RecyclerView)findViewById(R.id.AddListMembers_Recycler);
        recycleriew.setAdapter(adapter);
        recycleriew.setLayoutManager(new LinearLayoutManager(this));






        Toolbar mtoolbar = (Toolbar) findViewById(R.id.AddNewList_toolbar);
        mtoolbar.setTitle(getResources().getString(R.string.AddNewList_ScreenTitle));

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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



    @OnClick(R.id.addNewList_CreateList) void addNewList() {


        String listName = ((AppCompatEditText)findViewById(R.id.addNewList_ListNameEditText)).getText().toString().trim();
        if (!TextUtils.isEmpty(listName))
        {
            AddNewTaskList();
            finish();}
        else
        {

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.AddNewList__Msg1), Snackbar.LENGTH_LONG);
            snackbar.show();

        }


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


    public void AddNewTaskList()
    {
        String t = ((AppCompatEditText)findViewById(R.id.addNewList_ListNameEditText)).getText().toString();
        FirebaseManager.getInstance(this).createNewList1(t,freindsList);
    }

}
