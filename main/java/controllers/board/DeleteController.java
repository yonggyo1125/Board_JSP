package controllers.board;

import static commons.Utils.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.BoardDto;
import models.board.DeleteService;

@WebServlet("/board/delete")
public class DeleteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			DeleteService service = new DeleteService();
			BoardDto board = service.delete(req, resp);
			
			go(resp, "../board/list?boardId=" + board.getBoardId());
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
