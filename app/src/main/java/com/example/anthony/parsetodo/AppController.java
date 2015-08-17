package com.example.anthony.parsetodo;

import android.app.Application;
import android.support.v4.content.LocalBroadcastManager;

import com.example.anthony.parsetodo.dao.IRepository;
import com.example.anthony.parsetodo.dao.ParseRepository;
import com.example.anthony.parsetodo.models.Task;

import java.util.List;

/**
 * Created by Anthony on 8/17/2015.
 */
public class AppController extends Application{

    private IRepository repo;

    public AppController() {
        repo = new ParseRepository();
    }

    public List<Task> getTasks() {
        return repo.getTasks();
    }

    public void addTask(Task task) {
        repo.addTask(task);
    }


    public void addTask(String description, boolean complete) {
        Task t = new Task();
        t.setCompleted(complete);
        t.setDescription(description);
        repo.addTask(t);
    }

    public Task getTask(int position) {
        if (position < repo.getTasks().size()) return repo.getTasks().get(position);
        else return new Task();
    }
}
