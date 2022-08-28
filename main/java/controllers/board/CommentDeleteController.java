package controllers.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.comment.CommentDeleteService;
import models.board.comment.CommentDto;

import static commons.Utils.*;

@WebServlet("/board/comment/delete")
public class CommentDeleteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentDeleteService service = new CommentDeleteService();
			CommentDto comment =  service.delete(req, resp);
			
			go(resp, "../../board/view?id=" + comment.getBoardDataId(), "parent");
		} catch (RuntimeException e) {
			alertError(resp, e, "history.back()");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
