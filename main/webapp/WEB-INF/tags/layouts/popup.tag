<%@ tag description="팝업페이지 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="bodyClass" type="java.lang.String" %>
<layout:common title="${title}" bodyClass="${bodyClass}">
 	<jsp:body>
 		<jsp:doBody />
 	</jsp:body>
</layout:common>