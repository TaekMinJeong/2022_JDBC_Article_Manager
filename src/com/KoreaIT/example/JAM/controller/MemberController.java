package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class MemberController extends Controller {
	
	private Connection conn;
	
	public MemberController(Connection conn, Scanner sc) {
		//	부모 생성자
		//	기본은 부모 생성자가 생략됨
		super(sc);
		this.conn = conn;
	}

	public void doJoin(String cmd) {
		System.out.println("== 회원가입 ==");
		
		String loginId = null;
		String loginPw = null;
		String loginPwChk = null;
		String name = null;
		
		while(true) {
			System.out.printf("아이디 : ");
			loginId = sc.nextLine().trim();
			
			if(loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				continue;
			}
			
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*) > 0");
			sql.append("FROM `member`");
			sql.append("WHERE loginId = ?", loginId);
			
			boolean isLoginIdDup = DBUtil.selectRowBooleanValue(conn, sql);
			
			if(isLoginIdDup) {
				System.out.printf("%s은(는) 이미 사용중인 아이디입니다.\n", loginId);
				continue;
			}
			
			while(true) {
				boolean loginPwCheck = true;
				
				System.out.printf("비밀번호 : ");
				loginPw = sc.nextLine().trim();
				
				if(loginPw.length() == 0) {
					System.out.println("비밀번호를 입력해주세요.");
					continue;
				}
				
				while(true) {
					System.out.printf("비밀번호 확인 : ");
					loginPwChk = sc.nextLine().trim();
					
					if(loginPwChk.length() == 0) {
						System.out.println("비밀번호 확인을 입력해주세요.");
						continue;
					}
					
					if(loginPw.equals(loginPwChk) == false) {
						System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
						loginPwCheck = false;
						break;
					}
					else {
						break;
					}
				}
				
				if(loginPwCheck) {
					break;
				}
			}
			
			while(true) {
				System.out.printf("이름 : ");
				name = sc.nextLine().trim();
				
				if(name.length() == 0) {
					System.out.println("이름을 입력해주세요.");
					continue;
				}
				
				break;
			}
			
			break;
		}
		
		SecSql sql = new SecSql();
		
		sql.append("INSERT INTO `member`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", loginId = ?", loginId);
		sql.append(", loginPw = ?", loginPw);
		sql.append(", `name` = ?", name);
		
		
		
		DBUtil.insert(conn, sql);
		
		System.out.printf("%s회원님, 가입 되었습니다..\n", name);
		
	}

}
