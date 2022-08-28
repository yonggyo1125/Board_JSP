package models.board.comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 게시글 조회 
 *
 */
public class CommentInfoService {
	public CommentDto get(HttpServletRequest request, HttpServletResponse response) {
		
		/** 유효성 검사 S */
		CommentValidator validator = new CommentValidator();
		validator.check(request, "info");
		/** 유효성 검사 E */
		
		/** 댓글 권한 체크 */
		validator.permissionCheck(request, response);
		
		/** 댓글 조회 */
		int id = Integer.parseInt(request.getParameter("id").trim());
		CommentDto comment = CommentDao.getInstance().get(id);
		
		return comment;
	}
}
