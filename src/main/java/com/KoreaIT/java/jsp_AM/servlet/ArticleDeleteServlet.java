package com.KoreaIT.java.jsp_AM.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.KoreaIT.java.jsp_AM.util.DBUtil;
import com.KoreaIT.java.jsp_AM.util.SecSql;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/article/doDelete")
public class ArticleDeleteServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");

		// session에 setAttrtibute한 값을 가져오기 위해 추가
		HttpSession session = request.getSession();

		// 로그인을 하지 않았다면 글 삭제 눌렀을 때 알림창에 로그인 하라고 띄우고 login 페이지로 보내는 코드
		if (session.getAttribute("loginedMemberId") == null) {
			response.getWriter().append(
					String.format("<script>alert('로그인을 하고 이용해주세요.'); location.replace('../member/login');</script>"));
			return;
		}
		// 여기까지

		// DB 연결
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("클래스 x");
			e.printStackTrace();
		}

		String url = "jdbc:mysql://127.0.0.1:3306/24_08_JAM?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

		String user = "root";
		String password = "";

		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url, user, password);

			int id = Integer.parseInt(request.getParameter("id"));

			// 해당하는 글의 정보를 가져오기 위해 SQL SELECT문 작성 
			SecSql sql = SecSql.from("SELECT *");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			Map<String, Object> articleRow = DBUtil.selectRow(conn, sql);
			
			if (articleRow == null) {
				response.getWriter().append(
						String.format("<script>alert('%d번 글은 존재하지 않습니다.'); location.replace('list');</script>", id));
				return;
			}

			// 해당 글이 존재한다면 session에 저장되어 있는 로그인 한 회원의 memberId와 해당하는 글에 저장되어 있는
			// 작성자의 memberId와 일치하는지 판단하는 코드
			// 일치하지 않는다면 해당 글에 대한 권한이 없다고 알림창 띄우고 list 페이지로 돌려보내기
			int loginedMemberId = (int) session.getAttribute("loginedMemberId");

			if (loginedMemberId != (int) articleRow.get("memberId")) {

				response.getWriter().append(
						String.format("<script>alert('%d번 글에 대한 권한이 없습니다.'); location.replace('list');</script>", id));
				return;
			}
			// 여기까지

			// 일치한다면 해당하는 글을 지우기 위해 SQL DELETE문 작성
			sql = SecSql.from("DELETE");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			DBUtil.delete(conn, sql);

			// 해당 글이 지워졌다고 알림창 띄우고 list 페이지로 돌려보내기
			response.getWriter()
					.append(String.format("<script>alert('%d번 글이 삭제 됨'); location.replace('list');</script>", id));

		} catch (SQLException e) {
			System.out.println("에러 1 : " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
