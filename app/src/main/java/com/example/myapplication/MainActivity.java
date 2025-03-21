package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private FloatingActionButton buttonAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        buttonAddTask = findViewById(R.id.add_task_button);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        adapter = new TaskAdapter(taskViewModel);
        recyclerView.setAdapter(adapter);

        taskViewModel.getAllTasks().observe(this, adapter::setTasks);

        // Add Task Button
        buttonAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        adapter.setOnItemClickListener(task -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.putExtra("task_title", task.getTitle());
            intent.putExtra("task_description", task.getDescription());
            intent.putExtra("task_due_date", task.getDueDate());
            intent.putExtra("task_category", task.getCategory());
            intent.putExtra("task_priority", task.getPriority());
            startActivity(intent);
        });

        Spinner filterSpinner = findViewById(R.id.spinner_filter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = filterSpinner.getSelectedItem().toString();

                if (selectedFilter.equals("All")) {
                    adapter.setTasks(taskViewModel.getAllTasks().getValue());
                } else if (selectedFilter.equals("High Priority")) {
                    adapter.filterByPriority(1);
                } else if (selectedFilter.equals("Medium Priority")) {
                    adapter.filterByPriority(2);
                } else if (selectedFilter.equals("Low Priority")) {
                    adapter.filterByPriority(3);
                } else {
                    adapter.filterByCategory(selectedFilter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}


