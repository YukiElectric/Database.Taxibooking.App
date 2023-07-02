package com.example.demo1.daoUserInfo;

import java.sql.Date;

public abstract class UserDAOInterface {
    public abstract int InsertAccount(String user, String password);
    public abstract boolean CheckUser(String user);
    public abstract boolean CheckEmail(String email);
    public abstract boolean CheckPhone(String phone);
    public abstract boolean IsertInformation(int id, String name, String gender, String phone, String email, Date dob);
}
