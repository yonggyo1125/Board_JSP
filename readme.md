# 게시글 작성 

#### src/main/sql/boardData.sql 

```sql
CREATE TABLE `boardData` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '게시글 번호',
  `boardId` VARCHAR(45) NULL COMMENT '게시판 아아디',
  `gid` VARCHAR(45) NULL COMMENT '그룹 ID',
  `memNo` INT NULL DEFAULT 0 COMMENT '회원 번호',
  `poster` VARCHAR(30) NULL COMMENT '작성자명',
  `subject` VARCHAR(255) NULL COMMENT '글 제목',
  `content` LONGTEXT NULL COMMENT '게시글 내용',
  `regDt` DATETIME NULL DEFAULT NOW() COMMENT '등록일시',
  `modDt` DATETIME NULL COMMENT '수정일시',
  PRIMARY KEY (`id`),
  INDEX `recentPost` (`regDt` DESC) INVISIBLE,
  INDEX `memNo` (`memNo` ASC) VISIBLE);
```

#### src/main/java/models/board/BoardDto.java 

```java 
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
```

#### src/main/java/models/board/BoardListDto.java

```java
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
```

#### src/main/java/models/board/BoardMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="BoardMapper">
	<resultMap id="boardMap" type="models.board.BoardDto">
		<result property="id" column="id" />
		<result property="boardId" column="boardId" />
		<result property="gid" column="gid" />
		<result property="memNo" column="memNo" />
		<result property="poster" column="poster" />
		<result property="subject" column="subject" />
		<result property="content" column="content" />
		<result property="regDt" column="regDt" />
		<result property="modDt" column="modDt" />
	</resultMap>

	<!--  게시글 조회  -->
	<select id="get" parameterType="models.board.BoardDto" resultMap="boardMap">
		SELECT b.*, m.memId, m.memNm FROM boardData b 
					LEFT JOIN member m ON b.memNo = m.memNo 
			WHERE b.id=#{id};
	</select>

	<!-- 게시글 목록  -->
	<select id="gets" parameterType="models.board.BoardListDto" resultMap="boardMap">
		SELECT b.*, m.memId, m.memNm FROM boardData b 
					LEFT JOIN member m ON b.memNo = m.memNo 
			ORDER BY b.regDt DESC LIMIT #{offset}, #{limit};
	</select>
	
	<!--  게시글 추가 -->
	<insert id="register"  parameterType="models.board.BoardDto" 
		useGeneratedKeys="true"
		keyProperty="memNo">
			INSERT INTO boardData (boardId, gid, memNo, poster, subject, content, regDt) 
				VALUES (#{boardId}, #{gid}, #{memNo}, #{poster}, #{subject}, #{content}, #{regDt});
	</insert>
	
	<!--  게시글 수정 -->
	<update id="update" parameterType="models.board.BoardDto">
		UPDATE boardData 
				SET 
					poster = #{poster},
					subject = #{subject},
					content = #{content}, 
					modDt = NOW()
			WHERE id = #{id};
	</update>
	
	<!--  게시글 삭제  -->
	<delete id="delete" parameterType="models.board.BoardDto">
		DELETE FROM boardData WHERE id=#{id};
	</delete>
</mapper>
```


* * * 
# 게시글 수정

* * *