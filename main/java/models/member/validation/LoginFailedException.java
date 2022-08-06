package models.member.validation;

import models.member.MemberException;

/**
 * 로그인 실패(비밀번호 불일치, 기타 사유 실패)시 발생 
 * 
 * @author YONGGYO
 *
 */
public class LoginFailedException extends MemberException {
	
	public LoginFailedException() {
		this(bundle.getString("MEMBER_LOGIN_FAILED"));
	}
	
	public LoginFailedException(String message) {
		super(message);
	}
}
