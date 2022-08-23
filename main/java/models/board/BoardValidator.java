package models.board;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;
import models.validation.Validator;

/**
 * 게시글 작성, 수정, 삭제시 유효성 검사 
 * 
 * @author YONGGYO
 *
 */
public class BoardValidator implements Validator {
	
	private static BoardValidator instance = new BoardValidator();
	
	private BoardValidator() {}
	/**
	 * 유효성 검사 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {String} mode : register(등록), update(수정), delete(삭제)
	 */
	public void validate(HttpServletRequest request, String mode) {
		Map<String, String> checkFields = new HashMap<>();
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		// 게시글 등록, 수정 
		if (mode.equals("register") || mode.equals("update")) {
			if (mode.equals("register")) {
				checkFields.put("boardId", bundle.getString("REQUIRED_BOARD_ID"));
			} else {
				checkFields.put("id", bundle.getString("REQUIRED_ID"));
			}
			
			checkFields.put("gid", bundle.getString("REQUIRED_GROUP_ID"));
			checkFields.put("subject", bundle.getString("REQUIRED_SUBJECT"));
			checkFields.put("poster", bundle.getString("REQUIRED_POSTER"));
			
			if (!isLogin(request)) { // 비회원 비밀번호는 로그인 하지 않는 경우만 체크 
				checkFields.put("guestPw", bundle.getString("REQUIRED_GUEST_PW"));
			}
			checkFields.put("content", bundle.getString("REQUIRED_CONTENT"));
		} else if (mode.equals("delete")) {
			checkFields.put("id", bundle.getString("REQUIRED_ID"));
		}
		
		// 필수 데이터 검증
		requiredCheck(request, checkFields);
	}
	
	public static BoardValidator getInstance() {
		if (instance == null) {
			instance = new BoardValidator();
		}
		
		return instance;
	}
}
