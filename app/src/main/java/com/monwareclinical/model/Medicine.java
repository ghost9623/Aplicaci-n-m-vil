package com.monwareclinical.model;

import java.io.Serializable;

public class Medicine implements Serializable {

    public static final int MEDICINE_AVAILABLE = 1;
    public static final int MEDICINE_UNAVAILABLE = 0;

    String img;
    String name;
    double price;
    String desc;
    String routeOfAdministration;
    String excretion;
    int isAvailable;

    public Medicine() {
    }

    public Medicine(String img, String name, double price, String desc, String routeOfAdministration, String excretion, int isAvailable) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.routeOfAdministration = routeOfAdministration;
        this.excretion = excretion;
        this.isAvailable = isAvailable;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }

    public String getRouteOfAdministration() {
        return routeOfAdministration;
    }

    public String getExcretion() {
        return excretion;
    }

    public int getIsAvailable() {
        return isAvailable;
    }
}