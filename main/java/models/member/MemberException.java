package models.member;

import java.util.ResourceBundle;

public class MemberException extends RuntimeException {
	
	protected static ResourceBundle bundle;
	
	static {
		bundle = ResourceBundle.getBundle("bundle.common");
	}
	
	public MemberException() {}
	public MemberException(String message) {
		super(message);
	}
}
