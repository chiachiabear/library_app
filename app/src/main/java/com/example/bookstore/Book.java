package com.example.bookstore;

import java.io.Serializable;

public class Book implements Serializable {
    private String ID,Name,Author,Date,Introduction,Type,publication;
    private int picture;

    public Book(String name, String author, String date, String introduction, String type, String publication, int picture) {
        Name = name;
        Author = author;
        Date = date;
        Introduction = introduction;
        Type = type;
        this.publication = publication;
        this.picture = picture;
    }

    public Book(String name, String author, String type, int picture) {
        Name = name;
        Author = author;
        Type = type;
        this.picture = picture;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDate() {
        return Date;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public String getType() {
        return Type;
    }

    public int getPicture() {
        return picture;
    }

    public String getPublication() {
        return publication;
    }
}
