package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

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
			
			SecSql sql = new SecSql();
			
			sql.append("INSERT INTO article");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", `body` = ?", body);
			
			int id = DBUtil.insert(conn, sql);
			
			System.out.printf("%d번 글이 생성되었습니다.\n", id);
			
		}else if(cmd.startsWith("article modify ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);
			System.out.printf("== %d번 게시물 수정 ==\n", id);
			
			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();
			
			SecSql sql = new SecSql();
			
			sql.append("UPDATE article");
			sql.append("SET updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", `body` = ?", body);
			sql.append("WHERE id = ?", id);
			
			DBUtil.update(conn, sql);
			
			System.out.printf("%d번 글이 수정되었습니다.\n", id);
		}else if(cmd.startsWith("article delete ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);
			System.out.printf("== %d번 게시물 삭제 ==\n", id);
			
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*) FROM article");
			sql.append("WHERE id = ?", id);
			
			int articlesCount = DBUtil.selectRowIntValue(conn, sql);
			
			if(articlesCount == 0) {
				System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
				return;
			}
			
			sql = new SecSql();
			sql.append("DELETE FROM article");
			sql.append("WHERE id = ?", id);
			
			DBUtil.delete(conn, sql);
			
			System.out.printf("%d번 글이 삭제되었습니다.\n", id);
		}else if(cmd.equals("article list")) {
			System.out.println("== 게시물 리스트 ==");
			
			List<Article> articles = new ArrayList<>();
			
			SecSql sql = new SecSql();
			
			sql.append("SELECT *");
			sql.append("FROM article");
			sql.append("ORDER BY id DESC;");
			
			List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
			
			for(Map<String, Object> articleMap : articleListMap) {
				articles.add(new Article(articleMap));
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
