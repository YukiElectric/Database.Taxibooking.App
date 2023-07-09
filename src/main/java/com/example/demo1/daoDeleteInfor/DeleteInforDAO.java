package com.example.demo1.daoDeleteInfor;

import com.example.demo1.database.JDBCUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class DeleteInforDAO extends DAODeleteInforInterface{

    public static DeleteInforDAO getInstance(){
        return new DeleteInforDAO();
    }
    @Override
    public void DeleteCustomerInfor(int id, int manager_id) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE request SET customer_id = NULL WHERE customer_id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM user_information WHERE id = "+id;
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM account WHERE id = "+id;
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Xóa khách hàng id = "+id;
            sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteDriverInfor(int id, int manager_id) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE car SET driver_id = NULL WHERE driver_id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            sql = "UPDATE request SET driver_id = NULL WHERE driver_id = "+id;
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM driver WHERE id = "+id;
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM account WHERE id = "+id;
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Xóa tài xế id = "+id;
            sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteEmployeeInfor(String id, int manager_id) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "DELETE FROM employee WHERE employee_id LIKE '"+id+"'";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Xóa nhân viên id = "+id;
            sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteCarInfor(String id, int manager_id) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE request SET car_id = NULL WHERE car_id LIKE '"+id+"'";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            sql = "DELETE FROM car WHERE id LIKE '"+id+"'";
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Xóa xe có mã = "+id;
            sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void DeleteRequestInfor(String id, int manager_id) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "DELETE FROM request WHERE id LIKE '"+id+"'";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Xóa đơn hàng có mã = "+id;
            sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
