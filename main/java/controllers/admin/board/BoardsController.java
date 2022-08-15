package controllers.admin.board;

import static commons.Utils.*;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.admin.board.BoardAdminDto;
import models.admin.board.BoardListService;
import models.admin.board.BoardDeleteService;

@WebServlet("/admin/boards")
public class BoardsController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		/** 게시판 목록 S */
		BoardListService service = new BoardListService();
		List<BoardAdminDto> boards = service.gets();
		req.setAttribute("boards", boards);
		/** 게시판 목록 E */
		
		RequestDispatcher rd = req.getRequestDispatcher("/admin/board/list.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			BoardDeleteService service = new BoardDeleteService();
			service.delete(req);
			
			// 게시판 설정 삭제 완료 되면 설정 목록으로 이동한다.
			go(resp, "boards", "parent");
		} catch (RuntimeException e) {
			alertError(resp, e);
		}
	}
}
