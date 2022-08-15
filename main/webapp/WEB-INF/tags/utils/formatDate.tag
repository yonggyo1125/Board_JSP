<%@ tag body-content="empty" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ tag import="java.time.*" %>
<%@ tag import="java.time.format.*" %>
<%@ attribute name="pattern" type="java.lang.String" %>
<%@ attribute name="value" type="java.time.temporal.Temporal" required="true" %>
<%
	if (pattern == null || pattern.isBlank()) {
		pattern = "yyyy-MM-dd HH:mm:ss";
	}

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
	if (value != null) { 
		out.print(formatter.format(value));
	}
%>
