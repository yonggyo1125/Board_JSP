<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<fmt:setBundle basename="bundle.admin" />
<layout:admin>
	<h2><fmt:message key="BOARD_LIST" /></h2>
	
	<form id="ifrmList" name="ifrmList" method="post" action="<c:url value='/admin/boards' />" target="ifrmProcess" autocomplete="off">
		<table class="table_rows">
			<thead>
				<tr>
					<th width='30'>
						<input type="checkbox" class="check_all" data-target-name="boardId" id='check_all'>
						<label for='check_all'></label>
					</th>
					<th width='150'><fmt:message key="BOARD_ID" /></th>
					<th width='150'><fmt:message key="BOARD_NM" /></th>
					<th width='100'><fmt:message key="BOARD_ISUSE" /></th>
					<th width='140'><fmt:message key="BOARD_NO_OF_ROWS" /></th>
					<th width='100'><fmt:message key="BOARD_USE_COMMENT" /></th>
					<th width='150'><fmt:message key="REG_DT" /></th>
					<th><fmt:message key="MOD_DT" /></th>
					<th width='150'></th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="board" items="${boards}" varStatus="status">
				<tr>
					<td align='center'>
						<input type="checkbox" name="boardId" value="${board.boardId}" id="boardid_${status.index}">
						<label for='boardid_${status.index}'></label>
					</td>
					<td align='center'>
						<a href="<c:url value="/board/list?boardId=" />${board.boardId}" target="_blank">${board.boardId}</a>
					</td>
					<td align='center'>${board.boardNm}</td>
					<td align='center'>
						<c:if test="${board.isUse == 1}">
							<fmt:message key="USE" />
						</c:if>
						<c:if test="${board.isUse != 1}">
							<fmt:message key="NOT_USE" />
						</c:if>
					</td>
					<td align='center'><fmt:formatNumber value="${board.noOfRows}" /></td>
					<td align='center'>
						<c:if test="${board.useComment == 1}">
							<fmt:message key="USE" />
						</c:if>
						<c:if test="${board.useComment != 1}">
							<fmt:message key="NOT_USE" />
						</c:if>
					</td>
					<td align='center'>
						<util:formatDate value="${board.regDt}" pattern="yyyy.MM.dd HH:mm" />
					</td>
					<td>
						<util:formatDate value="${board.modDt}" pattern="yyyy.MM.dd HH:mm" />
					</td>
					<td align='center'>
						<a href="<c:url value="/admin/board?boardId=${board.boardId}" />" class="sbtn">
							<fmt:message key="MODIFY" />
						</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class='btn_grp inline_block'>
			<button type="submit"  class='black' onclick="return confirm('<fmt:message key="SURE_TO_DELETE" />');"><fmt:message key="DELETE_SELECTED_BOARD" /></button>
		</div>
	</form>
	
</layout:admin>