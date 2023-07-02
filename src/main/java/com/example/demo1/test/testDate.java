package com.example.demo1.test;

import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class testDate {
    private static boolean ResetPass(int ID, String password) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT password FROM account WHERE password = ? AND id = "+ID;
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1,password);
            ResultSet rs = st.executeQuery();
            if(!rs.next()){
                String new_sql = "UPDATE account SET password = '"+password+"' WHERE id ="+ID;
                PreparedStatement new_st = cnt.prepareStatement(new_sql);
                int result = new_st.executeUpdate();
                if(result>0){
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

    public static void main(String[] args) {
        System.out.println(ResetPass(36,"12345678"));
    }
}
