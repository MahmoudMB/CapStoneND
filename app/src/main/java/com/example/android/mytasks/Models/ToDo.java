package com.example.android.mytasks.Models;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SG on 7/24/2018.
 */

public class ToDo implements Serializable {

private String ListID;
private String Name;
private String OwnerUID;
private List<Task> Tasks;


    public String getListID() {
        return ListID;
    }

    public void setListID(String listID) {
        ListID = listID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Task> getTasks() {
        return Tasks;
    }

    public void setTasks(List<Task> tasks) {
        Tasks = tasks;
    }


    public ToDo() {
    }


    public ToDo(String listID, String name,String ownerUID) {
        ListID = listID;
        Name = name;
        OwnerUID = ownerUID;
    }



    public ToDo(DataSnapshot dataSnapshot){
        HashMap<String, Object> object = (HashMap<String, Object>) dataSnapshot.getValue();
        this.ListID=dataSnapshot.getKey();
        this.Name =object.get("Name").toString();
        this.Tasks = new ArrayList<>();
        this.OwnerUID = object.get("OwnerUID").toString();}



    public String getOwnerUID() {
        return OwnerUID;
    }

    public void setOwnerUID(String ownerUID) {
        OwnerUID = ownerUID;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ToDo other = (ToDo) obj;
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
