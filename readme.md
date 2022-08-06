## 마이바티스 연동
#### mybatis-config.xml : 운영중(production) 설정 파일
- mybatis-dev-config.xml : 개발중(production) 설정 파일 

- 마이바티스에서 LocalDateTime를 인식하기 위해서 하기 항목 추가
	- [mvnrepository]에서 [MyBatis TypeHandlers JSR310](https://mvnrepository.com/artifact/org.mybatis/mybatis-typehandlers-jsr310/1.0.2) jar 파일 다운받아 /WEB-INF/lib 에 추가할 것 

```xml
<typeHandlers>
	<typeHandler handler="org.apache.ibatis.type.LocalDateTypeHandler" />
 /typeHandlers>
```

- SQL 수행 및 수행 결과 매핑을 위한 Mapper 설정

```
<mappers>
	<mapper resource="models/member/MemberMapper.xml" />
</mappers>
```

- 마이바티스 SQL 실행 로그 및 자세한 오류 확인을 위해 log4j 연동

- [mvnrepository]에서 [Apache Log4j](https://mvnrepository.com/artifact/log4j/log4j/1.2.17) jar 파일 다운받아 /WEB-INF/lib 에 추가할 것 

```xml
<settings>
	<setting name="logImpl" value="LOG4J" /> <!-- log4j log setting  -->
</settings>
```

#### /WEB-INF/classes/log4j.properties
- log4j 설정

```
log4j.rootLogger=DEBUG, stdout

log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%5p [%t] - %m%n
```

#### /WEB-INF/classes/application.properties

- /WEB-INF/classes/application.properties 의 environment 설정에 따라 운영중 (mybatis-config.xml), 개발중(mybatis-dev-config.xml)으로 각기 다른 설정파일로 설정 정보 가져와 객체 생성

```
# 운영환경 설정 - production : 운영 중, development : 개발 중
environment=development
```

#### src/main/java/mybatis/Connection.java

- 데이터베이스 접속 객체 생성 

```java 
package mybatis;

... 생략

public class Connection {
	/** 데이터베이스 접속 객체 */
	private static SqlSessionFactory sqlSessionFactory;
	
	static {
		// 접속정보를 명시하고 있는 XML의 경로 읽기
		try {
			ResourceBundle config = ResourceBundle.getBundle("application");
			
			String environment = config.getString("environment");
			if (environment == null || environment.isBlank()) {
				environment = "development";
			}
			
			// mybatis-config.xml 파일의 경로 */
			String configPath = null;
			if (environment.equals("production")) { 
				configPath = "mybatis/config/mybatis-config.xml";
			} else { 
				configPath = "mybatis/config/mybatis-dev-config.xml";
			}

			Reader reader = Resources.getResourceAsReader(configPath);
			
			if (sqlSessionFactory == null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 데이터베이스 접속 세션 반환 */
	public static SqlSession getSqlSession() {
		return sqlSessionFactory.openSession();
	}
}
```

#### src/main/java/models/member/MemberMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="MemberMapper">
	 <resultMap id="memberMap" type="models.member.MemberDto">
	 	<result property="memNo" column="memNo" />
	 	<result property="memId" column="memId" />
	 	<result property="memPw" column="memPw" />
	 	<result property="memNm" column="memNm" />
	 	<result property="email" column="email" />
	 	<result property="mobile" column="mobile" />
	 	<result property="regDt" column="regDt" />
	 	<result property="modDt" column="modDt" />
	 </resultMap>
	 
	 <!-- 회원 조회 -->
	 <select id="member" parameterType="models.member.MemberDto" resultMap="memberMap">
	 	SELECT * FROM member WHERE memId=#{memId};
	 </select>
	 
	 <!--  회원 조회 count -->
	 <select id="memberCount" parameterType="models.member.MemberDto" resultType="int">
	 	SELECT COUNT(*) FROM member WHERE memId=#{memId};
	 </select>
	 
	 <!-- 회원목록 조회 -->
	 <select id="members" parameterType="models.member.MembersDto" resultMap="memberMap">
	 	SELECT * FROM member ORDER BY regDt DESC LIMIT #{offset}, #{limit};
	 </select>
	 
	 <!-- 회원 등록 -->
	 <insert id="register" 
	 				parameterType="models.member.MemberDto" 
	 				useGeneratedKeys="true"
	 				keyProperty="memNo">
	 	INSERT INTO member (memId, memNm, memPw, email, mobile) VALUES (#{memId}, #{memNm}, #{memPw}, #{email}, #{mobile});
	 </insert>
	 
	 <!--  회원 수정  -->
	 <update id="update" parameterType="models.member.MemberDto">
	 	UPDATE member 
	 		SET
	 			memNm=#{memNm},
	 			email=#{email},
	 			mobile=#{mobile}
			WHERE memId=#{memId}
	 </update>
	 
	 <!--  비밀번호 변경  -->
	 <update id="changePassword" parameterType="models.member.MemberDto">
	 	UPDATE member
	 		SET 
	 			memPw=#{memPw}
	 	WHERE memId=#{memId}
	 </update>
	 
	 <!--  회원 삭제  -->
	 <delete id="delete" parameterType="models.member.MemberDto">
	 	DELETE FROM member WHERE memId=#{memId}
	 </delete>
</mapper>
```

#### src/main/java/models/member/MemberDto.java

```java 
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
```


#### src/main/java/models/member/MemberDao.java

```java
package models.member;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;
import models.member.MemberDto;
import models.member.MembersDto;

public class MemberDao {
	
	private static MemberDao instance = new MemberDao();
	
	/**'
	 * 회원 등록 
	 * 
	 * @param {MemberDto} member
	 * @return {MemberDto}  추가 성공시 회원번호(memNo)가 포함된 객체 반환 
	 * 					실패시 null 반환
	 */
	public MemberDto register(MemberDto member) {
		try {
			SqlSession sqlSession = Connection.getSqlSession();
			int affectedRows = sqlSession.insert("MemberMapper.register", member);
			if (affectedRows <= 0) {
				throw new RuntimeException();
			}
			
			sqlSession.commit();
			sqlSession.close();
			
			return member;
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 회원정보 조회 
	 * @param {String} memId 아이디
	 * @return {MembeDto}  조회 실패시 null 반환
	 */
	public MemberDto get(String memId) {
		MemberDto member = null;
		try {
			SqlSession sqlSession = Connection.getSqlSession();
			MemberDto param = new MemberDto();
			param.setMemId(memId);
			member = sqlSession.selectOne("MemberMapper.member", param);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		return member;
	}
	
	/**
	 * 회원목록 조회 
	 * 
	 * @param {int} page 페이지번호, 기본값 1 
	 * @param {int} limit 1페이지당 레코드 수, 기본값 30 
	 * 
	 * @return {List<MemberDto>}
	 */
	public List<MemberDto> gets(int page, int limit) {
		page = (page == 0) ? page : 1;
		limit = (limit == 0) ? limit : 30;
		int offset = (page - 1) * limit;
		MembersDto param = new MembersDto(offset, limit);
		SqlSession sqlSession = Connection.getSqlSession();
		List<MemberDto> members = sqlSession.selectList("MemberMapper.members", param);
		
		return members;
	}
	
	public List<MemberDto> gets(int page) {
		return gets(page, 30);
	}
	
	public List<MemberDto> gets() {
		return gets(1);
	}
	
	/**
	 * 중복 아이이디 체크 
	 * 
	 * @param {String} memId
	 * @return {boolean}
	 */
	public boolean checkDuplicateId(String memId) {
		SqlSession sqlSession = Connection.getSqlSession();
		MemberDto param = new MemberDto();
		param.setMemId(memId);
		int count = sqlSession.selectOne("MemberMapper.memberCount", param);
		System.out.println(count);
		return count > 0;
	}
	
	public static MemberDao getInstance() {
		if (instance == null) {
			instance = new MemberDao();
		}
		
		return instance;
	}
}
```



## 회원 가입 기능 구현

#### src/main/java/controllers/JoinController.java : 회원가입

```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	try {
		MemberJoinService service = new MemberJoinService();
		service.join(req);
			
		// 가입 성공한 경우 로그인 페이지로 이동 
		go(resp, "../member/login", "parent");
	} catch (RuntimeException e) {
		alertError(resp, e);
	}
}
```

- 가입 처리는 서비스 클래스(MemberJoinService)에서 데이터 검증과 DB처리 진행

```
MemberJoinService service = new MemberJoinService();
service.join(req);
```

#### src/main/java/models/member/MemberJoinService.java

```java
package models.member;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.mindrot.bcrypt.BCrypt;

import models.member.validation.MemberValidationException;
import models.member.validation.MemberValidator;

/**
 * 회원 가입 처리 
 * 
 * @author YONGGYO
 */
public class MemberJoinService implements MemberValidator {
	
	public void join(HttpServletRequest request) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.common");
		
		/** 필수 항목 유효성 검사 */
		HashMap<String, String> checkFields = new HashMap<>();
		checkFields.put("memId", bundle.getString("MEMBER_REQUIRED_MEMID"));
		checkFields.put("memPw", bundle.getString("MEMBER_REQUIRED_MEMPW"));
		checkFields.put("memPwRe", bundle.getString("MEMBER_REQUIRED_MEMPWRE"));
		checkFields.put("memNm", bundle.getString("MEMBER_REQUIRED_MEMNM"));
		checkFields.put("isAgree", bundle.getString("MEMBER_REQUIRED_AGREE"));
		requiredCheck(request, checkFields);
		
		String memId = request.getParameter("memId");
		String memNm = request.getParameter("memNm");
		String memPw = request.getParameter("memPw");
		String memPwRe = request.getParameter("memPwRe");
		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
		
		/** 아이디 자리수 체크 */
		if (memId.length() < 6) {
			throw new MemberValidationException(bundle.getString("MEMBER_MEMID_LENGTH"));
		}
		
		/** 이미 가입된 회원인지 체크 */
		checkDupMember(memId);
			
		/** 비밀번호 복잡성 체크 */
		checkPassword(memPw);
		
		if (!memPw.equals(memPwRe)) {
			throw new MemberValidationException(bundle.getString("MEMBER_MEMPW_NOT_SAME"));
		}
		
		
		/** 휴대전화번호 체크 */
		mobile = mobile.replaceAll("\\D", "");
		checkMobileNum(mobile);
		
		/** 비밀번호 Bcrypt 방식으로 해시 처리 */
		String hash = BCrypt.hashpw(memPw, BCrypt.gensalt(10));
		
		/** 회원 가입 처리 S */
		MemberDto member = new MemberDto();
		member.setMemId(memId);
		member.setMemNm(memNm);
		member.setMemPw(hash);
		member.setEmail(email);
		member.setMobile(mobile);
		System.out.println(member);
		MemberDao memberDao = MemberDao.getInstance();
		MemberDto newMember = memberDao.register(member);
		if (newMember == null) {
			throw new MemberException(bundle.getString("MEMBER_JOIN_FAILED"));
		}
	
		/** 회원 가입 처리 E */
	}
}
```


### PC

![image1](https://github.com/yonggyo1125/curriculum300H/blob/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/6%EC%9D%BC%EC%B0%A8(3h)%20-%20%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4/images/project/image1.png)


### Mobile 

![image2](https://github.com/yonggyo1125/curriculum300H/blob/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/6%EC%9D%BC%EC%B0%A8(3h)%20-%20%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%B2%A0%EC%9D%B4%EC%8A%A4/images/project/image2.png)
