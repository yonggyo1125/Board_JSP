package controllers.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.CommentService;
import models.board.comment.CommentDto;

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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentService service = new CommentService();
			CommentDto comment = service.comment(req);
			
			String url = "../board/view?id=" + comment.getBoardDataId() + "#comment_" + comment.getId();
			go(resp, url, "parent");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
}
