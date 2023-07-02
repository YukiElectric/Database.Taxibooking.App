package com.example.demo1.daoGetCarInfor;

import com.example.demo1.daoSetLog.SetLogDAO;
import com.example.demo1.database.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SetCarInforDAO {
    public static SetCarInforDAO getInstance(){
        return new SetCarInforDAO();
    }

    public boolean CheckDriverID(int id){
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT account_type FROM account WHERE id = "+id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next() && rs.getString("account_type").equals("driver")){
                JDBCUtil.closeConnection(cnt);
                return true;
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean UpdateCarInfor(String id,String brand,String license, int seatnumber, int maintenance, int driver_id){
        System.out.println(id+" "+" "+brand+" "+" "+license+" "+" "+seatnumber+" chỗ "+" "+maintenance+" lần "+driver_id);
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "UPDATE car SET license = ?,brand = ?,seatnumber = ?,maintenance = ? WHERE id LIKE '"+id+"'";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setString(1,license);
            st.setString(2,brand);
            st.setInt(3,seatnumber);
            st.setInt(4,maintenance);
            int update = st.executeUpdate();
            System.out.println(update);
            if(update>0 && driver_id==0){
                JDBCUtil.closeConnection(cnt);
                return true;
            }
            else if(driver_id!=0){
                sql = "SELECT driver_id FROM car WHERE id LIKE '"+id+"'";
                st = cnt.prepareStatement(sql);
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    System.out.println("Count 3");
                    int driverID = rs.getInt("driver_id");
                    if(driverID==driver_id){
                        JDBCUtil.closeConnection(cnt);
                        return true;
                    }
                    System.out.println(driverID);
                    sql = "UPDATE car SET driver_id = ? WHERE id LIKE '"+id+"'";
                    st = cnt.prepareStatement(sql);
                    st.setInt(1,driver_id);
                    if(st.executeUpdate()>0){
                        System.out.println("Count 4");
                        sql = "SELECT id FROM car WHERE driver_id = "+driverID+" AND id <> '"+id+"'";
                        st = cnt.prepareStatement(sql);
                        ResultSet newRs = st.executeQuery();
                        System.out.println(newRs.next());
                        if(newRs.next()){
                            System.out.println("Count 5");
                            String carID = newRs.getString("id");
                            System.out.println(carID);
                            sql = "UPDATE car SET driver_id = "+driverID+" WHERE id LIKE '"+carID+"'";
                            st = cnt.prepareStatement(sql);
                            if(st.executeUpdate()>0){
                                System.out.println("Count 6");
                                JDBCUtil.closeConnection(cnt);
                                return true;
                            }
                        }
                    }
                }else{
                    sql = "UPDATE car SET driver_id = ? WHERE id LIKE '"+id+"'";
                    st = cnt.prepareStatement(sql);
                    st.setInt(1,driver_id);
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

    public boolean InsertCarInfor(String brand,String license, int seatnumber, int maintenance, int driver_id, int manager_id){
        try {
            String carID = "";
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS car_id FROM car";
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if(rs.next()) carID = rs.getString("car_id");
            int i = Integer.parseInt(carID.substring(1))+1;
            carID = "c%03d".formatted(i);
            if(driver_id==0){
                sql = "INSERT INTO car(id,license,brand,maintenance,seatnumber) VALUES ('"+carID+"',?,?,?,?)";
                st = cnt.prepareStatement(sql);
                st.setString(1,license);
                st.setString(2,brand);
                st.setInt(3,maintenance);
                st.setInt(4,seatnumber);
                if(st.executeUpdate()>0){
                    JDBCUtil.closeConnection(cnt);
                    SetLogDAO.getInstance().SetInsertCarInforLog(carID,manager_id);
                    return true;
                }
            }else{
                sql = "INSERT INTO car(id,license,brand,maintenance,seatnumber,driver_id) VALUES ('"+carID+"',?,?,?,?,?)";
                st = cnt.prepareStatement(sql);
                st.setString(1,license);
                st.setString(2,brand);
                st.setInt(3,maintenance);
                st.setInt(4,seatnumber);
                st.setInt(5,driver_id);
                if(st.executeUpdate()>0){
                    String newIDCar = "";
                    sql = "SELECT id FROM car WHERE driver_id = "+driver_id+" AND id <> '"+carID+"'";
                    st = cnt.prepareStatement(sql);
                    ResultSet newRs = st.executeQuery();
                    if(newRs.next()) newIDCar = rs.getString("id");
                    sql = "UPDATE car SET driver_id = NULL WHERE id LIKE '"+newIDCar+"'";
                    st = cnt.prepareStatement(sql);
                    if(st.executeUpdate()>0){
                        sql = "UPDATE driver SET car_id = '"+carID+"' WHERE id = "+driver_id;
                        st = cnt.prepareStatement(sql);
                        if(st.executeUpdate()>0){
                            JDBCUtil.closeConnection(cnt);
                            SetLogDAO.getInstance().SetInsertCarInforLog(carID,manager_id);
                            return true;
                        }
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
