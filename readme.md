# 게시글 설정(관리자) 기능 구현

## 회원 기능 수정 
- 게시판을 관리할 수 있는 관리자 회원과 일반회원을 구분할 수 있도록 처리 \
- memType이 admin이면 관리자

```
ALTER TABLE member
ADD COLUMN memType ENUM('member', 'admin') NULL DEFAULT 'member' AFTER mobile;
```

#### src/main/java/models/member/MemberDto.java

```java

... 생략

public class MemberDto {
	

	private String mobile; // 휴대전화
	private String memType; // 회원 유형 (admin - 관리자, member - 일반회원)

	
	public String getMemType() {
		return memType;
	}

	public void setMemType(String memType) {
		
		if (memType == null || memType.isBlank()) {
			memType = "member"; // 기본값은 일반회원
		}
		
		this.memType = memType;
	}

	... 생략
	
	@Override
	public String toString() {
		return "MemberDto [memNo=" + memNo + ", memId=" + memId + ", memPw=" + memPw + ", memNm=" + memNm + ", email="
				+ email + ", mobile=" + mobile + ",memType=" + memType +  ", regDt=" + regDt + ", modDt=" + modDt + "]";
	}
}
```

#### src/main/java/models/member/MemberMapper.java

- memType 추가 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="MemberMapper">
	 <resultMap id="memberMap" type="models.member.MemberDto">
	 
		... 생략
		
	 	<result property="memType" column="memType" />
	 	<result property="regDt" column="regDt" />
	 	<result property="modDt" column="modDt" />
	 </resultMap>
	 
	 ... 생략
	 
</mapper>
```

- 관리자로 설정할 회원을 데이터 베이스에서 직접 memType을 admin으로 변경해 줍니다.

#### src/main/java/filters/wrappers/AccessRequestWrapper.java 

```java

... 생략 

public class AccessRequestWrapper extends HttpServletRequestWrapper {
	public AccessRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	public AccessRequestWrapper(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super(request);
		
		... 생략 
		
		HttpSession session = request.getSession();
		MemberDto member = (session.getAttribute("member") == null)?null:(MemberDto)session.getAttribute("member");
		
		... 생략 
		
		/** 비회원 전용 URL 체크 E */
		
		/** 관리자 전용 URL 체크 S */
		if (URI.indexOf("/admin") != -1 && (member == null || ! member.getMemType().equals("admin"))) {
			request.setAttribute("errorMessage", common.getString("ADMIN_ONLY"));
			request.setAttribute("statusCode", 401);
			response.sendError(401);
		}
		/** 관리자 전용 URL 체크 E */
	}	
}
```

#### src/main/webapp/WEB-INF/classes/bundle/common_ko.properties

```

... 생략 

# 페이지 통제
MEMBER_ONLY=회원전용 페이지 입니다.
GUEST_ONLY=비회원 전용 페이지 입니다.
ADMIN_ONLY=관리자 전용 페이지 입니다.

... 생략 

```

#### src/main/webapp/WEB-INF/classes/bundle/common_en.properties

```
... 생략

# 페이지 통제
MEMBER_ONLY=Member Only
GUEST_ONLY=Guest Only
ADMIN_ONLY=Admin Only

... 생략 

```

