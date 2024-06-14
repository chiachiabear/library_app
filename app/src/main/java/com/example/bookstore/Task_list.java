package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        String formattedDate = sdf.format(currentDate.getTime());
        tvTaskListDate.setText(formattedDate+"任務清單");
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                String formattedDate = sdf.format(selectedDate.getTime());
                tvTaskListDate.setText(formattedDate+"任務清單");
                updateTaskList(formattedDate);
            }
        });
    }
    private void updateTaskList(String formattedDate) {
        Cursor cursor = productDatabase.rawQuery("SELECT task_id, task_content FROM task_list WHERE release_date = ?", new String[]{formattedDate});
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(0), cursor.getString(1));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        TasksAdapter adapter = new TasksAdapter(this, tasks);
        lvTask.setAdapter(adapter);
    }
}