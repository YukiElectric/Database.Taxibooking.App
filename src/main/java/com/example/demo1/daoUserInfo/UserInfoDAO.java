package com.example.demo1.daoUserInfo;

import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserInfoDAO extends UserDAOInterface {
    public static UserInfoDAO getInstance() {
        return new UserInfoDAO();
    }

    @Override
    public int InsertAccount(String user, String password) {
        int ID = 0;
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "INSERT INTO account (username, password, account_type) VALUES(?, ?, ?)";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1, user);
            st.setString(2, password);
            st.setString(3, "user");
            int rs = st.executeUpdate();
            if (rs > 0) {
                String new_sql = "SELECT id FROM account WHERE username = '" + user + "'";
                PreparedStatement new_st = cnt.prepareStatement(new_sql);
                ResultSet reS = new_st.executeQuery();
                if (reS.next()) {
                    ID = reS.getInt("id");
                    JDBCUtil.closeConnection(cnt);
                    return ID;
                }
                JDBCUtil.closeConnection(cnt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ID;
    }

    @Override
    public boolean CheckUser(String user) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1, user);
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

    @Override
    public boolean CheckEmail(String email) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM user_information WHERE email = ?";
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

    @Override
    public boolean CheckPhone(String phone) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM user_information WHERE phone_number = ?";
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

    @Override
    public boolean IsertInformation(int id, String name, String gender, String phone, String email, Date dob) {
        if (id != 0) {
            try {
                Connection cnt = JDBCUtil.getConnection();
                String sql = "INSERT INTO user_information (id,fullname,gender,email,phone_number,dob) VALUES(?,?,?,?,?,?)";
                PreparedStatement st = cnt.prepareStatement(sql);       //37
                st.setInt(1, id);
                st.setString(2, name);
                st.setString(3, gender);
                st.setString(4, email);
                st.setString(5, phone);
                st.setDate(6, dob);
                int new_row = st.executeUpdate();
                if (new_row <= 0) {
                    JDBCUtil.closeConnection(cnt);
                    return false;
                }
                JDBCUtil.closeConnection(cnt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public boolean UpdateCustomerAcc(int id,String account, String password){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE account SET username = ?, password = ? WHERE id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);       //38
            st.setString(1,account);
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
        return false;
    }

    public boolean UpdateUserInfor(int id, String name,String gender ,String email, String phone, Date date) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE user_information SET fullname = ?, gender = ?, email = ?, phone_number = ?, dob = ? WHERE id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);       //39
            st.setString(1,name);
            st.setString(2,gender);
            st.setString(3,email);
            st.setString(4,phone);
            st.setDate(5,date);
            int rs = st.executeUpdate();
            if(rs>0){
                JDBCUtil.closeConnection(cnt);
                return true;
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean CheckDriverPhone(String phone){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM driver WHERE phone_number = ?";
            PreparedStatement st = cnt.prepareStatement(sql);       //40
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
    public boolean CheckDriverEmail(String email){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM driver WHERE email = ?";
            PreparedStatement st = cnt.prepareStatement(sql);       //41
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

}
