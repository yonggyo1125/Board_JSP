package models.board;

import java.time.LocalDateTime;

public class BoardDto {
	private int id; // 게시글 등록 번호
	private String boardId; // 게시판 ID
	private String gid; // 그룹 ID
	private int memNo; // 회원번호
	private String memId; // 회원 ID 
	private String memNm; // 회원명
	private String poster; // 작성자
	private String subject; // 게시글 제목 
	private String content; // 게시글 내용 
	private LocalDateTime regDt; // 등록일시
	private LocalDateTime modDt; // 수정일시
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getBoardId() {
		return boardId;
	}
	
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	
	public String getGid() {
		return gid;
	}
	
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public int getMemNo() {
		return memNo;
	}
	
	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}
	
	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public String getMemNm() {
		return memNm;
	}

	public void setMemNm(String memNm) {
		this.memNm = memNm;
	}

	public String getPoster() {
		return poster;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public LocalDateTime getRegDt() {
		return regDt;
	}
	
	public void setRegDt(LocalDateTime regDt) {
		this.regDt = regDt;
	}
	
	public LocalDateTime getModDt() {
		return modDt;
	}
	
	public void setModDt(LocalDateTime modDt) {
		this.modDt = modDt;
	}

	@Override
	public String toString() {
		return "BoardDto [id=" + id + ", boardId=" + boardId + ", gid=" + gid + ", memNo=" + memNo + ", memId=" + memId
				+ ", memNm=" + memNm + ", poster=" + poster + ", subject=" + subject + ", content=" + content
				+ ", regDt=" + regDt + ", modDt=" + modDt + "]";
	}
}
