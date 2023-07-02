package com.example.demo1.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
	public static Connection getConnection() {
		Connection cnt = null;
		try {
			String url = "jdbc:postgresql://localhost:5432/TaxiBooking";
			String username = "postgres";
			String password = "07022003";
			cnt = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnt;
	}
	
	public static void closeConnection(Connection c) {
		try {
			if (c!=null) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
