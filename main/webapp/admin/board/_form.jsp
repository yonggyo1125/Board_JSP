<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="bundle.admin" />
<dl>
	<dt><fmt:message key="BOARD_ID" /></dt>
	<dd>
		<c:if test="${ ! empty board }">
			${board.boardId}
			<input type="hidden" name="boardId" value="${board.boardId}">
		</c:if>
		<c:if test="${ empty board }">
		<input type="text" name="boardId">
		</c:if>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_NM" /></dt>
	<dd>
		<input type="text" name="boardNm" value="${board.boardNm}"/>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_ISUSE" /></dt>
	<dd>
		<input type="radio" name="isUse" id="isUse_1" value="1" ${ empty board.isUse ? "": " checked"}>
		<label for="isUse_1"><fmt:message key="USE" /></label>
		<input type="radio" name="isUse" id="isUse_0" value="0" ${ empty board.isUse ? " checked": ""}>
		<label for="isUse_0"><fmt:message key="NOT_USE" /></label>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_NO_OF_ROWS" /></dt>
	<dd>
		<input type="number" name="noOfRows" value="${empty board.noOfRows ? 20 : board.noOfRows }"/>
	</dd>
</dl>
<dl>
	<dt><fmt:message key="BOARD_USE_COMMENT" /></dt>
	<dd>
		<input type="radio" name="useComment" id="useComment_1" value="1" ${ (empty board.useComment ||  board.useComment == 0) ? "": " checked"}>
		<label for="useComment_1"><fmt:message key="USE" /></label>
		<input type="radio" name="useComment" id="useComment_0" value="0" ${ (empty board.useComment ||  board.useComment == 0) ? " checked": ""}>
		<label for="useComment_0"><fmt:message key="NOT_USE" /></label>
	</dd>
</dl>