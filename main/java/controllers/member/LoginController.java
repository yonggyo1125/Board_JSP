package controllers.member;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
			
			/** 아이디 저장 처리 S */
			String memId = req.getParameter("memId");
			String saveMemId = req.getParameter("saveMemId");
			if (saveMemId == null) { 
				// 아이디 저장이 아니면 쿠키 삭제
				for (Cookie cookie : req.getCookies()) {
					if (cookie.getName().equals("savedMemId")) {
						cookie.setMaxAge(0);
					}
				}
			} else { 
				// 아이디 저장이라면 쿠기 추가 
				resp.addCookie(new Cookie("savedMemId", memId));
			}
			/** 아이디 저장 처리 E */
			
			// 로그인 성공시 메인페이지로 이동 
			String url = req.getContextPath();
			go(resp, url, "parent");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
	
}
