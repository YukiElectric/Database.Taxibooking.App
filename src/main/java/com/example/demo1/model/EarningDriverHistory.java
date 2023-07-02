package com.example.demo1.model;

public class EarningDriverHistory {
    private int month;
    private int year;

    private String earnMoney;
     private int countRequest;

    public EarningDriverHistory(int month, int year, String earnMoney, int countRequest) {
        this.month = month;
        this.year = year;
        this.earnMoney = earnMoney;
        this.countRequest = countRequest;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEarnMoney() {
        return earnMoney;
    }

    public void setEarnMoney(String earnMoney) {
        this.earnMoney = earnMoney;
    }

    public int getCountRequest() {
        return countRequest;
    }

    public void setCountRequest(int countRequest) {
        this.countRequest = countRequest;
    }
}
