package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;


public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getInstance(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        new Thread(() -> taskDao.insert(task)).start();
    }

    public void update(Task task) {
        new Thread(() -> taskDao.update(task)).start();
    }

    public void delete(Task task) {
        new Thread(() -> taskDao.delete(task)).start();
    }

    public LiveData<List<Task>> getTasksByCategory(String category) {
        return taskDao.getTasksByCategory(category);
    }
}
