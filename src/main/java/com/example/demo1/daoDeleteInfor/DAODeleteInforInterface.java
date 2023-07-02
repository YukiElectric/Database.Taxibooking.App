package com.example.demo1.daoDeleteInfor;

public abstract class DAODeleteInforInterface {
    public abstract void DeleteCustomerInfor(int id, int manager_id);
    public abstract void DeleteDriverInfor(int id, int manager_id);
    public abstract void DeleteEmployeeInfor(String id, int manager_id);
    public abstract void DeleteCarInfor(String id, int manager_id);
    public abstract void DeleteRequestInfor(String id, int manager_id);
}
