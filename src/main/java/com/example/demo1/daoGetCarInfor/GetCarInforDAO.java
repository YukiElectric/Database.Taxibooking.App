package com.example.demo1.daoGetCarInfor;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.CarInfor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetCarInforDAO {
    public static GetCarInforDAO getInstance(){
        return new GetCarInforDAO();
    }

    public ArrayList<CarInfor> GetCarInfor(){
        ArrayList<CarInfor> result = new ArrayList<>();
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT sub.*\n" +
                    "FROM (\n" +
                    "    SELECT car.*,\n" +
                    "           driver.fullname,\n" +
                    "           SUM(CASE WHEN request.status = 'Hoàn thành' THEN request.distance ELSE 0 END) AS distance,\n" +
                    "           ROW_NUMBER() OVER (PARTITION BY car.id ORDER BY car.id) AS row_num\n" +
                    "    FROM car\n" +
                    "    LEFT JOIN driver ON car.driver_id = driver.id\n" +
                    "    LEFT JOIN request ON request.car_id = car.id\n" +
                    "    GROUP BY car.id, driver.fullname\n" +
                    ") AS sub\n" +
                    "WHERE sub.row_num = 1\n" +
                    "ORDER BY sub.id ASC;";
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String id = rs.getString("id");
                String license = rs.getString("license");
                String brand = rs.getString("brand");
                int maintenance = rs.getInt("maintenance");
                int seatnumber = rs.getInt("seatnumber");
                int driver_id = rs.getInt("driver_id");
                String fullname = rs.getString("fullname");
                double distance = rs.getDouble("distance");
                CarInfor temp = new CarInfor(id,license,brand,maintenance,seatnumber,driver_id,fullname,distance);
                result.add(temp);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int GetCountCar(int i){
        int count = 0;
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql;
            switch (i){
                case 0: sql = "SELECT COUNT(*) FROM car";break;
                case 1: sql = "SELECT COUNT(*) FROM car\n" +
                        "WHERE seatnumber = 4 AND brand <> 'Vinfast'";break;
                case 2: sql = "SELECT COUNT(*) FROM car\n" +
                        "WHERE seatnumber = 4 AND brand LIKE 'Vinfast'";break;
                case 3: sql = "SELECT COUNT(*) FROM car WHERE seatnumber = 7";break;
                default: sql = "SELECT COUNT(*) FROM car WHERE seatnumber = 16";break;
            }
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()) count = rs.getInt("count");
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
