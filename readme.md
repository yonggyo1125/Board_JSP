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
</layout:main>
```
* * * 
# 게시글 페이징 

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
	<util:pagination page="${page}" total="${total}" pageCnt="10" url="/board/list?boardId=${boardInfo.boardId}" />
</layout:main>
... 생략 

<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<fmt:setBundle basename="bundle.board" />
<layout:main title="${title}" bodyClass="board-list">
	
	... 생략 
	
	<!--  페이지네이션 출력  -->
	<util:pagination page="${page}" total="${total}" pageCnt="5" url="/board/list?boardId=${boardInfo.boardId}" />
</layout:main>
```

#### src/main/webapp/WEB-INF/tags/utils/pagination.tag

```jsp
<%@ tag description="페이지네이션" pageEncoding="UTF-8" %>
<%@ tag body-content="empty" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag import="commons.Pagination" %>
<%@ attribute name="page" type="java.lang.Integer" required="true" %>
<%@ attribute name="total" type="java.lang.Integer" required="true" %>
<%@ attribute name="pageCnt"  type="java.lang.Integer" %>
<%@ attribute name="url" type="java.lang.String" required="true" %>
<%
	pageCnt = pageCnt ==  null ? 10 : pageCnt;
	url = request.getContextPath() + "/" + url;
	if (url.indexOf("?") == -1) {
		url += "?page=";
	} else {
		url += "&page=";
	}
	
	Pagination pagination = new Pagination(page, total, pageCnt);

	out.print("<div class='pagination'>");
	if (!pagination.isFirstCnt()) {
		String prevUrl =url +  pagination.getPrev(); 
		String firstUrl = url + 1;
		out.println("<a href='" + firstUrl + "' class='first'>First</a>");
		out.println("<a href='" + prevUrl + "' class='prev'>Prev</a>");
	}
	for (int p : pagination.getPages()) {
		String pageUrl = url + p;
		StringBuffer sb = new StringBuffer("<a href='");
		sb.append(pageUrl);
		sb.append("' class='page");
		if (page == p) { // 현재 페이지인 경우 
			sb.append(" on");
		}
		sb.append("'>");
		sb.append("" + p);
		sb.append("</a>");
		out.println(sb.toString());
	}
	if (!pagination.isLastCnt()) {
		String nextUrl =url +  pagination.getNext();
		String lastUrl = url + pagination.getLastPage();
		out.println("<a href='" + nextUrl + "' class='next'>Next</a>");
		out.println("<a href='" + lastUrl + "' class='last'>Last</a>");
	}
	out.print("</div>");
%>
```

#### src/main/java/commons.Pagination.java

```java
package commons;

import java.util.List;
import java.util.ArrayList;

public class Pagination {
	
	private int page; // 현재 페이지
	private int total;  // 전체 레코드 수
	private int prev; // 전 구간 마지막 페이지 
	private int next; // 다음 구간 시작 페이지 
	private int lastPage; // 마지막 페이지
	private int pageCnt; // 구간별 페이지 갯수
	private boolean isFirstCnt; // 첫 번째 구간 여부
	private boolean isLastCnt; // 마지막 구간 여부 
	private List<Integer> pages; // 구간별 페이지 번호
		
	/**
	 * 페이지 계산
	 * 
	 * @param {int} page : 현재 페이지  
	 * @param {int} total : 전체 레코드 수 
	 * @param {int} pageCnt : 페이지 구간 갯수  
	 */
	public Pagination(int page, int total, int pageCnt) {
		if (total < 1) {
			return;
		}
		this.total = total;
		this.pageCnt = pageCnt < 1 ? 10 : pageCnt; // 페이지 구간이 0 이하 인 경우는 기본값 10 지정
		this.lastPage = (int)Math.ceil(total / (double)this.pageCnt);  // 마지막 페이지 
		page = page < 1 ? 1 : page; // 페이지가 0 이하 일 경우 1로 고정
		if (page > this.lastPage) page = this.lastPage; // 페이지가 마지막 페이지보다 크다면 마지막 페이지로 고정
		this.page = page;
		pages = new ArrayList<>();
		
		/** 페이지 구간 구하기 S */
		int cnt = (int)Math.ceil(this.page / (double)this.pageCnt) - 1; // 현재 페이지 구간 번호
		int lastCnt = (int)Math.ceil(this.lastPage / (double)this.pageCnt) - 1; // 마지막 페이지 구간 번호

		if (cnt == 0) this.isFirstCnt = true;  // 첫번째 페이지 구간 체크
		if (cnt == lastCnt) this.isLastCnt = true; // 마지막 페이지 구간 체크
		/** 페이지 구간 구하기 E */
		
		/** 구간별 페이지 번호 S */
		int start = cnt * this.pageCnt + 1;
		for (int i = start; i <  start + this.pageCnt; i++) {
			pages.add(i);
			
			if (i == this.lastPage) { // 마지막 페이지에서 멈춤  
				break;
			}
		}
		/** 구간별 페이지 번호 E */
		
		/** 전 구간 마지막 페이지 S */
		if (!this.isFirstCnt) {
			prev = cnt * this.pageCnt;
		}
		/** 전 구간 마지막 페이지  E */
		/** 다음 구간 시작 페이지 S */
		if (!this.isLastCnt) {
			next = (cnt + 1)  * this.pageCnt + 1; 
		}
		/** 다음 구간 시작 페이지  E */
	}
	
	public Pagination(int page, int total) {
		this(page, total, 10);
	}

	public int getPage() {
		return page;
	}

	public int getTotal() {
		return total;
	}

	public int getPrev() {
		return prev;
	}

	public int getNext() {
		return next;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getPageCnt() {
		return pageCnt;
	}

	public boolean isFirstCnt() {
		return isFirstCnt;
	}

	public boolean isLastCnt() {
		return isLastCnt;
	}

	public List<Integer> getPages() {
		return pages;
	}

	@Override
	public String toString() {
		return "Pagination [page=" + page + ", total=" + total + ", prev=" + prev
				+ ", next=" + next + ", lastPage=" + lastPage + ", pageCnt=" + pageCnt + ", isFirstCnt=" + isFirstCnt
				+ ", isLastCnt=" + isLastCnt + ", pages=" + pages + "]";
	}
}
```

#### src/webapp/static/css/style.css 

```css

... 생략 

/** 레이어팝업 공통 */
#layer_dim { background:rgba(0,0,0, 0.7); position: fixed; top: 0; left: 0; width: 100%; height: 100%; cursor: pointer; z-index: 10; }
#layer_popup { background: #ffffff; position: fixed; z-index: 11; overflow-x: hidden; overflow-y: auto; }
#layer_popup .popup_title { border-bottom: 1px solid #000000;  margin-bottom: 20px; font-size: 1.15rem; font-weight: bold; padding: 0 5px 13px; }
#layer_popup .layer_close { cursor: pointer; position: absolute; right: 10px; top: 10px; font-size: 1.8rem; }

/** 페이지네이션 */
.pagination { text-align: center; padding: 15px 0; }
.pagination  a { display: inline-block; padding: 5px 10px; border: 1px solid #212121; border-radius: 5px; }
.pagination  a.on { background-color: #212121; color: #ffffff; }

... 생략 

```

#### src/webapp/static/css/board/board.css 

```css

... 생략

@media all and (max-width: 500px) {
	.body-board-view .content img { width: 100%; }
}

/** 게시글 목록 */

.body-board-list .list_top { display: flex; justify-content: space-between; margin-bottom: 10px; align-items: center; }
.body-board-list .list_top .btn { border: 1px solid #212121; padding: 10px 15px; border-radius: 5px; }
.body-board-list .list_top .btn:hover { background-color: #212121; color: #ffffff; }
.body-board-list .list_top .btn:hover * { color: #ffffff; }

.body-board-list .posts li.post { display: flex; justify-content: space-between; width: 100%; border-bottom: 1px solid #d5d5d5; padding: 0 10px; height: 45px; align-items: strech; }
.body-board-list .posts li.post a, .body-board-list .posts .post_info { line-height: 43px; }
.body-board-list .posts li:first-of-type { border-top: 1px solid #d5d5d5; }
.body-board-list .no_post { text-align: center; padding: 100px 0; font-size: 1.3rem; border-bottom: 1px solid #d5d5d5;  margin-bottom: 15px; }

... 생략 

```

* * * 
# 구현 완료 화면

![image6](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/13%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EB%AA%A9%EB%A1%9D%2C%20%ED%8E%98%EC%9D%B4%EC%A7%95%2C%20%EC%82%AD%EC%A0%9C%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image6.png)

![image4](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/13%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EB%AA%A9%EB%A1%9D%2C%20%ED%8E%98%EC%9D%B4%EC%A7%95%2C%20%EC%82%AD%EC%A0%9C%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image4.png)

![image5](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/13%EC%9D%BC%EC%B0%A8(3h)%20-%20%EA%B2%8C%EC%8B%9C%EA%B8%80%20%EB%AA%A9%EB%A1%9D%2C%20%ED%8E%98%EC%9D%B4%EC%A7%95%2C%20%EC%82%AD%EC%A0%9C%20%EA%B8%B0%EB%8A%A5%20%EA%B5%AC%ED%98%84/images/image5.png)