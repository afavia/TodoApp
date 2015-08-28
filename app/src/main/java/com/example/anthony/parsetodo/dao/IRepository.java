package com.example.anthony.parsetodo.dao;

import com.example.anthony.parsetodo.models.Objective;
import com.example.anthony.parsetodo.models.Task;
import java.util.List;

/**
 * Created by Anthony on 8/17/2015.
 */
public interface IRepository {

    List<Task> getTasks();

    void addTask(Task task);

    void update(Task task, int position);

    List<Objective> getObjectives();

    void addObjective(Objective objective);

    void update(Objective objective, int position);
}
