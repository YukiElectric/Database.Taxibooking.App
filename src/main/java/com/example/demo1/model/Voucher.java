package com.example.demo1.model;

public class Voucher {
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private  Type type;
    private int discount;
    private int min;
    private int percent;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public Voucher(Type type, int discount, int min, int percent) {
        ID=(int)(Math.random()*900000+100000)+"";
        this.type = type;
        this.discount = discount;
        this.min = min;
        this.percent = percent;
    }
}
