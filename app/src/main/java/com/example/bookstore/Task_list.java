package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Task_list extends AppCompatActivity {
    private CalendarView calendarView ;
    private TextView tvTaskListDate;
    private TextView tvNoteTask;
    private ListView lvTask;
    private Button btnGotoPublish;
    private SQLiteDatabase productDatabase;
    private String formattedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);
        calendarView = findViewById(R.id.calendarView);
        tvTaskListDate = findViewById(R.id.tv_task_list_date);
        lvTask = findViewById(R.id.lv_task);
        tvNoteTask = findViewById(R.id.tv_task_note);
        btnGotoPublish = findViewById(R.id.btn_go_to_publish_task);
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = sdf.format(currentDate.getTime());
        tvTaskListDate.setText(formattedDate+"任務清單");
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                formattedDate = sdf.format(selectedDate.getTime());
                tvTaskListDate.setText(formattedDate+"任務清單");
                updateTaskList();

            }

        });
        btnGotoPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Task_list.this, Publish_Task.class);
                startActivity(intent);
            }
        });
        tvNoteTask.setText("任務提醒:"+getUserActiveTasks(getUserIDFromPreferences(),formattedDate));

        showNavigationFragment();
    }
    private String getUserIDFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("userid", "Default User");
    }

    public void updateTaskList() {
        Cursor cursor = productDatabase.rawQuery("SELECT task_id, task_content, task_start_time,task_end_time,number_of_recruits FROM task_list WHERE release_date = ?", new String[]{formattedDate});
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        TasksAdapter adapter = new TasksAdapter(this, tasks, getUserIDFromPreferences(), this);
        lvTask.setAdapter(adapter);
    }
    public void addToPersonalTasks(String receiverId, int taskId) {
        productDatabase.execSQL(
                "INSERT INTO personal_tasks (receiver_id, task_id) VALUES (?, ?);",
                new Object[]{receiverId, taskId}
        );
        productDatabase.execSQL(
                "UPDATE task_list SET number_of_recruits = number_of_recruits - 1 WHERE task_id = ?;",
                new Object[]{taskId}
        );
        tvNoteTask.setText("任務提醒:"+getUserActiveTasks(getUserIDFromPreferences(),formattedDate));
    }

    public void removeFromPersonalTasks(String receiverId, int taskId) {
        productDatabase.execSQL(
                "DELETE FROM personal_tasks WHERE receiver_id = ? AND task_id = ?;",
                new Object[]{receiverId, taskId}
        );
        productDatabase.execSQL(
                "UPDATE task_list SET number_of_recruits = number_of_recruits + 1 WHERE task_id = ?;",
                new Object[]{taskId}
        );
        tvNoteTask.setText("任務提醒:"+getUserActiveTasks(getUserIDFromPreferences(),formattedDate));
    }

    public boolean isUserTask(String userId, int taskId) {
        Cursor cursor = productDatabase.rawQuery(
                "SELECT * FROM personal_tasks WHERE receiver_id = ? AND task_id = ?",
                new String[]{userId, String.valueOf(taskId)}
        );
        boolean isAccepted = cursor.moveToFirst();
        cursor.close();
        return isAccepted;
    }
    public boolean isFeatureTask(int taskId, String today) {
        Cursor cursor = productDatabase.rawQuery(
                "SELECT task_id FROM task_list WHERE task_id = ? AND release_date > ?;",
                new String[]{String.valueOf(taskId), today}
        );
        boolean isTrue = cursor.moveToFirst();
        cursor.close();
        return isTrue;
    }
    public String getUserActiveTasks(String userId, String today) {
        StringBuilder activeTasks = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputSdf = new SimpleDateFormat("MM-dd", Locale.getDefault());

        // 查询使用者的任务且未过期的任务
        String query = "SELECT DISTINCT t.release_date " +
                "FROM task_list t " +
                "JOIN personal_tasks p ON t.task_id = p.task_id " +
                "WHERE p.receiver_id = ? AND t.release_date >= ?" +
                "ORDER BY t.release_date";
        Cursor cursor = productDatabase.rawQuery(query, new String[]{userId, today});

        if (cursor.moveToFirst()) {
            do {
                String taskDate = cursor.getString(0);
                try {
                    // 解析日期并转换格式
                    String formattedDate = outputSdf.format(sdf.parse(taskDate));
                    activeTasks.append(formattedDate).append(" ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return activeTasks.toString().trim();
    }

    public void showNavigationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_task_list, new MainActivity.MainNavigationFragment());
        fragmentTransaction.commit();
    }

}