package com.example.busstation.models;


public class BusesFavorite {
    Buses buses;
    Boolean isOwner;

    public Buses getBuses() {
        return buses;
    }

    public void setBuses(Buses buses) {
        this.buses = buses;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public Boolean getOwner() {
        return isOwner;
    }
}
