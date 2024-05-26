package com.example.bookstore;

public class Book {
    private String ID,Name,Author,Date,Introduction,Type;
    private int picture;

    public Book(String ID, String name, String author, String date, String introduction, String type, int picture) {
        this.ID = ID;
        Name = name;
        Author = author;
        Date = date;
        Introduction = introduction;
        Type = type;
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
}
