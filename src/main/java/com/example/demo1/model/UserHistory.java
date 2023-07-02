package com.example.demo1.model;

import java.sql.Date;
import java.sql.Time;

public class UserHistory {
    private String request_id;
    private Time time;
    private Date date;
    private String departure;
    private String arrival;
    private String money;
    private double distance;
    private String status;
    private String driver_name;
    private String brand;
    private int seatnumber;

    private String license;

    public UserHistory(String request_id, Time time, Date date, String departure, String arrival, String money, double distance, String status, String driver_name, String brand, int seatnumber, String license) {
        this.request_id = request_id;
        this.time = time;
        this.date = date;
        this.departure = departure;
        this.arrival = arrival;
        this.money = money;
        this.distance = distance;
        this.status = status;
        this.driver_name = driver_name;
        this.brand = brand;
        this.seatnumber = seatnumber;
        this.license = license;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSeatnumber() {
        return seatnumber;
    }

    public void setSeatnumber(int seatnumber) {
        this.seatnumber = seatnumber;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
