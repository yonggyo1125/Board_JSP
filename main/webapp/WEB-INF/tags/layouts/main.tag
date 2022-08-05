<%@ tag description="메인 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:common>
	<jsp:attribute name="header">
		<section class='top_menu'>
			<div class="layout_width">
				<a href="<c:url value="/join" />"><fmt:message key="MEMBER_JOIN" /></a>
				<a href="<c:url value="/login" />"><fmt:message key="MEMBER_LOGIN" /></a>
			</div>
		</section>
		<section class="logo">
			<a href="<c:url value="/" />">
				<fmt:message key="SITE_TITLE" />
			</a>
		</section>
		
	</jsp:attribute>
	<jsp:attribute name="main_menu">
	<nav>
		<div class='layout_width'>
			<a href='#'>메뉴1</a>
			<a href='#'>메뉴2</a>
			<a href='#'>메뉴3</a>
			<a href='#'>메뉴4</a>
		</div>
	</nav>
	</jsp:attribute>
	<jsp:attribute name="footer">
		&copy;CopyRight ...
	</jsp:attribute>
	<jsp:body>
		<div class='layout_width'>
			<jsp:doBody />
		</div>
	</jsp:body>
</layout:common> 