package models.file;

public class FileNotFoundException extends FileException {
	
	public FileNotFoundException() {
		this(bundle.getString("FILE_NOT_FOUND"));
	}
	
	public FileNotFoundException(String message) {
		super(message);
	}
	
}
