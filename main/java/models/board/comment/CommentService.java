package models.board.comment;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;
import models.board.comment.CommentDto;
import models.member.MemberDto;
import models.board.comment.CommentDao;

import org.mindrot.bcrypt.BCrypt;

public class CommentService {
	public CommentDto comment(HttpServletRequest request) {
		
		String mode = request.getParameter("mode");
		if (mode == null || mode.isBlank()) mode = "register";
		
		 /** 유효성 검사 S */
		CommentValidator validator = new CommentValidator();
		validator.check(request, mode);
		/** 유효성 검사 E */
		
		CommentDao commentDao = CommentDao.getInstance();
		CommentDto comment = new CommentDto();
		
		if (mode != null && mode.equals("update")) {
			int id =  Integer.parseInt(request.getParameter("id").trim());
			comment.setId(id);
			String guestPw = request.getParameter("guestPw");
			if ( guestPw != null && !guestPw.isBlank()) {
				String hash = BCrypt.hashpw(guestPw.trim(), BCrypt.gensalt(12));
				comment.setGuestPw(hash);
			}
		} else {
			comment.setBoardDataId(Integer.parseInt(request.getParameter("boardDataId")));
			
			if (isLogin(request)) {
				MemberDto member = getMember(request);
				comment.setMemNo(member.getMemNo());
			} else {
				String hash = BCrypt.hashpw(request.getParameter("guestPw").trim(), BCrypt.gensalt(12));
				comment.setGuestPw(hash);
				comment.setMemNo(0);
			}
		}
		comment.setPoster(request.getParameter("poster"));
		comment.setContent(request.getParameter("content"));
		
		
		if (mode != null && mode.equals("update")) {
			boolean result = commentDao.update(comment);
			if (!result) {
				throw new CommentException("FAIL_TO_UPDATE_COMMENT");
			}
			comment = commentDao.get(Integer.parseInt(request.getParameter("id").trim()));
		} else {
			boolean result = commentDao.register(comment);
			if (!result) {
				throw new CommentException("FAIL_TO_REGISTER_COMMENT");
			}
		}
		
		return comment;
	}
}
