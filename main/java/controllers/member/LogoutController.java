package controllers.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.member.LogoutService;

@WebServlet("/member/logout")
public class LogoutController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LogoutService service = new LogoutService();
		service.logout(req);
		
		// 로그아웃 완료 후 메인페이지로 이동
		resp.sendRedirect(req.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
