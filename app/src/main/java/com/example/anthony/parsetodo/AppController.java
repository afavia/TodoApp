package com.example.anthony.parsetodo;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.anthony.parsetodo.dao.IRepository;
import com.example.anthony.parsetodo.dao.ParseRepository;
import com.example.anthony.parsetodo.events.LoginEvent;
import com.example.anthony.parsetodo.events.LoginResultEvent;
import com.example.anthony.parsetodo.events.LogoutEvent;
import com.example.anthony.parsetodo.events.LogoutResultEvent;
import com.example.anthony.parsetodo.events.RegisterEvent;
import com.example.anthony.parsetodo.events.RegisterResultEvent;
import com.example.anthony.parsetodo.events.TaskCreateEvent;
import com.example.anthony.parsetodo.events.TaskCreateResultEvent;
import com.example.anthony.parsetodo.models.Objective;
import com.example.anthony.parsetodo.models.Task;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Anthony on 8/17/2015.
 */
public class AppController extends Application {

    private IRepository repo;

    public static Bus bus = new Bus(ThreadEnforcer.MAIN);

    public AppController() {
        repo = new ParseRepository();
        bus.register(this);
    }

    public List<Task> getTasks() {
        return repo.getTasks();
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
        repo.update(task, taskPos);
    }

    public List<Objective> getObjectives() {
        return repo.getObjectives();
    }

    public Objective getObjective(int position) {
        if (position < repo.getObjectives().size()) return repo.getObjectives().get(position);
        else return new Objective();
    }
    public void addObjective(String name, boolean isComplete, Date dueDate) {
        try {


            Objective o = new Objective();
            o.setName(name);
            o.setCompleted(isComplete);
            o.setDueDate(dueDate);
            repo.addObjective(o);
        } catch (Exception e) {

            Log.e("APP", e.getMessage());
        }
    }

    public void updateObjective(Objective objective, int objectivePos) {
        repo.update(objective, objectivePos);
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
                                    msg = "Sorry this username is already taken";
                                    break;
                                case ParseException.USERNAME_MISSING:
                                    msg = "Sorry you must supply a username to register";
                                    break;
                                case ParseException.PASSWORD_MISSING:
                                    msg = "Sorry you must supply a password to register";
                                    break;
                                case ParseException.OBJECT_NOT_FOUND:
                                    msg = "Sorry, those credentials were invalid.";
                                    break;
                                default:
                                    msg = e.getLocalizedMessage();
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
                } else {
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

    @Subscribe
    public void onCreateTaskEvent(TaskCreateEvent event) {
        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                String task = params[0];
                if (task.length() > 0) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    addTask(task, false, cal.getTime());
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    bus.post(new TaskCreateResultEvent(true, ""));
                }
            }
        }.execute(event.Description);
    }

    public void initParse(Intent intent) {
        //        ParseCrashReporting.enable(this);
        Parse.initialize(this, getString(R.string.app_id), getString(R.string.client_id));
        ParseAnalytics.trackAppOpenedInBackground(intent);
        ParseObject.registerSubclass(Task.class);
        ParseObject.registerSubclass(Objective.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        for (int i = 0; i < 100; i++) {
            StringBuilder b = new StringBuilder();
            b.append("Objective ");
            b.append(i + 1);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.WEEK_OF_YEAR, 3);

            addObjective(b.toString(), false, cal.getTime());
        }
    }
}
