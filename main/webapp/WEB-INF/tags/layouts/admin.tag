<%@ tag description="관리자페이지 레이아웃" pageEncoding="UTF-8" %>
<%@ tag body-content="scriptless" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="bodyClass" type="java.lang.String" %>
<%
	request.setAttribute("addCss", "admin/style");
	request.setAttribute("addJs", "admin/common");
%>
<fmt:setBundle basename="bundle.common" />
<fmt:message var="title" key="ADMIN_MENU" />
<fmt:setBundle basename="bundle.admin" />
<layout:common title="${title}" bodyClass="admin">
	<jsp:attribute name="main_menu">
	<nav>
		<div>
			<a href="<c:url value="/" />"><fmt:message key="BACK_TO_BOARD" /></a>
			<a href="<c:url value="/admin/board" />"><fmt:message key="CREATE_BOARD" /></a>
			<a href="<c:url value="/admin/boards" />"><fmt:message key="BOARD_LIST" /></a>
		</div>
	</nav>
	</jsp:attribute>
	<jsp:body>
		<main>
			<jsp:doBody />
		</main>
	</jsp:body>
</layout:common>