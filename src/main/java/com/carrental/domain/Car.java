package com.carrental.domain;

public class Car {
    private String registrationNumber;
    private CarCategory carCategory;
    private Branch branch;

    public Car(String registrationNumber, CarCategory carCategory, Branch branch) {
        this.registrationNumber = registrationNumber;
        this.carCategory = carCategory;
        this.branch = branch;
    }
}
