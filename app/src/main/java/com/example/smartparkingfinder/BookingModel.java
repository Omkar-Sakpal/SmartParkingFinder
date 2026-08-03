package com.example.smartparkingfinder;

public class BookingModel {

    String bookingId;

    String parkingId;

    String parkingName;

    String bookingTime;

    String deviceName;

    String status;

    // NEW SMART FIELDS

    double parkingLat;

    double parkingLon;

    long expiryTime;

    boolean arrived;

    // Empty constructor

    public BookingModel() {

    }

    // Full constructor

    public BookingModel(

            String bookingId,

            String parkingId,

            String parkingName,

            String bookingTime,

            String deviceName,

            String status,

            double parkingLat,

            double parkingLon,

            long expiryTime,

            boolean arrived
    ) {

        this.bookingId = bookingId;

        this.parkingId = parkingId;

        this.parkingName = parkingName;

        this.bookingTime = bookingTime;

        this.deviceName = deviceName;

        this.status = status;

        this.parkingLat = parkingLat;

        this.parkingLon = parkingLon;

        this.expiryTime = expiryTime;

        this.arrived = arrived;
    }

    // Getters & Setters

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getParkingLat() {
        return parkingLat;
    }

    public void setParkingLat(double parkingLat) {
        this.parkingLat = parkingLat;
    }

    public double getParkingLon() {
        return parkingLon;
    }

    public void setParkingLon(double parkingLon) {
        this.parkingLon = parkingLon;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }
}