package com.example.demo1.model;

public enum Type {
    BOOKING("CAR","Đặt xe"),
    SHIP("SHOPPING_CART","Giao hàng"),
    FOOD("CUTLERY","Ăn uống");

    String gylpName;
    String name;
    Type(String gylpName,String name)
    {
        this.gylpName=gylpName;
        this.name=name;
    }

    public String getGylpName() {
        return gylpName;
    }

    public String getName() {
        return name;
    }
}
