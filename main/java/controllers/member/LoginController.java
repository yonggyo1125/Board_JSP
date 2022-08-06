package controllers.member;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.member.LoginService;
import static commons.Utils.*;

@WebServlet("/member/login")
public class LoginController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestDispatcher rd = req.getRequestDispatcher("/member/login.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			LoginService service = new LoginService();
			service.login(req);
			
			// 로그인 성공시 메인페이지로 이동 
			String url = req.getContextPath();
			go(resp, url, "parent");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
	
}
