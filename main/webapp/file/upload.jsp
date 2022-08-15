<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:popup title="파일 업로드" bodyClass="file-upload">
	<div class='mtitle'><fmt:message key="FILE_UPLOAD" /></div>
	
	<div class='stitle'><fmt:message key="FILE_SELECT" /></div>
	<div class='xi-plus' id='add_file'>
		<input type="file" name="file" id="file" multiple data-gid="${gid}"${empty param.isImageOnly ? "":" data-is-image-only='1'" }${empty param.updateDone ? "":" data-update-done='1'" }>
	</div>
		
	<%-- 업로드된 파일 목록 출력 --%>
	<ul id="uploaded_list"></ul>

	<script type="text/html" id="listTpl">
		<li>
			<a href="<c:url value="/file/download?id=" />#[id]">#[fileName]</a>
			<i class='delete xi-close' data-id="#[id]"></i>
		</li>
	</script>
</layout:popup>