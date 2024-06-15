package com.example.bookstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class TasksAdapter extends BaseAdapter {
    private Context context;
    private List<Task> lvTasks;
    private String userId;
    private Task_list activity;

    public TasksAdapter(Context context, List<Task> lvTasks, String userId, Task_list activity) {
        this.context = context;
        this.lvTasks = lvTasks;
        this.userId = userId;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return lvTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return lvTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_tasklist_layout,parent,false);
        }

        Button btnAcceptTask = convertView.findViewById(R.id.btn_accept_task);
        TextView tvTaskId = convertView.findViewById(R.id.tv_task_id);
        TextView tvTaskContent = convertView.findViewById(R.id.tv_task_content);
        TextView tvTaskNumber = convertView.findViewById(R.id.tv_task_number);

        Task task = lvTasks.get(position);

        tvTaskId.setText(task.getTask_id());
        tvTaskContent.setText(task.getTask_content());
        tvTaskNumber.setText(String.valueOf(task.getNumber_of_recruits()));

        if (activity.isUserTask(userId, Integer.parseInt(task.getTask_id()))) {
            btnAcceptTask.setText("取消任務");
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAcceptedTask));
        } else {
            if (task.getNumber_of_recruits() == 0) {
                btnAcceptTask.setText("已無餘額");
            } else {
                btnAcceptTask.setText("接受任務");
            }
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDefaultTask));
        }

        btnAcceptTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.isUserTask(userId, Integer.parseInt(task.getTask_id()))) {
                    activity.removeFromPersonalTasks(userId, Integer.parseInt(task.getTask_id()));
                } else if (task.getNumber_of_recruits() != 0) {
                    activity.addToPersonalTasks(userId, Integer.parseInt(task.getTask_id()));
                }
                activity.updateTaskList();
                //notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
