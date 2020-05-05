package com.ynov.mobileproject.models.todolist;

import java.util.Date;
import java.util.List;

public class ToDoUser {
    private int userId;
    private List<ToDoTask> tasks;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ToDoTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ToDoTask> tasks) {
        this.tasks = tasks;
    }
}

