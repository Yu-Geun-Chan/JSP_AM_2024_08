package com.KoreaIT.java.jsp_AM.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.KoreaIT.java.jsp_AM.service.ArticleService;
import com.KoreaIT.java.jsp_AM.util.DBUtil;
import com.KoreaIT.java.jsp_AM.util.SecSql;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ArticleController {

	// request, response, conn를 ArticleController에서 사용하기 위해 변수 선언
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Connection conn;
	
	private ArticleService articleService;

	// 생성자
	public ArticleController(HttpServletRequest request, HttpServletResponse response, Connection conn) {
		this.conn = conn;
		this.request = request;
		this.response = response;
		this.articleService = new ArticleService(conn);
	}

	// ArticleListServlet에서 하던걸 ArticleController 클래스의 showList 메서드에서 실행
	public void showList() throws ServletException, IOException {
		int page = 1;

		if (request.getParameter("page") != null && request.getParameter("page").length() != 0) {
			page = Integer.parseInt(request.getParameter("page"));
		}

		int itemsInAPage = 10;
		int limitFrom = (page - 1) * itemsInAPage;


		int totalCnt = articleService.getTotalCnt();
		int totalPage = (int) Math.ceil(totalCnt / (double) itemsInAPage);

		List<Map<String, Object>> articleRows = articleService.getForPrintArticles(limitFrom,itemsInAPage);

		HttpSession session = request.getSession();

		boolean isLogined = false;
		int loginedMemberId = -1;
		Map<String, Object> loginedMember = null;

		if (session.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
			loginedMember = (Map<String, Object>) session.getAttribute("loginedMember");
		}

		request.setAttribute("page", page);
		request.setAttribute("totalPage", totalPage);
		request.setAttribute("totalCnt", totalCnt);
		request.setAttribute("articleRows", articleRows);
		request.getRequestDispatcher("/jsp/article/list.jsp").forward(request, response);
	}

	public void showDetail() throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		SecSql sql = SecSql.from("SELECT A.*, M.name");
		sql.append("FROM article AS A");
		sql.append("INNER JOIN `member` AS M");
		sql.append("ON A.memberId = M.id");
		sql.append("WHERE A.id = ?", id);

		Map<String, Object> articleRow = DBUtil.selectRow(conn, sql);

		request.setAttribute("articleRow", articleRow);
		request.getRequestDispatcher("/jsp/article/detail.jsp").forward(request, response);
	}

	public void write() throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");

		// session에 setAttrtibute한 값을 가져오기 위해 추가
		HttpSession session = request.getSession();

		// 로그인을 하지 않았다면 write 페이지로 보내기 전에 알림창에 로그인 하라고 띄우고 login 페이지로 보내는 코드
		if (session.getAttribute("loginedMemberId") == null) {
			response.getWriter().append(
					String.format("<script>alert('로그인을 하고 이용해주세요.'); location.replace('../member/login');</script>"));
			return;
		}
		// 여기까지

		request.getRequestDispatcher("/jsp/article/write.jsp").forward(request, response);

	}

}
