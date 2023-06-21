package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] arge) {
		Scanner sc = new Scanner(System.in);
		
		int lastArticleId = 0;
		
		while(true) {
			System.out.printf("명령어 : ");
			String cmd = sc.nextLine().trim();
			
			if(cmd.equals("article write")) {
				System.out.println("== 게시물 작성 ==");
				
				int id = lastArticleId + 1;
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				
				
				
				Connection con = null;
				PreparedStatement pstmt = null;
				
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/jdbc_article_manager?serverTimezone=UTC",	//	DB URL
							"root", "");	//	USER_NAME 과 PASSWORD
					System.out.println("Success");
					
					String sql = "INSERT INTO article "
							+ "SET regDate = NOW(), "
							+ "updateDate = NOW(), "
							+ "title = '" + title + "', "
							+ "`body` = '" + body + "';";
					
					System.out.println(sql);
					
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
				
				
				
				lastArticleId++;
				
//				System.out.println(article);
				
//				System.out.printf("%d번 글이 생성되었습니다.\n", article.id);
			}else if(cmd.startsWith("article modify ")) {
				int id = Integer.parseInt(cmd.split(" ")[2]);
				System.out.printf("== %d번 게시물 수정 ==\n", id);
				
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String body = sc.nextLine();
				
				
				
				Connection con = null;
				PreparedStatement pstmt = null;
				
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(
							"jdbc:mysql://localhost:3306/jdbc_article_manager?serverTimezone=UTC",	//	DB URL
							"root", "");	//	USER_NAME 과 PASSWORD
					System.out.println("Success");
					
					String sql = "UPDATE article "
							+ "SET updateDate = NOW(), "
							+ "title = '" + title + "', "
							+ "`body` = '" + body + "' "
							+ "WHERE id = " + id + ";";
					
					System.out.println(sql);
					
					pstmt = con.prepareStatement(sql);
					
					pstmt.executeUpdate();
					
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
				
				System.out.printf("%d번 글이 수정되었습니다.\n", id);
			}else if(cmd.equals("article list")) {
				System.out.println("== 게시물 리스트 ==");
				
				
				
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
				
				
				
				
				
				
				if(articles.size() == 0) {
					System.out.println("게시물이 없습니다");
					continue;
				}
				
				System.out.println("번호	|	제목");
				
				for(Article article : articles) {
					System.out.printf("%d	|	%s\n", article.id, article.title);
				}
			}
			
			if(cmd.equals("exit")) {
				System.out.println("프로그램을 종료합니다.");
				break;
			}
		}
		sc.close();
	}
}
