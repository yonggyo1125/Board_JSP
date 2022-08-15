<%@ tag description="메인 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="bodyClass" type="java.lang.String" %>
<fmt:setBundle basename="bundle.common" />
<layout:common title="${title}" bodyClass="${bodyClass}">
	<jsp:attribute name="header">
		<header>
		<section class='top_menu'>
			<div class="layout_width">
				<c:if test="${ empty member }">
					<a href="<c:url value="/member/join" />"><fmt:message key="MEMBER_JOIN" /></a>
					<a href="<c:url value="/member/login" />"><fmt:message key="MEMBER_LOGIN" /></a>
				</c:if>
				<c:if test="${ ! empty member }">
					<a href="<c:url value="/mypage" />"><fmt:message key="MYPAGE" /></a>
					<a href="<c:url value="/member/logout" />"><fmt:message key="MEMBER_LOGOUT" /></a>
					<c:if test="${ member.memType == 'admin' }">
						<a href="<c:url value="/admin" />"><fmt:message key="ADMIN_MENU" /></a>
					</c:if>
				</c:if>
			</div>
		</section>
		<section class="logo">
			<a href="<c:url value="/" />">
				<fmt:message key="SITE_TITLE" />
			</a>
		</section>
		</header>
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
		<footer>
		&copy;CopyRight ...
		</footer>
	</jsp:attribute>
	<jsp:body>
		<main>
			<div class='layout_width'>
				<jsp:doBody />
			</div>
		</main>
	</jsp:body>
</layout:common> 