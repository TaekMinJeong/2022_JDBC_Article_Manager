package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
	public void run() {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			
			System.out.printf("명령어 : ");
			String cmd = sc.nextLine().trim();
			
			Connection conn = null;
			
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			}catch(ClassNotFoundException e){
				System.out.println("드라이버 로딩 실패");
				break;
			}
			
			String url = "jdbc:mysql://localhost:3306/jdbc_article_manager?serverTimezone=UTC";
			
			try {
				if(cmd.equals("exit")) {
					System.out.println("프로그램을 종료합니다.");
					break;
				}
				
				conn = DriverManager.getConnection(url, "root", "");
				
				doAction(conn, sc, cmd);
				
			}catch(SQLException e) {
				System.out.println("에러 : " + e);
				break;
			}finally {
				try {
					if(conn != null && !conn.isClosed()) { 
					}
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		sc.close();
	}

	private void doAction(Connection conn, Scanner sc, String cmd) {
		
		int lastArticleId = 0;
		
		if(cmd.equals("article write")) {
			System.out.println("== 게시물 작성 ==");
			
			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();
			
			PreparedStatement pstmt = null;
			
			try {
				String sql = "INSERT INTO article "
						+ "SET regDate = NOW(), "
						+ "updateDate = NOW(), "
						+ "title = '" + title + "', "
						+ "`body` = '" + body + "';";
				
				System.out.println(sql);
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.executeUpdate();
				
			}catch(SQLException e) {
				System.out.println("에러 : " + e);
			}finally {
				try {
					if(pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("글이 생성되었습니다.");
			
		}else if(cmd.startsWith("article modify ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);
			System.out.printf("== %d번 게시물 수정 ==\n", id);
			
			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();
			
			PreparedStatement pstmt = null;
			
			try {
				String sql = "UPDATE article "
						+ "SET updateDate = NOW(), "
						+ "title = '" + title + "', "
						+ "`body` = '" + body + "' "
						+ "WHERE id = " + id + ";";
				
				System.out.println(sql);
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.executeUpdate();
				
			}catch(SQLException e) {
				System.out.println("에러 : " + e);
			}finally {
				try {
					if(pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			System.out.printf("%d번 글이 수정되었습니다.\n", id);
		}else if(cmd.equals("article list")) {
			System.out.println("== 게시물 리스트 ==");
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			List<Article> articles = new ArrayList<>();
			
			try {
				String sql = "SELECT * "
						+ "FROM article "
						+ "ORDER BY id DESC;";
				
				pstmt = conn.prepareStatement(sql);
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
				
			}catch(SQLException e) {
				System.out.println("에러 : " + e);
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
			}
			
			if(articles.size() == 0) {
				System.out.println("게시물이 없습니다");
				return;
			}
			
			System.out.println("번호	|	제목");
			
			for(Article article : articles) {
				System.out.printf("%d	|	%s\n", article.id, article.title);
			}
		}
	}
}
