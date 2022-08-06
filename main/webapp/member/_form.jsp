<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="bundle.common" />
<dl>
	<dt><fmt:message key="MEMBER_MEMID" /></dt>
	<dd>
		<input type="text" name="memId" placeholder="<fmt:message key="MEMBER_MEMID" />">
	</dd>
</dl>
<dl>
	<dt><fmt:message key="MEMBER_MEMPW" /></dt>
	<dd>
		<input type="password" name="memPw" placeholder="<fmt:message key="MEMBER_MEMPW" />">
	</dd>
</dl>
<dl>
	<dt><fmt:message key="MEMBER_MEMPWRE" /></dt>
	<dd>
		<input type="password" name="memPwRe" placeholder="<fmt:message key="MEMBER_MEMPW" />">
	</dd>
</dl>
<dl>
	<dt><fmt:message key="MEMBER_MEMNM" /></dt>
	<dd>
		<input type="text" name="memNm" placeholder="<fmt:message key="MEMBER_MEMNM" />">
	</dd>
</dl>
<dl>
	<dt><fmt:message key="MEMBER_EMAIL" /></dt>
	<dd>
		<input type="email" name="email" placeholder="<fmt:message key="MEMBER_EMAIL" />">
	</dd>
</dl>
<dl>
	<dt><fmt:message key="MEMBER_MOBILE" /></dt>
	<dd>
		<input type="text" name="mobile" placeholder="<fmt:message key="MEMBER_MOBILE" />">
	</dd>
</dl>