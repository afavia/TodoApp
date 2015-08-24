package com.example.anthony.parsetodo.events;

/**
 * Created by Anthony on 8/24/2015.
 */
public class LogoutResultEvent {
    public String msg;
    public boolean isSuccessful;

    public LogoutResultEvent(boolean isSuccess, String msg) {
        isSuccessful = isSuccess;
        this.msg = msg;
    }
}
