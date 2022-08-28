<%@ tag description="댓글 작성 및 목록 출력"  pageEncoding="UTF-8" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ tag import="java.util.List" %>
<%@ tag import="models.board.comment.CommentDao" %>
<%@ tag import="models.board.comment.CommentDto" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/utils" %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %> <!-- 게시글 번호  -->
<fmt:setBundle basename="bundle.comment" />
<% jspContext.setAttribute("newLine", "\n"); %>
<%
/** 등록된 게시글 목록 조회 */
if (id > 0) {
	CommentDao commentDao = CommentDao.getInstance();
	List<CommentDto> comments = commentDao.gets(id);
	request.setAttribute("comments", comments);
} // endif 
%>

<script src="<c:url value="/static/js/board/comment.js" />"></script>
<form id="frmComment" name="frmComment" method="post" action="<c:url value="/board/comment" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="boardDataId" value="${id}" /> 
	<div class="ctop_box">
		<fmt:message key="POSTER" />
		<input type="text" name="poster" value="${member.memNm}">
		<c:if test="${ empty member }"> / 
		<fmt:message key="PASSWORD" />
		<input type="password" name="guestPw">
		</c:if>
	</div>
	<!--// top_box -->
	<div class='comment_box'>
		<textarea name="content" placeholder="<fmt:message key="COMMENT_GUIDE" />"></textarea>
		<button type="submit" id="comment_submit">
			<fmt:message key="WRITE_COMMENT" />
		</button>
	</div>
</form>
<c:if test="${ !empty comments }">
<ul class='comment_list'>
	<c:forEach var="comment" items="${comments}">
		<li class='comment' id="comment_${comment.id}">
			<div class='ctop'>
				<div class='left'>
					${comment.poster}(${ empty comment.memId ? "Guest" :  comment.memId })
				 / <util:formatDate value="${comment.regDt}" pattern="yyyy.MM.dd HH:mm" />
				 </div>
				 <div class="right">
				 	<c:if test="${comment.memNo == 0 || ( !empty member && comment.memNo ==  member.memNo) }">
				 		<a href="<c:url value="/board/comment?id=${comment.id}" />">[<fmt:message key="EDIT_COMMENT" />]</a>
				 		<a href="<c:url value="/board/comment/delete?id=${comment.id}" />" onclick="confirm('<fmt:message key="SURE_TO_DELETE" />');">[<fmt:message key="DELETE_COMMENT" />]</a>
					 </c:if>
				 </div>
			</div>
			<!--// ctop  -->
			<div class="comment_content">
			${fn:replace(comment.content, newLine, '<br>')}
			</div>
		</li>
		<!--// comment -->
	</c:forEach>
</ul>
<!--// comment_list -->
</c:if>