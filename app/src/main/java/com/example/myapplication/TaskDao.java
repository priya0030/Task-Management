package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY priority ASC, dueDate ASC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY priority ASC")
    LiveData<List<Task>> getTasksByCategory(String category);
}



