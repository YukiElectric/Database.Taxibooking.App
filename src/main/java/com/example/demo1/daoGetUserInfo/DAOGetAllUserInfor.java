package com.example.demo1.daoGetUserInfo;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.AllDriverInfor;
import com.example.demo1.model.AllEmployeeInfor;
import com.example.demo1.model.AllUserInfor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DAOGetAllUserInfor{

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

    public static DAOGetAllUserInfor getInstance(){
        return new DAOGetAllUserInfor();
    }
    public ArrayList<AllUserInfor> GetInfor(){
        ArrayList<AllUserInfor> result = new ArrayList<>();
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT account.username, account.password, user_information.*, \n" +
                    "COUNT(CASE WHEN request.status IN ('Hoàn thành', 'Đã hủy') THEN 1 END), \n" +
                    "SUM(CASE WHEN request.status = 'Hoàn thành' THEN request.distance ELSE 0 END),\n" +
                    "SUM(CASE WHEN request.status = 'Hoàn thành' THEN request.price ELSE 0 END) AS price\n" +
                    "FROM account\n" +
                    "JOIN user_information ON account.id = user_information.id\n" +
                    "LEFT JOIN request ON request.customer_id = account.id\n" +
                    "WHERE account.account_type LIKE 'user'\n" +
                    "GROUP BY account.id, user_information.id";
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String user = rs.getString("username");
                String password = rs.getString("password");
                String fullname = rs.getString("fullname");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date= dateFormat.format(rs.getDate("dob"));
                int count = rs.getInt("count");
                double sum = rs.getDouble("sum");
                String price = formatCurrency(rs.getDouble("price"));
                AllUserInfor temp = new AllUserInfor(id,user,password,fullname,gender,email,phone,date,count,sum,price);
                result.add(temp);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<AllDriverInfor> GetDriverInfor(){
        ArrayList<AllDriverInfor> result = new ArrayList<>();
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT account.username, account.password, driver.*, car.id AS id_car, car.license, car.brand,\n" +
                    "COUNT(CASE WHEN request.status = 'Hoàn thành' THEN 1 END), \n" +
                    "SUM(CASE WHEN request.status = 'Hoàn thành' THEN request.distance ELSE 0 END)\n" +
                    "FROM account\n" +
                    "JOIN driver ON account.id = driver.id\n" +
                    "JOIN car ON account.id = car.driver_id\n" +
                    "LEFT JOIN request ON request.driver_id = account.id\n" +
                    "WHERE account.account_type LIKE 'driver'\n" +
                    "GROUP BY account.id, driver.id,car.id, car.license, car.brand";
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String user = rs.getString("username");
                String password = rs.getString("password");
                String fullname = rs.getString("fullname");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date= dateFormat.format(rs.getDate("dob"));
                String cccd = rs.getString("cccd");
                String address = rs.getString("address");
                String status = rs.getString("status");
                String id_car = rs.getString("id_car");
                String license = rs.getString("license");
                String brand = rs.getString("brand");
                int count = rs.getInt("count");
                double sum = rs.getDouble("sum");
                AllDriverInfor temp = new AllDriverInfor(id,user,password,fullname,gender,email,phone,date,cccd,
                        address,status,id_car,license,brand,count,sum);
                result.add(temp);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<AllEmployeeInfor> GetEmployeeInfor(){
        ArrayList<AllEmployeeInfor> result = new ArrayList<>();
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM employee ORDER BY employee_id ASC";
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String id = rs.getString("employee_id");
                String fullname = rs.getString("fullname");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String phone = rs.getString("phone_number");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date= dateFormat.format(rs.getDate("dob"));
                String branch = rs.getString("branch");
                String address = rs.getString("address");
                String cccd = rs.getString("cccd");
                AllEmployeeInfor temp = new AllEmployeeInfor(id,fullname,gender,email,phone,date,branch,address,cccd);
                result.add(temp);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
