package com.example.myapplication;

public class Book {
    private int bookId;
    private String name;
    private String author;
    private double price;
    private String description;
    private String status;
    private String imageResourceName;

    public Book(int bookId,String name, String author, double price, String description, String status, String imageResourceName) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.price = price;
        this.description = description;
        this.status = status;
        this.imageResourceName = imageResourceName;
    }
    public Book(String name, String author, double price, String description, String status, String imageResourceName) {

        this.name = name;
        this.author = author;
        this.price = price;
        this.description = description;
        this.status = status;
        this.imageResourceName = imageResourceName;
    }

    // Getters
    public String getName() { return name; }
    public String getAuthor() { return author; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getImageResourceName() { return imageResourceName; }
    public String toString() {
        return name + " by " + author; // Customize this to display book details as needed
    }
    public int getBookId() {
        return bookId;
    }
}
