package models.member;

/**
 * 회원목록 Mapper 시에 페이징 추가 
 * 
 * @author YONGGYO
 *
 */
public class MembersDto extends MemberDto {
	private int offset; 
	private int limit;
	
	public MembersDto() {}
	
	public MembersDto(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
	}

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

	@Override
	public String toString() {
		return "MembersDto [offset=" + offset + ", limit=" + limit + "]";
	}
}
