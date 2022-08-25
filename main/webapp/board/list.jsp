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
	<util:pagination page="${page}" total="${total}" pageCnt="5" url="/board/list?boardId=${boardInfo.boardId}" />
</layout:main>