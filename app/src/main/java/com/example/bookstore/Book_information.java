package com.example.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Book_information extends AppCompatActivity {

    private TextView tvname,tvauthor,tvtype,tvpublication,tvdate,tvintroduction;
    private ImageView imgbook;
    private Button btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_information);

        tvname = findViewById(R.id.tv_bookname);
        tvauthor = findViewById(R.id.tv_bookauther);
        tvtype = findViewById(R.id.tv_booktype);
        tvpublication = findViewById(R.id.tv_bookpublication);
        tvdate = findViewById(R.id.tv_bookpublicationdate);
        tvintroduction = findViewById(R.id.tv_bookintroduction);
        imgbook = findViewById(R.id.imageView2);
        btn_back = findViewById(R.id.btn_back_searchpage);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btn_back_searchpage){
                    Intent intent = new Intent();
                    intent.setClass(Book_information.this,Search_book.class);
                    startActivity(intent);
                }
            }
        };

        btn_back.setOnClickListener(listener);

        Book book = (Book) getIntent().getSerializableExtra("book");

        tvname.setText(book.getName());
        tvauthor.setText(book.getAuthor());
        tvtype.setText(book.getType());
        tvpublication.setText(book.getPublication());
        tvdate.setText(book.getDate());
        tvintroduction.setText(book.getIntroduction());
        imgbook.setImageResource(book.getPicture());


    }
}