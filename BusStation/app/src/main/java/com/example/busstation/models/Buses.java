package com.example.busstation.models;

import java.util.List;

public class Buses {
    String name;
    int price;
    int seats;
    List<String> busstops;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getSeats() {
        return seats;
    }

    public List<String> getBusstops() {
        return busstops;
    }
}
