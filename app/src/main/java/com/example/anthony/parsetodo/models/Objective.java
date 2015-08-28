package com.example.anthony.parsetodo.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Anthony on 8/27/2015.
 */
@ParseClassName("Objective")
public class Objective extends ParseObject {
    public Objective() {

    }

    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }
    public void setUser(ParseUser currentUser) {
        put("user", currentUser);
    }

    public void setDueDate(Date dueDate) {
        put("due_date", dueDate);
    }

    public Date getDueDate() {
        return getDate("due_date");
    }

    public boolean isCompleted() {
        return getBoolean("completed");
    }

    public void setCompleted(boolean complete) {
        put("completed", complete);
    }

}
