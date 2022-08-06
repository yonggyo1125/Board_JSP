<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:main title="회원가입">
<form id="frmRegist" name="frmRegist" method="post" action="<c:url value="/member/join" />" target="ifrmProcess" autocomplete="off">
	<jsp:include page="_form.jsp" />
	<div class="btn_grp">
		<button type="reset"><fmt:message key="MEMBER_RESET" /></button>
		<button type="submit"><fmt:message key="MEMBER_JOIN" /></button>
	</div>
</form>
</layout:main>