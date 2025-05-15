package com.example.booksbazar;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String location;
    public String status;

    public User() {
    }

    public User(String firstName, String lastName, String email, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = city;
        this.status = "Offline";
    }

    public User(String firstName, String lastName, String location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }
}

