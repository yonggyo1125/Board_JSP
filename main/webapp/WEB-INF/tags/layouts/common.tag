<%@ tag description="공통 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="header" fragment="true" %>
<%@ attribute name="footer" fragment="true" %>
<%@ attribute name="main_menu"  fragment="true" %>
<%@ attribute name="title" type="java.lang.String" rtexprvalue="false" %>
<!DOCTYPE html> 
<html>
	<head>
		<meta charset="UTF-8"  />
		<meta name="viewport" content="user-scalable=yes, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
		<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/static/css/style.css' />" />
		<script src="<c:url  value='/static/js/common.js' />"></script>
		<c:if test="${empty title}">
			<title>${title}</title>
		</c:if>
		<c:if test="${!empty title}">
			<title>${title}</title>
		</c:if>
	</head>
	<body>
		<head>
			<jsp:invoke fragment="header" />
		</head>
		<jsp:invoke fragment="main_menu" />
		<main>
			<jsp:doBody />
		</main>
		<footer>
			<jsp:invoke fragment="footer" />
		</footer>
	</body>
</html>