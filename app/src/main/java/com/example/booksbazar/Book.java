package com.example.booksbazar;

public class Book {
    private String title;
    private String author;
    private String description;
    private String price;
    private String imageUrl;
    private String userId;

    public Book() {

    }
    public Book(String title, String author, String description, String price, String imageUrl, String userId) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getUserId() { return userId; }
}

