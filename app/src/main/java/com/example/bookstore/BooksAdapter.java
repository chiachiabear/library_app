package com.example.bookstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BooksAdapter extends BaseAdapter {

    private Context context;
    private List<Book> lvBooks;

    public BooksAdapter() {
    }

    public BooksAdapter(Context context, List<Book> lvBooks) {
        this.context = context;
        this.lvBooks = lvBooks;
    }

    @Override
    public int getCount() {
        return lvBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return lvBooks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_search_book_layout,parent,false);
        }
        ImageView img = convertView.findViewById(R.id.img_book);
        TextView tvName =  convertView.findViewById(R.id.tv_name);
        TextView tvAuthor = convertView.findViewById(R.id.tv_author);
        TextView tvType = convertView.findViewById(R.id.tv_type);

        Book book = lvBooks.get(position);
        img.setImageResource(book.getPicture());
        tvName.setText(book.getName());
        tvAuthor.setText(book.getAuthor());
        tvType.setText(book.getType());

        return convertView;
    }
}