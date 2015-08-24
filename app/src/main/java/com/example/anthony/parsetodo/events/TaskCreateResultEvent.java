package com.example.anthony.parsetodo.events;

/**
 * Created by affav_000 on 8/24/2015.
 */
public class TaskCreateResultEvent {
    public String msg;
    public boolean isSuccessful;

    public TaskCreateResultEvent(boolean isSuccess, String msg) {
        isSuccessful = isSuccess;
        this.msg = msg;
    }
}
