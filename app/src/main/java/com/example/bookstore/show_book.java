package com.example.bookstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class show_book extends AppCompatActivity {
    private ImageButton btnSet;
    private GridView gView;

    private SQLiteDatabase productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);
        btnSet = findViewById(R.id.btn_set);
        gView = findViewById(R.id.g_view);

        productDatabase = openOrCreateDatabase("library", MODE_PRIVATE, null);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_set) {
                    Intent intent = new Intent(show_book.this, Personal_Information.class);
                    startActivity(intent);
                }
            }
        };
        btnSet.setOnClickListener(listener);

        showNavigationFragment();

        // 调用 borrowhook 方法来显示书籍
        borrowhook();
    }

    private void borrowhook() {
        Cursor cursor = productDatabase.rawQuery("SELECT b.name, bl.return_date " +
                "FROM books b " +
                "INNER JOIN user_borrowing bl ,users u ON b.book_id = bl.book_id and bl.borrowing_id=u.user_id", null);

        List<Book> books = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String bookName = cursor.getString(0);
                String returnDate = cursor.getString(1);
                Book book = new Book(bookName, returnDate, R.drawable.img01);
                books.add(book);
            } while (cursor.moveToNext());
            cursor.close();
        }else {
            Toast.makeText(this, "沒有書籍", Toast.LENGTH_LONG).show();
        }

        BookAdapter adapter = new BookAdapter(this, books);
        gView.setAdapter(adapter);
    }


    public void showNavigationFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_show_book, new MainActivity.MainNavigationFragment());
        fragmentTransaction.commit();
    }

    // 书籍模型类
    public static class Book {
        private String name;
        private String returnDate;
        private int picture;

        public Book(String name, String returnDate, int picture) {
            this.name = name;
            this.returnDate = returnDate;
            this.picture = picture;
        }

        public String getName() {
            return name;
        }

        public String getReturnDate() {
            return returnDate;
        }

        public int getPicture() {
            return picture;
        }
    }


    public class BookAdapter extends BaseAdapter {
        private List<Book> books;
        private LayoutInflater inflater;

        public BookAdapter(AppCompatActivity context, List<Book> books) {
            this.books = books;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Object getItem(int position) {
            return books.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_show_book_layout, parent, false);
                holder = new ViewHolder();
                holder.bookImage = convertView.findViewById(R.id.im_book);
                holder.tvBookTitle = convertView.findViewById(R.id.tv_bookname);
                holder.tvReturnDate = convertView.findViewById(R.id.tv_returndate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Book book = books.get(position);

            holder.bookImage.setImageResource(book.getPicture());
            holder.tvBookTitle.setText(book.getName());
            holder.tvReturnDate.setText(book.getReturnDate());

            return convertView;
        }

        private class ViewHolder {
            ImageView bookImage;
            TextView tvBookTitle;
            TextView tvReturnDate;
        }
    }

}

