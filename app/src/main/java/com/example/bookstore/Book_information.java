package com.example.bookstore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.database.SQLException;

public class Book_information extends AppCompatActivity {

    private TextView tvname,tvauthor,tvtype,tvpublication,tvdate,tvintroduction;
    private ImageView imgbook;
    private Button btn_back,btn_borrow;
    private SQLiteDatabase productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_information);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userid", "Default User");

        tvname = findViewById(R.id.tv_bookname);
        tvauthor = findViewById(R.id.tv_bookauther);
        tvtype = findViewById(R.id.tv_booktype);
        tvpublication = findViewById(R.id.tv_bookpublication);
        tvdate = findViewById(R.id.tv_bookpublicationdate);
        tvintroduction = findViewById(R.id.tv_bookintroduction);
        imgbook = findViewById(R.id.imageView2);
        btn_back = findViewById(R.id.btn_back_searchpage);
        btn_borrow = findViewById(R.id.btn_borrow);
        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);
        Book book = (Book) getIntent().getSerializableExtra("book");

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btn_back_searchpage){
                    /*Intent intent = new Intent();
                    intent.setClass(Book_information.this,Search_book.class);
                    startActivity(intent);*/
                    finish();
                }else if(v.getId() == R.id.btn_borrow){
                    boolean result = isBookBorrowedByUser(userId, book.getID());
                    if(result == false){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String borrowingDate = sdf.format(Calendar.getInstance().getTime());
                        // 计算还书日期（借书日期加15天）
                        Calendar returnCalendar = Calendar.getInstance();
                        returnCalendar.add(Calendar.DAY_OF_YEAR, 15);
                        String returnDate = sdf.format(returnCalendar.getTime());
                        String sql = "INSERT INTO user_borrowing (user_id, book_id, borrowing_date, return_date) VALUES (?, ?, ?, ?)";
                        SQLiteStatement stmt = null;
                        stmt = productDatabase.compileStatement(sql);
                        stmt.bindString(1, userId);
                        stmt.bindString(2, book.getID());
                        stmt.bindString(3, borrowingDate);
                        stmt.bindString(4, returnDate);
                        stmt.executeInsert();
                        stmt.close();
                        Toast.makeText(Book_information.this, "借書成功", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Book_information.this, "已借閱", Toast.LENGTH_LONG).show();
                    }
                }
            }
        };

        btn_back.setOnClickListener(listener);
        btn_borrow.setOnClickListener(listener);


        tvname.setText(book.getName());
        tvauthor.setText(book.getAuthor());
        tvtype.setText(book.getType());
        tvpublication.setText(book.getPublication());
        tvdate.setText(book.getDate());
        tvintroduction.setText(book.getIntroduction());
        imgbook.setImageResource(book.getPicture());


    }

    public boolean isBookBorrowedByUser(String userId, String bookId) {
        String query = "SELECT 1 FROM user_borrowing WHERE user_id = ? AND book_id = ?";
        try (Cursor cursor = productDatabase.rawQuery(query, new String[]{userId, bookId})) {
            return cursor.moveToFirst();
        } catch (SQLException e) {
//            System.out.println(e.getMessage());
            return false;
        }
    }
}