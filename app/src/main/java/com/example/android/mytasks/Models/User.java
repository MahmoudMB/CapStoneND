package com.example.android.mytasks.Models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SG on 7/24/2018.
 */

public class User {


    private String Uid;
    private String Name;
    private String photoUrl;
    private String Email;


    public User(String uid, String Name, String photoUrl, String email) {
        Uid = uid;
        this.Name = Name;
        this.photoUrl = photoUrl;
        Email = email;
    }

    public User(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
        this.Uid=dataSnapshot.getKey();
        this.Name =object.get("Name").toString();
        this.photoUrl = object.get("photoUrl").toString();
        this.Email = object.get("Email").toString();
    }

    public User() {

    }
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.Name != other.Name && (this.Name == null || !this.Name.equals(other.Name))) {
            return false;
        }
        return true;
    }

@Override
    public int hashCode() {
        return Name.hashCode();
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }




}
