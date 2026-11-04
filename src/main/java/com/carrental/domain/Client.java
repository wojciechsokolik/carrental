package com.carrental.domain;

public class Client {
    private final String firstName;
    private final String lastName;
    private final String email;

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
