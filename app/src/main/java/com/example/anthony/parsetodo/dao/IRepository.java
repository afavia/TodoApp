package com.example.anthony.parsetodo.dao;

import com.example.anthony.parsetodo.models.Task;
import java.util.List;

/**
 * Created by Anthony on 8/17/2015.
 */
public interface IRepository {

    public List<Task> getTasks();

    public void addTask(Task task);
}
