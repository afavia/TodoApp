package com.example.anthony.parsetodo.dao;

import com.example.anthony.parsetodo.models.Objective;
import com.example.anthony.parsetodo.models.Task;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anthony on 8/17/2015.
 */
public class ParseRepository implements IRepository {
    @Override
    public List<Task> getTasks() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addTask(Task task) {
        task.setACL(new ParseACL(ParseUser.getCurrentUser()));
        task.setUser(ParseUser.getCurrentUser());
        try {
            task.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Task task, int position) {
        if (position >= 0 && position < getTasks().size())
            getTasks().set(position, task);
    }

    @Override
    public List<Objective> getObjectives() {
        ParseQuery<Objective> query = ParseQuery.getQuery(Objective.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ONLY);
        try {
            return query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void addObjective(Objective objective) {
        objective.setACL(new ParseACL(ParseUser.getCurrentUser()));
        objective.setUser(ParseUser.getCurrentUser());
        try {
            objective.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Objective objective, int position) {
        if (position >= 0 && position < getObjectives().size()) {
            getObjectives().set(position, objective);
        }
    }
}
