package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.comment.CommentPasswordCheckService;

import static commons.Utils.*;

@WebServlet("/board/comment/password")
public class CommentPasswordController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentPasswordCheckService service = new CommentPasswordCheckService();
			service.check(req);
			
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
			alert(resp, bundle.getString("PASSWORD_CONFIRM"), "parent.location.reload()");
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}	
}