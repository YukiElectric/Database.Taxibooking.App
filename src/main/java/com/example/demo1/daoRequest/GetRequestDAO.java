package com.example.demo1.daoRequest;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.RequestInfor;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class GetRequestDAO {
    public static GetRequestDAO getInstance(){
        return new GetRequestDAO();
    }

    private static String formatCurrency(double value) {
        Locale vietnameseLocale = new Locale("vi", "VN");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(vietnameseLocale);
        symbols.setCurrencySymbol("");
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMinimumFractionDigits(0);
        return decimalFormat.format(value).trim();
    }

    public ArrayList<RequestInfor> GetRequestInfor(){
        ArrayList<RequestInfor> result = new ArrayList<>();
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT request.* , user_information.fullname AS customer_name, user_information.phone_number AS phone,\n" +
                    "driver.fullname AS driver_name, car.brand\n" +
                    "FROM request\n" +
                    "LEFT JOIN user_information ON user_information.id = request.customer_id\n" +
                    "LEFT JOIN driver ON driver.id = request.driver_id\n" +
                    "LEFT JOIN car ON car.id = request.car_id\n" +
                    "ORDER BY id DESC";                 //23
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String request_id = rs.getString("id");
                Time time = rs.getTime("time");
                Date date = rs.getDate("date");
                String departure = rs.getString("departure");
                String arrival = rs.getString("arrival");
                String price = formatCurrency(rs.getDouble("price"));
                int driver_id = rs.getInt("driver_id");
                int customer_id = rs.getInt("customer_id");
                double distance = rs.getDouble("distance");
                String status = rs.getString("status");
                String car_id = rs.getString("car_id");
                String driver_name = rs.getString("driver_name");
                String customer_name = rs.getString("customer_name");
                String brand = rs.getString("brand");
                String phone = rs.getString("phone");
                RequestInfor temp = new RequestInfor(request_id,departure,time,date,arrival,price,driver_id,
                        customer_id,distance,status,car_id,driver_name,customer_name,brand,phone);
                result.add(temp);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public int GetCountRequest(int i){
        int count = 0;
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql;
            switch (i){
                case 0: sql = "SELECT COUNT(*) FROM request WHERE status IN('Hoàn thành','Đã hủy')";break;
                case 1: sql = "SELECT COUNT(*) FROM request WHERE status LIKE 'Hoàn thành'";break;
                default: sql = "SELECT COUNT(*) FROM request WHERE status LIKE 'Đã hủy'";break;
            }
            PreparedStatement st = cnt.prepareStatement(sql);       //24
            ResultSet rs = st.executeQuery();
            while(rs.next()) count = rs.getInt("count");
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
