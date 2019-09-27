<%@ page import="java.util.List" %>

<%@include file="/html/common/header.jsp"%>
<div id="login-form">
		<h3><fmt:message key="welcome" bundle="${traduccion}"/>:</h3>	
		<form action="<%=ControllerPaths.USUARIO%>" method="post">
			
		<%@include file="/html/common/action-error.jsp"%>
			
			<%
				List<String> parameterErrors = errors.getErrors(ParameterNames.ACTION);
				for (String error: parameterErrors) {
					%><li><fmt:message key="<%=error%>" bundle="${traduccion}"/></li><%
				}
			%>
		
			<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.LOGIN%>"/>
			<label><fmt:message key="loginmensage" bundle="${traduccion}"/>:</label>
			
			<%
				List<String> parameterErrorsEmail = errors.getErrors(ParameterNames.EMAIL);
				for (String errorEmail: parameterErrorsEmail) {
					%><li><fmt:message key="<%=errorEmail%>" bundle="${traduccion}"/></li><%
				}
			%>
			<input type="email" name="<%=ParameterNames.EMAIL%>" 
				placeholder="usuario@ejemplo.com"
				value="<%=ParamsUtils.getParameter(request, ParameterNames.EMAIL)%>" required 
				minlength="8" maxlength="75"/>
			
			<%
				List<String> parameterErrorsPass = errors.getErrors(ParameterNames.PASSWORD);
				for (String errorPass: parameterErrorsPass) {
					%><li><fmt:message key="<%=errorPass%>" bundle="${traduccion}"/></li><%
				}
			%>
			<br><label><fmt:message key="loginmensage2" bundle="${traduccion}"/>:</label>		
			<input type="password" name="<%=ParameterNames.PASSWORD%>" required minlength="8" maxlength="30"/>
			<br><input class="buttoncolor" type="submit" value="Entrar" />
		</form>
		<a class="alogin" href="<%=request.getContextPath()%><%=ViewPaths.FORGETPASS%>"><fmt:message key="forgotpass" bundle="${traduccion}"/></a>
		<h3><fmt:message key="forgetpassmensage2" bundle="${traduccion}"/></h3>
		<a class="alogin" href="<%=request.getContextPath()%><%=ViewPaths.REGISTRO%>"><fmt:message key="registrarse" bundle="${traduccion}"/></a>
</div>

<%@include file="/html/common/footer.jsp"%>
