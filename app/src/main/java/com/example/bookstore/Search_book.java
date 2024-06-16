package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Search_book extends AppCompatActivity {

    private ListView lvBook;
    private EditText etSearch;
    private Button btnSearch,btnBackMainpage;
    private SQLiteDatabase productDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        lvBook = findViewById(R.id.lvbook);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.search_btn);


        productDatabase = openOrCreateDatabase("library",MODE_PRIVATE,null);


        Cursor cursor = productDatabase.rawQuery("SELECT book_id,name,author,publication_date,introduction,book_type,publication,picture FROM books",null);
        cursor.moveToFirst();

        SearchBook(cursor);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = etSearch.getText().toString();
                if(v.getId() == R.id.search_btn){
                    String query = "SELECT book_id,name,author,publication_date,introduction,book_type,publication,picture FROM books WHERE name LIKE ? OR author LIKE ? OR book_type LIKE ?";
                    String searchKeyword = "%" + keyword + "%";
                    String[] selectionArgs = new String[]{searchKeyword, searchKeyword, searchKeyword};
                    Cursor cursor = productDatabase.rawQuery(query, selectionArgs);
                    cursor.moveToFirst();
                    SearchBook(cursor);
                }
            }
        };

        btnSearch.setOnClickListener(listener);
        showNavigationFragment();

    }


    public void showNavigationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_navigation_show_book, new MainActivity.MainNavigationFragment());
        fragmentTransaction.commit();
    }

    private void SearchBook(Cursor cursor){
        List<Book> books = new ArrayList<>();
        for(int i = 0; i < cursor.getCount(); i++){
            Book book = new Book(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),R.drawable.img01);
            books.add(book);
            cursor.moveToNext();
        }
        BooksAdapter adapter = new BooksAdapter(this,books);
        lvBook.setAdapter(adapter);
    }
}