package models.board;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import commons.BadRequestException;
import org.mindrot.bcrypt.BCrypt;

public class PasswordCheckService {
	
	public void check(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 필수 항목 체크 S */
		BoardValidator validator = BoardValidator.getInstance();
		Map<String, String> checkFields = new HashMap<>();
		checkFields.put("id", bundle.getString("REQUIRED_ID"));
		checkFields.put("password", bundle.getString("REQUIRED_GUEST_PW"));
		validator.requiredCheck(request, checkFields);
		/** 필수 항목 체크 E */
		BoardDao dao = BoardDao.getInstance();
		int id = Integer.parseInt(request.getParameter("id").trim());
		BoardDto board = dao.get(id);
		if (board == null) {
			throw new BadRequestException(bundle.getString("POST_NOT_EXISTS"));
		}
		
		String hash = board.getGuestPw();
		boolean result = BCrypt.checkpw(request.getParameter("password").trim(), hash);
		if (!result) {
			throw new BadRequestException(bundle.getString("INCORECT_PASSWORD"));
		}
		
		request.getSession().setAttribute("password_confirmed_" + id, true);
	}
}
