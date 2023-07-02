package com.example.demo1.model;

public class AllEmployeeInfor {
    private String employee_id;
    private String fullname;
    private String gender;
    private String email;
    private String phone;
    private String date;
    private String branch;
    private String address;
    private String cccd;

    public AllEmployeeInfor(String employee_id, String fullname, String gender, String email, String phone, String date, String branch, String address, String cccd) {
        this.employee_id = employee_id;
        this.fullname = fullname;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.date = date;
        this.branch = branch;
        this.address = address;
        this.cccd = cccd;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String emoloyee_id) {
        this.employee_id = emoloyee_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
}
