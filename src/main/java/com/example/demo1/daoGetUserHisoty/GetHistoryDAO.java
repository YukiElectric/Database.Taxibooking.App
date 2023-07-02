package com.example.demo1.daoGetUserHisoty;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.UserHistory;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class GetHistoryDAO implements DAOGetHistoryInterface<UserHistory>{

    public static GetHistoryDAO getInstance(){
        return new GetHistoryDAO();
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

    @Override
    public ArrayList<UserHistory> getHistory(int id) {
        ArrayList<UserHistory> result = new ArrayList<>();
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT request.*, driver.fullname AS driver_name, car.brand, car.seatnumber, car.license\n" +
                    "FROM request\n" +
                    "JOIN driver ON request.driver_id = driver.id\n" +
                    "JOIN car ON driver.id = car.driver_id\n" +
                    "WHERE request.customer_id = ? ORDER BY id DESC";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String request_id = rs.getString("id");
                Time time = rs.getTime("time");
                Date date = rs.getDate("date");
                String departure = rs.getString("departure");
                String arrival = rs.getString("arrival");
                double value = rs.getDouble("price");
                String money = formatCurrency(value);
                double distance = rs.getDouble("distance");
                String status = rs.getString("status");
                String driver_name = rs.getString("driver_name");
                String brand = rs.getString("brand");
                int seatnumber = rs.getInt("seatnumber");
                String license = rs.getString("license");
                UserHistory userHistory = new UserHistory(request_id,time,date,departure,arrival,money,distance,status,driver_name,brand,seatnumber,license);
                result.add(userHistory);
            }
            JDBCUtil.closeConnection(cnt);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public UserHistory checkStatus(int id) {
        UserHistory resultReturn = null;
        ArrayList<UserHistory> result = GetHistoryDAO.getInstance().getHistory(id);
        for (UserHistory usH : result){
            if(!(usH.getStatus().equals("Hoàn thành") || usH.getStatus().equals("Đã hủy"))){
                resultReturn = usH;
            }
        }
        return resultReturn;
    }

    @Override
    public int countRequest(int id) {
        int result = 0;
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(id) FROM request WHERE customer_id = ? AND status LIKE 'Hoàn thành'";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                result = rs.getInt("count");
                JDBCUtil.closeConnection(cnt);
                return result;
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public double sumDistance(int id) {
        double result = 0;
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT SUM(distance) FROM request \n" +
                    "WHERE customer_id = ? AND status LIKE 'Hoàn thành'";
            PreparedStatement st = cnt.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                result = rs.getDouble("sum");
                JDBCUtil.closeConnection(cnt);
                return result;
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
