package models.admin.board;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminValidator;

public class BoardDeleteService {
	
	public void delete(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.admin");
		
		/** 필수 입력 항목 체크 S */
		BoardAdminValidator validator = new BoardAdminValidator();
		Map<String, String> checkFields = new HashMap<>();
		checkFields.put("boardId", bundle.getString("REQUIRED_DELETE_BOARD_ID"));
		validator.requiredCheck(request, checkFields);
		
		/** 필수 입력 항목 체크 E */
		
		/** 삭제 처리 S */
		BoardAdminDao dao = BoardAdminDao.getInstance();
		String[] ids = request.getParameterValues("boardId");
		for (String id : ids) {
			dao.delete(id);
		}
		/** 삭제 처리 E */
	}
}
