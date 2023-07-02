package com.example.demo1.model;

public enum CarType {
    Car4(15000.0),
    CarV4(13000.0),
    Car7(20000.0),
    Car16(50000.0);
    private double money;
    CarType(double money){
        this.money = money;
    }
    public double getMoney() {
        return money;
    }
}
