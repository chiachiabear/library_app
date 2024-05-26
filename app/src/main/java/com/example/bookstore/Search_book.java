package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Search_book extends AppCompatActivity {

    private ListView lvBook;
    private EditText etSearch;
    private SQLiteDatabase productDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        lvBook = findViewById(R.id.lvbook);
        etSearch = findViewById(R.id.et_search);

        MainActivity mainActivity = new MainActivity();
        productDatabase = mainActivity.getProductDatabase();
        Cursor cursor = productDatabase.rawQuery("SELECT name,author,book_type,picture FROM books",null);
        cursor.moveToFirst();

        List<Book> books = new ArrayList<>();
        for(int i = 0; i < cursor.getCount(); i++){
            Book book = new Book(cursor.getString(0),cursor.getString(1),cursor.getString(2),R.drawable.img01);
            books.add(book);
            cursor.moveToNext();
        }
        BooksAdapter adapter = new BooksAdapter(this,books);
        lvBook.setAdapter(adapter);


    }
}