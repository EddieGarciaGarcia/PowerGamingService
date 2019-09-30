<%@ page import="com.eddie.utils.util.*, com.eddie.utils.model.*, com.eddie.controller.*" %>
<%
	Errors errors = (Errors) request.getAttribute(AttributeNames.ERRORS);
	if (errors == null) errors = new Errors();
%>
