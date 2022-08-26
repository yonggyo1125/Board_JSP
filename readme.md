# 댓글 기능 구현 

#### src/main/webapp/WEB-INF/tags/layouts/main.tag

- 사용중인 게시판을 메인 메뉴 항목에 노출

```jsp
<%@ tag description="메인 레이아웃" pageEncoding="UTF-8" %>
<%@ tag import="java.util.List" %>
<%@ tag import="models.admin.board.BoardAdminDao, models.admin.board.BoardAdminDto" %>

... 생략 

<%@ attribute name="bodyClass" type="java.lang.String" %>
<%
	BoardAdminDao dao = BoardAdminDao.getInstance();
	List<BoardAdminDto> boards = dao.gets();
%>
<c:set var="boards" value="<%=boards%>" />
<fmt:setBundle basename="bundle.common" />
<layout:common title="${title}" bodyClass="${bodyClass}">
	
	... 생략 
	
	<jsp:attribute name="main_menu">
	<nav>
		<div class='layout_width'>
		<c:if test="${ !empty boards}">
			<c:forEach var="board" items="${boards}">
				<c:if test="${board.isUse == 1}">
					<a href="<c:url value="/board/list?boardId=${board.boardId}" />">${board.boardNm}</a>
				</c:if>
			</c:forEach>	
		</c:if> 
		</div>
	</nav>
	
	... 생략 
	
</layout:common> 
```

#### src/main/webapp/board/view.jsp

```jsp

... 생략 

<%@ taglib prefix="board" tagdir="/WEB-INF/tags/board" %>
<fmt:setBundle basename="bundle.board" />
<layout:main title="${title}" bodyClass="board-view">
	
	... 생략
	
	<c:if test="${boardInfo.useComment == 1 }">
		<!--  댓글 영역 -->
		<board:comment id="${board.id}"/>
	</c:if>
</layout:main>
```

#### src/main/sql/commentData.sql

```sql
CREATE TABLE `commentData` (
   `id` int NOT NULL AUTO_INCREMENT,
   `boardDataId` int DEFAULT NULL COMMENT '게시글 번호',
   `memNo` int DEFAULT NULL COMMENT '회원번호',
   `poster` varchar(45) DEFAULT NULL COMMENT '작성자명',
   `guestPw` varchar(65) DEFAULT NULL COMMENT '비회원 비밀번호',
   `content` text COMMENT '댓글 내용',
   `regDt` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
   `modDt` datetime DEFAULT NULL COMMENT '수정일시',
   PRIMARY KEY (`id`),
   KEY `memNo` (`memNo`),
   KEY `regDt` (`regDt`),
   KEY `fk_boardData_id` (`boardDataId`),
   CONSTRAINT `fk_boardData_id` FOREIGN KEY (`boardDataId`) REFERENCES `boarddata` (`id`)
 );
```

#### src/main/webapp/WEB-INF/tags/board/comment.tag 

```jsp
<%@ tag description="댓글 작성 및 목록 출력"  pageEncoding="UTF-8" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %> <!-- 게시글 번호  -->
<form id="frmComment" name="frmComment" method="post" action="<c:url value="/board/comment" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="id" value="${id}" /> 
	<div class="ctop_box">
		작성자 <input type="text" name="poster" value="${member.memNm}">
		<c:if test="${ empty member }">
		/ 비밀번호 <input type="password" name="guestPw">
		</c:if>
	</div>
	<!--// top_box -->
	<div class='comment_box'>
		<textarea name="content" placeholder="댓글일 입력하세요..."></textarea>
		<button type="submit" id="comment_submit">댓글작성</button>
	</div>
</form>
```

#### src/main/java/models/board/comment/CommentDto.java

```java
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
```

#### src/main/java/models/board/comment/CommentMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="CommentMapper">
	<resultMap id="commentMap" type="models.board.comment.CommentDto">
		<result property="id" column="id" />
		<result property="boardDataId" column="boardDataId" />
		<result property="memNo" column="memNo" />
		<result property="poster" column="poster" />
		<result property="content" column="content" />
		<result property="regDt" column="regDt" />
		<result property="modDt" column="modDt" />
	</resultMap>
	
	<!-- 댓글 조회  -->
	<select id="get" parameterType="models.board.comment.CommentDto" resultMap="commentMap">
		SELECT c.*, m.memNm, m.memId FROM commentData c 
						LEFT JOIN member m ON c.memNo = m.memNo WHERE c.id = #{id};
	</select>
	
	<!--  댓글 목록 조회 -->
	<select id="gets" parameterType="models.board.comment.CommentDto" resultMap="commentMap">
		SELECT c.*, m.memNm, m.memId FROM commentData c 
			LEFT JOIN member m ON c.memNo = m.memNo WHERE c.boardDataId = #{boardDataId} ORDER BY c.regDt;
	</select>
	
	<!-- 댓글 추가  -->
	<insert id="register" parameterType="models.board.comment.CommentDto" 
			useGeneratedKeys="true" keyProperty="id">
		INSERT INTO commentData (boardDataId, memNo, poster, guestPw, content)
			VALUES (#{boardDataId}, #{memNo}, #{poster}, #{guestPw}, #{content});
	</insert>
	
	<!-- 댓글 수정 -->
	<update id="update" parameterType="models.board.comment.CommentDto">
		UPDATE commentData
			SET 
				poster=#{poster},
				guestPw=#{guestPw},
				content=#{content},
				modDt=NOW()
			WHERE id=#{id};
	</update>
	
	<!-- 댓글 삭제  -->
	<delete id="delete" parameterType="models.board.comment.CommentDto">
		DELETE FROM commentData FROM id=#{id};
	</delete>
</mapper>
```

#### src/main/java/mybatis/config/mybatis-config.xml  and mybatis-dev-config.xml 

```

... 생략

<configuration>
	... 생략 
	
	<mappers>
	... 생략 
	
	<mapper resource="models/board/comment/CommentMapper.xml" />
  </mappers>
</configuration>
```

#### src/main/java/models/board/comment/CommentDao.java

```java
package models.board.comment;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;

public class CommentDao {
	
	/**
	 * 댓글 조회 
	 * 
	 * @param id
	 * @return
	 */
	public CommentDto get(int id) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		CommentDto param = new CommentDto();
		param.setId(id);
		
		CommentDto comment = sqlSession.selectOne("CommentMapper.get", param);
				
		sqlSession.close();
		
		return comment;
	}
	
	/**
	 * 게시글 별 댓글 목록 
	 * 
	 * @param boardDataId
	 * @return
	 */
	public List<CommentDto> gets(int boardDataId) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		CommentDto param = new CommentDto();
		param.setBoardDataId(boardDataId);
		List<CommentDto> rows = sqlSession.selectList("CommentMapper.gets", param);
		
		sqlSession.close();
		
		return rows;
	}
	
	/**
	 * 댓글 추가 
	 * 
	 * @param comment
	 * @return
	 */
	public boolean register(CommentDto comment) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.insert("CommentMapper.register", comment);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	 
	/**
	 * 댓글 수정 
	 * 
	 * @param comment
	 * @return
	 */
	public boolean update(CommentDto comment) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.update("CommentMapper.update", comment);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 댓글 삭제
	 * 
	 * @param id
	 * @return
	 */
	public boolean delete(int id) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		CommentDto param = new CommentDto();
		param.setId(id);
		
		int affectedRows = sqlSession.update("CommentMapper.delete", param);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
}
```

#### src/main/java/controllers/board/CommentController.java

```java
package controllers.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.CommentService;
import models.board.comment.CommentDto;

import static commons.Utils.*;

/**
 * 댓글 작성 
 * 
 * @author YONGGYO
 *
 */
@WebServlet("/board/comment")
public class CommentController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentService service = new CommentService();
			CommentDto comment = service.comment(req);
			
			String url = "../board/view?id=" + comment.getBoardDataId() + "#comment_" + comment.getId();
			go(resp, url, "parent");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
}
```

#### src/main/java/models/board/comment/CommentService.java

```java

```