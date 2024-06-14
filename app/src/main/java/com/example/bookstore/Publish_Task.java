package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.ArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Publish_Task extends AppCompatActivity {


    private CalendarView calendarView;
    private TextView tvTaskListDate;
    private Button BtnToText;
    //private ListView lvTask;
    private SQLiteDatabase productDatabase;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task);
        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);
        fragmentManager = getSupportFragmentManager();
        calendarView = findViewById(R.id.calendarView2);
        BtnToText=findViewById(R.id.btn_to_text_fragment);
        tvTaskListDate = findViewById(R.id.tv_task_list_date2);
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
                //updateTaskList(formattedDate);
                showListFragment();
            }
        });


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextFragment();
            }
        };
        BtnToText.setOnClickListener(listener);
    }


    // ListFragment
    public static class ListFragment extends Fragment {

        private ArrayList<String> items;
        private ArrayAdapter<String> adapter;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_list, container, false);

            ListView listView = view.findViewById(R.id.lv_publish_task);
            //String[] items = {"Item 1", "Item 2", "Item 3"};
            items = new ArrayList<>(Arrays.asList("Item 1", "Item 2", "Item 3"));
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
            return view;
        }
    }

    // TextFragment
    public static class TextFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_text, container, false);

            TextView StartTime = view.findViewById(R.id.fragment_text_start_time);
            TextView EndTime = view.findViewById(R.id.fragment_text_end_time);
            TextView Number = view.findViewById(R.id.fragment_text_number);
            TextView Content = view.findViewById(R.id.fragment_text_content);


            TextView TvStartTime = view.findViewById(R.id.publish_tv_start_time);
            TextView TvEndTime = view.findViewById(R.id.publish_tv_end_time);
            TextView TvNumber = view.findViewById(R.id.publish_tv_number);
            TextView TvContent = view.findViewById(R.id.publish_tv_content);
            return view;
        }
    }
    public void showListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ListFragment());
        fragmentTransaction.commit();
    }
    public void showTextFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new TextFragment());
        fragmentTransaction.addToBackStack(null);  // 将事务添加到返回栈，使得可以返回到 ListFragment
        fragmentTransaction.commit();
    }
}