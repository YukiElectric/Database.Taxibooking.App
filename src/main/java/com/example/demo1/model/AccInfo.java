package com.example.demo1.model;

public class AccInfo {
    private int id;
    private String user;
    private String password;
    private String account_type;

    public AccInfo() {
        super();
    }
    public AccInfo(int id, String user, String password, String account_type) {
        super();
        this.id = id;
        this.user = user;
        this.password = password;
        this.account_type = account_type;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAccount_type() {
        return account_type;
    }
    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }
}
