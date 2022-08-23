# 게시글 작성 

#### src/main/sql/boardData.sql 

```sql
CREATE TABLE `boardData` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '게시글 번호',
  `boardId` VARCHAR(45) NULL COMMENT '게시판 아아디',
  `gid` VARCHAR(45) NULL COMMENT '그룹 ID',
  `memNo` INT NULL DEFAULT 0 COMMENT '회원 번호',
  `poster` VARCHAR(30) NULL COMMENT '작성자명',
  `guestPw` VARCHAR(65) NULL COMMENT '비회원 비밀번호',
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

import java.util.List;
import java.time.LocalDateTime;

import models.file.FileDto;

public class BoardDto {
	private int id; // 게시글 등록 번호
	private String boardId; // 게시판 ID
	private String gid; // 그룹 ID
	private int memNo; // 회원번호
	private String memId; // 회원 ID 
	private String memNm; // 회원명
	private String poster; // 작성자
	private String guestPw; // 비회원 비밀번호
	private String subject; // 게시글 제목 
	private String content; // 게시글 내용 
	private LocalDateTime regDt; // 등록일시
	private LocalDateTime modDt; // 수정일시
	
	private List<FileDto> imageFiles; // 이미지 파일 목록
	private List<FileDto> attachedFiles; // 첨부이미지 파일 목록 
	
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
		// gid가 없는 경우 자동 생성 
		if (gid == null) {
			gid = "" + System.currentTimeMillis();
		}
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
	
	public String getGuestPw() {
		return guestPw;
	}

	public void setGuestPw(String guestPw) {
		this.guestPw = guestPw;
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

	public List<FileDto> getImageFiles() {
		return imageFiles;
	}

	public void setImageFiles(List<FileDto> imageFiles) {
		this.imageFiles = imageFiles;
	}

	public List<FileDto> getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(List<FileDto> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	@Override
	public String toString() {
		return "BoardDto [id=" + id + ", boardId=" + boardId + ", gid=" + gid + ", memNo=" + memNo + ", memId=" + memId
				+ ", memNm=" + memNm + ", poster=" + poster + ", guestPw=" + guestPw + ", subject=" + subject
				+ ", content=" + content + ", regDt=" + regDt + ", modDt=" + modDt + ", imageFiles=" + imageFiles
				+ ", attachedFiles=" + attachedFiles + "]";
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
		<result property="memId" column="memId" />
		<result property="memNm" column="memNm" />
		<result property="poster" column="poster" />
		<result property="guestPw" column="guestPw" />
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
		keyProperty="id">
			INSERT INTO boardData (boardId, gid, memNo, poster, guestPw, subject, content) 
				VALUES (#{boardId}, #{gid}, #{memNo}, #{poster}, #{guestPw}, #{subject}, #{content});
	</insert>
	
	<!--  게시글 수정 -->
	<update id="update" parameterType="models.board.BoardDto">
		UPDATE boardData 
				SET 
					poster = #{poster},
					guestPw=#{guestPw},
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

#### src/main/java/mybatis/config/mybatis-config.xml && mybatis-dev-config.xml 

```xml

... 생략 

<configuration>
	
	... 생략 
	
	<mappers>

	... 생략 
	
	<mapper resource="models/board/BoardMapper.xml" />
  </mappers>
</configuration>
```

#### src/main/java/models/board/BoardDao.java

```java
package models.board;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import mybatis.Connection;

public class BoardDao {
	/**
	 * 게시글 목록 
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<BoardDto> gets(int page, int limit) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardListDto param = new BoardListDto();
		int offset = (page - 1) * limit;
		param.setOffset(offset);
		param.setLimit(limit);
		List<BoardDto> posts = sqlSession.selectList("BoardMapper.gets", param);
		sqlSession.commit();
		sqlSession.close();
		
		return posts;
	}
	
	public List<BoardDto> gets(int page) {
		return gets(page, 20);
	}
	
	public List<BoardDto> gets() {
		return gets(1);
	}
	
	/**
	 * 게시글 조회
	 * 
	 */
	public BoardDto get(int id) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardDto param = new BoardDto();
		param.setId(id);
		
		BoardDto post = sqlSession.selectOne("BoardMapper.get", param);
		
		sqlSession.close();
		
		return post;
	}
	
	/**
	 * 게시글 추가 
	 * 
	 * @param {BoardDto} dto
	 * @return {boolean}
	 */
	public boolean register(BoardDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.insert("BoardMapper.register", dto);
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 수정
	 * 
	 * @param {BoardDto} dto
	 * @return {boolean}
	 */
	public boolean update(BoardDto dto) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		int affectedRows = sqlSession.update("BoardMapper.update", dto);
		
		sqlSession.commit();
		sqlSession.close();
		
		return affectedRows > 0;
	}
	
	/**
	 * 게시글 삭제 
	 * 
	 * @param {int} id 게시글 번호 
	 * @return {boolea}
	 */
	public boolean delete(int id) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardDto param = new BoardDto();
		param.setId(id);
		int affectedRows = sqlSession.update("BoardMapper.delete", param);
		
		sqlSession.commit();
		sqlSession.close();
		return affectedRows > 0;
	}
}
```

#### src/main/java/controller/board/WriteController.java

```java
package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import commons.BadRequestException;
import models.board.BoardDto;
import models.board.WriteService;
import models.admin.board.BoardAdminDto;
import models.admin.board.BoardInfoService;

@WebServlet("/board/write")
public class WriteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		BoardAdminDto boardInfo = null;
		try {
			String boardId = req.getParameter("boardId");
			if (boardId == null || boardId.isBlank()) {
				throw new BadRequestException();
			}
			
			/** 게시판 설정 조회 S */
			boardInfo = new BoardInfoService().get(req);
			if (boardInfo == null) {
				throw new BadRequestException(bundle.getString("NOT_EXISTS_BOARD"));
			}
			req.setAttribute("boardInfo", boardInfo);
			/** 게시판 설정 조회 E */
			
 		} catch (RuntimeException e) {
			alertError(resp, e, "history.back();");
			return;
		}
		
		String[] addJs = { "board/form", "ckeditor/ckeditor" };
		req.setAttribute("addJs", addJs);
		
		BoardDto board = new BoardDto();
		board.setBoardId(boardInfo.getBoardId());
		req.setAttribute("board", board);
		RequestDispatcher rd = req.getRequestDispatcher("/board/write.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			WriteService service = new WriteService();
			BoardDto board = service.write(req);
			
			// 작성 완료 후 게시글 보기 페이지로 이동 
			String url = "../board/view?id=" + board.getId();
			go(resp, url, "parent");
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}	
}
```

#### src/main/java/models/board/BoardException.java

```java
package models.board;

public class BoardException extends RuntimeException {
	public BoardException(String message) {
		super(message);
	}
}
```

#### src/main/java/models/commons/Utils.java

```java

... 생략

import models.member.MemberDto;

... 생략 

public class Utils {
	
	... 생략 
	
	/**
	 * 로그인 여부 체크 
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isLogin(HttpServletRequest request) {
		
		return request.getAttribute("member") != null;
	}
	
	/**
	 * 로그인 회원정보 조회
	 * 
	 * @param request
	 * @return
	 */
	public static MemberDto getMember(HttpServletRequest request) {
		if (!isLogin(request)) {
			return null;
		}
		
		MemberDto member = (MemberDto)request.getSession().getAttribute("member");
		
		return member;
	}
	
	/**
	 * 관리자 여부 체크 
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAdmin(HttpServletRequest request) {
		MemberDto member = getMember(request);
		if (member == null) {
			return false;
		}
		
		return member.getMemType().equals("admin");
	}
}
```

#### src/main/java/models/board/BoardValidator.java

```java
package models.board;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.BadRequestException;

import static commons.Utils.*;
import models.validation.Validator;
import models.board.BoardDao;
import models.member.MemberDto;

/**
 * 게시글 작성, 수정, 삭제시 유효성 검사 
 * 
 * @author YONGGYO
 *
 */
public class BoardValidator implements Validator {
	
	private static BoardValidator instance = new BoardValidator();
	
	ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");

	private BoardValidator() {}
	/**
	 * 유효성 검사 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {String} mode : register(등록), update(수정), delete(삭제)
	 */
	public void validate(HttpServletRequest request, String mode) {
		Map<String, String> checkFields = new HashMap<>();
		
		// 게시글 등록, 수정 
		if (mode.equals("register") || mode.equals("update")) {
			if (mode.equals("register")) {
				checkFields.put("boardId", bundle.getString("REQUIRED_BOARD_ID"));
			} else {
				checkFields.put("id", bundle.getString("REQUIRED_ID"));
			}
			
			checkFields.put("gid", bundle.getString("REQUIRED_GROUP_ID"));
			checkFields.put("subject", bundle.getString("REQUIRED_SUBJECT"));
			checkFields.put("poster", bundle.getString("REQUIRED_POSTER"));
			
			if (request.getParameter("guestPw") != null) { // 비회원 비밀번호가 있는 경우는 체크 
				checkFields.put("guestPw", bundle.getString("REQUIRED_GUEST_PW"));
			}	
			checkFields.put("content", bundle.getString("REQUIRED_CONTENT"));
		} else if (mode.equals("delete")) {
			checkFields.put("id", bundle.getString("REQUIRED_ID"));
		}
		
		// 필수 데이터 검증
		requiredCheck(request, checkFields);
		
		/** 게시글 수정 또는 삭제인 경우는 본인 게시글인지 체크 S */ 
		if (mode.equals("update") || mode.equals("delete")) {
			permissionCheck(request);
		}
		/** 게시글 수정 또는 삭제인 경우는 본인 게시글인지 체크 E */ 
	}
	
	/**
	 * 게시글 접근 권한 체크 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {int} id
	 */
	public void permissionCheck(HttpServletRequest request) {
		permissionCheck(request, null);
	}
	
	public void permissionCheck(HttpServletRequest request, HttpServletResponse response) {
		
		String _id = request.getParameter("id").trim();
		int id;
		try {
			id = Integer.parseInt(_id);
		} catch (Exception e) {
			throw new BadRequestException();
		}
		
		BoardDto board = BoardDao.getInstance().get(id);
		if (board == null) {
			throw new BoardException(bundle.getString("NOT_EXISTS_BOARD"));
		}
		// 관리자는 무조건 권한 있음 
		if (isAdmin(request)) {
			return;
		}
		
		int memNo = board.getMemNo();
		MemberDto member = getMember(request);
		if (memNo > 0) {
			if (member == null ||  member.getMemNo() != memNo) {
				throw new BoardException(bundle.getString("NOT_YOUR_POST"));
			}
		} else {
			/** 
			 * 비회원 게시글의 경우 비밀번호 일치 여부 확인 후 일치하는 경우 
			 * password_confirmed_게시글 번호 형태로 세션에 추가된다.  없으면 본인 글 검증 안된 상태
			 */
			if (request.getSession().getAttribute("password_confirmed_" + id) == null) {
				if (response != null) {
					
					/** 
					 * 비회원 게시글의 경우 비밀번호 검증 여부 체크 하고 
					 * 미 검증시 비밀번호 확인 페이지로 이동  
					 */
					if (board.getMemNo() == 0 && request.getSession().getAttribute("password_confirmed_" + id) == null) {
						try {
							request.setAttribute("board", board);
							RequestDispatcher rd = request.getRequestDispatcher("/board/password.jsp");
							rd.forward(request, response);
						} catch (Exception e) { 
							e.printStackTrace();
						} 
					}
				} else {
					throw new BoardException(bundle.getString("NOT_YOUR_POST"));
				}
			}
		}
	}
	
	public static BoardValidator getInstance() {
		if (instance == null) {
			instance = new BoardValidator();
		}
		
		return instance;
	}
}
```

#### src/main/java/models/board/WriteService.java

```java
package models.board;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;

import models.file.FileDao;
import models.member.MemberDto;

import org.mindrot.bcrypt.BCrypt;

/**
 * 게시글 작성 
 * 
 * @author YONGGYO
 *
 */
public class WriteService {
	
	public BoardDto write(HttpServletRequest request) {
		
		/** 유효성 검사 S */
		BoardValidator boardValidator = BoardValidator.getInstance();
		boardValidator.validate(request, "register");
		/** 유효성 검사 E */
		
		/** 게시글 등록 S */
		BoardDao dao = BoardDao.getInstance();
		BoardDto board = new BoardDto();
		MemberDto member = getMember(request);
		board.setBoardId(request.getParameter("boardId"));
		board.setGid(request.getParameter("gid"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		board.setPoster(request.getParameter("poster"));
		if (member == null) { // 비회원인 경우 
			board.setMemNo(0);
			String hash = BCrypt.hashpw(request.getParameter("guestPw"), BCrypt.gensalt(12));
			board.setGuestPw(hash);
		} else {
			board.setMemNo(member.getMemNo());
		}
		
		boolean result = dao.register(board);
		if (!result) {
			return null;
		}
		
		// 이미지 및 첨부 파일 업로드 완료 처리
		String gid = board.getGid();
		FileDao fileDao = FileDao.getIntance();
		fileDao.updateDone(gid + "_image");
		fileDao.updateDone(gid + "_attached");
		
		return board;
	}
}
```

#### src/main/webapp/board/write.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.board" />
<fmt:message var="title" key="BOARD_WRITE" />
<layout:main title="${title}" bodyClass="board_form">
<h1 class="mtitle">${title}</h1>	
<form id="frmRegist" class='form_box form_box2' name="frmRegist" method="post" action="<c:url value="/board/write" />" target="ifrmProcess">
	<input type="hidden" name="boardId" value="${boardId}" />
	<jsp:include page="_form.jsp" />
	<div class='btn_grp'>
		<button type="reset"><fmt:message key="RESET" /></button>
		<button type="submit"><fmt:message key="WRITE" /></button>
	</div>
</form>
</layout:main>
```

#### src/main/webapp/board/_form.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="bundle.board" />
<input type="hidden" name="gid" value="${board.gid}">
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="SUBJECT" />
	</dt>
	<dd class='mobile_fullwidth' >
		<input type="text" name="subject" value="${board.subject}" placeholder="<fmt:message key="SUBJECT" />" />
	</dd>
</dl>
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="POSTER" />
	</dt>
	<dd class='mobile_fullwidth' >
		<input type="text" name="poster" value="${ empty board.poster ? member.memNm : board.poster }" placeholder="<fmt:message key="POSTER" />" />
	</dd> 
</dl>
<c:if test="${ empty member || (!empty board && board.memNo == 0) }">
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="GUEST_PW" />
	</dt>
	<dd class='mobile_fullwidth' >
		<input type="password" name=guestPw placeholder="<fmt:message key="GUEST_PW" />" />
	</dd>
</dl>
</c:if>
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="CONTENT" />
	</dt>
	<dd class='mobile_fullwidth' >
		<textarea name="content" id="content" placeholder="<fmt:message key="CONTENT" />">${board.content}</textarea>
		<button type="button" id="add_images"><fmt:message key="ADD_IMAGES" /></button>
		<ul class="attach_images">
		<c:if test="${board.imageFiles != null }">
			<c:forEach var="file" items="${board.imageFiles}">
				<li>
					<a href='../file/download?id=${file.id}'>${file.fileName}</a>
					<span class="remove" data-id=${file.id}>[X]</span>
				</li>
			</c:forEach>  
		</c:if>
		</ul>
	</dd>
</dl>
<dl>
	<dt>파일첨부</dt>
	<dd>
		<button type="button" id="add_files"><fmt:message key="ADD_FILES" /></button>
		<ul class="attach_files">
		<c:if test="${board.attachedFiles != null }">
			<c:forEach var="file" items="${board.attachedFiles}">
				<li>
					<a href='../file/download?id=${file.id}'>${file.fileName}</a>
					<span class="remove" data-id=${file.id}>[X]</span>
				</li>
			</c:forEach>  
		</c:if>
		</ul>
	</dd>
</dl>

<script type="text/html" id="tpl_file">
	<li>
		<a href='../file/download?id=#[id]'>#[fileName]</a>
		<span class="remove" data-id=#[id]>[X]</span>
	</li>
</script>
```

#### src/main/webapp/static/js/form.js

```javascript
const boardForm = {
	/**
	* 파일 업로드 팝업 
	*
	* @param mode :  mode값이 image이면 그룹ID_image 형태로 아니면 그룹ID_attached로 생성 
	*/
	uploadPopup(mode) {
		const gidEl = document.querySelector("input[name='gid']");
		if (!gidEl || gidEl.value.trim() == "") {
			return;
		}
		let gid = gidEl.value.trim();
		gid = mode == 'image' ? `${gid}_image`:`${gid}_attached`;
		let url = `../file/upload?gid=${gid}`;
		if (mode == 'image') {
			url += "&isImageOnly=1";
		}
		
		layer.open(url, null, 360, 500, true);
	},
	/**
	* 파일 삭제 처리 
	*
	 */
	delete(e) {
		if (!confirm('정말 삭제하시겠습니까?')) {
			return;
		}
		
		const target = e.currentTarget;
		const id = target.dataset.id;
		const parentEl = target.parentElement;
		
		const xhr = new XMLHttpRequest();
		const url = `../file/delete?id=${id}`;
		xhr.open("GET", url);
		xhr.addEventListener("readystatechange", function() {
			if (xhr.status == 200 && xhr.readyState == XMLHttpRequest.DONE) {
				const result = JSON.parse(xhr.responseText);
				if (result.error) {
					alert(result.message);
					return;
				}
				
				parentEl.parentElement.removeChild(parentEl);
			}
		});
		xhr.send(null);		
		
	}
};

/** 이벤트 처리 S */
window.addEventListener("DOMContentLoaded", function() {
	/** CK 에디터 로드 */
	CKEDITOR.replace("content", { height: 350 });
	
	/** 이미지 추가 버튼 클릭 처리 S */
	const addImagesEl = document.getElementById("add_images");
	if (addImagesEl) {
		addImagesEl.addEventListener("click", function() {
			boardForm.uploadPopup("image");
		});
	}
	/** 이미지 추가 버튼 클릭 처리 E */
	/** 파일 추가 버튼 클릭 처리 S */
	const addFilesEl = document.getElementById("add_files");
	if (addFilesEl) {
		addFilesEl.addEventListener("click", function() {
			boardForm.uploadPopup();
		});
	}
	/** 파일 추가 버튼 클릭 처리 E */
	
	/** 파일 삭제 버튼 클릭 처리 S */
	const removeEls = document.querySelectorAll(".attach_images .remove, .attach_files .remove");
	for (el of removeEls) {
		el.addEventListener("click", boardForm.delete);
	}
	/** 파일 삭제 버튼 클릭 처리 E */
});
/** 이벤트 처리 E */

/** 파일 업로드 콜백 S */
function fileUploadCallback(files) {
	if (files && files.length > 0) {
		
		const domParser = new DOMParser();
		const tplFile = document.getElementById("tpl_file").innerHTML;
		const imageURI = "../static/upload/";
		const attachImagesEl = document.querySelector(".attach_images");
		 const attachFilesEl = document.querySelector(".attach_files");
		for (file of files) {
			
			let html = tplFile;
			html = html.replace(/#\[id\]/g, file.id)
							.replace(/#\[fileName\]/g, file.fileName);
			const dom = domParser.parseFromString(html, "text/html");
			const liEl = dom.querySelector("li");
			const removeEl = liEl.querySelector(".remove");
			/** 파일 삭제 이벤트 바인딩 S */
			if (removeEl) {
				removeEl.addEventListener("click", boardForm.delete);
			}
			/** 파일 삭제 이벤트 바인딩 E */
			
			if (file.gid.indexOf("image") != -1) { // 이미지 파일 
				const url = `${imageURI}${file.id % 10}/${file.id}`;
				const img = `<img src='${url}' style='max-width: 700px;' />`;
				CKEDITOR.instances.content.insertHtml(img);
				attachImagesEl.appendChild(liEl);
			} else {
				attachFilesEl.appendChild(liEl);
			}
		}
	} // endif 
	
	layer.close();
}
/** 파일 업로드 콜백 E */
```

#### src/main/webapp/WEB-INF/classes/board_ko.properties 

```
BOARD_WRITE=게시글 작성
BOARD_UPDATE=게시글 수정
BOARD_PASSWORD=비밀번호 확인
PASSWORD_CONFIRM=비밀번호가 확인되었습니다.

WRITE=작성하기
UPDATE=수정하기
DELETE=삭제하기
VIEW=게시글보기
RESET=다시 작성
CONFIRM=확인하기

SUBJECT=제목
CONTENT=내용
POSTER=작성자명
REG_DT=작성일자
GUEST_PW=비밀번호

ADD_IMAGES=이미지 추가
ADD_FILES=파일 추가

# 유효성 검사
NOT_EXISTS_BOARD=등록되지 않은 게시판입니다.
POST_NOT_EXISTS=등록되지 않은 게시글입니다.
NOT_YOUR_POST=본인이 작성한 게시글이 아닙니다.
INCORECT_PASSWORD=비밀번호가 일치하지 않습니다.

REQUIRED_BOARD_ID=잘못된 접근입니다.
REQUIRED_ID=잘못된 접근입니다.
REQUIRED_GROUP_ID=잘못된 접근입니다.
REQUIRED_SUBJECT=제목을 입력하세요.
REQUIRED_POSTER=작성자를 입력하세요.
REQUIRED_GUEST_PW=비회원 게시글 비밀번호를 입력하세요.
REQUIRED_CONTENT=게시글을 입력하세요.
```

#### src/main/webapp/WEB-INF/classes/board_en.properties 

```
BOARD_WRITE=Write a Post
BOARD_UPDATE=Update a Post
BOARD_PASSWORD=Password Check
WRITE=Write
UPDATE=Update
DELETE=Delete
VIEW=View
RESET=Reset
CONFIRM=Confirm

SUBJECT=Subject
CONTENT=Content
POSTER=Poster
REG_DT=Date
GUEST_PW=Password

ADD_IMAGES=Add images
ADD_FILES=Add files

# 유효성 검사
NOT_EXISTS_BOARD = The board doesn't exists.
POST_NOT_EXISTS=The Post doesn't exists.
NOT_YOUR_POST=This is not your post.
PASSWORD_CONFIRM=Your password confirmed.
INCORECT_PASSWORD=Your password is incorrect.

REQUIRED_BOARD_ID=Wrong request
REQUIRED_ID=Wrong request
REQUIRED_GROUP_ID=Wrong request
REQUIRED_SUBJECT=Please Input the subject.
REQUIRED_POSTER=Please Input the poster.
REQUIRED_GUEST_PW=Please Input the guest password.
REQUIRED_CONTENT=Please Input the content.
```

* * * 
# 게시글 수정

#### src/main/java/models/board/ViewService.java

```java
package models.board;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import commons.BadRequestException;
import models.file.FileDao;
import models.file.FileDto;


public class ViewService {
	
	public BoardDto view(HttpServletRequest request, HttpServletResponse response) {
		String _id = request.getParameter("id");
		if (_id == null || _id.isBlank()) {
			throw new BadRequestException();
		}
		
		int id;
		try {
			id = Integer.parseInt(_id.trim());  
		} catch (Exception e) { // 숫자가 아니라면 예외 발생
			throw new BadRequestException();
		}
		
		// 게시글 접근 권한 체크
		 BoardValidator.getInstance().permissionCheck(request, response);
		
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 게시글 조회 S */
		BoardDao dao = BoardDao.getInstance();
		BoardDto board = dao.get(id);
		if (board == null) {
			throw new BoardException(bundle.getString("POST_NOT_EXISTS"));
		}
		
		String gid = board.getGid();
		FileDao fileDao = FileDao.getIntance();
		
		// 이미지 파일 조회 
		List<FileDto> imageFiles = fileDao.getsDoneDesc(gid + "_image");
		board.setImageFiles(imageFiles);
		
		// 첨부 파일 조회 
		List<FileDto> attachedFiles = fileDao.getsDoneDesc(gid + "_attached");
		board.setAttachedFiles(attachedFiles);
		/** 게시글 조회 E */
		
		return board;
	}
}
```

#### src/main/java/controller/board/UpdateController.java

```java
package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import commons.BadRequestException;
import models.board.ViewService;
import models.admin.board.BoardAdminDao;
import models.admin.board.BoardAdminDto;
import models.board.BoardDto;
import models.board.UpdateService;

@WebServlet("/board/update")
public class UpdateController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
			BoardDto board = new ViewService().view(req, resp);
			/** 게시판 설정 조회 S */
			
			req.setAttribute("board", board);
			BoardAdminDto boardInfo = BoardAdminDao.getInstance().get(board.getBoardId());
			if (boardInfo == null) {
				throw new BadRequestException(bundle.getString("NOT_EXISTS_BOARD"));
			}
			
			req.setAttribute("boardInfo", boardInfo);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
			return;
		}
		
		String[] addJs = { "board/form", "ckeditor/ckeditor" };
		req.setAttribute("addJs", addJs);
		
		RequestDispatcher rd = req.getRequestDispatcher("/board/update.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			UpdateService service = new UpdateService();
			BoardDto board = service.update(req);
			
			// 수정 완료 후 게시글 보기로 이동
			String url = "../board/view?id=" + board.getId();
			go(resp, url, "parent");
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
}
```

### src/main/java/models/board/UpdateService.java

```java
package models.board;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;

import models.file.FileDao;
import models.member.MemberDto;

import org.mindrot.bcrypt.BCrypt;

public class UpdateService {
	public BoardDto update(HttpServletRequest request) {
		
		/** 유효성 검사 S */
		BoardValidator boardValidator = BoardValidator.getInstance();
		boardValidator.validate(request, "update");
		/** 유효성 검사 E */
		
		/** 게시글 수정 S */
		BoardDao dao = BoardDao.getInstance();
		BoardDto board = new BoardDto();
		board.setId(Integer.parseInt(request.getParameter("id").trim()));
		board.setBoardId(request.getParameter("boardId"));
		board.setSubject(request.getParameter("subject"));
		board.setContent(request.getParameter("content"));
		board.setPoster(request.getParameter("poster"));
		if (board.getMemNo() == 0) {
			String hash = BCrypt.hashpw(request.getParameter("guestPw"), BCrypt.gensalt(12));
			board.setGuestPw(hash);
		}
		boolean result = dao.update(board);
		if (!result) {
			return null;
		}
		
		board = dao.get(board.getId());
		
		// 이미지 및 첨부 파일 업로드 완료 처리
		String gid = board.getGid();
		FileDao fileDao = FileDao.getIntance();
		fileDao.updateDone(gid + "_image");
		fileDao.updateDone(gid + "_attached");
		/** 게시글 수정 E */
		
		return board;
	}
}
```

#### src/main/webapp/board/update.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.board" />
<fmt:message var="title" key="BOARD_UPDATE" />
<layout:main title="${title}" bodyClass="board_form">
<h1 class="mtitle">${boardInfo.boardNm}</h1>	
<form id="frmUpdate" name="frmUpdate" class='form_box form_box2'  method="post" action="<c:url value="/board/update" />" target="ifrmProcess">
	<input type="hidden" name="id" value="${board.Id}" />
	<jsp:include page="_form.jsp" />
	<div class='btn_grp'>
		<button type="reset"><fmt:message key="RESET" /></button>
		<button type="submit"><fmt:message key="UPDATE" /></button>
	</div>
</form>
</layout:main>
```

### src/main/java/controllers/board/PasswordController.java

```java
package controllers.board;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import models.board.PasswordCheckService;

@WebServlet("/board/password")
public class PasswordController extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PasswordCheckService service = new PasswordCheckService();
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

#### src/main/webapp/board/password.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.board" />
<fmt:message var="title" key="BOARD_PASSWORD" />
<layout:main title="${title}" bodyClass="board_password">
<h1 class="mtitle">${title}</h1>
<form class="form_box" method="post" action="<c:url value="/board/password" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="id" value="${board.id}">
	<dl>
		<dt class='mobile_hidden'>
			<fmt:message key="GUEST_PW" />
		</dt>
		<dd class='mobile_fullwidth' >
			<input type="password" name="password" placeholder="<fmt:message key="GUEST_PW" />">
		</dd>
	</dl>
	<div class="btn_grp mt10">
		<button type="submit" class="black"><fmt:message key="CONFIRM" /></button>
	</div>
</form>
</layout:main>
```

* * * 
# 완성 화면

![image1](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/12%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EC%9E%91%EC%84%B1%2C%20%EC%88%98%EC%A0%95%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image1.png)

![image2](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/12%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EC%9E%91%EC%84%B1%2C%20%EC%88%98%EC%A0%95%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image2.png)

![image3](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/12%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EC%9E%91%EC%84%B1%2C%20%EC%88%98%EC%A0%95%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image3.png)

![image4](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/12%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EC%9E%91%EC%84%B1%2C%20%EC%88%98%EC%A0%95%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image4.png)

![image5](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/12%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EC%9E%91%EC%84%B1%2C%20%EC%88%98%EC%A0%95%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image5.png)

