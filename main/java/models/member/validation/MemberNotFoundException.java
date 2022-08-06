package models.member.validation;

import models.member.MemberException;

/**
 * 회원을 찾지 못하였을 경우 발생 
 * 
 * @author YONGGYO
 *
 */
public class MemberNotFoundException extends MemberException {
	
	public MemberNotFoundException() {
		this(bundle.getString("MEMBER_NOT_FOUND"));
	}
	
	public MemberNotFoundException(String message) {
		super(message);
	}
}
