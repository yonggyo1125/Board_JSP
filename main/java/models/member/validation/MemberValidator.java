package models.member.validation;

import java.util.ResourceBundle;

import models.validation.Validator;
import models.member.validation.MemberValidationException;
import models.member.MemberDao;

/**
 * 
 * @author YONGGYO
 *
 */
public interface MemberValidator extends Validator {
	
	ResourceBundle bundle = ResourceBundle.getBundle("bundle.common");
	
	/**
	 * 이미 가입된 회원인지 체크 
	 * 
	 * @param memId 회원아이디
	 * @throws {MemberValidationException}
	 */
	default void checkDupMember(String memId) {
		if (MemberDao.getInstance().checkDuplicateId(memId)) {
			throw new MemberValidationException(bundle.getString("MEMBER_EXISTS_MEMID"));
		}
	}
	
	/**
	 * 비밀번호 복잡성 체크 
	 * @param {String} password
	 * @throws {MemberValidationException}
	 */
	default void checkPassword(String password) {
		/** 비밀번호는 8자리 이상 입력 */
		if (password == null || password.length() < 8) {
			throw new MemberValidationException(bundle.getString("MEMBER_PASSWORD_LENGTH"));
		}
	}
	
	/**
	 *  휴대전화번호 체크
	 * 
	 * @param {String} mobile
	 * @throws {MemberValidationException}
	 */
	default void checkMobileNum(String mobile) {
		mobile = mobile.replaceAll("\\D", "");
		
		String pattern = "01[016789]\\d{3,4}\\d{4}";
		if (!mobile.matches(pattern)) {
			throw new MemberValidationException(bundle.getString("WRONG_CELLPHONE_PATTERN"));
		}
	}
}
