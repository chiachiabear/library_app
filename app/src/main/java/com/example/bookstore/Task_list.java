package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Task_list extends AppCompatActivity {
    private CalendarView calendarView ;
    private TextView tvTaskListDate;
    private ListView lvTask;
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
                tvTaskListDate.setText(formattedDate+"任務清單"+getUserIDFromPreferences());
                updateTaskList();
            }
        });
    }
    private String getUserIDFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("userid", "Default User");
    }

    public void updateTaskList() {
        Cursor cursor = productDatabase.rawQuery("SELECT task_id, task_content, number_of_recruits FROM task_list WHERE release_date = ?", new String[]{formattedDate});
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(0), cursor.getString(1),cursor.getInt(2));
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

}