package com.example.demo1.daoRequest;

import java.sql.Date;
import java.sql.Time;

public abstract class DAORequestInterface {
    public abstract boolean sendRequest(int id, String departure, String arrival, Time time, Date date,double price,double distance,int choice);

    public abstract boolean acceptRequest(int id);

    public abstract boolean updateRequest(int id);
}
