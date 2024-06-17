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
public class PublishTasksAdapter extends BaseAdapter{
    private Context context;
    private List<Task> lvTasks;
    private String userId;
    private Publish_Task activity;
    private Publish_Task.ListFragment listFragment;

    public PublishTasksAdapter(Context context, List<Task> lvTasks, String userId, Publish_Task activity, Publish_Task.ListFragment listFragment) {
        this.context = context;
        this.lvTasks = lvTasks;
        this.userId = userId;
        this.activity = activity;
        this.listFragment = listFragment;
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
        TextView tvTaskTime = convertView.findViewById(R.id.tv_task_time);
        TextView tvTaskContent = convertView.findViewById(R.id.tv_task_content);
        TextView tvTaskNumber = convertView.findViewById(R.id.tv_task_number);

        Task task = lvTasks.get(position);

        tvTaskTime.setText(task.getStart_time()+"~"+task.getEnd_time());
        tvTaskContent.setText(task.getTask_content());
        tvTaskNumber.setText(String.valueOf(task.getNumber_of_recruits()));
        if (task.getNumber_of_recruits() == 0) {
            btnAcceptTask.setText("招募成功");
            convertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAcceptedTask));
        } else {
            btnAcceptTask.setText("取消發布");
        }
        btnAcceptTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (task.getNumber_of_recruits() == 0) {

                } else {
                    activity.removeFromTaskList(userId, Integer.parseInt(task.getTask_id()));

                }

                if (listFragment != null) {
                    listFragment.updateTaskList();
                }
                activity.tvTaskPubliseDate.setText("已發布日期:"+activity.getPublisherActiveTasks(activity.getUserIDFromPreferences(),activity.formattedDate));

            }
        });

        return convertView;

    }
}