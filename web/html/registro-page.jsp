<%@ page import="java.util.List" %>
<%@ page import="com.eddie.model.*" %>
<%@include file="/html/common/header.jsp"%>

<section class="registro">
	<h2><fmt:message key="RegistroUsuario" bundle="${traduccion}"/></h2>
	<%@include file="/html/common/action-error.jsp"%>
	<form action="<%=ControllerPaths.USUARIO%>" method="post">
	<%
				
				List<String> parameterErrors = errors.getErrors(ParameterNames.REGISTRO);
				for (String error: parameterErrors) {
					%><li><fmt:message key="<%=error%>" bundle="${traduccion}"/></li><% 
				}
			%>
	<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.REGISTRO%>" />
		<label><fmt:message key="name" bundle="${traduccion}"/>*: </label><input type="text" name="<%=ParameterNames.NOMBRE%>" required minlength="3" maxlength="45"/>
		<label><fmt:message key="apellido1" bundle="${traduccion}"/>: </label><input type="text" name="<%=ParameterNames.APELLIDO1%>" minlength="3" maxlength="45"/>
		<label><fmt:message key="apellido2" bundle="${traduccion}"/>: </label><input type="text" name="<%=ParameterNames.APELLIDO2%>" minlength="3" maxlength="45"/><br>
		<label>Email*: </label><input type="email" name="<%=ParameterNames.EMAIL%>" 
				placeholder="usuario@ejemplo.com"
				value="<%=ParamsUtils.getParameter(request, ParameterNames.EMAIL)%>" required minlength="8" maxlength="75"/><br>
		<label><fmt:message key="telefono" bundle="${traduccion}"/>: </label><input type="text" name="<%=ParameterNames.TELEFONO%>" minlength="9" maxlength="30"><br>
		<label><fmt:message key="password" bundle="${traduccion}"/>*: </label><input type="password" name="<%=ParameterNames.PASSWORD%>"required minlength="8" maxlength="30"/><br>
		<label><fmt:message key="fechaNacimiento" bundle="${traduccion}"/>*: </label><input type="date" name="<%=ParameterNames.FECHANACIMIENTO%>" required/><br>
		<label><fmt:message key="genero" bundle="${traduccion}"/>*: </label><select type="text" name="<%=ParameterNames.GENERO%>" required>
			<option value="H"><fmt:message key="hombre" bundle="${traduccion}"/></option>
			<option value="M"><fmt:message key="mujer" bundle="${traduccion}"/></option>
			<option value="O"><fmt:message key="otro" bundle="${traduccion}"/></option>
		</select><br>
		<label><fmt:message key="obligatorio" bundle="${traduccion}"></fmt:message></label><br>
		
		
		<input class="buttoncolor" type="submit" value="Registrar"/>	
	</form>
</section>

<%@include file="/html/common/footer.jsp"%>
