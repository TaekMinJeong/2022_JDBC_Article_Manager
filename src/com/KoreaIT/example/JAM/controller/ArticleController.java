package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Article;
import com.KoreaIT.example.JAM.service.ArticleService;
import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class ArticleController extends Controller {
	
	private ArticleService articleService;
	private Connection conn;
	
	public ArticleController(Connection conn, Scanner sc) {
		// 부모 생성자
		// 기본은 부모 생성자가 생략됨
		super(sc);
		this.conn = conn;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite(String cmd) {
		System.out.println("== 게시물 작성 ==");
		
		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();
		
		int id = articleService.doWrite(title, body);
		
		
		System.out.printf("%d번 글이 생성되었습니다.\n", id);
	}

	public void doModify(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);
		System.out.printf("== %d번 게시물 수정 ==\n", id);
		
		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();
		
		articleService.doModify(id, title, body);
		
		System.out.printf("%d번 글이 수정되었습니다.\n", id);
	}

	public void doDelete(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);
		System.out.printf("== %d번 게시물 삭제 ==\n", id);
		
		boolean isArticleExists = articleService.isArticleExists(id);
		
		if(isArticleExists == false) {
			System.out.printf("%d번 게시글은 존재하지 않습니다\n", id);
			return;
		}
		
		articleService.doDelete(id);
		
		System.out.printf("%d번 글이 삭제되었습니다.\n", id);
	}

	public void showDetail(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);
		System.out.printf("== %d번 게시물 상세보기 ==\n", id);
		
		SecSql sql = new SecSql();
		sql.append("SELECT * FROM article");
		sql.append("WHERE id = ?", id);
		
		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
		
		if(articleMap.isEmpty()) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}
		
		Article article = new Article(articleMap);
		
		System.out.printf("번호		: %d\n", article.id);
		System.out.printf("작성날짜	: %s\n", article.regDate);
		System.out.printf("수정날짜	: %s\n", article.updateDate);
		System.out.printf("제목		: %s\n", article.title);
		System.out.printf("내용		: %s\n", article.body);
	}

	public void showList(String cmd) {
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
