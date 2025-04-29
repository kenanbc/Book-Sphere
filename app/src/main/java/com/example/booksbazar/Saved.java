package com.example.booksbazar;

public class Saved {
    String bookId;
    String userId;

    public Saved(){

    }
    public Saved(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
