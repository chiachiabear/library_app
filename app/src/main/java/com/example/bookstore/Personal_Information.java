package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Personal_Information extends AppCompatActivity {
    private TextView tvName,tvClass,tvId,tvAccount;
    private EditText etPhone;
    private Button btnSure,btnLogout;
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
        btnLogout = findViewById(R.id.btn_logout);
        btnSure = findViewById(R.id.btn_sure);

        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etPhone.getText().toString();
                if (v.getId() == R.id.btn_sure) {
                    // Save the phone number to the database
                    savePhoneNumber(phoneNumber);
                    Toast.makeText(Personal_Information.this, "儲存成功", Toast.LENGTH_SHORT).show();
                }else if(v.getId() == R.id.btn_logout) {
                    Intent intent = new Intent();
                    intent.setClass(Personal_Information.this, Login.class);
                    startActivity(intent);
                }
            }

        };
        btnSure.setOnClickListener(listener);
        btnLogout.setOnClickListener(listener);
        fetchUserData();

        showNavigationFragment();
    }
    private void fetchUserData() {
        Cursor cursor = productDatabase.rawQuery("SELECT name, user_id, phone, department,mail FROM users WHERE user_id = 'u001'", null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range")
            String name = "姓名:     " + cursor.getString(cursor.getColumnIndex("name"));
            String userId = "id:           " + cursor.getString(cursor.getColumnIndex("user_id"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String department = "專業:      " + cursor.getString(cursor.getColumnIndex("department"));
            String mail = "帳號:      " + cursor.getString(cursor.getColumnIndex("mail"));

            tvName.setText(name);
            tvId.setText(userId);
            tvAccount.setText(mail);
            etPhone.setText(phone);
            tvClass.setText(department);
        }
        cursor.close();
    }
    private void savePhoneNumber(String phoneNumber) {
        productDatabase.execSQL("UPDATE users SET phone = ? WHERE user_id = ?", new Object[]{phoneNumber, "u001"});
    }


    public void showNavigationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_personal, new MainActivity.MainNavigationFragment());
        fragmentTransaction.commit();
    }


}