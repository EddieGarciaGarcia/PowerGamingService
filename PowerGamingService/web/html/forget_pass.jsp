<%@include file="/html/common/header.jsp"%>
<section class="recovery">
	<form action="<%=ControllerPaths.USUARIO%>" method="post">
	<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.FORGOTPASS%>"/>
	<label><fmt:message key="forgetpassmensage" bundle="${traduccion}"/></label><br>
	<input type="email" name="<%=ParameterNames.EMAIL%>" 
				placeholder="usuario@ejemplo.com"
				value="<%=ParamsUtils.getParameter(request, ParameterNames.EMAIL)%>" required 
				minlength="8" maxlength="75"/><br>
	<input class="buttoncolor" type="submit" value="Enviar"/>
	</form>
	<h3><fmt:message key="forgetpassmensage2" bundle="${traduccion}"/></h3>
		<a class="alogin" href="<%=request.getContextPath()%><%=ViewPaths.REGISTRO%>"><fmt:message key="registrarse" bundle="${traduccion}"/></a>
</section>
<%@include file="/html/common/footer.jsp"%>