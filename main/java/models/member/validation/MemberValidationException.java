package models.member.validation;

import models.validation.ValidationException;

public class MemberValidationException extends ValidationException {
	public MemberValidationException(String message) {
		super(message);
	}
}