package com.example.demo1.model;

import java.sql.Date;

public class DriverInfor {
    String fullname;
    String email;
    String phone;
    String cccd;
    Date date;
    String address;

    public DriverInfor(String fullname, String email, String phone, String cccd, Date date, String address) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.cccd = cccd;
        this.date = date;
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
