package com.example.demo1.daoRequest;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferRequestData;
import javafx.application.Platform;

import java.sql.*;
import java.util.concurrent.TimeUnit;

public class SetRequestDAO extends DAORequestInterface {

    public static SetRequestDAO getInstance() {
        return new SetRequestDAO();
    }

    @Override
    public boolean sendRequest(int id, String departure, String arrival, Time time, Date date, double price, double distance, int choice) {
        try {
            String result = "";
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS id FROM request";
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) result = rs.getString("id");
            int i = Integer.parseInt(result.substring(1)) + 1;
            result = "r%04d".formatted(i);
            switch (choice) {
                case 0:
                    sql = "SELECT driver.id,driver.car_id FROM driver\n" +
                            "JOIN car ON car.id = driver.car_id\n" +
                            "WHERE driver.status LIKE 'Không có đơn' AND car.seatnumber = 4 AND  car.brand <> 'Vinfast'";
                    break;
                case 1:
                    sql = "SELECT driver.id,driver.car_id FROM driver\n" +
                            "JOIN car ON car.id = driver.car_id\n" +
                            "WHERE driver.status LIKE 'Không có đơn' AND car.seatnumber = 4 AND  car.brand LIKE 'Vinfast'";
                    break;
                case 2:
                    sql = "SELECT driver.id,driver.car_id FROM driver\n" +
                            "JOIN car ON car.id = driver.car_id\n" +
                            "WHERE driver.status LIKE 'Không có đơn' AND car.seatnumber = 7";
                    break;
                default:
                    sql = "SELECT driver.id,driver.car_id FROM driver\n" +
                            "JOIN car ON car.id = driver.car_id\n" +
                            "WHERE driver.status LIKE 'Không có đơn' AND car.seatnumber = 16";
                    break;
            }
            int driver_id;
            String car_id;
            st = cnt.prepareStatement(sql);
            rs = st.executeQuery();
            sql = "INSERT INTO request(id,time,date,departure,arrival,price,customer_id,driver_id,distance,status,car_id) \n" +
                    "VALUES(?,?,?,?,?,?,?,NULL,?,?,NULL)";
            st = cnt.prepareStatement(sql);
            st.setString(1, result);
            st.setTime(2, time);
            st.setDate(3, date);
            st.setString(4, departure);
            st.setString(5, arrival);
            st.setDouble(6, price);
            st.setInt(7, id);
            st.setDouble(8, distance);
            st.setString(9, "Thiết lập đơn");
            int row = st.executeUpdate();
            RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
            if (row > 0) {
                while (rs.next()) {
                    driver_id = rs.getInt("id");
                    car_id = rs.getString("car_id");
                    sql = "UPDATE driver SET status = 'Đang có đơn' WHERE id = " + driver_id;
                    st = cnt.prepareStatement(sql);
                    st.executeUpdate();
                    sql = "UPDATE request SET driver_id = " + driver_id + ", car_id = '" + car_id + "' WHERE id = '" + result + "'";
                    st = cnt.prepareStatement(sql);
                    int newRow = st.executeUpdate();
                    if (newRow > 0) {
                        int count = 0;
                        while (count < 60) {
                            sql = "SELECT status FROM request WHERE id = ?";
                            st = cnt.prepareStatement(sql);
                            st.setString(1, result);
                            count++;
                            TimeUnit.SECONDS.sleep(1);
                            ResultSet rs2 = st.executeQuery();
                            String status = "";
                            while (rs2.next()) status = rs2.getString("status");
                            if (status.equals("Đã tiếp nhận")) {
                                JDBCUtil.closeConnection(cnt);
                                return true;
                            }

                        }
                    }
                    sql = "UPDATE driver SET status = 'Không có đơn'";
                    st = cnt.prepareStatement(sql);
                    st.executeUpdate();
                }
                sql = "UPDATE request SET status = 'Đã hủy' WHERE id = '" + result + "'";
                st = cnt.prepareStatement(sql);
                st.executeUpdate();
            }
            JDBCUtil.closeConnection(cnt);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean acceptRequest(int id) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS request_id FROM request WHERE driver_id = " + id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String request_id = rs.getString("request_id");
                sql = "SELECT status FROM request WHERE id = '" + request_id + "'";
                st = cnt.prepareStatement(sql);
                ResultSet newRs = st.executeQuery();
                if (newRs.next()) {
                    String status = newRs.getString("status");
                    if (!status.equals("Thiết lập đơn")) {
                        JDBCUtil.closeConnection(cnt);
                        return false;
                    } else {
                        sql = "UPDATE request SET status = 'Đã tiếp nhận' WHERE id LIKE '" + request_id + "'";
                        st = cnt.prepareStatement(sql);
                        st.executeUpdate();
                        sql = "UPDATE driver SET status = 'Đang có đơn' WHERE id = " + id;
                        st = cnt.prepareStatement(sql);
                        st.executeUpdate();
                        JDBCUtil.closeConnection(cnt);
                        return true;
                    }
                }
            }
            JDBCUtil.closeConnection(cnt);
            JDBCUtil.closeConnection(cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRequest(int id) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS request_id FROM request WHERE customer_id = " + id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String request_id = rs.getString("request_id");
                sql = "SELECT driver_id, status FROM request WHERE id LIKE '" + request_id + "'";
                st = cnt.prepareStatement(sql);
                ResultSet newRs = st.executeQuery();
                if (newRs.next()) {
                    int driver_id = newRs.getInt("driver_id");
                    String status = newRs.getString("status");
                    if (status.equals("Đang tiến hành")) {
                        JDBCUtil.closeConnection(cnt);
                        return false;
                    } else {
                        sql = "UPDATE request SET status = 'Đã hủy' WHERE id LIKE '" + request_id + "'";
                        st = cnt.prepareStatement(sql);
                        st.executeUpdate();
                        sql = "UPDATE driver SET status = 'Không có đơn' WHERE id = " + driver_id;
                        st = cnt.prepareStatement(sql);
                        st.executeUpdate();
                        JDBCUtil.closeConnection(cnt);
                        return true;
                    }
                }
            }
            JDBCUtil.closeConnection(cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean DriverAcceptRequest(int id) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS request_id FROM request WHERE driver_id = " + id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String request_id = rs.getString("request_id");
                sql = "SELECT status FROM request WHERE id = '" + request_id + "'";
                st = cnt.prepareStatement(sql);
                ResultSet newRs = st.executeQuery();
                if (newRs.next()) {
                    String status = newRs.getString("status");
                    if (status.equals("Đã hủy")) {
                        JDBCUtil.closeConnection(cnt);
                        return false;
                    } else {
                        sql = "UPDATE request SET status = 'Đang tiến hành' WHERE id LIKE '" + request_id + "'";
                        st = cnt.prepareStatement(sql);
                        st.executeUpdate();
                        JDBCUtil.closeConnection(cnt);
                        return true;
                    }
                }
            }
            JDBCUtil.closeConnection(cnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void CompleRequest(int id) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS request_id FROM request WHERE driver_id = " + id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                sql = "UPDATE request SET status = 'Hoàn thành' WHERE id LIKE '" + rs.getString("request_id") + "'";
                st = cnt.prepareStatement(sql);
                st.executeUpdate();
                sql = "UPDATE driver SET status = 'Không có đơn' WHERE id = " + id;
                st = cnt.prepareStatement(sql);
                st.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean CheckCustomerRequest(int id) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS request_id FROM request WHERE customer_id = " + id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                sql = "SELECT status FROM request WHERE id LIKE '" + rs.getString("request_id") + "'";
                st = cnt.prepareStatement(sql);
                ResultSet newRs = st.executeQuery();
                if (newRs.next() && newRs.getString("status").equals("Đang tiến hành")) {
                    JDBCUtil.closeConnection(cnt);
                    return true;
                }
            }
            JDBCUtil.closeConnection(cnt);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean CheckDriverRequest(int id) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS request_id FROM request WHERE driver_id = " + id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                sql = "SELECT status FROM request WHERE id LIKE '" + rs.getString("request_id") + "'";
                st = cnt.prepareStatement(sql);
                ResultSet newRs = st.executeQuery();
                if (newRs.next() && newRs.getString("status").equals("Đã hủy")) {
                    JDBCUtil.closeConnection(cnt);
                    return true;
                }
            }
            JDBCUtil.closeConnection(cnt);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean CheckCompleteRequest(int id) {
        try {
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT MAX(id) AS request_id FROM request WHERE customer_id = " + id;
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                sql = "SELECT status FROM request WHERE id LIKE '" + rs.getString("request_id") + "'";
                st = cnt.prepareStatement(sql);
                ResultSet newRs = st.executeQuery();
                if (newRs.next() && newRs.getString("status").equals("Hoàn thành")) {
                    JDBCUtil.closeConnection(cnt);
                    return true;
                }
            }
            JDBCUtil.closeConnection(cnt);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Thread thread;

    public void ChekStatusRequest(int id) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Connection cnt = JDBCUtil.getConnection();
                        String sql = "SELECT status FROM request WHERE driver_id = " + id;
                        PreparedStatement st = cnt.prepareStatement(sql);
                        ResultSet rs = st.executeQuery();
                        while (rs.next()) {
                            if (rs.getString("status").equals("Thiết lập đơn")) {
                                TransferRequestData.setRequestStatus(!TransferRequestData.isRequestStatus());
                            }
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Platform.runLater(() ->{
            thread.start();
        });
    }

}
