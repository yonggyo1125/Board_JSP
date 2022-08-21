package models.board;

public class BoardListDto extends BoardDto {
	private int offset; // 레코드 시작번호
	private int limit; // 레코드 갯수
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
