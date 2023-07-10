package com.example.demo1.dao;

import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccInfoDAO extends DAOInterface{

    public static AccInfoDAO getInstance(){
        return new AccInfoDAO();
    }
    @Override
    public int CheckUser(String user) {
        int ID = 0;
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT ID FROM account WHERE username = ?"; //1
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1,user);
            ResultSet rs = st.executeQuery();
           if(rs.next()) ID = rs.getInt("ID");
            JDBCUtil.closeConnection(cnt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ID;
    }

    @Override
    public boolean CheckPass(String user, String password) {
        try {
            int ID = AccInfoDAO.getInstance().CheckUser(user);
            if(ID!=0){
                Connection cnt = JDBCUtil.getConnection();
                String sql = "SELECT password FROM account WHERE ID ="+ID; //2
                PreparedStatement st = cnt.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                if(rs.next() && rs.getString("password").equals(password)){
                    JDBCUtil.closeConnection(cnt);
                    return true;
                }
                JDBCUtil.closeConnection(cnt);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int CheckAccountType(int ID) {
        int result = 0;
        try {
            if(ID!=0){
                Connection cnt = JDBCUtil.getConnection();
                String sql = "SELECT account_type FROM account WHERE ID ="+ID; //3
                PreparedStatement st = cnt.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    String type = rs.getString("account_type");
                    switch(type){
                        case "manager" : result = 1;break;
                        case "driver" : result = 2;break;
                        default: result = 3;break;
                    }
                }
                JDBCUtil.closeConnection(cnt);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public int GetCountAcc(int i){
        int count = 0;
        try{
            String type_input;
            Connection cnt = JDBCUtil.getConnection();
            switch(i){
                case 0: type_input = "user_information";break;
                case 1: type_input = "driver";break;
                default: type_input = "employee";break;
            }
            String sql = "SELECT COUNT(*) FROM "+type_input; //4
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()) count = rs.getInt("count");
            JDBCUtil.closeConnection(cnt);
        }catch(Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
