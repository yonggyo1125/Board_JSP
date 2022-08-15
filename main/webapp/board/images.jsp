<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<c:if test="${empty param.isAjax}">
<fmt:setBundle basename="bundle.imageboard" />
<fmt:message var="title" key="BOARD_TITLE" />
<layout:main title="${title}" bodyClass="image_board">
	<h1 class='mtitle'>${title}</h1>
	<div class='ar'>
		<button type="button" id="add_images"  class="sbtn">
			<i class='xi-plus'></i> <fmt:message key="ADD_IMAGES" />
		</button>
	</div>
	<div id="image_posts"></div>
</layout:main>
</c:if>
<c:if test="${!empty param.isAjax}">
<c:if test="${images != null}">
<c:forEach var="image" items="${images}">
	<div class='image_box' data-id="${image.id}">
		<img src="<util:fileurl id='${image.id}' />" />
	</div>
</c:forEach>
</c:if>
</c:if>