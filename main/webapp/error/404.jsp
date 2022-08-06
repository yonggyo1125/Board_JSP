<%@ page contentType="text/html; charset=utf-8"  isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<c:set var="status" value="<%=response.getStatus()%>" />
<c:set var="method" value="<%=request.getMethod()%>" />
<c:set var="URI" value="<%=request.getRequestURI()%>" />
<layout:error title="404 PAGE NOT FOUND">
	${status} ${method} ${URI} ${exception.message}
</layout:error>