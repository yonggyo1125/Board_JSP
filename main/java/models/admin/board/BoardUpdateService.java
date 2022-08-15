package models.admin.board;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminValidator;

/**
 * 게시판 설정 수정
 * 
 * @author YONGGYO
 *
 */
public class BoardUpdateService {
	
	public void update(HttpServletRequest request) {
		 ResourceBundle bundle = ResourceBundle.getBundle("bundle.admin");
			
			/** 필수 입력 항목 체크 S */
			BoardAdminValidator validator = new BoardAdminValidator();
			Map<String, String> checkFields = new HashMap<>();
			checkFields.put("boardId", bundle.getString("REQUIRED_BOARD_ID"));
			checkFields.put("boardNm", bundle.getString("REQUIRED_BOARD_NM"));
			validator.requiredCheck(request, checkFields);
			
			/** 필수 입력 항목 체크 E */
			
			String boardId = request.getParameter("boardId");
			String boardNm= request.getParameter("boardNm");
			String isUse = request.getParameter("isUse");
			String noOfRows = request.getParameter("noOfRows");
			String useComment = request.getParameter("useComment");
			
			isUse = (isUse == null || isUse.isBlank()) ? "0": isUse;
			noOfRows = (noOfRows == null || noOfRows.isBlank()) ? "20" : noOfRows;
			useComment = (useComment == null || noOfRows.isBlank()) ? "0" : useComment;
			
			/** 게시판 설정 수정 S */
			BoardAdminDto dto = new BoardAdminDto();
			dto.setBoardId(boardId);
			dto.setBoardNm(boardNm);
			dto.setIsUse(Integer.parseInt(isUse));
			dto.setNoOfRows(Integer.parseInt(noOfRows));
			dto.setUseComment(Integer.parseInt(useComment));
		
			BoardAdminDao dao = BoardAdminDao.getInstance();
			if (!dao.update(dto)) {
				new BoardAdminException(bundle.getString("UPDATE_BOARD_ERROR"));
			}
			
			/** 게시판 설정 수정 E */
	}
}
