package com.example.demo1.daoGetUserInfo;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.UserInfo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAOGetUserInfor implements GetUserInfoDAOInterface<UserInfo>{
    public static DAOGetUserInfor getInstance(){
        return new DAOGetUserInfor();
    }

    @Override
    public UserInfo GetUserInfor(int id) {
        UserInfo result = null;
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM user_information WHERE id = "+id;       //21
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String name = rs.getString("fullname");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                Date dob = rs.getDate("dob");
                result = new UserInfo(name,gender,email,phone,dob);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean UpdateUserInfor(int id, String name, String email, String phone, Date date) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE user_information SET fullname = ?, email = ?, phone_number = ?, dob = ? WHERE id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);       //22
            st.setString(1,name);
            st.setString(2,email);
            st.setString(3,phone);
            st.setDate(4,date);
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
}
