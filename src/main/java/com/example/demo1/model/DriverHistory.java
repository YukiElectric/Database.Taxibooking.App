package com.example.demo1.model;

import java.sql.Date;
import java.sql.Time;

public class DriverHistory {
    private String request_id;
    private Time time;
    private Date date;
    private String departure;
    private String arrival;
    private String money;
    private double distance;
    private String status;
    private String customer_name;
    private String customer_phone;

    public DriverHistory(String request_id, Time time, Date date, String departure, String arrival, String money, double distance, String status, String customer_name, String customer_phone) {
        this.request_id = request_id;
        this.time = time;
        this.date = date;
        this.departure = departure;
        this.arrival = arrival;
        this.money = money;
        this.distance = distance;
        this.status = status;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
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

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }
}
