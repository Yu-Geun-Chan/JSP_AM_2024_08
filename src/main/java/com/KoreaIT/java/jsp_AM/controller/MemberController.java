package com.KoreaIT.java.jsp_AM.controller;

import java.sql.Connection;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberController {

	// request, response, conn를 ArticleController에서 사용하기 위해 변수 선언
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Connection conn;

	// 생성자
	public MemberController(HttpServletRequest request, HttpServletResponse response, Connection conn) {
		this.conn = conn;
		this.request = request;
		this.response = response;
	}

}
