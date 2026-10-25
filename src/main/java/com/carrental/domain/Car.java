package com.carrental.domain;

import java.util.Objects;

public class Car {
    private final String registrationNumber;
    private final CarCategory carCategory;
    private final Branch branch;

    public Car(String registrationNumber, CarCategory carCategory, Branch branch) {
        this.registrationNumber = registrationNumber;
        this.carCategory = carCategory;
        this.branch = branch;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public Branch getBranch() {
        return branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car other)) return false;

        return registrationNumber.equals(other.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }
}
