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

import java.time.LocalDateTime;

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

	@Override
	public String toString() {
		return "BoardDto [id=" + id + ", boardId=" + boardId + ", gid=" + gid + ", memNo=" + memNo + ", memId=" + memId
				+ ", memNm=" + memNm + ", poster=" + poster + ", guestPw=" + guestPw + ", subject=" + subject
				+ ", content=" + content + ", regDt=" + regDt + ", modDt=" + modDt + "]";
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
			INSERT INTO boardData (boardId, gid, memNo, poster, guestPw, subject, content, regDt) 
				VALUES (#{boardId}, #{gid}, #{memNo}, #{poster}, #{guestPw}, #{subject}, #{content}, #{regDt});
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
}
```

#### src/main/java/models/board/BoardValidator.java

```java
package models.board;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import static commons.Utils.*;
import models.validation.Validator;

/**
 * 게시글 작성, 수정, 삭제시 유효성 검사 
 * 
 * @author YONGGYO
 *
 */
public class BoardValidator implements Validator {
	
	private static BoardValidator instance = new BoardValidator();
	
	private BoardValidator() {}
	/**
	 * 유효성 검사 
	 * 
	 * @param {HttpServletRequest} request
	 * @param {String} mode : register(등록), update(수정), delete(삭제)
	 */
	public void validate(HttpServletRequest request, String mode) {
		Map<String, String> checkFields = new HashMap<>();
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
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
			
			if (!isLogin(request)) { // 비회원 비밀번호는 로그인 하지 않는 경우만 체크 
				checkFields.put("guestPw", bundle.getString("REQUIRED_GUEST_PW"));
			}
			checkFields.put("content", bundle.getString("REQUIRED_CONTENT"));
		} else if (mode.equals("delete")) {
			checkFields.put("id", bundle.getString("REQUIRED_ID"));
		}
		
		// 필수 데이터 검증
		requiredCheck(request, checkFields);
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
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.board" />
<fmt:message var="title" key="BOARD_WRITE" />
<layout:main title="${title}" bodyClass="board_form">
<h1 class="mtitle">${boardInfo.boardNm}</h1>	
<form id="frmRegist" class='form_box form_box2' name="frmRegist" method="post" action="<c:url value="/board/write" />" target="ifrmProcess">
	<jsp:include page="_form.jsp" />
	<div class='btn_grp'>
		<button type="reset"><fmt:message key="RESET" /></button>
		<button type="submit"><fmt:message key="WRITE" /></button>
	</div>
</form>
</layout:main>
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
WRITE=작성하기
UPDATE=수정하기
DELETE=삭제하기
VIEW=게시글보기
RESET=다시 작성

SUBJECT=제목
CONTENT=내용
POSTER=작성자명
REG_DT=작성일자
GUEST_PW=비밀번호

ADD_IMAGES=이미지 추가
ADD_FILES=파일 추가
```

#### src/main/webapp/WEB-INF/classes/board_en.properties 

```
BOARD_WRITE=Write a Post
BOARD_UPDATE=Update a Post
WRITE=Write
UPDATE=Update
DELETE=Delete
VIEW=View
RESET=Reset

SUBJECT=Subject
CONTENT=Content
POSTER=Poster
REG_DT=Date
GUEST_PW=Password

ADD_IMAGES=Add Images
ADD_FILES=Add Files
```



* * * 
# 게시글 수정

#### src/main/java/controller/board/UpdateController.java

```java

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

