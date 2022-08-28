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
<%@ tag import="java.util.List" %>
<%@ tag import="models.board.comment.CommentDao" %>
<%@ tag import="models.board.comment.CommentDto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %> <!-- 게시글 번호  -->
<fmt:setBundle basename="bundle.comment" />
<% jspContext.setAttribute("newLine", "\n"); %>
<%
/** 등록된 게시글 목록 조회 */
if (id > 0) {
	CommentDao commentDao = CommentDao.getInstance();
	List<CommentDto> comments = commentDao.gets(id);
	request.setAttribute("comments", comments);
} // endif 
%>

<script src="<c:url value="/static/js/board/comment.js" />"></script>
<form id="frmComment" name="frmComment" method="post" action="<c:url value="/board/comment" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="boardDataId" value="${id}" /> 
	<div class="ctop_box">
		<fmt:message key="POSTER" />
		<input type="text" name="poster" value="${member.memNm}">
		<c:if test="${ empty member }"> / 
		<fmt:message key="PASSWORD" />
		<input type="password" name="guestPw">
		</c:if>
	</div>
	<!--// top_box -->
	<div class='comment_box'>
		<textarea name="content" placeholder="<fmt:message key="COMMENT_GUIDE" />"></textarea>
		<button type="submit" id="comment_submit">
			<fmt:message key="WRITE_COMMENT" />
		</button>
	</div>
</form>
<c:if test="${ !empty comments }">
<ul class='comment_list'>
	<c:forEach var="comment" items="${comments}">
		<li class='comment' id="comment_${comment.id}">
			<div class='ctop'>
				<div class='left'>
					${comment.poster}(${ empty comment.memId ? "Guest" :  comment.memId })
				 / <util:formatDate value="${comment.regDt}" pattern="yyyy.MM.dd HH:mm" />
				 </div>
				 <div class="right">
				 	<c:if test="${comment.memNo == 0 || ( !empty member && comment.memNo ==  member.memNo) }">
				 		<a href="<c:url value="/board/comment?id=${comment.id}" />">[<fmt:message key="EDIT_COMMENT" />]</a>
				 		<a href="<c:url value="/board/comment/delete?id=${comment.id}" />" onclick="confirm('<fmt:message key="SURE_TO_DELETE" />');">[<fmt:message key="DELETE_COMMENT" />]</a>
					 </c:if>
				 </div>
			</div>
			<!--// ctop  -->
			<div class="comment_content">
			${fn:replace(comment.content, newLine, '<br>')}
			</div>
		</li>
		<!--// comment -->
	</c:forEach>
</ul>
<!--// comment_list -->
</c:if>
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
		DELETE FROM commentData WHERE id=#{id};
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

#### src/main/java/commons/Utils.java

```java

... 생략

public class Utils {
	
	... 생략
	
	public static void back(HttpServletResponse response) {
		back(response, "self");
	}
	
	/**
	 * 페이지 새로고침 
	 * 
	 * @param {HttpServletResponse} response
	 * @param {String} target : self, parent, top
	 */
	public static void reload(HttpServletResponse response, String target) {
		try {
			if (target == null || target.isBlank()) target = "self";
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.printf("<script>%s.location.reload();</script>", target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload(HttpServletResponse response) {
		reload(response, "self");
	}
	
	... 생략
	
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

import models.board.comment.CommentDeleteService;
import models.board.comment.CommentDto;

import static commons.Utils.*;

@WebServlet("/board/comment/delete")
public class CommentDeleteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentDeleteService service = new CommentDeleteService();
			CommentDto comment =  service.delete(req, resp);
			
			go(resp, "../../board/view?id=" + comment.getBoardDataId(), "parent");
		} catch (RuntimeException e) {
			alertError(resp, e, "history.back()");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
```

#### src/min/java/models/board/comment/CommentException.java

```java
package models.board.comment;

/**
 * 댓글 관련 예외 처리
 *
 */
public class CommentException extends RuntimeException {
	public CommentException(String message) {
		super(message);
	}
}
```

#### src/main/java/models/board/comment/CommentValidator.java

```java
package models.board.comment;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.BadRequestException;

import static commons.Utils.*;

import models.board.BoardException;
import models.member.MemberDto;
import models.validation.Validator;

/**
 * 댓글 관련 유효성 검사 
 * 
 * @author YONGGYO
 *
 */
public class CommentValidator implements Validator {
	
	ResourceBundle bundle = ResourceBundle.getBundle("bundle.comment");
	
	/**
	 * 유효성 검사 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {String} mode : register(등록), update(수정), delete(삭제)
	 */
	public void check(HttpServletRequest request, String mode) {
		Map<String, String> checkFields = new HashMap<>();

		if (mode.equals("register") || mode.equals("update")) { // 등록, 수정 
			
			/** 공통 필수 항목 체크 S */
			checkFields.put("poster", bundle.getString("REQUIRED_POSTER"));
			checkFields.put("content", bundle.getString("REQUIRED_CONTENT"));
			
			// 로그인 하지 않은 경우는 비회원 비밀번호 필수 
			if (request.getParameter("guestPw") != null) {
				checkFields.put("guestPw", bundle.getString("REQUIRED_GUEST_PW"));
			}
			/** 공통 필수 항목 E */
			
			if (mode.equals("register")) { // 댓글 등록일 때 필수 항목
				checkFields.put("boardDataId", bundle.getString("REQUIRED_BOARD_DATA_ID"));
			} else { // 댓글 수정일때 필수 항목
				checkFields.put("id", bundle.getString("REQUIRED_ID"));
			}	
		} else if (mode.equals("delete") || mode.equals("info")){ // 삭제 
			checkFields.put("id", bundle.getString("REQUIRED_ID"));
		}
		
		requiredCheck(request, checkFields);
		/** 공통 필수 항목 체크 S */
	}
	
	/**
	 * 댓글 수정, 삭제 권한 체크 
	 * 
	 * @param request
	 * @param response
	 */
	public void permissionCheck(HttpServletRequest request, HttpServletResponse response) {
		String _id = request.getParameter("id").trim();
		int id;
		try {
			id = Integer.parseInt(_id);
		} catch (Exception e) {
			throw new BadRequestException();
		}
		
		// 관리자는 무조건 권한 있음 
		if (isAdmin(request)) {
			return;
		}
		
		CommentDto comment = CommentDao.getInstance().get(id);
		if (comment == null) {
			throw new CommentException(bundle.getString("NOT_EXISTS_COMMENT"));
		}
		
		int memNo = comment.getMemNo();
		MemberDto member = getMember(request);
		if (memNo > 0) {
			if (member == null ||  member.getMemNo() != memNo) {
				throw new CommentException(bundle.getString("NOT_YOUR_COMMENT"));
			}
		} else {
			if (response == null) {
				throw new CommentException(bundle.getString("NOT_YOUR_COMMENT"));
			}
			/** 
			 * 비회원 댓글의 경우 비밀번호 일치 여부 확인 후 일치하는 경우 
			 * password_confirmed_c_댓글 번호 형태로 세션에 추가된다.  없으면 본인 글 검증 안된 상태
			 */
			if (request.getSession().getAttribute("password_confirmed_c_" + id) == null) {
				try {
					request.setAttribute("comment", comment);
					request.setAttribute("isComment", true);
					RequestDispatcher rd = request.getRequestDispatcher("/board/password.jsp");
					rd.forward(request, response);
				} catch (Exception e) { 
					e.printStackTrace();
				} 
			}
		}
	}
}
```

#### src/main/java/models/board/comment/CommentService.java

```java
package models.board.comment;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;
import models.board.comment.CommentDto;
import models.member.MemberDto;
import models.board.comment.CommentDao;

import org.mindrot.bcrypt.BCrypt;

public class CommentService {
	public CommentDto comment(HttpServletRequest request) {
		
		String mode = request.getParameter("mode");
		if (mode == null || mode.isBlank()) mode = "register";
		
		 /** 유효성 검사 S */
		CommentValidator validator = new CommentValidator();
		validator.check(request, mode);
		/** 유효성 검사 E */
		
		CommentDao commentDao = CommentDao.getInstance();
		CommentDto comment = new CommentDto();
		
		if (mode != null && mode.equals("update")) {
			int id =  Integer.parseInt(request.getParameter("id").trim());
			comment.setId(id);
			String guestPw = request.getParameter("guestPw");
			if ( guestPw != null && !guestPw.isBlank()) {
				String hash = BCrypt.hashpw(guestPw.trim(), BCrypt.gensalt(12));
				comment.setGuestPw(hash);
			}
		} else {
			comment.setBoardDataId(Integer.parseInt(request.getParameter("boardDataId")));
			
			if (isLogin(request)) {
				MemberDto member = getMember(request);
				comment.setMemNo(member.getMemNo());
			} else {
				String hash = BCrypt.hashpw(request.getParameter("guestPw").trim(), BCrypt.gensalt(12));
				comment.setGuestPw(hash);
				comment.setMemNo(0);
			}
		}
		comment.setPoster(request.getParameter("poster"));
		comment.setContent(request.getParameter("content"));
		
		
		if (mode != null && mode.equals("update")) {
			boolean result = commentDao.update(comment);
			if (!result) {
				throw new CommentException("FAIL_TO_UPDATE_COMMENT");
			}
			comment = commentDao.get(Integer.parseInt(request.getParameter("id").trim()));
		} else {
			boolean result = commentDao.register(comment);
			if (!result) {
				throw new CommentException("FAIL_TO_REGISTER_COMMENT");
			}
		}
		
		return comment;
	}
}
```

#### src/main/java/models/board/comment/CommentInfoService.java

```java 
package models.board.comment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 게시글 조회 
 *
 */
public class CommentInfoService {
	public CommentDto get(HttpServletRequest request, HttpServletResponse response) {
		
		/** 유효성 검사 S */
		CommentValidator validator = new CommentValidator();
		validator.check(request, "info");
		/** 유효성 검사 E */
		
		/** 댓글 권한 체크 */
		validator.permissionCheck(request, response);
		
		/** 댓글 조회 */
		int id = Integer.parseInt(request.getParameter("id").trim());
		CommentDto comment = CommentDao.getInstance().get(id);
		
		return comment;
	}
}
```

#### src/main/java/models/board/comment/CommentPasswordCheckService.java

```java 
package models.board.comment;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.bcrypt.BCrypt;

import commons.BadRequestException;

public class CommentPasswordCheckService {
	
	public void check(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 필수 항목 체크 S */
		CommentValidator validator = new CommentValidator();
		Map<String, String> checkFields = new HashMap<>();
		checkFields.put("id", bundle.getString("REQUIRED_ID"));
		checkFields.put("password", bundle.getString("REQUIRED_GUEST_PW"));
		validator.requiredCheck(request, checkFields);
		/** 필수 항목 체크 E */
		
		CommentDao dao = CommentDao.getInstance();
		int id = Integer.parseInt(request.getParameter("id").trim());
		CommentDto comment = dao.get(id);
		if (comment == null) {
			throw new BadRequestException(bundle.getString("NOT_EXISTS_COMMENT"));
		}
		
		String hash = comment.getGuestPw();
		
		boolean result = BCrypt.checkpw(request.getParameter("password").trim(), hash);
		if (!result) {
			throw new BadRequestException(bundle.getString("INCORECT_PASSWORD"));
		}
		
		request.getSession().setAttribute("password_confirmed_c_" + id, true);
	}
}
```

#### src/main/java/models/controllers/board/CommentPasswordController.java

```java 
package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.comment.CommentPasswordCheckService;

import static commons.Utils.*;

@WebServlet("/board/comment/password")
public class CommentPasswordController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentPasswordCheckService service = new CommentPasswordCheckService();
			service.check(req);
			
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
			alert(resp, bundle.getString("PASSWORD_CONFIRM"), "parent.location.reload()");
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}	
}
```

#### src/main/java/models/controllers/board/comment/CommentDeleteController.java

```java 
package controllers.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.comment.CommentDeleteService;
import models.board.comment.CommentDto;

import static commons.Utils.*;

@WebServlet("/board/comment/delete")
public class CommentDeleteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			CommentDeleteService service = new CommentDeleteService();
			CommentDto comment =  service.delete(req, resp);
			
			go(resp, "../../board/view?id=" + comment.getBoardDataId(), "parent");
		} catch (RuntimeException e) {
			alertError(resp, e, "history.back()");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
```

#### src/main/java/models/models/board/comment/CommentDeleteService.java

```java
package models.board.comment;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 댓글 삭제 
 * 
 *
 */
public class CommentDeleteService {
	public CommentDto delete(HttpServletRequest request, HttpServletResponse response) {
		
		 /** 유효성 검사 S */
		CommentValidator validator = new CommentValidator();
		validator.check(request, "delete");
		/** 유효성 검사 E */
		
		/** 댓글 권한 체크 */
		validator.permissionCheck(request, response);
		
		/** 댓글 조회 */
		CommentDao commentDao = CommentDao.getInstance();
		int id = Integer.parseInt(request.getParameter("id").trim());
		CommentDto comment = commentDao.get(id);
		if (request.getSession().getAttribute("password_confirmed_c_" + id) != null) {
			boolean result = commentDao.delete(id);
			
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.comment");
			
			if (!result) {
				throw new CommentException(bundle.getString("FAIL_TO_DELETE_COMMENT"));
			}
		}
		return comment;
	}
}
```

#### src/main/webapp/board/comment_form.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.comment" />
<fmt:message var="title" key="UPDATE_COMMENT"  />
<layout:main title="${title}" bodyClass="comment-form">
<h1 class='mtitle'>${title}</h1>
<form id="frmComment" class="form_box" name="frmComment" method="post" action="<c:url value="/board/comment" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="mode" value="update">
	<input type="hidden" name="id" value="${comment.id}"> 
	<dl>
		<dt><fmt:message key="POSTER" /></dt>
		<dd><input type="text" name="poster" value="${comment.poster}"></dd>
	</dl>
	
	<c:if test="${ comment.memNo == 0 }">
	<dl> 
		<dt><fmt:message key="PASSWORD" /></dt>
		<dd><input type="password" name="guestPw"></dd>
	</dl>
	</c:if>
	<textarea name="content" placeholder="<fmt:message key="COMMENT_GUIDE" />">${comment.content}</textarea>
	<div class='btn_grp mb10'>
	<button type="submit" id="comment_submit" class='black'>
		<fmt:message key="UPDATE_COMMENT" />
	</button>
	</div>
</form>
</layout:main>
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

#### src/main/webapp/WEB-INF/classes/bundle/comment_ko.properties

```
POSTER=작성자
PASSWORD=비밀번호
WRITE_COMMENT=댓글작성
UPDATE_COMMENT=댓글수정
EDIT_COMMENT=수정
DELETE_COMMENT=삭제
COMMENT_GUIDE=댓글을 입력하세요...

REQUIRED_POSTER=작성자를 입력하세요.
REQUIRED_CONTENT=댓글을 입력하세요.
REQUIRED_GUEST_PW=비밀번호를 입력하세요.
REQUIRED_BOARD_DATA_ID=잘못된 요청입니다.
REQUIRED_ID=잘못된 요청입니다.

FAIL_TO_REGISTER_COMMENT=댓글 등록에 실패하였습니다.
FAIL_TO_UPDATE_COMMENT=댓글 수정에 실패하였습니다.
FAIL_TO_DELETE_COMMENT=댓글 삭제에 실패하였습니다.
SURE_TO_DELETE=정말 댓글을 삭제하시겠습니까?
NOT_EXISTS_COMMENT=등록된 댓글이 아닙니다.
NOT_YOUR_COMMENT=본인 댓글이 아닙니다.
```

#### src/main/webapp/WEB-INF/classes/bundle/comment_en.properties

```
POSTER=poster
PASSWORD=Password
WRITE_COMMENT=Write a comment
EDIT_COMMENT=EDIT
DELETE_COMMENT=DELETE
COMMENT_GUIDE=Please write a comment...

REQUIRED_POSTER=Please input the poster.
REQUIRED_CONTENT=Please input the content.
REQUIRED_GUEST_PW=Please input the password.
REQUIRED_BOARD_DATA_ID=Wrong request.
REQUIRED_ID=Wrong request.

FAIL_TO_REGISTER_COMMENT=Fail to register a comment.
FAIL_TO_UPDATE_COMMENT=Fail to update a comment.
FAIL_TO_DELETE_COMMENT=Fail to delete a comment.
SURE_TO_DELETE=Are you sure to delete this comment?
NOT_EXISTS_COMMENT=The comment doen't exist.
NOT_YOUR_COMMENT=Not your comment.
```

* * * 
# 완성화면

![image1](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/14%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EB%8C%93%EA%B8%80%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image1.png)

![image2](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/14%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EB%8C%93%EA%B8%80%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image2.png)

![image3](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/14%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EB%8C%93%EA%B8%80%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image3.png)

