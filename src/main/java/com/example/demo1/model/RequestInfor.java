package com.example.demo1.model;

import java.sql.Date;
import java.sql.Time;

public class RequestInfor {
    private String id_request;
    private String departure;
    private Time time;
    private Date date;
    private String arrival;
    private String price;
    private int driver_id;
    private int customer_id;
    private double distance;
    private String status;
    private String car_id;
    private String driver_name;
    private String customer_name;
    private String brand;
    private String customer_phone;

    public RequestInfor(String id_request, String departure, Time time, Date date, String arrival, String price, int driver_id, int customer_id, double distance,
                        String status, String car_id, String driver_name, String customer_name, String brand, String customer_phone) {
        this.id_request = id_request;
        this.departure = departure;
        this.time = time;
        this.date = date;
        this.arrival = arrival;
        this.price = price;
        this.driver_id = driver_id;
        this.customer_id = customer_id;
        this.distance = distance;
        this.status = status;
        this.car_id = car_id;
        this.driver_name = driver_name;
        this.customer_name = customer_name;
        this.brand = brand;
        this.customer_phone = customer_phone;
    }

    public String getId_request() {
        return id_request;
    }

    public void setId_request(String id_request) {
        this.id_request = id_request;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
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

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
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

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }
}
