package models.board.comment;

import java.time.LocalDateTime;

public class CommentDto {
	
	private int id; // 댓글 등록 번호 
	private int boardDataId; // 게시글 번호
	private int memNo; // 회원번호
	private String memNm; // 회원명
	private String memId; // 회원 ID
	private String poster; // 작성자명
	private String guestPw; // 비회원 비밀번호
	private String content; // 댓글 내용
	private LocalDateTime regDt; // 등록일시
	private LocalDateTime modDt; // 수정일시
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getBoardDataId() {
		return boardDataId;
	}
	
	public void setBoardDataId(int boardDataId) {
		this.boardDataId = boardDataId;
	}
	
	public int getMemNo() {
		return memNo;
	}
	
	public void setMemNo(int memNo) {
		this.memNo = memNo;
	}
	
	public String getMemNm() {
		return memNm;
	}
	
	public void setMemNm(String memNm) {
		this.memNm = memNm;
	}
	
	public String getMemId() {
		return memId;
	}
	
	public void setMemId(String memId) {
		this.memId = memId;
	}
	
	public String getPoster() {
		return poster;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public String getGuestPw() {
		return guestPw;
	}
	
	public void setGuestPw(String guestPw) {
		this.guestPw = guestPw;
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
		return "CommentDto [id=" + id + ", boardDataId=" + boardDataId + ", memNo=" + memNo + ", memNm=" + memNm
				+ ", memId=" + memId + ", poster=" + poster + ", guestPw=" + guestPw + ", content=" + content
				+ ", regDt=" + regDt + ", modDt=" + modDt + "]";
	}
}