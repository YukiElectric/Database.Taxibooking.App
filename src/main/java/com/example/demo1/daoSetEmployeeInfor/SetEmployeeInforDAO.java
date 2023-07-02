package com.example.demo1.daoSetEmployeeInfor;

import com.example.demo1.daoSetLog.SetLogDAO;
import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SetEmployeeInforDAO {

    public static SetEmployeeInforDAO getInstance(){
        return new SetEmployeeInforDAO();
    }

    public boolean insertEmployeeInfor(int manager_id,String name, String gender, String phone, String email, Date date,String cccd, String address,String branch){
        try {
            String result = "";
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(employee_id) AS employee_id FROM employee";
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next()) result = rs.getString("employee_id");
            int i = Integer.parseInt(result.substring(2)) + 1;
            result = "nv%04d".formatted(i);
            sql = "INSERT INTO employee(employee_id,fullname,gender,phone_number,email,dob,cccd,address,branch) VALUES ('"+result+"',?,?,?,?,?,?,?,?)";
            st = cnt.prepareStatement(sql);
            st.setString(1,name);
            st.setString(2,gender);
            st.setString(3,phone);
            st.setString(4,email);
            st.setDate(5,date);
            st.setString(6,cccd);
            st.setString(7,address);
            st.setString(8,branch);
            if(st.executeUpdate()>0){
                JDBCUtil.closeConnection(cnt);
                SetLogDAO.getInstance().SetAddEmployeeAccLog(result,manager_id);
                return true;
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateEmployeeInfor(String id,String name,String gender,String phone, String email, Date date,String cccd, String address,String branch){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE employee SET fullname = ?,gender = ?,phone_number = ?,email = ?,dob = ?,cccd = ?,address = ?,branch = ? WHERE employee_id LIKE '"+id+"'";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1,name);
            st.setString(2,gender);
            st.setString(3,phone);
            st.setString(4,email);
            st.setDate(5,date);
            st.setString(6,cccd);
            st.setString(7,address);
            st.setString(8,branch);
            if(st.executeUpdate()>0){
                JDBCUtil.closeConnection(cnt);
                return true;
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkEmployeeEmail(String email){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM employee WHERE email = ?";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                JDBCUtil.closeConnection(cnt);
                return true;
            }
            JDBCUtil.closeConnection(cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkEmployeePhone(String phone){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM employee WHERE phone_number = ?";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1, phone);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                JDBCUtil.closeConnection(cnt);
                return true;
            }
            JDBCUtil.closeConnection(cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
