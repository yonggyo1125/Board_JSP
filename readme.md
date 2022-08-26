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

#### src/main/webapp/WEB-INF/tags/board/comment.tag 

```jsp
<%@ tag description="댓글 작성 및 목록 출력"  pageEncoding="UTF-8" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %> <!-- 게시글 번호  -->
<form id="frmComment" name="frmComment" method="post" action="<c:url value="/board/comment" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="id" value="${id}" /> 
	<div class="top_box">
		작성자 : <input type="text" name="poster" value="${member.memNm}">
		<c:if test="${ empty member }">
		/ 비밀번호 : <input type="password" name="guestPw">
		</c:if>
	</div>
	<!--// top_box -->
	<div class='comment_box'>
		<textarea name="content"></textarea>
		<button type="submit" id="comment_submit">댓글작성</button>
	</div>
</form>
```