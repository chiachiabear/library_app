package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Publish_Task extends AppCompatActivity {


    private CalendarView calendarView;
    private TextView tvTaskListDate;
    public static TextView tvTaskPubliseDate;
    public String formattedDate;

    private Button BtnToText;
    //private ListView lvTask;
    private static SQLiteDatabase productDatabase;

    public static FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task);
        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);
        fragmentManager = getSupportFragmentManager();
        calendarView = findViewById(R.id.calendarView2);
        BtnToText=findViewById(R.id.btn_to_text_fragment);
        tvTaskListDate = findViewById(R.id.tv_task_list_date2);
        tvTaskPubliseDate = findViewById(R.id.tv_task_publish_date);
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
                //updateTaskList(formattedDate);
                showListFragment(formattedDate);
            }
        });

        tvTaskPubliseDate.setText("已發布日期:"+getPublisherActiveTasks(this.getUserIDFromPreferences(),formattedDate));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextFragment(formattedDate);
                tvTaskPubliseDate.setText("已發布日期:"+getPublisherActiveTasks(getUserIDFromPreferences(),formattedDate));
            }
        };
        showListFragment(formattedDate);
        BtnToText.setOnClickListener(listener);
        showNavigationFragment();

    }

    public static String getPublisherActiveTasks(String userId, String today) {
        StringBuilder activeTasks = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputSdf = new SimpleDateFormat("MM-dd", Locale.getDefault());

        String query = "SELECT DISTINCT release_date " +
                "FROM task_list " +
                "WHERE publisher_id = ? AND release_date >= ?" +
                "ORDER BY release_date";
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

    // ListFragment
    public static class ListFragment extends Fragment {

        //private ArrayList<String> items;
        //private ArrayAdapter<String> adapter;
        private ListView listView;
        private PublishTasksAdapter adapter;
        private ArrayList<Task> tasks;
        private SQLiteDatabase productDatabase;
        //private String formattedDate = "";

        private String formattedDate;
        private static final String ARG_DATE = "date";


        public static ListFragment newInstance(String date) {
            ListFragment fragment = new ListFragment();
            Bundle args = new Bundle();
            args.putString(ARG_DATE, date);
            fragment.setArguments(args);
            Log.d("ListFragment", "newInstance: formattedDate = " + date);
            return fragment;
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_list, container, false);
            listView = view.findViewById(R.id.lv_publish_task);
            tasks = new ArrayList<>();
            if (getArguments() != null) {
                formattedDate = getArguments().getString(ARG_DATE);
            }
            updateTaskList();
            if (adapter == null) {
                adapter = new PublishTasksAdapter(getActivity(), tasks, ((Publish_Task) getActivity()).getUserIDFromPreferences(), (Publish_Task) getActivity(),this);
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged(); // adpter not null
            }

            return view;
        }

        public void updateTaskList() {
            if (formattedDate == null) {
                return;  // formattDate為0退出
            }

            tasks.clear();
            Cursor cursor = Publish_Task.productDatabase.rawQuery("SELECT task_id, task_content, task_start_time, task_end_time, number_of_recruits FROM task_list WHERE release_date = ? AND publisher_id = ?;", new String[]{formattedDate,((Publish_Task) getActivity()).getUserIDFromPreferences()});

            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

    }

    // TextFragment
    public static class TextFragment extends Fragment {

        private String formattedDate;
        private static final String ARG_DATE = "date";


        public static TextFragment newInstance(String date) {
            TextFragment fragment = new TextFragment();
            Bundle args = new Bundle();
            args.putString(ARG_DATE, date);
            fragment.setArguments(args);
            Log.d("ListFragment", "newInstance: formattedDate = " + date);
            return fragment;
        }
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_text, container, false);
            if (getArguments() != null) {
                formattedDate = getArguments().getString(ARG_DATE);
            }


            TextView StartTime = view.findViewById(R.id.fragment_text_start_time);
            TextView EndTime = view.findViewById(R.id.fragment_text_end_time);
            TextView Number = view.findViewById(R.id.fragment_text_number);
            TextView Content = view.findViewById(R.id.fragment_text_content);

            TextView TvStartTime = view.findViewById(R.id.publish_tv_start_time);
            TextView TvEndTime = view.findViewById(R.id.publish_tv_end_time);
            TextView TvNumber = view.findViewById(R.id.publish_tv_number);
            TextView TvContent = view.findViewById(R.id.publish_tv_content);

            Button BtnAddTask = view.findViewById(R.id.btn_add_task);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String startTime = TvStartTime.getText().toString().trim();
                    String endTime = TvEndTime.getText().toString().trim();
                    int number = Integer.parseInt(TvNumber.getText().toString().trim());
                    String content = TvContent.getText().toString().trim();

                    AddtoTaskList(formattedDate, startTime, content, endTime, ((Publish_Task) getActivity()).getUserIDFromPreferences(), number);
                    ((Publish_Task) getActivity()).tvTaskPubliseDate.setText("已發布日期:"+ ((Publish_Task) getActivity()).getPublisherActiveTasks(((Publish_Task) getActivity()).getUserIDFromPreferences(),formattedDate));
                    showListFragment(formattedDate);

                }
            };
            BtnAddTask.setOnClickListener(listener);
            return view;
        }
    }


    public void removeFromTaskList(String publisher_id , int taskId) {
        productDatabase.execSQL(
                "DELETE FROM task_list WHERE publisher_id  = ? AND task_id = ?;",
                new Object[]{publisher_id , taskId}
        );
    }
    public static void showListFragment(String date) {

        ListFragment listFragment = ListFragment.newInstance(date);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, listFragment);
        fragmentTransaction.commit();
    }
    public void showTextFragment(String date) {
        //FragmentManager fragmentManager = getSupportFragmentManager();
        TextFragment textFragment = TextFragment.newInstance(date);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, textFragment);
        fragmentTransaction.addToBackStack(null); // 将事务添加到返回栈，使得可以返回到 ListFragment
        fragmentTransaction.commit();
    }
    public void showNavigationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_publish_task, new MainActivity.MainNavigationFragment());
        fragmentTransaction.commit();
    }
    public  String getUserIDFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("userid", "Default User");
    }
    public static void AddtoTaskList(String release_date, String task_start_time, String task_content, String task_end_time, String publisher_id, int number_of_recruits){
        productDatabase.execSQL(
                "INSERT INTO task_list (release_date, task_start_time, task_content, task_end_time, publisher_id, number_of_recruits) " +
                        "VALUES (?, ?, ?, ?, ?, ?);",
                new Object[]{release_date, task_start_time, task_content, task_end_time, publisher_id, number_of_recruits}
        );
    }

}