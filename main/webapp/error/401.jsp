<%@ page contentType="text/html; charset=utf-8"  isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<c:set var="status" value="<%=response.getStatus()%>" />
<c:set var="method" value="<%=request.getMethod()%>" />
<% if (exception != null) exception.getStackTrace(); %>
<layout:error title="401 NOT AUTHORIZED">
	<div class="form_box mt20">
		<h1>${ empty statusCode ? status : statusCode }</h1> 
		<h2>${method} ${sessionScope.requestURL}</h2>
		<c:if test="${!empty exception }">
			<h3>${exception.message}</h3>
		</c:if>
		<c:if test="${ !empty errorMessage }">
			<h3>${errorMessage}</h3>
		</c:if>
		<div class='btn_grp mt50' >
			<a href="<c:url value="/" />">확인</a>
		</div>
	</div> <!--//  form_box -->
</layout:error>
