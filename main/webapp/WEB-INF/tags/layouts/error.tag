<%@ tag description="에러페이지 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="bodyClass" type="java.lang.String" %>
<fmt:setBundle basename="bundle.common" />
<layout:main title="${title}" bodyClass="error">
	<jsp:body>
		<jsp:doBody />
	</jsp:body>
</layout:main>