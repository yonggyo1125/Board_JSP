<%@ tag description="메인 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:common>
	<jsp:attribute name="header">
		<div class="logo">게시판</div>
	</jsp:attribute>
	<jsp:attribute name="main_menu">
	<nav>
		<a href='#'>메뉴1</a>
		<a href='#'>메뉴2</a>
		<a href='#'>메뉴3</a>
		<a href='#'>메뉴4</a>
	</nav>
	</jsp:attribute>
	<jsp:attribute name="footer">
		&copy;CopyRight ...
	</jsp:attribute>
	<jsp:body>
		<jsp:doBody />
	</jsp:body>
</layout:common> 