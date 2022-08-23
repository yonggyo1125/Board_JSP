package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import commons.BadRequestException;
import models.board.ViewService;
import models.admin.board.BoardAdminDao;
import models.admin.board.BoardAdminDto;
import models.board.BoardDto;
import models.board.UpdateService;

@WebServlet("/board/update")
public class UpdateController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
			BoardDto board = new ViewService().view(req, resp);
			/** 게시판 설정 조회 S */
			
			req.setAttribute("board", board);
			BoardAdminDto boardInfo = BoardAdminDao.getInstance().get(board.getBoardId());
			if (boardInfo == null) {
				throw new BadRequestException(bundle.getString("NOT_EXISTS_BOARD"));
			}
			
			req.setAttribute("boardInfo", boardInfo);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
			return;
		}
		
		String[] addJs = { "board/form", "ckeditor/ckeditor" };
		req.setAttribute("addJs", addJs);
		
		RequestDispatcher rd = req.getRequestDispatcher("/board/update.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			UpdateService service = new UpdateService();
			BoardDto board = service.update(req);
			
			// 수정 완료 후 게시글 보기로 이동
			String url = "../board/view?id=" + board.getId();
			go(resp, url, "parent");
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
}
