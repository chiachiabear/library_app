package com.example.bookstore;

public class Task {
    private String task_id, release_date, start_time, end_time,task_content, publisher_id;
    private int number_of_recruits;

    public Task(String task_id, String release_date, String start_time, String end_time, String task_content, String publisher_id, int number_of_recruits) {
        this.task_id = task_id;
        this.release_date = release_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.task_content = task_content;
        this.publisher_id = publisher_id;
        this.number_of_recruits = number_of_recruits;
    }
    public Task(String task_id, String task_content, String start_time, String end_time, int number_of_recruits) {
        this.task_id = task_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.task_content = task_content;
        this.number_of_recruits = number_of_recruits;
    }

    public String getTask_id() {
        return task_id;
    }

    public String getRelease_date() {
        return release_date;
    }
    public String getStart_time() {
        return start_time;
    }
    public String getEnd_time() {
        return end_time;
    }
    public String getTask_content() {
        return task_content;
    }

    public String getPublisher_id() {
        return publisher_id;
    }

    public int getNumber_of_recruits() {
        return number_of_recruits;
    }
}
