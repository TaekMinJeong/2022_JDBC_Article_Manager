package com.KoreaIT.example.JAM;

public class Article extends Object {
	public Article(int id, String title, String body) {
		this.id = id;
		this.title = title;
		this.body = body;
	}
	int id;
	String title;
	String body;
	
	@Override
//	toString() 을 사용하기 위해서는 Object 를 상속받아야함
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", body=" + body + "]";
	}
}
