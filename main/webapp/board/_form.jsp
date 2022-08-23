<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="bundle.board" />
<input type="hidden" name="gid" value="${board.gid}">
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="SUBJECT" />
	</dt>
	<dd class='mobile_fullwidth' >
		<input type="text" name="subject" value="${board.subject}" placeholder="<fmt:message key="SUBJECT" />" />
	</dd>
</dl>
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="POSTER" />
	</dt>
	<dd class='mobile_fullwidth' >
		<input type="text" name="poster" value="${board.poster}" placeholder="<fmt:message key="POSTER" />" />
	</dd> 
</dl>
<c:if test="${ empty member }">
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="GUEST_PW" />
	</dt>
	<dd class='mobile_fullwidth' >
		<input type="text" name=guestPw value="${board.guestPw}" placeholder="<fmt:message key="GUEST_PW" />" />
	</dd>
</dl>
</c:if>
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="CONTENT" />
	</dt>
	<dd class='mobile_fullwidth' >
		<textarea name="content" id="content" placeholder="<fmt:message key="CONTENT" />">${board.content}</textarea>
		<button type="button" id="add_images"><fmt:message key="ADD_IMAGES" /></button>
		<ul class="attach_images"></ul>
	</dd>
</dl>
<dl>
	<dt>파일첨부</dt>
	<dd>
		<button type="button" id="add_files"><fmt:message key="ADD_FILES" /></button>
		<ul class="attach_files"></ul>
	</dd>
</dl>

<script type="text/html" id="tpl_file">
	<li>
		<a href='../file/download?id=#[id]'>#[fileName]</a>
		<span class="remove" data-id=#[id]>[X]</span>
	</li>
</script>