package com.example.android.mytasks.Models;

/**
 * Created by SG on 7/29/2018.
 */

public class Member {

    private String Uid;
    private String Name;
    private String photoUrl;
    private String Email;
    private String Status;


    public Member(String uid, String name, String photoUrl, String email, String status) {
        Uid = uid;
        Name = name;
        this.photoUrl = photoUrl;
        Email = email;
        Status = status;
    }


    public Member() {
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

    public void setName(String name) {
        Name = name;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
