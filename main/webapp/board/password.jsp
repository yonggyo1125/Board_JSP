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