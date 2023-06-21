package com.KoreaIT.example.JAM.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCInsertTest {
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/jdbc_article_manager?serverTimezone=UTC",	//	DB URL
					"root", "");	//	USER_NAME 과 PASSWORD
			System.out.println("Success");
			
			String sql = "INSERT INTO article\r\n"
					+ "SET regDate = NOW(),\r\n"
					+ "updateDate = NOW(),\r\n"
					+ "title = CONCAT('제목', RAND()),\r\n"
					+ "`body` = CONCAT('제목', RAND());";
			
			pstmt = con.prepareStatement(sql);
			
			int row = pstmt.executeUpdate();
			System.out.printf("row : " + row);
			
		}catch(SQLException ex) {
			System.out.println("SQLException" + ex);
			ex.printStackTrace();
		}
		catch(Exception ex) {
			System.out.println("Exception" + ex);
			ex.printStackTrace();
		}finally {
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					con.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
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
