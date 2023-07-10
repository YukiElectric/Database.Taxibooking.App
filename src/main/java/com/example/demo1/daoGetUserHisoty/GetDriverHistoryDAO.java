package com.example.demo1.daoGetUserHisoty;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.DriverHistory;
import com.example.demo1.model.EarningDriverHistory;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class GetDriverHistoryDAO implements DAOGetHistoryInterface<DriverHistory> {

    public static GetDriverHistoryDAO getInstance(){
        return new GetDriverHistoryDAO();
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
    public ArrayList<DriverHistory> getHistory(int id) {
        ArrayList<DriverHistory> result = new ArrayList<>();
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT request.*, user_information.fullname AS customer_name, user_information.phone_number\n" +
                    "FROM request\n" +
                    "JOIN user_information ON request.customer_id = user_information.id\n" +
                    "WHERE request.driver_id = ? ORDER BY id DESC";     //14
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
                String customer_name = rs.getString("customer_name");
                String phone = rs.getString("phone_number");
                DriverHistory driverHistory = new DriverHistory(request_id,time,date,departure,arrival,money,distance,status,customer_name,phone);
                result.add(driverHistory);
            }
            JDBCUtil.closeConnection(cnt);
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public DriverHistory checkStatus(int id) {
        DriverHistory resultReturn = null;
        ArrayList<DriverHistory> result = GetDriverHistoryDAO.getInstance().getHistory(id);
        for (DriverHistory usH : result){
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
            String sql = "SELECT COUNT(id) FROM request WHERE driver_id = ? AND status LIKE 'Hoàn thành'";
            PreparedStatement st = cnt.prepareStatement(sql);       //15
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
                    "WHERE driver_id = ? AND status LIKE 'Hoàn thành'";
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

    public ArrayList<EarningDriverHistory> GetEaringHistory(int id){
        ArrayList<EarningDriverHistory> result = new ArrayList<>();
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT \n" +
                    "EXTRACT(MONTH FROM date) AS month, \n" +
                    "EXTRACT(YEAR FROM date) AS year, COUNT(*), \n" +
                    "SUM(price)*0.7 AS sum\n" +
                    "FROM request WHERE driver_id = ? AND status LIKE 'Hoàn thành'\n" +
                    "GROUP BY EXTRACT(MONTH FROM date), EXTRACT(YEAR FROM date)\n" +
                    "ORDER BY EXTRACT(YEAR FROM date), EXTRACT(MONTH FROM date)";
            PreparedStatement prSt = cnt.prepareStatement(sql);
            prSt.setInt(1,id);
            ResultSet rs = prSt.executeQuery();
            while(rs.next()){
                int month = (int) rs.getDouble("month");
                int year = (int) rs.getDouble("year");
                int count = rs.getInt("count");
                String earn = formatCurrency(rs.getDouble("sum"));
                EarningDriverHistory earnDH = new EarningDriverHistory(month,year,earn,count);
                result.add(earnDH);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
