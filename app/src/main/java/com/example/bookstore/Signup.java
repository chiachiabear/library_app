package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.security.SecureRandom;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class Signup extends AppCompatActivity {
    private EditText etAccount;
    private EditText etPassword;
    private EditText etUserName;
    private EditText etEmail;
    private Button btnSignUp;
    private Button btnToLoginPage;
    private ListView lvProducts;

    private Spinner spRoles;
    private int rolei = 0;
    private String[] roles = {"receiver","publisher"};
    private SQLiteDatabase productDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etUserName=findViewById(R.id.et_username);
        etEmail=findViewById(R.id.et_email);
        btnSignUp = findViewById(R.id.btn_signup);
        btnToLoginPage = findViewById(R.id.btn_to_signup_page);
        //MainActivity mainActivity = new MainActivity();
        lvProducts=findViewById(R.id.lv_products);
        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);
        listAllProducts();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btn_signup){
                    String account = etAccount.getText().toString();
                    String password = etPassword.getText().toString();
                    String name=etUserName.getText().toString();
                    String email=etEmail.getText().toString();
                    //String uid=generateUserIdWithPrefix("u", 6);
                    String insertSql = "INSERT INTO " + "users" + "(user_id,password,name,mail)VALUES('" + account + "', '" + password + "', '" + name + "', '" + email + "')";
                    productDatabase.execSQL(insertSql);
                    Intent intent = new Intent();
                    intent.setClass(Signup.this, Login.class);
                    startActivity(intent);
                }else if (v.getId() == R.id.btn_to_signup_page){
                    Intent intent = new Intent();
                    intent.setClass(Signup.this, Login.class);
                    startActivity(intent);
                }
            }
        };
        btnSignUp.setOnClickListener(listener);
        btnToLoginPage.setOnClickListener(listener);
    }
    public static String generateUserIdWithPrefix(String prefix, int length) {
        SecureRandom random = new SecureRandom();
        int bound = (int) Math.pow(10, length);
        int randomNumber = random.nextInt(bound);
        return prefix + randomNumber;
    }
    private void listAllProducts(){
        //String insertSql = "INSERT INTO users (user_id password, name, mail) VALUES ('" + account + "', '" + password + "', '" + name + "', '" + email + "')";
        Cursor cursor=productDatabase.rawQuery("SELECT user_id as _id,password FROM "+ "users",null);
        SimpleCursorAdapter scAdapter=new SimpleCursorAdapter(Signup.this,android.R.layout.simple_list_item_2,
                cursor,new String[]{"_id","password"},new int[]{android.R.id.text1,android.R.id.text2},0);
        lvProducts.setAdapter(scAdapter);
    }
}
