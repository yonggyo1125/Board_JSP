<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:main title="회원가입" bodyClass="member">
<h1 class='mtitle'>회원가입</h1>
<form id="frmRegist" class="form_box" name="frmRegist" method="post" action="<c:url value="/member/join" />" target="ifrmProcess" autocomplete="off">
	<jsp:include page="_form.jsp" />
	<div class='ac mt20'>
	 	<input type="checkbox" name="isAgree" value="1" id="isAgree">
	 	<label for="isAgree"><fmt:message key="MEMBER_JOIN_TERM" /></label>
	 </div>
	<div class="btn_grp mt20">
		<button type="reset"><fmt:message key="MEMBER_RESET" /></button>
		<button type="submit"><fmt:message key="MEMBER_JOIN" /></button>
	</div>
</form>
</layout:main>