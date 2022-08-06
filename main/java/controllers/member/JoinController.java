package controllers.member;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.member.MemberJoinService;
import static commons.Utils.*;

@WebServlet("/member/join")
public class JoinController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		RequestDispatcher rd = req.getRequestDispatcher("/member/join.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			MemberJoinService service = new MemberJoinService();
			service.join(req);
			
			// 가입 성공한 경우 로그인 페이지로 이동 
			go(resp, "../member/login", "parent");
		} catch (RuntimeException e) {
			alertError(resp, e);
		}
	}
	
}
