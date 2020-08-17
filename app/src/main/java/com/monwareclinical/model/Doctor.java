package com.monwareclinical.model;

public class Doctor {

    String img;
    String name;

    public Doctor() {
    }

    public Doctor(String img, String name) {
        this.img = img;
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }
}