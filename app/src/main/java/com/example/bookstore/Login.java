package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnToSignPage;
    private SQLiteDatabase productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnToSignPage = findViewById(R.id.btn_to_signup_page);

        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btn_to_signup_page) {
                    Intent intent = new Intent();
                    intent.setClass(Login.this,Signup.class);
                    startActivity(intent);
                } else if(v.getId() == R.id.btn_login){
                    String account = etAccount.getText().toString();
                    String password = etPassword.getText().toString();
                    loginUser(account,password);
                }
            }
        };
        btnLogin.setOnClickListener(listener);
        btnToSignPage.setOnClickListener(listener);
    }
    private void loginUser(String account, String password) {
        String query = "SELECT * FROM users WHERE mail = ? AND password = ?";
        Cursor cursor = productDatabase.rawQuery(query, new String[]{account, password});

        if (cursor != null && cursor.moveToFirst()) {
            String userId = cursor.getString(0);
            Toast.makeText(this, "登录成功，用户ID：" + userId, Toast.LENGTH_LONG).show();
            saveUserIDToPreferences(userId);
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "登录失败，用户名或密码错误", Toast.LENGTH_LONG).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }
    private void saveUserIDToPreferences(String userid) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userid", userid);
        editor.apply();
    }

}