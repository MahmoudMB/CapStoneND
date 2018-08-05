package com.example.android.mytasks.Models;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SG on 7/24/2018.
 */

public class Task implements Serializable {


    private String TaskID;
    private String Name;
    private String Description;
    private String Date;
    private boolean Status;

    public Task(String taskID, String name, String description, String date, boolean status) {
        TaskID = taskID;
        Name = name;
        Description = description;
        Date = date;
        Status = status;
    }

    public Task(String taskID, String name, boolean status) {
        TaskID = taskID;
        Name = name;

        Status = status;
    }

    public Task(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
        this.TaskID=dataSnapshot.getKey();
        this.Name =object.get("Name").toString();
      //  this.Description =object.get("Description").toString();
       // this.Date =object.get("Date").toString();

/*
        String Statustring  = object.get("Status").toString();

        if (Statustring.equals("TRUE"))
            this.Status =true;
            else if (Statustring.equals("FALSE"))*/


        this.Status =false;

    }



    public Task() {
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }



    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if (this.Name != other.Name && (this.Name == null || !this.Name.equals(other.Name))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Name.hashCode();
    }

}
