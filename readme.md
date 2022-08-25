# 게시글 목록 

#### src/main/java/models/board/BoardDao.java 

```java

... 생략 

public class BoardDao {
	private static BoardDao instance = new BoardDao();
	
	/**
	 * 게시글 목록 
	 * @param String boardId 게시판 아이디
	 * @param page
	 * @param limit
	 * @return
	 */
	public List<BoardDto> gets(String boardId, int page, int limit) {
		SqlSession sqlSession = Connection.getSqlSession();
		BoardListDto param = new BoardListDto();
		param.setBoardId(boardId);
		int offset = (page - 1) * limit;
		param.setOffset(offset);
		param.setLimit(limit);
		List<BoardDto> posts = sqlSession.selectList("BoardMapper.gets", param);
		sqlSession.commit();
		sqlSession.close();
		
		return posts;
	}
	
	public List<BoardDto> gets(String boardId,  int page) {
		return gets(boardId, page, 20);
	}
	
	public List<BoardDto> gets(String boardId) {
		return gets(boardId, 1);
	}
	
	/**
	 * 게시판별 총 게시글 수 
	 * 
	 * @param boardId
	 * @return
	 */
	public int getTotal(String boardId) {
		SqlSession sqlSession = Connection.getSqlSession();
		
		BoardDto param = new BoardDto();
		param.setBoardId(boardId);
		int total = sqlSession.selectOne("BoardMapper.total", param);
		
		sqlSession.close();
		
		return total;
	}
	
	... 생략 
	
}
```

#### src/main/java/models/board/BoardMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="BoardMapper">
	
	... 생략 
	
	<!-- 게시글 목록  -->
	<select id="gets" parameterType="models.board.BoardListDto" resultMap="boardMap">
		SELECT b.*, m.memId, m.memNm FROM boardData b 
					LEFT JOIN member m ON b.memNo = m.memNo 
			WHERE boardId=#{boardId} 
			ORDER BY b.regDt DESC LIMIT #{offset}, #{limit};
	</select>
	
	<!-- 전체 게시글 수  -->
	<select id="total" parameterType="models.board.BoardDto" resultType="int">
		SELECT COUNT(*) FROM boardData WHERE boardId=#{boardId};
	</select>
	
	... 생략 
	
</mapper>
```

#### src/main/java/models/board/ListService.java 

```java
package models.board;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import commons.BadRequestException;
import models.admin.board.BoardAdminDao;
import models.admin.board.BoardAdminDto;
import models.board.BoardDao;
import models.board.BoardDto;

public class ListService {
	public Map<String, Object> getList(HttpServletRequest request) {
		
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.board");
		
		/** 게시판 아이디 필수 체크 S */
		String boardId = request.getParameter("boardId");
		if (boardId == null || boardId.isBlank()) {
			throw new BadRequestException();
		}
		/** 게시판 아이디 필수 체크 E */
		
		/**  등록된 게시판인지 체크 S */
		BoardAdminDto boardInfo = BoardAdminDao.getInstance().get(boardId);
		if (boardInfo == null) {
			throw new BoardException(bundle.getString("NOT_EXISTS_BOARD"));
		}
		/** 등록된 게시판인지 체크 E */
		
		/** 게시판 사용 가능 여부 체크 S */
		if (boardInfo.getIsUse() == 0) {
			throw new BoardException(bundle.getString("NOT_ALLOWED_BOARD"));
		}
		/** 게시판 사용 가능 여부 체크 E */
		
		BoardDao dao = BoardDao.getInstance();

		int page = 1;
		try {
			page = request.getParameter("page")  == null ? 1 : Integer.parseInt(request.getParameter("page").trim());
		} catch (Exception e) {  // 페이지 번호가 숫자가 아닌 경우는 1로 고정
			page = 1;
		}
		
		/** 1 페이지당 게시글 수 */
		int limit = boardInfo.getNoOfRows() > 0 ?  boardInfo.getNoOfRows() : 20;
		
		List<BoardDto> posts = dao.gets(boardId, page, limit);
		int total = dao.getTotal(boardId);
		
		Map<String,Object> results = new HashMap<>();
		
		results.put("page", page);
		results.put("total", total);
		results.put("posts", posts);
		results.put("boardInfo", boardInfo);
		
		request.setAttribute("page", page);
		request.setAttribute("total", total);
		request.setAttribute("posts", posts);
		request.setAttribute("boardInfo", boardInfo);
		
		request.setAttribute("title", boardInfo.getBoardNm());
		
		return results;
	}
}
```

#### src/main/java/controllers/board/ListController.java 

```java
package controllers.board;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static commons.Utils.*;
import models.board.ListService;

@WebServlet("/board/list")
public class ListController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			
			ListService service = new ListService();
			service.getList(req);
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e, "history.back();");
			return;
		}
		
		String[] addCss = { "board/board "};
		req.setAttribute("addCss", addCss);
		
		RequestDispatcher rd = req.getRequestDispatcher("/board/list.jsp");
		rd.forward(req, resp);
	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
```



#### src/main/webapp/WEB-INF/classes/bundle/board_ko.properties

```

... 생략

SURE_TO_DELETE=정말 삭제하시겠습니까?
FAIL_TO_DELETE=게시글 삭제에 실패하였습니다.
NOT_ALLOWED_BOARD=사용이 제한된 게시판입니다.
NO_POST=등록된 게시글이 없습니다.
```

#### src/main/webapp/WEB-INF/classes/bundle/board_en.properties

```

... 생략 

SURE_TO_DELETE=Are you sure to delete?
FAIL_TO_DELETE=Fail to Delete the post.
NOT_ALLOWED_BOARD=Not allowed Board
NO_POST=No post registered.
```

#### src/main/webapp/board/list.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<fmt:setBundle basename="bundle.board" />
<layout:main title="${title}" bodyClass="board-list">
	<h1 class='mtitle'>${boardInfo.boardNm}</h1>
	
	<div class='list_top'>
	<c:if test="${ total > 0 }">
		<div class='page_info'>
			Page : <fmt:formatNumber value="${page}" /> / Total : <fmt:formatNumber value="${total}" />
		</div>
	</c:if>
		<a href='../board/write?boardId=${boardInfo.boardId}' class='btn'><i class='xi-pen'></i> 글쓰기</a>
	</div>
	<ul class='posts'>
	<c:if test="${ empty posts }">
		<li class='no_post'>
			<fmt:message key="NO_POST" />
		</li>
	</c:if>
	<c:if test="${ !empty posts }">
		<c:forEach var="post" items="${posts}">
			<li class='post'>
				<a href='../board/view?id=${post.id}'>${post.subject}</a>
				<span class="post_info">
					${post.poster}(${ empty post.memId ? "Guest" : post.memId}) 
					/ <util:formatDate value="${post.regDt}" pattern="yyyy.MM.dd HH:mm" />
				</span>
			</li>
		</c:forEach>.
	</c:if>
	</ul>
	<!--  페이지네이션 출력  -->
	<util:pagination page="${page}" total="${total}" />
</layout:main>
```
* * * 
# 게시글 페이징 

#### src/main/webapp/board/list.jsp

```jsp

... 생략 

<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<fmt:setBundle basename="bundle.board" />
<layout:main title="${title}" bodyClass="board-list">
	
	... 생략 
	
	<!--  페이지네이션 출력  -->
	<util:pagination page="${page}" total="${total}" />
</layout:main>
```

#### src/main/webapp/WEB-INF/tags/utils/pagination.tag

```jsp

```

#### src/main/java/commons.Pagination.java

```java

```