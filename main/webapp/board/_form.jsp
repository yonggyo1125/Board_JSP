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
		<input type="text" name="poster" value="${ empty board.poster ? member.memNm : board.poster }" placeholder="<fmt:message key="POSTER" />" />
	</dd> 
</dl>
<c:if test="${ empty member || (board != null && board.id != 0 && board.memNo == 0) }">
<dl>
	<dt class='mobile_hidden'>
		<fmt:message key="GUEST_PW" />
	</dt>
	<dd class='mobile_fullwidth' >
		<input type="password" name=guestPw placeholder="<fmt:message key="GUEST_PW" />" />
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
		<ul class="attach_images">
		<c:if test="${board.imageFiles != null }">
			<c:forEach var="file" items="${board.imageFiles}">
				<li>
					<a href='../file/download?id=${file.id}'>${file.fileName}</a>
					<span class="remove" data-id=${file.id}>[X]</span>
				</li>
			</c:forEach>  
		</c:if>
		</ul>
	</dd>
</dl>
<dl>
	<dt>파일첨부</dt>
	<dd>
		<button type="button" id="add_files"><fmt:message key="ADD_FILES" /></button>
		<ul class="attach_files">
		<c:if test="${board.attachedFiles != null }">
			<c:forEach var="file" items="${board.attachedFiles}">
				<li>
					<a href='../file/download?id=${file.id}'>${file.fileName}</a>
					<span class="remove" data-id=${file.id}>[X]</span>
				</li>
			</c:forEach>  
		</c:if>
		</ul>
	</dd>
</dl>

<script type="text/html" id="tpl_file">
	<li>
		<a href='../file/download?id=#[id]'>#[fileName]</a>
		<span class="remove" data-id=#[id]>[X]</span>
	</li>
</script>