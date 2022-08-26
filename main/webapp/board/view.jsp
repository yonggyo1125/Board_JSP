<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<%@ taglib prefix="board" tagdir="/WEB-INF/tags/board" %>
<fmt:setBundle basename="bundle.board" />
<layout:main title="${title}" bodyClass="board-view">
	<div class='top_box'>
		<div class='left'>${boardInfo.boardNm}</div>
		<div class='right'>
			${board.poster}(${ empty board.memId ? "Guest" :  board.memId})
			/ <util:formatDate value="${board.regDt}" pattern="yyyy.MM.dd HH:mm" />
		</div>
	</div>
	<!--// top_box -->
	<div class='content_box'>
		<div class='subject'>${board.subject}</div>
		<div class='content'>${board.content}</div>
	</div>
	<!--// content_box -->
	<c:if test="${ ! empty board.attachedFiles }">
	<!--  첨부파일 S  -->
	<ul class='attach_files'>
	<c:forEach var="file" items="${board.attachedFiles}" varStatus="status">
		<li>
			<a href='../file/download?id=${file.id}' target="ifrmProcess">${status.count}. ${file.fileName}</a>
		</li>
	</c:forEach>
	</ul>
	<!--  첨부파일 E  -->
	</c:if>
	<div class='board_btns'>
		<c:if test="${board.memNo == 0 || (member != null && board.memNo == member.memNo) }">
			<fmt:message var="sureToDelete" key="SURE_TO_DELETE" />
			<a href='../board/delete?id=${board.id}' onclick="return confirm('${sureToDelete}');">
				<i class='xi-trash'></i>
				<fmt:message key="DELETE" />
			</a>
			<a href='../board/update?id=${board.id}'>
				<i class='xi-check'></i>
				<fmt:message key="MODIFY" />
			</a>
		</c:if>
		<a href='../board/write?boardId=${board.boardId}'>
			<i class='xi-pen'></i>
			<fmt:message key="WRITE" />
		</a>
		<a href='../board/list?boardId=${board.boardId}'>
			<i class='xi-list'></i>
			<fmt:message key="LIST" />
		</a>
	</div>
	<c:if test="${boardInfo.useComment == 1 }">
		<!--  댓글 영역 -->
		<board:comment id="${board.id}"/>
	</c:if>
</layout:main>