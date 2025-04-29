package com.example.booksbazar;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String description;
    private String genre;
    private String price;
    private String imageUrl;
    private String userId;

    public Book() {

    }
    public Book(String title, String author, String description, String price, String imageUrl, String userId, String genre) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.genre = genre;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getUserId() { return userId; }

    public String getGenre() {
        return genre;
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

