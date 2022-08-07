package models.file;

import java.util.ResourceBundle;

public class FileException extends RuntimeException {
	protected static ResourceBundle bundle =  ResourceBundle.getBundle("bundle.common");
	
	public FileException() {
		this(bundle.getString("FILE_PROCESS_ERROR"));
	}
	
	public FileException(String message) {
		super(message);
	}
}
