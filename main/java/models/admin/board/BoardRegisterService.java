package models.admin.board;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminValidator;

/**
 * 게시판 설정 등록
 * 
 * @author YONGGYO
 *
 */
public class BoardRegisterService {
	
	public void register(HttpServletRequest request) {
		
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
		
		/** 이미 등록된 게시판 아이디 여부 체크 */
		validator.checkDuplicateBoardId(boardId);
		
		/** 게시판 설정 등록 S */
		BoardAdminDto dto = new BoardAdminDto();
		dto.setBoardId(boardId);
		dto.setBoardNm(boardNm);
		dto.setIsUse(Integer.parseInt(isUse));
		dto.setNoOfRows(Integer.parseInt(noOfRows));
		dto.setUseComment(Integer.parseInt(useComment));
	
		BoardAdminDao dao = BoardAdminDao.getInstance();
		if (!dao.register(dto)) {
			new BoardAdminException(bundle.getString("CREATE_BOARD_ERROR"));
		}
		
		/** 게시판 설정 등록 E */
	}
} 
