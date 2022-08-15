package models.member;

import java.time.LocalDateTime;

public class MemberDto {
	
	private int memNo; // 회원번호 
	private String memId; // 아이디
	private String memPw; // 비밀번호
	private String memNm; // 회원명 
	private String email; // 이메일 
	private String mobile; // 휴대전화
	private String memType; // 회원 유형 (admin - 관리자, member - 일반회원)
	private LocalDateTime regDt; // 가입일
	private LocalDateTime modDt; // 정보 수정일
	
	public MemberDto() {}
	
	public MemberDto(int memNo, String memId, String memPw, String memNm, String email, String mobile,
			LocalDateTime regDt, LocalDateTime modDt) {
		
		this.memNo = memNo;
		this.memId = memId;
		this.memPw = memPw;
		this.memNm = memNm;
		this.email = email;
		this.mobile = mobile;
		this.regDt = regDt;
		this.modDt = modDt;
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
	
	public String getMemPw() {
		return memPw;
	}
	
	public void setMemPw(String memPw) {
		this.memPw = memPw;
	}
	
	public String getMemNm() {
		return memNm;
	}
	
	public void setMemNm(String memNm) {
		this.memNm = memNm;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getMemType() {
		return memType;
	}

	public void setMemType(String memType) {
		
		if (memType == null || memType.isBlank()) {
			memType = "member"; // 기본값은 일반회원
		}
		
		this.memType = memType;
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
		return "MemberDto [memNo=" + memNo + ", memId=" + memId + ", memPw=" + memPw + ", memNm=" + memNm + ", email="
				+ email + ", mobile=" + mobile + ",memType=" + memType +  ", regDt=" + regDt + ", modDt=" + modDt + "]";
	}
}