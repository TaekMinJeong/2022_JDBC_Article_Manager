package com.KoreaIT.example.JAM.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.KoreaIT.example.JAM.Article;

public class JDBCSelectTEST {
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Article> articles = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/jdbc_article_manager?serverTimezone=UTC",	//	DB URL
					"root", "");	//	USER_NAME 과 PASSWORD
			System.out.println("Success");
			
			String sql = "SELECT * "
					+ "FROM article "
					+ "ORDER BY id DESC;";
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String regDate = rs.getString("regDate");
				String updateDate = rs.getString("updateDate");
				String title = rs.getString("title");
				String body = rs.getString("body");
				
				Article article = new Article(id, regDate, updateDate, title, body);
				articles.add(article);
				
				System.out.println("결과 : " + article);
			}
			
		}catch(SQLException ex) {
			System.out.println("SQLException" + ex);
			ex.printStackTrace();
		}
		catch(Exception ex) {
			System.out.println("Exception" + ex);
			ex.printStackTrace();
		}finally {
			try {
				if(rs != null && !rs.isClosed()) {
					rs.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
			try {
				if(pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
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
