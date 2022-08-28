package models.board.comment;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.BadRequestException;

import static commons.Utils.*;

import models.board.BoardException;
import models.member.MemberDto;
import models.validation.Validator;

/**
 * 댓글 관련 유효성 검사 
 * 
 * @author YONGGYO
 *
 */
public class CommentValidator implements Validator {
	
	ResourceBundle bundle = ResourceBundle.getBundle("bundle.comment");
	
	/**
	 * 유효성 검사 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {String} mode : register(등록), update(수정), delete(삭제)
	 */
	public void check(HttpServletRequest request, String mode) {
		Map<String, String> checkFields = new HashMap<>();

		if (mode.equals("register") || mode.equals("update")) { // 등록, 수정 
			
			/** 공통 필수 항목 체크 S */
			checkFields.put("poster", bundle.getString("REQUIRED_POSTER"));
			checkFields.put("content", bundle.getString("REQUIRED_CONTENT"));
			
			// 로그인 하지 않은 경우는 비회원 비밀번호 필수 
			if (request.getParameter("guestPw") != null) {
				checkFields.put("guestPw", bundle.getString("REQUIRED_GUEST_PW"));
			}
			/** 공통 필수 항목 E */
			
			if (mode.equals("register")) { // 댓글 등록일 때 필수 항목
				checkFields.put("boardDataId", bundle.getString("REQUIRED_BOARD_DATA_ID"));
			} else { // 댓글 수정일때 필수 항목
				checkFields.put("id", bundle.getString("REQUIRED_ID"));
			}	
		} else if (mode.equals("delete") || mode.equals("info")){ // 삭제 
			checkFields.put("id", bundle.getString("REQUIRED_ID"));
		}
		
		requiredCheck(request, checkFields);
		/** 공통 필수 항목 체크 S */
	}
	
	/**
	 * 댓글 수정, 삭제 권한 체크 
	 * 
	 * @param request
	 * @param response
	 */
	public void permissionCheck(HttpServletRequest request, HttpServletResponse response) {
		String _id = request.getParameter("id").trim();
		int id;
		try {
			id = Integer.parseInt(_id);
		} catch (Exception e) {
			throw new BadRequestException();
		}
		
		// 관리자는 무조건 권한 있음 
		if (isAdmin(request)) {
			return;
		}
		
		CommentDto comment = CommentDao.getInstance().get(id);
		if (comment == null) {
			throw new CommentException(bundle.getString("NOT_EXISTS_COMMENT"));
		}
		
		int memNo = comment.getMemNo();
		MemberDto member = getMember(request);
		if (memNo > 0) {
			if (member == null ||  member.getMemNo() != memNo) {
				throw new CommentException(bundle.getString("NOT_YOUR_COMMENT"));
			}
		} else {
			if (response == null) {
				throw new CommentException(bundle.getString("NOT_YOUR_COMMENT"));
			}
			/** 
			 * 비회원 댓글의 경우 비밀번호 일치 여부 확인 후 일치하는 경우 
			 * password_confirmed_c_댓글 번호 형태로 세션에 추가된다.  없으면 본인 글 검증 안된 상태
			 */
			if (request.getSession().getAttribute("password_confirmed_c_" + id) == null) {
				try {
					request.setAttribute("comment", comment);
					request.setAttribute("isComment", true);
					RequestDispatcher rd = request.getRequestDispatcher("/board/password.jsp");
					rd.forward(request, response);
				} catch (Exception e) { 
					e.printStackTrace();
				} 
			}
		}
	}
}