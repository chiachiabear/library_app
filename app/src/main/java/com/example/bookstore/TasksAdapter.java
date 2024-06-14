package com.example.bookstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TasksAdapter extends BaseAdapter {
    private Context context;
    private List<Task> lvTasks;
    public TasksAdapter(Context context, List<Task> lvTasks) {
        this.context = context;
        this.lvTasks = lvTasks;
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

        TextView tvTaskId = convertView.findViewById(R.id.tv_task_id);
        TextView tvTaskContent = convertView.findViewById(R.id.tv_task_content);

        Task task = lvTasks.get(position);
        tvTaskId.setText(task.getTask_id());
        tvTaskContent.setText(task.getTask_content());

        return convertView;
    }
}
