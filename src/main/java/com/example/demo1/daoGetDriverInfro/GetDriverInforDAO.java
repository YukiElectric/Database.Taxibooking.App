package com.example.demo1.daoGetDriverInfro;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.DriverInfor;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetDriverInforDAO implements DAOGetDriverInforInterface<DriverInfor>{

    public static GetDriverInforDAO getInstance(){
        return new GetDriverInforDAO();
    }
    @Override
    public DriverInfor GetDriver(int id) {
       DriverInfor result = null;
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM driver WHERE id = "+id;     //12
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String name = rs.getString("fullname");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                Date date = rs.getDate("dob");
                String cccd = rs.getString("cccd");
                String address = rs.getString("address");
                result = new DriverInfor(name,email,phone,cccd,date,address);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
