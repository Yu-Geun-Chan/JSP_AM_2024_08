package com.KoreaIT.java.jsp_AM.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/article/write")
public class ArticleWriteServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		
		// session에 setAttrtibute한 값을 가져오기 위해 추가
		HttpSession session = request.getSession();
		
		// 로그인을 하지 않았다면 write 페이지로 보내기 전에 알림창에 로그인 하라고 띄우고 login 페이지로 보내는 코드
		if (session.getAttribute("loginedMemberId") == null) {
			response.getWriter()
			.append(String.format("<script>alert('로그인을 하고 이용해주세요.'); location.replace('../member/login');</script>"));
			return;
		}
		// 여기까지

		request.getRequestDispatcher("/jsp/article/write.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
