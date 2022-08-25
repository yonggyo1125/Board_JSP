package models.board;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import commons.BadRequestException;
import models.admin.board.BoardAdminDao;
import models.admin.board.BoardAdminDto;
import models.board.BoardDao;
import models.board.BoardDto;

public class ListService {
	public Map<String, Object> getList(HttpServletRequest request) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 게시판 아이디 필수 체크 S */
		String boardId = request.getParameter("boardId");
		if (boardId == null || boardId.isBlank()) {
			throw new BadRequestException();
		}
		/** 게시판 아이디 필수 체크 E */
		
		/**  등록된 게시판인지 체크 S */
		BoardAdminDto boardInfo = BoardAdminDao.getInstance().get(boardId);
		if (boardInfo == null) {
			throw new BoardException(bundle.getString("NOT_EXISTS_BOARD"));
		}
		/** 등록된 게시판인지 체크 E */
		
		/** 게시판 사용 가능 여부 체크 S */
		if (boardInfo.getIsUse() == 0) {
			throw new BoardException(bundle.getString("NOT_ALLOWED_BOARD"));
		}
		/** 게시판 사용 가능 여부 체크 E */
		
		BoardDao dao = BoardDao.getInstance();

		int page = 1;
		try {
			page = request.getParameter("page")  == null ? 1 : Integer.parseInt(request.getParameter("page").trim());
		} catch (Exception e) {  // 페이지 번호가 숫자가 아닌 경우는 1로 고정
			page = 1;
		}
		
		/** 1 페이지당 게시글 수 */
		int limit = boardInfo.getNoOfRows() > 0 ?  boardInfo.getNoOfRows() : 20;
		
		List<BoardDto> posts = dao.gets(boardId, page, limit);
		int total = dao.getTotal(boardId);
		
		Map<String,Object> results = new HashMap<>();

		results.put("page", page);
		results.put("total", total);
		results.put("posts", posts);
		results.put("boardInfo", boardInfo);
		
		request.setAttribute("page", page);
		request.setAttribute("total", total);
		request.setAttribute("posts", posts);
		request.setAttribute("boardInfo", boardInfo);
		
		request.setAttribute("title", boardInfo.getBoardNm());
		
		return results;
	}
}
