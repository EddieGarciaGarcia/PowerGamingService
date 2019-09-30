<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.eddie.utils.util.*, com.eddie.utils.model.*" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope['user-locale']}" scope="session"/>
<fmt:setBundle basename = "resources.messages" var="traduccion" scope="session"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="heigth:100%; width:100%;">
<head>
	<title>Power Gaming</title>
	<link rel="icon" href="<%=request.getContextPath()%>/imgs/powergaming.ico">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="<%=request.getContextPath()%>/html/css/index.css" rel="stylesheet" type="text/css">
	<link href="https://fonts.googleapis.com/css?family=Oswald" rel="stylesheet"> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/html/js/jQuery3.1.1.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/html/js/index.js"></script>   
</head>

<body>

<%
List<Formato> formato= (List<Formato>) request.getAttribute(AttributeNames.FORMATO_RESULTADOS);
List<TipoEdicion> tipoEdicion= (List<TipoEdicion>) request.getAttribute(AttributeNames.TIPOEDICION_RESULTADOS);
%>
	

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
                    	<%@include file="/html/common/user-menu.jsp"%>
                    </ul>
                </nav>
                <div class="carrito">
                	<%@include file="/html/carrito.jsp"%>
                </div>
     </header>
	
