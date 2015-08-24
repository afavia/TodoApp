package com.example.anthony.parsetodo.events;

/**
 * Created by affav_000 on 8/24/2015.
 */
public class TaskCreateEvent {
    public String Description;

    public TaskCreateEvent(String description) {
        Description = description;
    }
}
