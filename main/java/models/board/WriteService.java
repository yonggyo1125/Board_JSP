package models.board;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;
import models.member.MemberDto;

import org.mindrot.bcrypt.BCrypt;

/**
 * 게시글 작성 
 * 
 * @author YONGGYO
 *
 */
public class WriteService {
	
	public BoardDto write(HttpServletRequest request) {
		
		/** 유효성 검사 S */
		BoardValidator boardValidator = BoardValidator.getInstance();
		boardValidator.validate(request, "register");
		/** 유효성 검사 E */
		
		/** 게시글 등록 S */
		BoardDao dao = BoardDao.getInstance();
		BoardDto board = new BoardDto();
		MemberDto member = getMember(request);
		board.setBoardId(request.getParameter("boardId"));
		board.setGid(request.getParameter("gid"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		if (member == null) { // 비회원인 경우 
			board.setMemNo(0);
			String hash = BCrypt.hashpw(request.getParameter("guestPw"), BCrypt.gensalt(12));
			board.setGuestPw(hash);
		} else {
			board.setMemNo(member.getMemNo());
		}
		
		boolean result = dao.register(board);
		if (!result) {
			return null;
		}
		
		return board;
	}
}
