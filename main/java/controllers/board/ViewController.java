package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.BadRequestException;

import static commons.Utils.*;

import models.admin.board.BoardAdminDao;
import models.admin.board.BoardAdminDto;
import models.board.BoardDto;
import models.board.ViewService;

@WebServlet("/board/view")
public class ViewController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String[] addCss = { "board/board" };
			req.setAttribute("addCss", addCss);
			
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
				
			/** 게시글 조회 S */
			req.setAttribute("isViewPage", true);
			BoardDto board = new ViewService().view(req, resp);
			System.out.println(board);
			req.setAttribute("board", board);
			req.setAttribute("title", board.getSubject());
			/** 게시글 조회 E */
	
			/** 게시판 설정 조회 S */
			BoardAdminDto boardInfo = BoardAdminDao.getInstance().get(board.getBoardId());
			if (boardInfo == null) {
				throw new BadRequestException(bundle.getString("NOT_EXISTS_BOARD"));
			}
			
			req.setAttribute("boardInfo", boardInfo);
			/** 게시판 설정 조회 E */
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
			return;
		}
				
		RequestDispatcher rd = req.getRequestDispatcher("/board/view.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
