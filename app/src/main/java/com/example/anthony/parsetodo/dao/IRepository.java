package com.example.anthony.parsetodo.dao;

import com.example.anthony.parsetodo.models.Task;
import java.util.List;

/**
 * Created by Anthony on 8/17/2015.
 */
public interface IRepository {

    List<Task> getTasks();

    void addTask(Task task);

    void updateTask(Task task, int position);
}
