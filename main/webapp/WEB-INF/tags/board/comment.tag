<%@ tag description="댓글 작성 및 목록 출력"  pageEncoding="UTF-8" %>
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %> <!-- 게시글 번호  -->
<form id="frmComment" name="frmComment" method="post" action="<c:url value="/board/comment" />" target="ifrmProcess" autocomplete="off">
	<input type="hidden" name="id" value="${id}" /> 
	<div class="top_box">
		작성자 : <input type="text" name="poster" value="${member.memNm}">
		<c:if test="${ empty member }">
		/ 비밀번호 : <input type="password" name="guestPw">
		</c:if>
	</div>
	<!--// top_box -->
	<div class='comment_box'>
		<textarea name="content"></textarea>
		<button type="submit" id="comment_submit">댓글작성</button>
	</div>
</form>