package models.admin.board;

import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminDao;

public class BoardInfoService {
	
	public BoardAdminDto get(HttpServletRequest request) {
			String boardId = request.getParameter("boardId");
			if (boardId != null && !boardId.isBlank()) {
				BoardAdminDto board = BoardAdminDao.getInstance().get(boardId);
				return board;
			}
		
			return null;
	}
}
