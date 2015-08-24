package com.example.anthony.parsetodo.events;

/**
 * Created by Anthony on 8/24/2015.
 */
public class LoginResultEvent {
    public String msg;
    public boolean isSuccessful;

    public LoginResultEvent(boolean isSuccess, String msg) {
        isSuccessful = isSuccess;
        this.msg = msg;
    }
}
