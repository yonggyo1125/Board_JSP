package models.board.comment;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 댓글 삭제 
 * 
 *
 */
public class CommentDeleteService {
	public CommentDto delete(HttpServletRequest request, HttpServletResponse response) {
		
		 /** 유효성 검사 S */
		CommentValidator validator = new CommentValidator();
		validator.check(request, "delete");
		/** 유효성 검사 E */
		
		/** 댓글 권한 체크 */
		validator.permissionCheck(request, response);
		
		/** 댓글 조회 */
		CommentDao commentDao = CommentDao.getInstance();
		int id = Integer.parseInt(request.getParameter("id").trim());
		CommentDto comment = commentDao.get(id);
		if (request.getSession().getAttribute("password_confirmed_c_" + id) != null) {
			boolean result = commentDao.delete(id);
			
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.comment");
			
			if (!result) {
				throw new CommentException(bundle.getString("FAIL_TO_DELETE_COMMENT"));
			}
		}
		return comment;
	}
}
