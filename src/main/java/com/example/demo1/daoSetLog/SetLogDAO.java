package com.example.demo1.daoSetLog;

import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class SetLogDAO {
    public static SetLogDAO getInstance(){
        return new SetLogDAO();
    }

    public void SetAddCustomerAccLog(int id, int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Thêm khách hàng có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetRepairCustomerAccLog(int id, int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Sửa thông tin khách hàng có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetAddDriverAccLog(int id, int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Thêm tài xế có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetRepairDriverAccLog(int id, int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Sửa thông tin tài xế có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetAddEmployeeAccLog(String id,int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Thêm nhân viên có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetRepairEmployeeAccLog(String id,int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Sửa thông tin nhân viên có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void SetRepairCarInforLog(String id,int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Sửa thông tin xe có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SetInsertCarInforLog(String id,int manager_id){
        try{
            Connection cnt = JDBCUtil.getConnection();
            Date date = Date.valueOf(LocalDate.now());
            Time time = Time.valueOf(LocalTime.now());
            String action = "Thêm xe có id = "+id;
            String sql = "INSERT INTO log(action,time,date,id) VALUES ('"+action+"','"+time+"','"+date+"','"+manager_id+"')";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.executeUpdate();
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
