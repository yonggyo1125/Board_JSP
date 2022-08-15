package controllers.admin.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.admin.board.BoardAdminDto;
import models.admin.board.BoardInfoService;
import models.admin.board.BoardRegisterService;
import models.admin.board.BoardUpdateService;
import static commons.Utils.*;

@WebServlet("/admin/board")
public class IndexController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardInfoService service = new BoardInfoService();
		BoardAdminDto board = service.get(req);
		req.setAttribute("board", board);
		
		RequestDispatcher rd = req.getRequestDispatcher("/admin/board/index.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String mode = req.getParameter("mode");
			if (mode != null && mode.equals("modify")) {
				BoardUpdateService service = new BoardUpdateService();
				service.update(req);
			} else {
				BoardRegisterService service = new BoardRegisterService();
				service.register(req);
			}
			
			// 게시판 설정 등록이 완료 되면 설정 목록으로 이동한다.
			go(resp, "boards", "parent");
		} catch (RuntimeException e) {
			alertError(resp, e);
		}	
	}
}
