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
import models.board.BoardDto;
import models.board.WriteService;
import models.admin.board.BoardAdminDto;
import models.admin.board.BoardInfoService;

@WebServlet("/board/write")
public class WriteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		BoardAdminDto boardInfo = null;
		try {
			String boardId = req.getParameter("boardId");
			if (boardId == null || boardId.isBlank()) {
				throw new BadRequestException();
			}
			
			/** 게시판 설정 조회 S */
			boardInfo = new BoardInfoService().get(req);
			if (boardInfo == null) {
				throw new BadRequestException(bundle.getString("NOT_EXISTS_BOARD"));
			}
			req.setAttribute("boardInfo", boardInfo);
			/** 게시판 설정 조회 E */
			
 		} catch (RuntimeException e) {
			alertError(resp, e, "history.back();");
			return;
		}
		
		String[] addJs = { "board/form", "ckeditor/ckeditor" };
		req.setAttribute("addJs", addJs);
		
		BoardDto board = new BoardDto();
		board.setBoardId(boardInfo.getBoardId());
		req.setAttribute("board", board);
		RequestDispatcher rd = req.getRequestDispatcher("/board/write.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			WriteService service = new WriteService();
			BoardDto board = service.write(req);
			
			// 작성 완료 후 게시글 보기 페이지로 이동 
			String url = "../board/view?id=" + board.getId();
			go(resp, url, "parent");
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
	
}
