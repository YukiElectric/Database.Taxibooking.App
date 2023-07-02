package com.example.demo1.model;

public class CarInfor {
    private String car_id;
    private String license;
    private String brand;
    private int maintenance;
    private int seat_number;
    private int driver_id;
    private String fullname;
    private double distance;

    public CarInfor(String request_id, String license, String brand, int maintenance, int seat_number, int driver_id, String fullname, double distance) {
        this.car_id = request_id;
        this.license = license;
        this.brand = brand;
        this.maintenance = maintenance;
        this.seat_number = seat_number;
        this.driver_id = driver_id;
        this.fullname = fullname;
        this.distance = distance;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(int maintenance) {
        this.maintenance = maintenance;
    }

    public int getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(int seat_number) {
        this.seat_number = seat_number;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
