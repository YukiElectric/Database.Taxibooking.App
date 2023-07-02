package com.example.demo1.daoGetUserHisoty;

import java.util.ArrayList;

public interface DAOGetHistoryInterface<T>{
    public ArrayList<T> getHistory(int id);
    public T checkStatus(int id);
    public int countRequest(int id);
    public double sumDistance(int id);
}
