<%@ page import="java.util.List, com.eddie.model.*" %>
<%@include file="/html/common/header.jsp"%>
<section style="margin: 150px;">
	<%if(u==null){ %>
		<form action="<%=ControllerPaths.USUARIO%>" method="post">
			<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.MENSAGE%>"/>
			<p><fmt:message key="contactomensage" bundle="${traduccion}"/></p>
			<input type="email" name="<%=ParameterNames.EMAIL%>" 
				placeholder="usuario@ejemplo.com"
				value="<%=ParamsUtils.getParameter(request, ParameterNames.EMAIL)%>" required 
				minlength="8" maxlength="75"/>
			<p><fmt:message key="contactomensage2" bundle="${traduccion}"/></p>
			<textarea name="<%=ParameterNames.MENSAGE %>" rows="10" cols="50"></textarea><br>
			<input type="submit" value="<fmt:message key="enviar" bundle="${traduccion}"/>" />
		</form>
	<%}if(u!=null){ %>
		<form action="<%=ControllerPaths.USUARIO%>" method="post">
			<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.MENSAGE%>"/>
			<p><fmt:message key="contactomensage3" bundle="${traduccion}"/> <%=u.getNombreUser()%><br>
			<fmt:message key="contactomensage2" bundle="${traduccion}"/></p>
			<textarea name="<%=ParameterNames.MENSAGE %>" rows="10" cols="50"></textarea><br>
			<input class="buttoncolor" type="submit" value="<fmt:message key="enviar" bundle="${traduccion}"/>" />
		</form>
	<%}%>
</section>
<%@include file="/html/common/footer.jsp"%>
