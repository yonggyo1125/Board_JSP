<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.comment" />
<fmt:message var="title" key="UPDATE_COMMENT"  />
<layout:main title="${title}" bodyClass="comment-form">
<h1 class='mtitle'>${title}</h1>
<form id="frmComment" class="form_box" name="frmComment" method="post" action="<c:url value="/board/comment" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="mode" value="update">
	<input type="hidden" name="id" value="${comment.id}"> 
	<dl>
		<dt><fmt:message key="POSTER" /></dt>
		<dd><input type="text" name="poster" value="${comment.poster}"></dd>
	</dl>
	
	<c:if test="${ comment.memNo == 0 }">
	<dl> 
		<dt><fmt:message key="PASSWORD" /></dt>
		<dd><input type="password" name="guestPw"></dd>
	</dl>
	</c:if>
	<textarea name="content" placeholder="<fmt:message key="COMMENT_GUIDE" />">${comment.content}</textarea>
	<div class='btn_grp mb10'>
	<button type="submit" id="comment_submit" class='black'>
		<fmt:message key="UPDATE_COMMENT" />
	</button>
	</div>
</form>
</layout:main>