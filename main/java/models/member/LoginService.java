package models.member;

import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.bcrypt.BCrypt;

import models.member.validation.MemberValidator;
import models.member.validation.LoginFailedException;
import models.member.validation.MemberNotFoundException;

/**
 * 로그인 처리 
 * 
 * @author YONGGYO
 *
 */
public class LoginService implements MemberValidator {
	
	public void login(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.common");
		
		/** 필수 입력 항목 체크 S */
		HashMap<String, String> checkFields = new HashMap<>();
		checkFields.put("memId", bundle.getString("MEMBER_REQUIRED_MEMID"));
		checkFields.put("memPw", bundle.getString("MEMBER_REQUIRED_MEMPW"));
		requiredCheck(request, checkFields);
		/** 필수 입력 항목 체크 E */
		
		/** 아이디 및 비밀번호 체크 S */
		String memId = request.getParameter("memId");
		String memPw = request.getParameter("memPw");
		
		MemberDao dao = MemberDao.getInstance();
		
		MemberDto member = dao.get(memId);
		if (member == null) {
			throw new MemberNotFoundException();
		}
		
		// 비밀번호 체크 - 해시 일치 여부 체크 
		if (!BCrypt.checkpw(memPw, member.getMemPw())) {
			throw new LoginFailedException(bundle.getString("MEMBER_PASSWORD_INCORRECT"));
		}
		
		/** 아이디 및 비밀번호 체크 E */
		
		/** 회원 정보 세션 처리 S */
		HttpSession session = request.getSession();
		member.setMemPw("");
		session.setAttribute("member", member);
		/** 회원 정보 세션 처리 E */
	}
}
