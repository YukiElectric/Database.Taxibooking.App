package com.example.demo1.daoGetUserInfo;

import java.sql.Date;

public interface GetUserInfoDAOInterface<T> {
    public T GetUserInfor(int id);
    public boolean UpdateUserInfor(int id,String name, String email, String phone, Date date);

}
