package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] arge) {
		Scanner sc = new Scanner(System.in);
		
		List<Article> articles = new ArrayList<>();
		
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
				
				Article article = new Article(id, title, body);
				articles.add(article);
				
				
				
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
				
				System.out.printf("%d번 글이 생성되었습니다.\n", article.id);
			}else if(cmd.equals("article list")) {
				System.out.println("== 게시물 리스트 ==");
				
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
