<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:main title="로그인" bodyClass="login">
	<h1 class='mtitle'><fmt:message key="MEMBER_LOGIN" /></h1>
	<form id="frmLogin" name="frmLogin" class="form_box" method="post" action="<c:url value="/member/login" />" target="ifrmProcess" autocomplete="off">
		<dl>
			<dt class='mobile_hidden'><fmt:message key="MEMBER_MEMID" /></dt>
			<dd class="mobile_fullwidth">
				<input type="text" name="memId" placeholder="<fmt:message key="MEMBER_MEMID" />" />
			</dd>
		</dl>
		<dl>
			<dt class='mobile_hidden'><fmt:message key="MEMBER_MEMPW" /></dt>
			<dd class="mobile_fullwidth">
				<input type="password" name="memPw" placeholder="<fmt:message key="MEMBER_MEMPW" />" />
			</dd>
		</dl>
		<div class="btn_grp mt20">
			<button type="submit" class="black"><fmt:message key="MEMBER_LOGIN" /></button>
		</div>
	</form>
</layout:main>