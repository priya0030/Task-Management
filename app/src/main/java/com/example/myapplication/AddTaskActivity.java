package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextDueDate, editTextCategory;
    private Spinner prioritySpinner;
    private Button buttonSave;
    private TaskViewModel taskViewModel;

    private int taskId = -1; // Used for editing existing tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextDueDate = findViewById(R.id.edit_text_due_date);
        editTextCategory = findViewById(R.id.edit_text_category);
        prioritySpinner = findViewById(R.id.spinner_priority);
        buttonSave = findViewById(R.id.button_save);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        ImageView back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> {
            finish(); // Close AddTaskActivity and go back to MainActivity
        });
        // Populate priority spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);

        // Set up date picker dialog for due date
        editTextDueDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTaskActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String dueDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        editTextDueDate.setText(dueDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Check if we are editing an existing task
        Intent intent = getIntent();
        if (intent.hasExtra("task_id")) {
            setTitle("Edit Task");
            taskId = intent.getIntExtra("task_id", -1);
            editTextTitle.setText(intent.getStringExtra("task_title"));
            editTextDescription.setText(intent.getStringExtra("task_description"));
            editTextDueDate.setText(intent.getStringExtra("task_due_date"));
            editTextCategory.setText(intent.getStringExtra("task_category"));
            prioritySpinner.setSelection(intent.getIntExtra("task_priority", 1) - 1); // Adjust for zero-based index
        } else {
            setTitle("Add Task");
        }

        buttonSave.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String dueDate = editTextDueDate.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        int priority = prioritySpinner.getSelectedItemPosition() + 1; // Convert index to priority (1, 2, 3)

        if (title.isEmpty()) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(title, description, priority, dueDate, category, false);

        if (taskId != -1) { // Editing an existing task
            task.setId(taskId);
            taskViewModel.update(task);
        } else { // Adding a new task
            taskViewModel.insert(task);
        }

        finish();
    }
}

