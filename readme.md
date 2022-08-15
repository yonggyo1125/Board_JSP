# 게시글 설정(관리자) 기능 구현

## 회원 기능 수정 
- 게시판을 관리할 수 있는 관리자 회원과 일반회원을 구분할 수 있도록 처리
- memType이 admin이면 관리자

```
ALTER TABLE member
ADD COLUMN memType ENUM('member', 'admin') NULL DEFAULT 'member' AFTER mobile;
```

#### src/main/java/models/member/MemberDto.java

```java

... 생략

public class MemberDto {
	
	... 생략

	private String mobile; // 휴대전화
	private String memType; // 회원 유형 (admin - 관리자, member - 일반회원)

	... 생략 
	
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

# 관리자
ADMIN_MENU=관리자 메뉴
```

#### src/main/webapp/WEB-INF/classes/bundle/common_en.properties

```
... 생략

# 페이지 통제
MEMBER_ONLY=Member Only
GUEST_ONLY=Guest Only
ADMIN_ONLY=Admin Only

... 생략 

# 관리자
ADMIN_MENU=ADMIN MENU
```

#### src/main/webapp/WEB-INF/classes/bundle/admin_ko.properties

```
# 공통
BACK_TO_BOARD=게시판으로
REGISTER=등록하기
RESET=다시입력
USE=사용
NOT_USE=미사용
REG_DT=등록일시
MOD_DT=수정일시
MODIFY=수정하기
DELETE=삭제하기

# 게시판
CREATE_BOARD=게시판 생성
UPDATE_BOARD=게시판 수정
BOARD_LIST=게시판 목록
BOARD_ID=게시판 아이디
BOARD_NM=게시판 이름
BOARD_ISUSE=사용여부
BOARD_NO_OF_ROWS=1페이당 노출 갯수
BOARD_USE_COMMENT=댓글 사용여부

REQUIRED_BOARD_ID=게시판 아이디를 입력하세요.
REQUIRED_BOARD_NM=게시판 이름을 입력하세요.
REQUIRED_DELETE_BOARD_ID=삭제할 게시판 아이디를 선택하세요.
DUPLICATE_BOARD_ID=이미 등록된 게시판 아이디 입니다.

CREATE_BOARD_ERROR=게시판을 생성하는데 실패하였습니다.
UPDATE_BOARD_ERROR=게시판을 수정하는데 실패하였습니다.
DELETE_SELECTED_BOARD=선택된 게시판 삭제하기
SURE_TO_DELETE=정말 삭제하시겠습니까?
```

#### src/main/webapp/WEB-INF/classes/bundle/admin_en.properties

```
# 공통
BACK_TO_BOARD=BACK TO BOARD
REGISTER=REGISTER
RESET=RESET
USE=Use
NOT_USE=Not Use
REG_DT=RegDt
MOD_DT=ModDt
MODIFY=MODIFY
DELETE=DELETE

# 게시판
CREATE_BOARD=CREATE BOARD
UPDATE_BOARD=UPDATE_BOARD
BOARD_LIST=Board List
BOARD_ID=Board ID
BOARD_NM=Board Name
BOARD_ISUSE=is Use?
BOARD_NO_OF_ROWS=No Of Rows
BOARD_USE_COMMENT=Use Comment

REQUIRED_BOARD_ID=Please Input Board ID.
REQUIRED_BOARD_NM=Please Input Board Name.
REQUIRED_DELETE_BOARD_ID=Please Select Board IDs to delete.
DUPLICATE_BOARD_ID=Already Registered Board ID.

CREATE_BOARD_ERROR=Failed to Create the Board.
UPDATE_BOARD_ERROR=Failed to Update the Board.
DELETE_SELECTED_BOARD=Delete Selected Board
SURE_TO_DELETE=Are you sure to delete?
```


#### src/main/webapp/WEB-INF/tags/layouts/main.tag

```jsp

... 생략

<layout:common title="${title}" bodyClass="${bodyClass}">
	<jsp:attribute name="header">
		<header>
		<section class='top_menu'>
			<div class="layout_width">
			
				... 생략
				
				<c:if test="${ ! empty member }">
					<a href="<c:url value="/mypage" />"><fmt:message key="MYPAGE" /></a>
					<a href="<c:url value="/member/logout" />"><fmt:message key="MEMBER_LOGOUT" /></a>
					<c:if test="${ member.memType == 'admin' }">
						<a href="<c:url value="/admin" />"><fmt:message key="ADMIN_MENU" /></a>
					</c:if>
				</c:if>
			</div>
		</section>
		
		... 생략 
		
	</jsp:attribute>
	
	... 생략 
	
</layout:common> 
```

## 관리자 페이지 구성하기

- 관리자페이지는 다음과 같이 구성한다.
	- 게시판 생성
	- 게시판 설정 목록
	- 게시판 설정 수정

#### src/main/webapp/WEB-INF/tags/layouts/admin.tag

```jsp
<%@ tag description="관리자페이지 레이아웃" pageEncoding="UTF-8" %>
<%@ tag body-content="scriptless" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="bodyClass" type="java.lang.String" %>
<%
	request.setAttribute("addCss", "admin/style");
	request.setAttribute("addJs", "admin/common");
%>
<fmt:setBundle basename="bundle.common" />
<fmt:message var="title" key="ADMIN_MENU" />
<fmt:setBundle basename="bundle.admin" />
<layout:common title="${title}" bodyClass="admin">
	<jsp:attribute name="main_menu">
	<nav>
		<div>
			<a href="<c:url value="/" />"><fmt:message key="BACK_TO_BOARD" /></a>
			<a href="<c:url value="/admin/board" />"><fmt:message key="CREATE_BOARD" /></a>
			<a href="<c:url value="/admin/boards" />"><fmt:message key="BOARD_LIST" /></a>
		</div>
	</nav>
	</jsp:attribute>
	<jsp:body>
		<main>
			<jsp:doBody />
		</main>
	</jsp:body>
</layout:common>
```

#### src/main/webapp/static/css/admin/style.css 

```css
/** 공통 */
h2 { border-bottom: 2px solid #212121; padding-bottom: 10px; margin-bottom: 20px; }
main { max-width: 1200px; width: 100%; padding: 20px; }

dl { display: flex; width: 100%; margin: 10px 0; }
dt { width: 150px; }
dd { width: calc(100% - 150px); }

.btn_grp { margin-top: 30px; }

.inline_block { display: block; margin: 0 !important; }
.inline_block  * { display: inline-block !important; }
.inline_block  button { padding-left: 20px; padding-right: 20px;  }

/** 테이블 공통 S */
.table_rows { width: 100%; border-spacing: 0; padding: 0; margin-bottom: 10px; }
.table_rows thead th {  background-color: orange;  padding: 10px; }
.table_rows tbody td { border-bottom: 1px solid #212121; padding: 10px; }
/** 테이블 공통 E */
```

#### src/main/webapp/static/js/admin/common.js

```javascript
window.addEventListener("DOMContentLoaded", function() {
	/** 체크 박스 전체 선택 처리 S */
	const checkAllEls = document.getElementsByClassName("check_all");
	for (let el of checkAllEls) {
		el.addEventListener("click", function() {
			const targetName = this.dataset.targetName;
			const els = document.getElementsByName(targetName);
			for (const _el of els) {
				_el.checked = this.checked;
			}
		});
	}
	/** 체크 박스 전체 선택 처리 E */
}); 
```

#### src/main/java/controllers/admin/IndexController.java

```java
package controllers.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin")
public class IndexController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestDispatcher rd = req.getRequestDispatcher("/admin/index.jsp");
		rd.forward(req, resp);
	}
}
```	

#### src/main/webapp/admin/index.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:admin>
	<h1><fmt:message key="ADMIN_MENU" /></h1>
</layout:admin>
```


#### src/main/java/controllers/admin/board/IndexController.java

```java 
package controllers.admin.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.admin.board.BoardRegisterService;
import models.admin.board.BoardUpdateService;
import models.admin.board.BoardAdminDto;
import models.admin.board.BoardInfoService;
import static commons.Utils.*;

@WebServlet("/admin/board")
public class IndexController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BoardInfoService service = new BoardInfoService();
		BoardAdminDto board = service.get(req);
		req.setAttribute("board", board);
		
		RequestDispatcher rd = req.getRequestDispatcher("/admin/board/index.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String mode = req.getParameter("mode");
			if (mode != null && mode.equals("modify")) {
				BoardUpdateService service = new BoardUpdateService();
				service.update(req);
			} else {
				BoardRegisterService service = new BoardRegisterService();
				service.register(req);
			}
			
			// 게시판 설정 등록이 완료 되면 설정 목록으로 이동한다.
			go(resp, "boards", "parent");
		} catch (RuntimeException e) {
			alertError(resp, e);
		}
	}
}

```


#### src/main/webapp/admin/board/index.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.admin" />
<layout:admin>
	<h2>
		<c:if test="${ empty board }">
		<fmt:message key="CREATE_BOARD" />
		</c:if>
		<c:if test="${ ! empty board }">
		<fmt:message key="UPDATE_BOARD" />
		</c:if>
	</h2>
	<form id="frmRegist" name="frmRegist" method="post" action="<c:url value="/admin/board" />" target="ifrmProcess" autocomplete="off">
		<jsp:include page="_form.jsp" />
		<c:if test="${ ! empty board }">
			<input type="hidden" name="mode" value="modify">
		</c:if>
		<div class='btn_grp'>
			<button type="reset"><fmt:message key="RESET" /></button>
			<button type="submit">
				<c:if test="${ empty board }">
				<fmt:message key="REGISTER" />
				</c:if>
				<c:if test="${ ! empty board }">
				<fmt:message key="MODIFY" />
				</c:if>
			
			</button>
		</div>
	</form>
</layout:admin>
```

#### src/main/webapp/admin/board/_form.jsp

```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="bundle.admin" />
<dl>
	<dt><fmt:message key="BOARD_ID" /></dt>
	<dd>
		<c:if test="${ ! empty board }">
			${board.boardId}
			<input type="hidden" name="boardId" value="${board.boardId}">
		</c:if>
		<c:if test="${ empty board }">
		<input type="text" name="boardId">
		</c:if>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_NM" /></dt>
	<dd>
		<input type="text" name="boardNm" value="${board.boardNm}"/>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_ISUSE" /></dt>
	<dd>
		<input type="radio" name="isUse" id="isUse_1" value="1" ${ empty board.isUse ? "": " checked"}>
		<label for="isUse_1"><fmt:message key="USE" /></label>
		<input type="radio" name="isUse" id="isUse_0" value="0" ${ empty board.isUse ? " checked": ""}>
		<label for="isUse_0"><fmt:message key="NOT_USE" /></label>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_NO_OF_ROWS" /></dt>
	<dd>
		<input type="number" name="noOfRows" value="${empty board.noOfRows ? 20 : board.noOfRows }"/>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_USE_COMMENT" /></dt>
	<dd>
		<input type="radio" name="useComment" id="useComment_1" value="1" ${ (empty board.useComment ||  board.useComment == 0) ? "": " checked"}>
		<label for="useComment_1"><fmt:message key="USE" /></label>
		<input type="radio" name="useComment" id="useComment_0" value="0" ${ (empty board.useComment ||  board.useComment == 0) ? " checked": ""}>
		<label for="useComment_0"><fmt:message key="NOT_USE" /></label>
	</dd>
</dl>
```

#### src/main/java/models/admin/board/BoardAdminDto.java

```java
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
```

#### src/main/sql/boardAdmin.sql

```sql
CREATE TABLE `boardAdmin` (
  `boardId` VARCHAR(45) NOT NULL COMMENT '게시판 아이디 ',
  `boardNm` VARCHAR(45) NULL COMMENT '게시판 이름',
  `isUse` TINYINT(1) NULL DEFAULT 0 COMMENT '게시판 사용 여부  1 - 사용, 0 - 미사용',
  `noOfRows` INT NULL DEFAULT 20 COMMENT '1페이지당 노출 게시글 수 ',
  `useComment` TINYINT(1) NULL DEFAULT 0 COMMENT '댓글 사용여부  1 - 사용, 0 - 미사용',
  `regDt` DATETIME NULL DEFAULT NOW() COMMENT '설정 등록일',
  `modDt` DATETIME NULL COMMENT '설정 수정일',
  PRIMARY KEY (`boardId`),
  UNIQUE INDEX `boardId_UNIQUE` (`boardId` ASC) VISIBLE);
```

#### src/main/java/models/admin/board/BoardAdminMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="BoardAdminMapper">
	<resultMap id="boardAdminMap" type="models.admin.board.BoardAdminDto">
		<result property="boardId" column="boardId" />
		<result property="boardNm" column="boardNm" />
		<result property="isUse" column="isUse" />
		<result property="noOfRows" column="noOfRows" />
		<result property="useComment" column="useComment" />
		<result property="regDt" column="regDt" />
		<result property="modDt" column="modDt" />
	</resultMap>
	
	<!-- 게시판 설정 조회 -->
	<select id="board" parameterType="models.admin.board.BoardAdminDto" resultMap="boardAdminMap">
		SELECT * FROM boardAdmin WHERE boardId=#{boardId};
	</select>
	
	<!--  게시판 설정 목록 조회  -->
	<select id="boards" parameterType="models.admin.board.BoardAdminDto" resultMap="boardAdminMap">
		SELECT * FROM boardAdmin ORDER BY regDt DESC
	</select>
	
	<!--  게시판 아이디 중복 체크 -->
	<select id="duplicateCheck" parameterType="models.admin.board.BoardAdminDto" resultType="int">
		SELECT COUNT(*) FROM boardAdmin WHERE boardId=#{boardId};
	</select>
	
	<!-- 게시판 설정 추가 -->
	<insert id="register" parameterType="models.admin.board.BoardAdminDto">
		INSERT INTO boardAdmin (boardId, boardNm, isUse, noOfRows, useComment) 
			VALUES (#{boardId}, #{boardNm}, #{isUse}, #{noOfRows}, #{useComment});
	</insert>
	
	<!-- 게시판 설정 수정 -->
	<update id="update" parameterType="models.admin.board.BoardAdminDto">
		UPDATE boardAdmin
			SET 
				boardNm=#{boardNm},
				isUse=#{isUse},
				noOfRows=#{noOfRows},
				useComment=#{useComment},
				modDt=NOW()
			WHERE boardId=#{boardId};
	</update>
	
	<!-- 게시판 설정 삭제 -->
	<delete id="delete" parameterType="models.admin.board.BoardAdminDto">
		DELETE FROM boardAdmin WHERE boardId=#{boardId};
	</delete>
</mapper>
```

#### src/main/java/mybatis/config/mybatis-config.xml | mybatis-dev-config.xml
```
... 생략 

<configuration>

	... 생략 
	
  <mappers>
	<mapper resource="models/member/MemberMapper.xml" />
	<mapper resource="models/file/FileMapper.xml" />
	<mapper resource="models/admin/board/BoardAdminMapper.xml" />
  </mappers>
</configuration>
```

#### src/main/java/models/admin/board/BoardAdminDao.java

```java
package models.admin.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;

public class BoardAdminDao {
	public static BoardAdminDao instance = new BoardAdminDao();
	
	private BoardAdminDao() {}
	
	/**
	 * 게시글 설정 등록 
	 * @param dto
	 * @return {boolean}
	 */
	public boolean register(BoardAdminDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.insert("BoardAdminMapper.register", dto);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 설정 수정
	 * 
	 * @param {BoardAdminDto} dto
	 * @return {boolean}
	 */
	public boolean update(BoardAdminDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.update("BoardAdminMapper.update", dto);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 설정 삭제
	 * 
	 * @param {String} boardId
	 * @return {boolean}
	 */
	public boolean delete(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardAdminDto param = new BoardAdminDto();
		param.setBoardId(boardId);
		
		int affectedRows = sqlSession.delete("BoardAdminMapper.delete", param);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 설정 목록
	 * 
	 * @return {List<BoardAdminDto>}
	 */
	public List<BoardAdminDto> gets() {
		SqlSession sqlSession = Connection.getSqlSession();
		List<BoardAdminDto> boards = sqlSession.selectList("BoardAdminMapper.boards");
		
		sqlSession.close();
		
		return boards;
		
	
	}
	
	/**
	 * 게시글 설정 조회
	 * 
	 * @param {String} boardId
	 * @return
	 */
	public BoardAdminDto get(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		BoardAdminDto param = new BoardAdminDto();
		param.setBoardId(boardId);
		BoardAdminDto board = sqlSession.selectOne("BoardAdminMapper.board", param);

		sqlSession.close();
		
		return board;
	}
	
	/**
	 * 중복 게시판 아이디 여부 체크 
	 * 
	 * @param {String} boardId
	 * @return {boolean}
	 */
	public boolean checkDuplicateBoardId(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardAdminDto param = new BoardAdminDto();
		param.setBoardId(boardId);
		
		int count = sqlSession.selectOne("BoardAdminMapper.duplicateCheck", param);
		sqlSession.close();
		
		return count > 0;
	}
	
	public static BoardAdminDao getInstance() {
		return instance;
	}
}
```

#### src/main/java/models/admin/board/BoardAdminValidator.java

```java
package models.admin.board;

import models.validation.Validator;

import java.util.ResourceBundle;

import models.validation.ValidationException;

public class BoardAdminValidator implements Validator {
	/**
	 * 중복 게시판 아이디 여부 체크 
	 * 
	 * @param {String} boardId
	 * @throws {ValidationException}
	 */
	public void checkDuplicateBoardId(String boardId) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.admin");
		BoardAdminDao dao = BoardAdminDao.getInstance();
		if (dao.checkDuplicateBoardId(boardId)) {
			throw new ValidationException(bundle.getString("DUPLICATE_BOARD_ID"));
		}
	}
}

```

#### src/main/java/models/admin/board/BoardAdminException.java

```java
package models.admin.board;

public class BoardAdminException extends RuntimeException {
	public BoardAdminException(String message) {
		super(message);
	}
}

```

#### src/main/java/models/admin/board/BoardRegisterService.java

```java
package models.admin.board;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminValidator;

/**
 * 게시판 설정 등록
 * 
 * @author YONGGYO
 *
 */
public class BoardRegisterService {
	
	public void register(HttpServletRequest request) {
		
		 ResourceBundle bundle = ResourceBundle.getBundle("bundle.admin");
		
		/** 필수 입력 항목 체크 S */
		BoardAdminValidator validator = new BoardAdminValidator();
		Map<String, String> checkFields = new HashMap<>();
		checkFields.put("boardId", bundle.getString("REQUIRED_BOARD_ID"));
		checkFields.put("boardNm", bundle.getString("REQUIRED_BOARD_NM"));
		validator.requiredCheck(request, checkFields);
		
		/** 필수 입력 항목 체크 E */
		
		String boardId = request.getParameter("boardId");
		String boardNm= request.getParameter("boardNm");
		String isUse = request.getParameter("isUse");
		String noOfRows = request.getParameter("noOfRows");
		String useComment = request.getParameter("useComment");
		
		isUse = (isUse == null || isUse.isBlank()) ? "0": isUse;
		noOfRows = (noOfRows == null || noOfRows.isBlank()) ? "20" : noOfRows;
		useComment = (useComment == null || noOfRows.isBlank()) ? "0" : useComment;
		
		/** 이미 등록된 게시판 아이디 여부 체크 */
		validator.checkDuplicateBoardId(boardId);
		
		/** 게시판 설정 등록 S */
		BoardAdminDto dto = new BoardAdminDto();
		dto.setBoardId(boardId);
		dto.setBoardNm(boardNm);
		dto.setIsUse(Integer.parseInt(isUse));
		dto.setNoOfRows(Integer.parseInt(noOfRows));
		dto.setUseComment(Integer.parseInt(useComment));
	
		BoardAdminDao dao = BoardAdminDao.getInstance();
		if (!dao.register(dto)) {
			new BoardAdminException(bundle.getString("CREATE_BOARD_ERROR"));
		}
		
		/** 게시판 설정 등록 E */
	}
}
```

* * * 
## 게시판 설정 목록 

#### src/main/java/models/admin/board/BoardsControllr.java

```java
package controllers.admin.board;

import static commons.Utils.*;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.admin.board.BoardAdminDto;
import models.admin.board.BoardListService;
import models.admin.board.BoardDeleteService;

@WebServlet("/admin/boards")
public class BoardsController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		/** 게시판 목록 S */
		BoardListService service = new BoardListService();
		List<BoardAdminDto> boards = service.gets();
		req.setAttribute("boards", boards);
		/** 게시판 목록 E */
		
		RequestDispatcher rd = req.getRequestDispatcher("/admin/board/list.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			BoardDeleteService service = new BoardDeleteService();
			service.delete(req);
			
			// 게시판 설정 삭제 완료 되면 설정 목록으로 이동한다.
			go(resp, "boards", "parent");
		} catch (RuntimeException e) {
			alertError(resp, e);
		}
	}
}

```

#### src/main/java/models/admin/board/BoardListService.java

```java
package models.admin.board;

import java.util.List;

public class BoardListService {
	
	public List<BoardAdminDto> gets() {
		return BoardAdminDao.getInstance().gets();
	}
}
```

#### src/main/webapp/WEB-INF/tags/utils/formatDate.tag

- LocalDateTime, LocalDate, LocalTime 등의 형식을 변경하기 위한 커스텀 태그 추가 

```jsp
<%@ tag body-content="empty" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag import="java.time.*" %>
<%@ tag import="java.time.format.*" %>
<%@ attribute name="pattern" type="java.lang.String" %>
<%@ attribute name="value" type="java.time.temporal.Temporal" required="true" %>
<%
	if (pattern == null || pattern.isBlank()) {
		pattern = "yyyy-MM-dd HH:mm:ss";
	}

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	if (value != null) { 
		out.print(formatter.format(value));
	}
%>
```

#### src/main/webapp/admin/board/list.jsp 

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<fmt:setBundle basename="bundle.admin" />
<layout:admin>
	<h2><fmt:message key="BOARD_LIST" /></h2>
	
	<form id="ifrmList" name="ifrmList" method="post" action="<c:url value='/admin/boards' />" target="ifrmProcess" autocomplete="off">
		<table class="table_rows">
			<thead>
				<tr>
					<th width='30'>
						<input type="checkbox" class="check_all" data-target-name="boardId" id='check_all'>
						<label for='check_all'></label>
					</th>
					<th width='150'><fmt:message key="BOARD_ID" /></th>
					<th width='200'><fmt:message key="BOARD_NM" /></th>
					<th width='100'><fmt:message key="BOARD_ISUSE" /></th>
					<th width='140'><fmt:message key="BOARD_NO_OF_ROWS" /></th>
					<th width='100'><fmt:message key="BOARD_USE_COMMENT" /></th>
					<th width='150'><fmt:message key="REG_DT" /></th>
					<th><fmt:message key="MOD_DT" /></th>
					<th width='150'></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="board" items="${boards}" varStatus="status">
				<tr>
					<td align='center'>
						<input type="checkbox" name="boardId" value="${board.boardId}" id="boardid_${status.index}">
						<label for='boardid_${status.index}'></label>
					</td>
					<td align='center'>
						<a href="<c:url value="/board/list?id=" />${board.boardId}" target="_blank">${board.boardId}</a>
					</td>
					<td align='center'>${board.boardNm}</td>
					<td align='center'>
						<c:if test="${board.isUse == 1}">
							<fmt:message key="USE" />
						</c:if>
						<c:if test="${board.isUse != 1}">
							<fmt:message key="NOT_USE" />
						</c:if>
					</td>
					<td align='center'><fmt:formatNumber value="${board.noOfRows}" /></td>
					<td align='center'>
						<c:if test="${board.useComment == 1}">
							<fmt:message key="USE" />
						</c:if>
						<c:if test="${board.useComment != 1}">
							<fmt:message key="NOT_USE" />
						</c:if>
					</td>
					<td align='center'>
						<util:formatDate value="${board.regDt}" pattern="yyyy.MM.dd HH:mm" />
					</td>
					<td>
						<util:formatDate value="${board.modDt}" pattern="yyyy.MM.dd HH:mm" />
					</td>
					<td align='center'>
						<a href="<c:url value="/admin/board?boardId=${board.boardId}" />" class="sbtn">
							<fmt:message key="MODIFY" />
						</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class='btn_grp inline_block'>
			<button type="submit"  class='black' onclick="return confirm('<fmt:message key="SURE_TO_DELETE" />');"><fmt:message key="DELETE_SELECTED_BOARD" /></button>
		</div>
	</form>
	
</layout:admin>
```

#### src/main/java/models/admin/board/BoardUpdateService.java

```java
package models.admin.board;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminValidator;

/**
 * 게시판 설정 수정
 * 
 * @author YONGGYO
 *
 */
public class BoardUpdateService {
	
	public void update(HttpServletRequest request) {
		 ResourceBundle bundle = ResourceBundle.getBundle("bundle.admin");
			
			/** 필수 입력 항목 체크 S */
			BoardAdminValidator validator = new BoardAdminValidator();
			Map<String, String> checkFields = new HashMap<>();
			checkFields.put("boardId", bundle.getString("REQUIRED_BOARD_ID"));
			checkFields.put("boardNm", bundle.getString("REQUIRED_BOARD_NM"));
			validator.requiredCheck(request, checkFields);
			
			/** 필수 입력 항목 체크 E */
			
			String boardId = request.getParameter("boardId");
			String boardNm= request.getParameter("boardNm");
			String isUse = request.getParameter("isUse");
			String noOfRows = request.getParameter("noOfRows");
			String useComment = request.getParameter("useComment");
			
			isUse = (isUse == null || isUse.isBlank()) ? "0": isUse;
			noOfRows = (noOfRows == null || noOfRows.isBlank()) ? "20" : noOfRows;
			useComment = (useComment == null || noOfRows.isBlank()) ? "0" : useComment;
			
			/** 게시판 설정 수정 S */
			BoardAdminDto dto = new BoardAdminDto();
			dto.setBoardId(boardId);
			dto.setBoardNm(boardNm);
			dto.setIsUse(Integer.parseInt(isUse));
			dto.setNoOfRows(Integer.parseInt(noOfRows));
			dto.setUseComment(Integer.parseInt(useComment));
		
			BoardAdminDao dao = BoardAdminDao.getInstance();
			if (!dao.update(dto)) {
				new BoardAdminException(bundle.getString("UPDATE_BOARD_ERROR"));
			}
			
			/** 게시판 설정 수정 E */
	}
}
```

#### src/main/java/models/admin/board/BoardInfoService.java

```java 
package models.admin.board;

import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminDao;

public class BoardInfoService {
	
	public BoardAdminDto get(HttpServletRequest request) {
			String boardId = request.getParameter("boardId");
			if (boardId != null && !boardId.isBlank()) {
				BoardAdminDto board = BoardAdminDao.getInstance().get(boardId);
				return board;
			}
		
			return null;
	}
}
```

#### src/main/java/models/admin/board/BoardDeleteService.java

```java
package models.admin.board;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import models.admin.board.BoardAdminValidator;

public class BoardDeleteService {
	
	public void delete(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.admin");
		
		/** 필수 입력 항목 체크 S */
		BoardAdminValidator validator = new BoardAdminValidator();
		Map<String, String> checkFields = new HashMap<>();
		checkFields.put("boardId", bundle.getString("REQUIRED_DELETE_BOARD_ID"));
		validator.requiredCheck(request, checkFields);
		
		/** 필수 입력 항목 체크 E */
		
		/** 삭제 처리 S */
		BoardAdminDao dao = BoardAdminDao.getInstance();
		String[] ids = request.getParameterValues("boardId");
		for (String id : ids) {
			dao.delete(id);
		}
		/** 삭제 처리 E */
	}
}
```

* * * 
# 게시판 관리자 구현 이미지

- 관리자로 로그인하였을 때 관리자 메뉴 노출 및 일반 회원 접근 통제

![image1](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/11%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EA%B8%B0%EB%8A%A5%20%EB%B6%84%EC%84%9D%2C%20%EC%82%AC%EC%9D%B4%ED%8A%B8%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%2C%20%ED%9A%8C%EC%9B%90%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image1.png)

- 관리자 메뉴 메인 

![image2](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/11%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EA%B8%B0%EB%8A%A5%20%EB%B6%84%EC%84%9D%2C%20%EC%82%AC%EC%9D%B4%ED%8A%B8%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%2C%20%ED%9A%8C%EC%9B%90%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image2.png)

- 게시판 생성 

![image3](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/11%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EA%B8%B0%EB%8A%A5%20%EB%B6%84%EC%84%9D%2C%20%EC%82%AC%EC%9D%B4%ED%8A%B8%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%2C%20%ED%9A%8C%EC%9B%90%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image3.png)

- 게시판 목록 

![image4](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/11%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EA%B8%B0%EB%8A%A5%20%EB%B6%84%EC%84%9D%2C%20%EC%82%AC%EC%9D%B4%ED%8A%B8%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%2C%20%ED%9A%8C%EC%9B%90%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image4.png)

- 게시판 수정

![image5](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/11%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EA%B8%B0%EB%8A%A5%20%EB%B6%84%EC%84%9D%2C%20%EC%82%AC%EC%9D%B4%ED%8A%B8%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%2C%20%ED%9A%8C%EC%9B%90%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image5.png)

- 게시판 삭제

![image6](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/11%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%ED%8C%90%20%EA%B8%B0%EB%8A%A5%20%EB%B6%84%EC%84%9D%2C%20%EC%82%AC%EC%9D%B4%ED%8A%B8%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%2C%20%ED%9A%8C%EC%9B%90%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image6.png)


