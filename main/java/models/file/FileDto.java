package models.file;

import java.time.LocalDateTime;

public class FileDto {
	
	private int id; // 파일 등록번호
	private String gid; // 파일 그룹 ID
	private String fileName; // 파일명
	private String contentType; // 파일 유형
	private long fileSize; // 파일 사이즈
	private int isDone; // 파일 업로드 완료 여부 1 - 완료
	private LocalDateTime regDt; // 파일 등록일
	
	public FileDto() {}

	public FileDto(int id, String gid, String fileName, String contentType, long fileSize, byte isDone,
			LocalDateTime regDt) {
		this.id = id;
		this.gid = gid;
		this.fileName = fileName;
		this.contentType = contentType;
		this.fileSize = fileSize;
		this.isDone = isDone;
		this.regDt = regDt;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getGid() {
		return gid;
	}



	public void setGid(String gid) {
		this.gid = gid;
	}



	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public int getIsDone() {
		return isDone;
	}
	
	public void setIsDone(int isDone) {
		this.isDone = isDone;
	}
	
	public LocalDateTime getRegDt() {
		return regDt;
	}
	
	public void setRegDt(LocalDateTime regDt) {
		this.regDt = regDt;
	}
	
	@Override
	public String toString() {
		return "FileDto [id=" + id + ", gid=" + gid + ", fileName=" + fileName + ", contentType=" + contentType + ", fileSize="
				+ fileSize + ", isDone=" + isDone + ", regDt=" + regDt + "]";
	}
}
