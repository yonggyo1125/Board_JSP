package models.member;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.mindrot.bcrypt.BCrypt;

import models.member.validation.MemberValidationException;
import models.member.validation.MemberValidator;

/**
 * 회원 가입 처리 
 * 
 * @author YONGGYO
 */
public class MemberJoinService implements MemberValidator {
	
	public void join(HttpServletRequest request) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.common");
		
		/** 필수 항목 유효성 검사 */
		HashMap<String, String> checkFields = new HashMap<>();
		checkFields.put("memId", bundle.getString("MEMBER_REQUIRED_MEMID"));
		checkFields.put("memPw", bundle.getString("MEMBER_REQUIRED_MEMPW"));
		checkFields.put("memPwRe", bundle.getString("MEMBER_REQUIRED_MEMPWRE"));
		checkFields.put("memNm", bundle.getString("MEMBER_REQUIRED_MEMNM"));
		checkFields.put("isAgree", bundle.getString("MEMBER_REQUIRED_AGREE"));
		requiredCheck(request, checkFields);
		
		String memId = request.getParameter("memId");
		String memNm = request.getParameter("memNm");
		String memPw = request.getParameter("memPw");
		String memPwRe = request.getParameter("memPwRe");
		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
		
		/** 아이디 자리수 체크 */
		if (memId.length() < 6) {
			throw new MemberValidationException(bundle.getString("MEMBER_MEMID_LENGTH"));
		}
		
		/** 이미 가입된 회원인지 체크 */
		checkDupMember(memId);
			
		/** 비밀번호 복잡성 체크 */
		checkPassword(memPw);
		
		if (!memPw.equals(memPwRe)) {
			throw new MemberValidationException(bundle.getString("MEMBER_MEMPW_NOT_SAME"));
		}
		
		
		/** 휴대전화번호 체크 */
		mobile = mobile.replaceAll("\\D", "");
		checkMobileNum(mobile);
		
		/** 비밀번호 Bcrypt 방식으로 해시 처리 */
		String hash = BCrypt.hashpw(memPw, BCrypt.gensalt(10));
		
		/** 회원 가입 처리 S */
		MemberDto member = new MemberDto();
		member.setMemId(memId);
		member.setMemNm(memNm);
		member.setMemPw(hash);
		member.setEmail(email);
		member.setMobile(mobile);
		System.out.println(member);
		MemberDao memberDao = MemberDao.getInstance();
		MemberDto newMember = memberDao.register(member);
		if (newMember == null) {
			throw new MemberException(bundle.getString("MEMBER_JOIN_FAILED"));
		}
	
		/** 회원 가입 처리 E */
	}
}