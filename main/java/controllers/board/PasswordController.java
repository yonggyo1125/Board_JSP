package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import models.board.PasswordCheckService;

@WebServlet("/board/password")
public class PasswordController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PasswordCheckService service = new PasswordCheckService();
			service.check(req);
			
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
			alert(resp, bundle.getString("PASSWORD_CONFIRM"), "parent.location.reload()");
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
	
}
