package models.member.validation;

import models.validation.Validator;
import models.member.validation.MemberValidationException;

/**
 * 
 * @author YONGGYO
 *
 */
public interface MemberValidator extends Validator {
	
	/**
	 * 이미 가입된 회원인지 체크 
	 * 
	 * @param memId 회원아이디
	 * @throws {MemberValidationException}
	 */
	default void checkDupMember(String memId) {
		
	}
	
	/**
	 * 비밀번호 복잡성 체크 
	 * @param {String} password
	 * @throws {MemberValidationException}
	 */
	default void checkPassword(String password) {
		
	}
	
	/**
	 *  휴대전화번호 체크
	 * 
	 * @param {String} mobile
	 * @throws {MemberValidationException}
	 */
	default void checkMobileNum(String mobile) {
		
	}
}
