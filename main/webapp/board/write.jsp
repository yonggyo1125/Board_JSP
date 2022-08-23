<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.board" />
<fmt:message var="title" key="BOARD_WRITE" />
<layout:main title="${title}" bodyClass="board_form">
<h1 class="mtitle">${boardInfo.boardNm}</h1>	
<form id="frmRegist" class='form_box form_box2' name="frmRegist" method="post" action="<c:url value="/board/write" />" target="ifrmProcess">
	<input type="hidden" name="boardId" value="${board.boardId}" />
	<jsp:include page="_form.jsp" />
	<div class='btn_grp'>
		<button type="reset"><fmt:message key="RESET" /></button>
		<button type="submit"><fmt:message key="WRITE" /></button>
	</div>
</form>
</layout:main>