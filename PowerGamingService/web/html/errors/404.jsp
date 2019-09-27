<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.eddie.service.*, com.eddie.controller.*,com.eddie.utils.util.*, com.eddie.model.Usuario" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope['user-locale']}" scope="session"/>
<fmt:setBundle basename = "resources.messages" var="traduccion" scope="session"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="heigth:100%; width:100%;">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<%=request.getContextPath()%>/html/css/index.css" rel="stylesheet" type="text/css">
    <script type="text/Javascript" href="<%=request.getContextPath()%>/html/js/index.js"></script>
	<script type="text/Javascript" href="<%=request.getContextPath()%>/html/js/fucniones.js"></script>
</head>

<body>


	<header class="menu"> 
                <img class="menufig" src="<%=request.getContextPath()%>/imgs/powergaminglogo.png"></img>
                <div class="menuimg">
                    <a  href="<%=ControllerPaths.USUARIO%>?<%=ParameterNames.ACTION%>=<%=Actions.CAMBIAR_IDIOMA%>&amp;<%=ParameterNames.LOCALE%>=en">
                    <img id="img1" src="<%=request.getContextPath()%>/imgs/en-us.png"></img></a>
                    <a  href="<%=ControllerPaths.USUARIO%>?<%=ParameterNames.ACTION%>=<%=Actions.CAMBIAR_IDIOMA%>&amp;<%=ParameterNames.LOCALE%>=es">
                    <img id="img2" src="<%=request.getContextPath()%>/imgs/es.png"></img></a>
                </div>
                <nav>
                    <ul class="nav">
                    	<li><a href="<%=request.getContextPath()%><%=ViewPaths.INICIO%>"><fmt:message key="inicio" bundle="${traduccion}"></fmt:message></a></li>
                    </ul>
                </nav>
               
     </header>
	<section class="error">
	<h1>Página no encontrada</h1>
	<a class="alogin" href="/PowerGamingWeb">Continuar a la Home</a>
	</section>
	<footer class="footer">
            <p>©powergaming<br>
            <fmt:message key="contacto" bundle="${traduccion}"></fmt:message>:<br>
            Email: powergaming2019@gmail.com<br>
            <fmt:message key="telefono" bundle="${traduccion}"></fmt:message> 654634543<br>
            <fmt:message key="mensagefooter" bundle="${traduccion}"></fmt:message>
            <a  href="<%=request.getContextPath()%><%=ViewPaths.CONTACTO%>"><button><fmt:message key="contacto" bundle="${traduccion}"></fmt:message></button></a>
            </p>
    </footer>
	</body>
</html>
