package models.validation;

/**
 * 유효성 검사 실패시 발생 
 * 
 * @author YONGGYO
 *
 */
public class ValidationException extends RuntimeException {
	
	public ValidationException(String message) {
		super(message);
	}
}
