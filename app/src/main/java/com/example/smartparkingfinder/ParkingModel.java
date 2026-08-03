package com.example.smartparkingfinder;

public class ParkingModel {

    String id;
    String name;

    double lat;
    double lon;

    int available;

    // Empty constructor

    public ParkingModel() {

    }

    // Full constructor

    public ParkingModel(
            String id,
            String name,
            double lat,
            double lon,
            int available) {

        this.id = id;
        this.name = name;

        this.lat = lat;
        this.lon = lon;

        this.available = available;
    }

    // Getters

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public double getLat() {

        return lat;
    }

    public double getLon() {

        return lon;
    }

    public int getAvailable() {

        return available;
    }

    // Setters

    public void setId(String id) {

        this.id = id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setLat(double lat) {

        this.lat = lat;
    }

    public void setLon(double lon) {

        this.lon = lon;
    }

    public void setAvailable(int available) {

        this.available = available;
    }
}