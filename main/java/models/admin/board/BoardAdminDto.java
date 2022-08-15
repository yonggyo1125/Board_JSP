package models.admin.board;

import java.time.LocalDateTime;

/**
 * 게시판 설정 DTO 
 * 
 * @author YONGGYO
 *
 */
public class BoardAdminDto {
	
	private String boardId; // 게시판 아이디 
	private String boardNm; // 게시판 이름
	private int isUse; // 게시판 사용 여부
	private int noOfRows; // 1페이지당 노출 게시글 수 
	private int useComment; // 댓글 사용 여부
	private LocalDateTime regDt; // 설정 등록일
	private LocalDateTime modDt; // 설정 수정일
	
	public String getBoardId() {
		return boardId;
	}
	
	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	
	public String getBoardNm() {
		return boardNm;
	}
	
	public void setBoardNm(String boardNm) {
		this.boardNm = boardNm;
	}
	
	public int getIsUse() {
		return isUse;
	}
	
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	
	public int getNoOfRows() {
		return noOfRows;
	}
	
	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}
	
	public int getUseComment() {
		return useComment;
	}
	
	public void setUseComment(int useComment) {
		this.useComment = useComment;
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
		return String.format(
				"BoardAdminDto [boardId=%s, boardNm=%s, isUse=%s, noOfRows=%s, useComment=%s, regDt=%s, modDt=%s]",
				boardId, boardNm, isUse, noOfRows, useComment, regDt, modDt);
	}
}
