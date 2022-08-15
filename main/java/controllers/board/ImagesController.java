package controllers.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.images.ListService;
import models.file.FileDto;

/**
 * 이미지 게시판 
 * 
 * @author YONGGYO
 *
 */
@WebServlet("/board/images")
public class ImagesController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setAttribute("addJs", new String[] { "board/image_board"} );
		req.setAttribute("addCss", new String[] {"board/image_board"} );
		
		/** 이미지 목록 조회 S */
		List<FileDto> images = new ListService().gets();
		req.setAttribute("images", images);
		/** 이미지 목록 조회 E */
		
		RequestDispatcher rd = req.getRequestDispatcher("/board/images.jsp");
		rd.forward(req, resp);
	}
}
