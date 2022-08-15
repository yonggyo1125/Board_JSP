<%@ tag description="공통 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="header" fragment="true" %>
<%@ attribute name="footer" fragment="true" %>
<%@ attribute name="main_menu"  fragment="true" %>
<%@ attribute name="add_css" fragment="true" %>
<%@ attribute name="add_js" fragment="true" %>
<%@ attribute name="title" type="java.lang.String" %>
<%@ attribute name="bodyClass" type="java.lang.String" %>
<fmt:setBundle basename="bundle.common" />
<!DOCTYPE html> 
<html>
	<head>
		<meta charset="UTF-8"  />
		<meta name="viewport" content="user-scalable=yes, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
		<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/static/css/style.css' />" />
		 <%-- 추가 CSS --%>
		 <c:if test="${ !empty addCss }">
		 	<c:forEach var="css" items="${addCss}">
		 	<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/${css}.css" />" />
		 	</c:forEach>
		 </c:if>
		 <script src="<c:url  value='/static/js/layer.js' />"></script>
		<script src="<c:url  value='/static/js/common.js' />"></script>
		<%-- 추가 JS --%>
		<c:if test="${ !empty addJs }">
		 	<c:forEach var="js" items="${addJs}">
		 	<script src="<c:url value="/static/js/${js}.js" />"></script>
		 	</c:forEach>
		 </c:if>
		<c:if test="${empty title}">
			<title><fmt:message key="SITE_TITLE" /></title>
		</c:if>
		<c:if test="${!empty title}">
			<title>${title}</title>
		</c:if>
	</head>
	<body class="body-${empty bodyClass ?"main":bodyClass}">
		<jsp:invoke fragment="header" />
		<jsp:invoke fragment="main_menu" />
		<jsp:doBody />
		<jsp:invoke fragment="footer" />
	</body>
	<iframe id="ifrmProcess" name="ifrmProcess" width="100%" height="0" class="dn"></iframe>
</html>