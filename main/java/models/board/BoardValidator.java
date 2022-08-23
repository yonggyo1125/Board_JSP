package models.board;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.BadRequestException;

import static commons.Utils.*;
import models.validation.Validator;
import models.board.BoardDao;
import models.member.MemberDto;

/**
 * 게시글 작성, 수정, 삭제시 유효성 검사 
 * 
 * @author YONGGYO
 *
 */
public class BoardValidator implements Validator {
	
	private static BoardValidator instance = new BoardValidator();
	
	ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");

	private BoardValidator() {}
	/**
	 * 유효성 검사 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {String} mode : register(등록), update(수정), delete(삭제)
	 */
	public void validate(HttpServletRequest request, String mode) {
		Map<String, String> checkFields = new HashMap<>();
		
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
			
			if (request.getParameter("guestPw") != null) { // 비회원 비밀번호가 있는 경우는 체크 
				checkFields.put("guestPw", bundle.getString("REQUIRED_GUEST_PW"));
			}	
			checkFields.put("content", bundle.getString("REQUIRED_CONTENT"));
		} else if (mode.equals("delete")) {
			checkFields.put("id", bundle.getString("REQUIRED_ID"));
		}
		
		// 필수 데이터 검증
		requiredCheck(request, checkFields);
		
		/** 게시글 수정 또는 삭제인 경우는 본인 게시글인지 체크 S */ 
		if (mode.equals("update") || mode.equals("delete")) {
			permissionCheck(request);
		}
		/** 게시글 수정 또는 삭제인 경우는 본인 게시글인지 체크 E */ 
	}
	
	/**
	 * 게시글 접근 권한 체크 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {int} id
	 */
	public void permissionCheck(HttpServletRequest request) {
		permissionCheck(request, null);
	}
	
	public void permissionCheck(HttpServletRequest request, HttpServletResponse response) {
		
		String _id = request.getParameter("id").trim();
		int id;
		try {
			id = Integer.parseInt(_id);
		} catch (Exception e) {
			throw new BadRequestException();
		}
		
		BoardDto board = BoardDao.getInstance().get(id);
		if (board == null) {
			throw new BoardException(bundle.getString("NOT_EXISTS_BOARD"));
		}
		// 관리자는 무조건 권한 있음 
		if (isAdmin(request)) {
			return;
		}
		
		int memNo = board.getMemNo();
		MemberDto member = getMember(request);
		if (memNo > 0) {
			if (member == null ||  member.getMemNo() != memNo) {
				throw new BoardException(bundle.getString("NOT_YOUR_POST"));
			}
		} else {
			/** 
			 * 비회원 게시글의 경우 비밀번호 일치 여부 확인 후 일치하는 경우 
			 * password_confirmed_게시글 번호 형태로 세션에 추가된다.  없으면 본인 글 검증 안된 상태
			 */
			if (request.getSession().getAttribute("password_confirmed_" + id) == null) {
				if (response != null) {
					
					/** 
					 * 비회원 게시글의 경우 비밀번호 검증 여부 체크 하고 
					 * 미 검증시 비밀번호 확인 페이지로 이동  
					 */
					if (board.getMemNo() == 0 && request.getSession().getAttribute("password_confirmed_" + id) == null) {
						try {
							request.setAttribute("board", board);
							RequestDispatcher rd = request.getRequestDispatcher("/board/password.jsp");
							rd.forward(request, response);
						} catch (Exception e) { 
							e.printStackTrace();
						} 
					}
				} else {
					throw new BoardException(bundle.getString("NOT_YOUR_POST"));
				}
			}
		}
	}
	
	public static BoardValidator getInstance() {
		if (instance == null) {
			instance = new BoardValidator();
		}
		
		return instance;
	}
}
