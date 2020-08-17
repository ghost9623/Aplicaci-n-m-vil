package com.monwareclinical.model;

import java.io.Serializable;

public class Clinic implements Serializable {

    // Clinic's name
    String name;

    // Description
    String description;

    // Workable hours
    String opensAt;
    String closesAt;

    // Location
    String streetAddress;
    String city;
    String state;
    String extNumber;
    String phoneNumber;

    public Clinic() {
    }

    public Clinic(String name, String description, String opensAt, String closesAt, String streetAddress, String city, String state, String extNumber, String phoneNumber) {
        this.name = name;
        this.description = description;
        this.opensAt = opensAt;
        this.closesAt = closesAt;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.extNumber = extNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getOpensAt() {
        return opensAt;
    }

    public String getClosesAt() {
        return closesAt;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getExtNumber() {
        return extNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", opensAt='" + opensAt + '\'' +
                ", closesAt='" + closesAt + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", extNumber='" + extNumber + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}