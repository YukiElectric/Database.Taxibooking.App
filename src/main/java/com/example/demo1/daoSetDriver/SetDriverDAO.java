package com.example.demo1.daoSetDriver;

import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SetDriverDAO {

    public static SetDriverDAO getInstance(){
        return new SetDriverDAO();
    }
    public int InsertAccDriver(String user, String password){
        int id = 0;
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "INSERT INTO account (username, password, account_type) VALUES (?,?,'driver')";        //29
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1,user);
            st.setString(2,password);
            int rs = st.executeUpdate();
            if(rs>0){
               sql = "SELECT id FROM account WHERE username LIKE '"+user+"'";
               st = cnt.prepareStatement(sql);
                ResultSet reS = st.executeQuery();
                if(reS.next()){
                    id = reS.getInt("id");
                }
            }
            JDBCUtil.closeConnection(cnt);
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public boolean InsertDriverInfor(int id, String name, String gender, String phone, String email, String cccd, String carId, String address, Date date){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "INSERT INTO driver(id,fullname,gender,email,phone_number,dob,cccd,car_id,address,status) VALUES(?,?,?,?,?,?,?,?,?,'Không có đơn')";
            PreparedStatement st = cnt.prepareStatement(sql);   //30
            st.setInt(1,id);
            st.setString(2,name);
            st.setString(3,gender);
            st.setString(4,email);
            st.setString(5,phone);
            st.setDate(6,date);
            st.setString(7,cccd);
            st.setString(8,carId);
            st.setString(9,address);
            int rs = st.executeUpdate();
            if(rs>0){
                sql = "UPDATE car SET driver_id = "+id+" WHERE id LIKE '"+carId+"'";
                st = cnt.prepareStatement(sql);
                int newRs = st.executeUpdate();
                if(newRs>0){
                    JDBCUtil.closeConnection(cnt);
                    return true;
                }
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean UpdateAccDriver(int id, String user, String password){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE account SET username = ?, password = ? WHERE id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);       //31
            st.setString(1,user);
            st.setString(2,password);
            int rs = st.executeUpdate();
            if(rs>0){
                JDBCUtil.closeConnection(cnt);
                return true;
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public boolean UpdateDriverInfor(int id,String name, String gender, String phone, String email, String cccd, String carId, String address, Date date){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE driver SET fullname = ?,gender = ?,email = ?,phone_number = ?,dob = ?,cccd = ?,address = ?,car_id =? WHERE id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);   //32
            st.setString(1,name);
            st.setString(2,gender);
            st.setString(3,email);
            st.setString(4,phone);
            st.setDate(5,date);
            st.setString(6,cccd);
            st.setString(7,address);
            st.setString(8,carId);
            if(st.executeUpdate()>0){
                sql = "UPDATE car SET driver_id = NULL WHERE driver_id = "+id;
                st = cnt.prepareStatement(sql);
                if(st.executeUpdate()>0){
                    sql = "UPDATE car SET driver_id = "+id+" WHERE id LIKE '"+carId+"'";
                    st = cnt.prepareStatement(sql);
                    if(st.executeUpdate()>0){
                        JDBCUtil.closeConnection(cnt);
                        return true;
                    }
                }
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
