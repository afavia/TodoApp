package com.example.anthony.parsetodo.events;

/**
 * Created by Anthony on 8/24/2015.
 */
public class RegisterEvent {
    public String Username;
    public String Password;

    public RegisterEvent(String username, String password) {
        Username = username;
        Password = password;
    }
}
