package com.example.booksbazar;

import com.google.firebase.firestore.Exclude;

public class Book {
    @Exclude
    private String bookId;
    private String title;
    private String author;
    private String genre;
    private Integer price;
    private String imageUrl;
    private String userId;
    private String condition;
    private Integer numberOfPages;
    private Float rating;

    public Book() {

    }
    public Book(String title, String author, Integer price, String imageUrl, String userId, String genre, String condition, Integer numberOfPages, Float rating) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.genre = genre;
        this.condition = condition;
        this.numberOfPages = numberOfPages;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Integer getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getUserId() { return userId; }

    public String getGenre() {
        return genre;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bookId != null && bookId.equals(book.bookId);
    }

    @Override
    public int hashCode() {
        return bookId != null ? bookId.hashCode() : 0;
    }

}

