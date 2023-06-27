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

import com.KoreaIT.example.JAM.controller.ArticleController;
import com.KoreaIT.example.JAM.controller.MemberController;
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
		
		MemberController memberController = new MemberController(conn, sc);
		ArticleController articleController = new ArticleController(conn, sc);
		
		if(cmd.equals("member join")) {
			memberController.doJoin(cmd);
		}else if(cmd.equals("article write")) {
			articleController.doWrite(cmd);
		}else if(cmd.startsWith("article modify ")) {
			articleController.doModify(cmd);
		}else if(cmd.startsWith("article delete ")) {
			articleController.doDelete(cmd);
		}else if(cmd.startsWith("article detail ")) {
			articleController.showDetail(cmd);;
		}else if(cmd.equals("article list")) {
			articleController.showList(cmd);
		}else {
			System.out.println("존재하지 않는 명령어입니다.");
		}
	}
}
