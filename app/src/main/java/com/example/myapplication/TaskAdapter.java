package com.example.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Task;
import com.example.myapplication.TaskViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private List<Task> allTasks = new ArrayList<>();
    private OnItemClickListener listener;
    private TaskViewModel taskViewModel;

    public TaskAdapter(TaskViewModel viewModel) {
        this.taskViewModel = viewModel;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDescription.setText(currentTask.getDescription());
        holder.textViewDueDate.setText("Due: " + currentTask.getDueDate());
        holder.textViewCategory.setText("Category: " + currentTask.getCategory());

        // Change priority indicator color
        int priorityColor = getPriorityColor(currentTask.getPriority(), holder.itemView.getContext());
        holder.viewPriority.setBackgroundColor(priorityColor);

        // Set checkbox state
        holder.checkBoxCompleted.setChecked(currentTask.isCompleted());

        // Strike-through text if completed
        if (currentTask.isCompleted()) {
            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // Handle checkbox click
        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            currentTask.setCompleted(isChecked);
            taskViewModel.update(currentTask);
        });

        // Handle task click for editing
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentTask);
            }
        });

        // Handle long press to delete
        holder.itemView.setOnLongClickListener(v -> {
            taskViewModel.delete(currentTask);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        this.allTasks = new ArrayList<>(tasks);
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        tasks = allTasks.stream()
                .filter(task -> task.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public void filterByPriority(int priority) {
        tasks = allTasks.stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle, textViewDescription, textViewDueDate, textViewCategory;
        private CheckBox checkBoxCompleted;
        private View viewPriority;

        public TaskViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewDueDate = itemView.findViewById(R.id.text_view_due_date);
            textViewCategory = itemView.findViewById(R.id.text_view_category);
            checkBoxCompleted = itemView.findViewById(R.id.checkbox_completed);
            viewPriority = itemView.findViewById(R.id.view_priority);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private int getPriorityColor(int priority, Context context) {
        switch (priority) {
            case 1: return ContextCompat.getColor(context, R.color.red);
            case 2: return ContextCompat.getColor(context, R.color.yellow);
            case 3: return ContextCompat.getColor(context, R.color.green);
            default: return ContextCompat.getColor(context, R.color.gray);
        }
    }
}
