package controllers.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.comment.CommentDto;
import models.board.comment.CommentInfoService;
import models.board.comment.CommentService;
import models.board.comment.CommentValidator;

import static commons.Utils.*;

/**
 * 댓글 작성 
 * 
 * @author YONGGYO
 *
 */
@WebServlet("/board/comment")
public class CommentController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentDto comment = new CommentInfoService().get(req, resp);
			
			req.setAttribute("comment", comment);
			
			RequestDispatcher rd = req.getRequestDispatcher("/board/comment_form.jsp");
			rd.forward(req, resp);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
			return;
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String mode = req.getParameter("mode");
			CommentService service = new CommentService();
			CommentDto comment = service.comment(req);
			
			String url = "../board/view?id=" + comment.getBoardDataId() + "#comment_" + comment.getId();
			System.out.println(comment);
			System.out.println(url);
			go(resp, url, "parent");
			if (mode == null || ! mode.equals("update")) {
				reload(resp, "parent");
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
}
