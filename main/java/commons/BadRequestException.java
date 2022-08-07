package commons;

import java.util.ResourceBundle;

public class BadRequestException extends RuntimeException {
	
	private static ResourceBundle bundle =  ResourceBundle.getBundle("bundle.common");

	public BadRequestException() {
		this(bundle.getString("BAD_REQUEST"));
	}
	
	public BadRequestException(String message) {
		super(message);
	}
}
