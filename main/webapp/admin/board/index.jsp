<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.admin" />
<layout:admin>
	<h2>
		<c:if test="${ empty board }">
		<fmt:message key="CREATE_BOARD" />
		</c:if>
		<c:if test="${ ! empty board }">
		<fmt:message key="UPDATE_BOARD" />
		</c:if>
	</h2>
	<form id="frmRegist" name="frmRegist" method="post" action="<c:url value="/admin/board" />" target="ifrmProcess" autocomplete="off">
		<jsp:include page="_form.jsp" />
		<c:if test="${ ! empty board }">
			<input type="hidden" name="mode" value="modify">
		</c:if>
		<div class='btn_grp'>
			<button type="reset"><fmt:message key="RESET" /></button>
			<button type="submit">
				<c:if test="${ empty board }">
				<fmt:message key="REGISTER" />
				</c:if>
				<c:if test="${ ! empty board }">
				<fmt:message key="MODIFY" />
				</c:if>
			
			</button>
		</div>
	</form>
</layout:admin>