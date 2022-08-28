package models.board.comment;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.bcrypt.BCrypt;

import commons.BadRequestException;

public class CommentPasswordCheckService {
	
	public void check(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 필수 항목 체크 S */
		CommentValidator validator = new CommentValidator();
		Map<String, String> checkFields = new HashMap<>();
		checkFields.put("id", bundle.getString("REQUIRED_ID"));
		checkFields.put("password", bundle.getString("REQUIRED_GUEST_PW"));
		validator.requiredCheck(request, checkFields);
		/** 필수 항목 체크 E */
		
		CommentDao dao = CommentDao.getInstance();
		int id = Integer.parseInt(request.getParameter("id").trim());
		CommentDto comment = dao.get(id);
		if (comment == null) {
			throw new BadRequestException(bundle.getString("NOT_EXISTS_COMMENT"));
		}
		
		String hash = comment.getGuestPw();
		
		boolean result = BCrypt.checkpw(request.getParameter("password").trim(), hash);
		if (!result) {
			throw new BadRequestException(bundle.getString("INCORECT_PASSWORD"));
		}
		
		request.getSession().setAttribute("password_confirmed_c_" + id, true);
	}
}
