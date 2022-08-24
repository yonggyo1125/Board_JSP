# 게시글 보기

### 
```
... 생략 

public class ViewService {
	
	public BoardDto view(HttpServletRequest request, HttpServletResponse response) {
		
		... 생략 
		
		int id;
		try {
			id = Integer.parseInt(_id.trim());  
		} catch (Exception e) { // 숫자가 아니라면 예외 발생
			throw new BadRequestException();
		}
		
		// 게시글 접근 권한 체크
		if (request.getAttribute("isViewPage") == null) {
			BoardValidator.getInstance().permissionCheck(request, response);
		}
		
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		... 생략 
		
	}
}
```


#### src/main/java/controllers/board/ViewController.java

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

import commons.BadRequestException;

import static commons.Utils.*;

import models.admin.board.BoardAdminDao;
import models.admin.board.BoardAdminDto;
import models.board.BoardDto;
import models.board.ViewService;

@WebServlet("/board/view")
public class ViewController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String[] addCss = { "board/board" };
			req.setAttribute("addCss", addCss);
			
			ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
				
			/** 게시글 조회 S */
			BoardDto board = new ViewService().view(req, resp);
			req.setAttribute("board", board);
			req.setAttribute("title", board.getSubject());
			/** 게시글 조회 E */
	
			/** 게시판 설정 조회 S */
			BoardAdminDto boardInfo = BoardAdminDao.getInstance().get(board.getBoardId());
			if (boardInfo == null) {
				throw new BadRequestException(bundle.getString("NOT_EXISTS_BOARD"));
			}
			
			req.setAttribute("boardInfo", boardInfo);
			/** 게시판 설정 조회 E */
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
			return;
		}
				
		RequestDispatcher rd = req.getRequestDispatcher("/board/view.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
```

#### src/main/webapp/board/view.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<fmt:setBundle basename="bundle.board" />
<layout:main title="${title}" bodyClass="board-view">
	<div class='top_box'>
		<div class='left'>${boardInfo.boardNm}</div>
		<div class='right'>
			${board.poster}${ empty board.memId ? "" : "(" + board.memId + ")"} 
			/ <util:formatDate value="${board.regDt}" pattern="yyyy.MM.dd HH:mm" />
		</div>
	</div>
	<!--// top_box -->
	<div class='content_box'>
		<div class='subject'>${board.subject}</div>
		<div class='content'>${board.content}</div>
	</div>
	<!--// content_box -->
	<div class='view_btns'>
		<c:if test="${board.memNo == 0 || (member != null && board.memNo == member.memNo) }">
			<fmt:message var="sureToDelete" key="SURE_TO_DELETE" />
			<a href='../board/delete?id=${board.id}' onclick="return confirm('${sureToDelete}');">
				<fmt:message key="DELETE" />
			</a>
			<a href='../board/update?id=${board.id}'>
				<fmt:message key="MODIFY" />
			</a>
		</c:if>
		<a href='../board/list?boardId=${board.boardId}'>
			<fmt:message key="LIST" />
		</a>
	</div>
</layout:main>
```

#### src/main/webapp/static/css/board/board.css 

```css
/** 게시글 보기 */
.body-board-view main { padding-bottom: 100px; }
.body-board-view .top_box { display: flex; justify-content: space-between; border: 1px solid #212121; padding: 15px 10px; margin: 20px 0 10px;  }
.body-board-view .top_box .left { font-size: 1.3rem; font-weight: bold; }
.body-board-view .content_box { margin-bottom: 10px; }
.body-board-view .subject { border: 1px solid #212121; padding: 15px 10px; font-size: 1.25rem; margin-bottom: 10px; font-weight: bold; }
.body-board-view .content { border: 1px solid #212121; padding: 10px; min-height: 450px; }
.body-board-view .content img { max-width: 1057px; }

.attach_files { list-style: none; margin-bottom: 10px; }
.attach_files li { border: 1px solid #d5d5d5; padding: 10px; background-color: #f8f8f8; margin-bottom: 2px; }
.attach_files li a:hover { text-decoration: underline; }

.board_btns { text-align: right; }
.board_btns a { display: inline-block; background-color: #212121; color: #fff; height: 40px; line-height: 40px; padding: 0 18px; font-size: 1.2rem; border-radius: 5px; }
.board_btns a:hover { background-color: #000; }
.board_btns a i { color: #fff; font-size: 1.2rem; }

@media all and (max-width: 500px) {
	.body-board-view .content img { width: 100%; }
}
```

* * * 
# 게시글 삭제

#### src/main/java/Controller/board/DeleteController.java

```java
package controllers.board;

import static commons.Utils.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.board.BoardDto;
import models.board.DeleteService;

@WebServlet("/board/delete")
public class DeleteController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			DeleteService service = new DeleteService();
			BoardDto board = service.delete(req, resp);
			
			go(resp, "../board/list?boardId=" + board.getBoardId());
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
```
#### src/main/java/models/board/DeleteService.java

```java
package models.board;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import commons.BadRequestException;
import models.board.BoardDao;
import models.file.FileDao;
import models.file.FileDto;

/**
 * 게시글 삭제 
 * 
 * @author YONGGYO
 *
 */
public class DeleteService {
	
	public BoardDto delete(HttpServletRequest request, HttpServletResponse response) {
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
		
		 if ( ! isAdmin(request) && board.getMemNo() == 0 && request.getSession().getAttribute("password_confirmed_" + id) == null) {
			 	return null;
		 }
		/** 게시글 조회 E */
		
		/** 첨부 파일 삭제 S */
		 List<FileDto> files = FileDao.getIntance().gets(board.getGid() + "%");
		 fileDelete(request, files);
		/** 첨부 파일 삭제 E */
		
		/** 게시글 삭제 S */
		if (!dao.delete(id)) {
			throw new BoardException(bundle.getString("FAIL_TO_DELETE"));
		}
		/** 게시글 삭제 E */
		return board;
	}
	
	/**
	 * 파일 삭제 
	 * 
	 * @param request
	 * @param files
	 */
	private void fileDelete(HttpServletRequest request, List<FileDto> files) {
		if (files == null) {
			return;
		}
		FileDao fileDao = FileDao.getIntance();
		String uploadPath = request.getServletContext().getRealPath(File.separator + "static" + File.separator  + "upload");
		for (FileDto file : files) {
			int fid = file.getId();
			String uploadFilePath = uploadPath + File.separator + "" + (fid % 10) + File.separator + "" + fid;
			File f = new File(uploadFilePath);
			if (f.exists()) {
				f.delete();
			}
			fileDao.delete(fid);
		}
	}
}
```

#### src/main/webapp/WEB-INF/classes/bundle/board_ko.properties

```
BOARD_WRITE=게시글 작성
BOARD_UPDATE=게시글 수정
BOARD_PASSWORD=비밀번호 확인
PASSWORD_CONFIRM=비밀번호가 확인되었습니다.

WRITE=글쓰기
UPDATE=수정하기
DELETE=삭제하기
VIEW=게시글보기
RESET=다시 작성
CONFIRM=확인하기
LIST=목록
MODIFY=수정
DELETE=삭제

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

SURE_TO_DELETE=정말 삭제하시겠습니까?
FAIL_TO_DELETE=게시글 삭제에 실패하였습니다.
```

#### src/main/webapp/WEB-INF/classes/bundle/board_en.properties

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
LIST=List
MODIFY=Modify
DELETE=Delete

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
SURE_TO_DELETE=Are you sure to delete?
FAIL_TO_DELETE=Fail to Delete the post.
```

* * * 
# 게시글 목록 


#### src/main/java/Controller/board/ListController.java

```java
package controllers.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/board/list")
public class ListController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestDispatcher rd = req.getRequestDispatcher("/board/list.jsp");
		rd.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
```

#### src/main/webapp/board/list.jsp 

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.board" />
<layout:main title="${title}" bodyClass="board-list">

</layout:main>
```

* * * 
# 구현 화면

![image1](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/13%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EB%AA%A9%EB%A1%9D%2C%20%ED%8E%98%EC%9D%B4%EC%A7%95%2C%20%EC%82%AD%EC%A0%9C%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image1.png)

![image2](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/13%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EB%AA%A9%EB%A1%9D%2C%20%ED%8E%98%EC%9D%B4%EC%A7%95%2C%20%EC%82%AD%EC%A0%9C%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image2.png)

![image3](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/13%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EB%AA%A9%EB%A1%9D%2C%20%ED%8E%98%EC%9D%B4%EC%A7%95%2C%20%EC%82%AD%EC%A0%9C%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image3.png)

