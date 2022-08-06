package models.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 로그아웃 처리
 * 
 * @author YONGGYO
 *
 */
public class LogoutService {
	
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
	}
}
