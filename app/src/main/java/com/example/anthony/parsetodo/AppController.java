package com.example.anthony.parsetodo;

import android.app.Application;
import android.content.Intent;

import com.example.anthony.parsetodo.activities.LoginActivity;
import com.example.anthony.parsetodo.activities.RegisterActivity;
import com.example.anthony.parsetodo.activities.TodoActivity;
import com.example.anthony.parsetodo.dao.IRepository;
import com.example.anthony.parsetodo.dao.ParseRepository;
import com.example.anthony.parsetodo.events.LoginEvent;
import com.example.anthony.parsetodo.events.LoginResultEvent;
import com.example.anthony.parsetodo.events.LogoutEvent;
import com.example.anthony.parsetodo.events.LogoutResultEvent;
import com.example.anthony.parsetodo.events.RegisterEvent;
import com.example.anthony.parsetodo.events.RegisterResultEvent;
import com.example.anthony.parsetodo.models.Task;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.Date;
import java.util.List;

/**
 * Created by Anthony on 8/17/2015.
 */
public class AppController extends Application{

    private IRepository repo;

    public static Bus bus = new Bus(ThreadEnforcer.MAIN);

    public AppController() {
        repo = new ParseRepository();
        bus.register(this);
    }

    public List<Task> getTasks() {
        return repo.getTasks();
    }

    public void addTask(Task task) {
        repo.addTask(task);
    }


    public void addTask(String description, boolean complete, Date dueDate) {
        Task t = new Task();
        t.setCompleted(complete);
        t.setDescription(description);
        t.setDueDate(dueDate);
        repo.addTask(t);
    }

    public Task getTask(int position) {
        if (position < repo.getTasks().size()) return repo.getTasks().get(position);
        else return new Task();
    }

    public void updateTask(Task task, int taskPos) {
        repo.updateTask(task, taskPos);

    }

    @Subscribe
    public void performAction(LogoutEvent event) {
        ParseUser.logOut();
        bus.post(new LogoutResultEvent(true, ""));
    }

    @Subscribe
    public void login(LoginEvent loginEvent) {
        ParseUser.logInInBackground(loginEvent.Username,
                loginEvent.Password,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            bus.post(new LoginResultEvent(true, ""));
                        } else {
                            String msg = "";
                            switch (e.getCode()) {
                                case ParseException.USERNAME_TAKEN:
                                    msg ="Sorry this username is already taken";
                                    break;
                                case ParseException.USERNAME_MISSING:
                                    msg ="Sorry you must supply a username to register";
                                    break;
                                case ParseException.PASSWORD_MISSING:
                                    msg ="Sorry you must supply a password to register";
                                    break;
                                case ParseException.OBJECT_NOT_FOUND:
                                    msg ="Sorry, those credentials were invalid.";
                                    break;
                                default:
                                    msg =e.getLocalizedMessage();
                                    break;
                            }
                            bus.post(new LoginResultEvent(false, msg));
                        }
                    }
                });
    }

    @Subscribe
    public void onRegisterEvent(RegisterEvent event) {
        ParseUser user = new ParseUser();
        user.setUsername(event.Username);
        user.setPassword(event.Password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    bus.post(new RegisterResultEvent(true, ""));
                }
                else {
                    String msg = "";
                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            msg = "Sorry this username is already taken";
                            break;
                        case ParseException.USERNAME_MISSING:
                            msg = "Sorry you must supply a username to register";
                            break;
                        case ParseException.PASSWORD_MISSING:
                            msg = "Sorry you must supply a password to register";
                            break;
                        default:
                            msg = e.getLocalizedMessage();
                            break;
                    }
                    bus.post(new RegisterResultEvent(false, msg));
                }
            }
        });
    }
}
