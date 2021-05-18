package com.example.busstation.models;

import java.util.List;

public class Buses {
    int image;
    String maso;
    String name;
    int price;
    int seats;
    List<String> busstops;

    public Buses(int image, String maso, String name) {
        this.image = image;
        this.maso = maso;
        this.name = name;
    }


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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMaso() {
        return maso;
    }

    public void setMaso(String maso) {
        this.maso = maso;
    }

    public void setName(String name) {
        this.name = name;
    }
}
