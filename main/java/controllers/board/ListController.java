package controllers.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import models.board.ListService;

@WebServlet("/board/list")
public class ListController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {			
			ListService service = new ListService();
			service.getList(req);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
			return;
		}
		
		String[] addCss = { "board/board"};
		req.setAttribute("addCss", addCss);
		
		RequestDispatcher rd = req.getRequestDispatcher("/board/list.jsp");
		rd.forward(req, resp);
	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
