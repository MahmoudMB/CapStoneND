package com.example.android.mytasks.Utilis;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.mytasks.Interfaces.FirebaseCallBacks;
import com.example.android.mytasks.Interfaces.UserLogin;
import com.example.android.mytasks.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SG on 7/24/2018.
 */

public class FirebaseManager {
    private volatile static FirebaseManager sFirebaseManager;
    private DatabaseReference mUserReference;
    private FirebaseCallBacks callBacks;
    private static FirebaseAuth mAuth;
    public static FirebaseManager getInstance(FirebaseCallBacks callBacks) {
        if(sFirebaseManager == null) {
            synchronized (FirebaseManager.class) {
                sFirebaseManager = new FirebaseManager(callBacks);
            }
        }
        return sFirebaseManager;
    }




    private FirebaseManager(FirebaseCallBacks callBacks)
    {
        mUserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mUserReference.keepSynced(true);
        this.callBacks = callBacks;
    }



    public static void createNewUser(String Email,String Pass,final String Name,String PhotoUrl,final UserLogin mUsersCallbacks)
    {
        Log.v("Result1",Email);
        mAuth = FirebaseAuth.getInstance();
        final boolean[] result = new boolean[1];
        mAuth.createUserWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.v("Result1",user.getEmail());
                    Log.v("Result2",user.getUid());
                    Map<String,Object> map=new HashMap<>();
                    map.put("Uid",user.getUid());
                    map.put("Name",Name);
                    map.put("Email",user.getEmail());
                    Log.v("Result3",user.getUid());
                    map.put("PhotoUrl"," ");
                    String keyToPush= mAuth.getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(keyToPush).setValue(map);
                    result[0] = true;

                    mUsersCallbacks.CheckStatusOfSignUp(true,"Succefful Sign Up");
                    Log.v("Result","Succeful");


                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(Name)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });



                }
                else
                {
                    Log.d("failed", "onComplete: Failed=" + task.getException().getMessage()); //ADD THIS

                    result[0] = false;
                    Log.v("Result","not succeful");
                    mUsersCallbacks.CheckStatusOfSignUp(false,task.getException().getMessage());
                }

            }
        });








//return result[0];

    }





    public static void GetTasksLists()
    {


    }


    public void createNewList1(String ListName,List<User> freindsList)
    {

        Map<String,Object> mapList=new HashMap<>();
        mapList.put("Name",ListName);
        Log.v("User Id",FirebaseAuth.getInstance().getCurrentUser().getUid());
        String keyToPush= FirebaseDatabase.getInstance().getReference().child("TasksList").push().getKey();
        mapList.put("ListID",keyToPush);
        mapList.put("OwnerUID",FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Lists").child(keyToPush).setValue(mapList);


        //FirebaseDatabase.getInstance().getReference().child("TasksList").child(keyToPush).setValue(mapList);

        for (int i=0;i<freindsList.size();i++)
        {

            Map<String,Object>        map=new HashMap<>();
            map.put("Uid",freindsList.get(i).getUid());
            map.put("Email",freindsList.get(i).getEmail());
            map.put("Name",freindsList.get(i).getName());
            map.put("PhotoUrl",freindsList.get(i).getPhotoUrl());
            if (TextUtils.isEmpty(freindsList.get(i).getPhotoUrl()))
                map.put("PhotoUrl"," ");
            FirebaseDatabase.getInstance().getReference().child("TasksList").child(keyToPush).child("Friends").child(freindsList.get(i).getUid()).setValue(map);
            FirebaseDatabase.getInstance().getReference().child("Users").child(freindsList.get(i).getUid()).child("Lists").child(keyToPush).setValue(mapList);
        }


        Map<String,Object>  map=new HashMap<>();
        map.put("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("Email",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        map.put("Name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        map.put("PhotoUrl",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()+"");
        if (TextUtils.isEmpty(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()+""))
            map.put("PhotoUrl"," ");


        FirebaseDatabase.getInstance().getReference().child("TasksList").child(keyToPush).child("Friends").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map);



    }



    public void createNewList(String ListName, List<User> freindsList)
    {



        Map<String,Object> map=new HashMap<>();
        map.put("Name",ListName);

        String keyToPush= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Lists").push().getKey();
        map.put("ListID",keyToPush);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Lists").child(keyToPush).setValue(map);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).keepSynced(true);







        for (int i=0;i<freindsList.size();i++)
        {

             map=new HashMap<>();
            map.put("Uid",freindsList.get(i).getUid());
            map.put("Email",freindsList.get(i).getEmail());
            map.put("Name",freindsList.get(i).getName());
            map.put("PhotoUrl",freindsList.get(i).getPhotoUrl());
            if (TextUtils.isEmpty(freindsList.get(i).getPhotoUrl()))
                map.put("PhotoUrl"," ");
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("TasksList").child(keyToPush).child("Friends").child(freindsList.get(i).getUid()).setValue(map);


        }


    }



    public void createNewTask(String TaskName,String ListID)
    {
        Map<String,Object> map=new HashMap<>();
        map.put("Name",TaskName);
        String keyToPush= FirebaseDatabase.getInstance().getReference().child("TasksList").child(ListID).push().getKey();
        map.put("TaskID",keyToPush);
        map.put("Status",false);
        FirebaseDatabase.getInstance().getReference().child("TasksList").child(ListID).child(keyToPush).setValue(map);
     //   FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).keepSynced(true);

    }
    public void ChangeTaskStatus(String TaskID,String ListID,Boolean Status)
    {
        Map<String,Object> map=new HashMap<>();
        map.put("Status",Status);
        FirebaseDatabase.getInstance().getReference().child("TasksList").child(ListID).child(TaskID).updateChildren(map);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).keepSynced(true);
    }




    public static void SignIn(String Email,String Pass,final UserLogin mUsersCallbacks)
    {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sign in", "signInWithEmail:success");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                  mUsersCallbacks.UpdateUiSignIn(user,"Succefful Sign in");
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Sign in", "signInWithEmail:failure", task.getException());

                    mUsersCallbacks.UpdateUiSignIn(null,task.getException().getMessage());
                }

            }
        });

    }








}
