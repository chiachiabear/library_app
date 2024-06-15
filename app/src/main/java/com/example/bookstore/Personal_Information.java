package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Personal_Information extends AppCompatActivity {
    private TextView tvName,tvClass,tvId,tvAccount;
    private EditText etPhone;
    private SQLiteDatabase productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        tvName = findViewById(R.id.tv_name);
        tvClass = findViewById(R.id.tv_class);
        tvId = findViewById(R.id.tv_id);
        tvAccount = findViewById(R.id.tv_account);
        etPhone = findViewById(R.id.et_phone);

        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);


    }


}