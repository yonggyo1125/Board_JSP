<%@ tag description="파일 업로드 URL" pageEncoding="UTF-8" body-content="empty" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag import="java.io.File" %>
<%@ attribute name="id" type="java.lang.Integer" required="true" %>
<%
	String url = request.getContextPath() + File.separator + "static" + File.separator + "upload" + File.separator + String.valueOf(id % 10) + File.separator + "" + id;
	out.print(url);
%>