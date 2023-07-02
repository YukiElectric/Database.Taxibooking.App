package com.example.demo1.daoResetPass;

import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserResetDAO extends ResetInterface{

    public static UserResetDAO getInstance(){
        return new UserResetDAO();
    }
    @Override
    public int CheckEmail(String email) {
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT id FROM user_information WHERE email = ? OR phone_number = ?";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1,email);
            st.setString(2,email);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                JDBCUtil.closeConnection(cnt);
                return rs.getInt("ID");
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean ResetPass(int ID, String password) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE account SET password = ? WHERE id = ? AND ? <> password";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1,password);
            st.setInt(2,ID);
            st.setString(3,password);
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
