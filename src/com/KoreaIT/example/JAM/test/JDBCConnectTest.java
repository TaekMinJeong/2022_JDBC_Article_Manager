package com.KoreaIT.example.JAM.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnectTest {
	public static void main(String[] args) {
		Connection con = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/jdbc_article_manager?serverTimezone=UTC",	//	DB URL
					"root", "");	//	USER_NAME 과 PASSWORD
			System.out.println("Success");
		}catch(SQLException ex) {
			System.out.println("SQLException" + ex);
			ex.printStackTrace();
		}
		catch(Exception ex) {
			System.out.println("Exception" + ex);
			ex.printStackTrace();
		}finally {
			try {
				if(con != null && !con.isClosed()) {
					con.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
}
