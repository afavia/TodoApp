package com.example.anthony.parsetodo.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.Date;

/**
 * Created by anthony on 8/13/15.
 */
@ParseClassName("Task")
public class Task extends ParseObject {
    public Task() {

    }

    public boolean isCompleted() {
        return getBoolean("completed");
    }

    public void setCompleted(boolean complete) {
        put("completed", complete);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
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

    public void setNotes(String notes) {
        put("notes", notes);
    }

    public String getNotes() {
        return getString("notes");
    }

}

