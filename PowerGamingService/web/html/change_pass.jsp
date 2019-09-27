<%@include file="/html/common/header.jsp"%>
<section class="login-form">
	<form action="<%=ControllerPaths.USUARIO%>" method="post">
	<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.CHANGEPASS%>"/>
	<input type="hidden" name="<%=ParameterNames.EMAIL%>" value="<%=ParamsUtils.getParameter(request, ParameterNames.EMAIL)%>"/>
	<label><fmt:message key="cambiarpass" bundle="${traduccion}"/></label><br>
	<input type="password" name="<%=ParameterNames.PASSWORD%>" 
				placeholder="password" required 
				minlength="8" maxlength="75"/><br>
	<input class="buttoncolor" type="submit" value="<fmt:message key="enviar" bundle="${traduccion}"/>"/>
	</form>
</section>
<%@include file="/html/common/footer.jsp"%>