<%@ page import="com.eddie.service.*, com.eddie.controller.*,java.util.List, com.eddie.model.*" %>
<%@include file="/html/common/header.jsp"%>
<%@include file="/html/common/buscador.jsp"%>

<h1><fmt:message key="welcome" bundle="${traduccion}"/></h1>
<section class="sectionjuegos">
	<%
		List<Juego> todos= (List<Juego>) request.getAttribute(AttributeNames.RESULTADOS_TODOS);
		List<Juego> valoracion= (List<Juego>) request.getAttribute(AttributeNames.RESULTADOS_TODOS_VALOR);
	%>
	
		<%		
		if (todos!=null && !todos.isEmpty()) {
			%>
			<h2><fmt:message key="indextitle" bundle="${traduccion}"/></h2>
			<%
			for (Juego resultado: todos) {
				%>
					<div>
					<a class="a2" href="<%=ControllerPaths.JUEGO%>?
							<%=ParameterNames.ACTION%>=<%=Actions.JUEGO%>&amp;<%=ParameterNames.ID%>=
							<%=resultado.getIdJuego()%>">
						<img src="<%=request.getContextPath()%>/imgs/icojuego/<%=(Integer)resultado.getIdJuego()%>.jpg"></img><p class="pjuego"><%=resultado.getNombre()%></p>
					</a>
					
					</div>
					<%}%>
					<p><center>
						<c:url var="urlBase" value="/inicio" scope="page"></c:url>
					
						<!-- A la anterior pagina -->
						<c:if test="${page > 1}">
							<a class="paginacion" href="${urlBase}?page=${page - 1}">
								<fmt:message key="anterior" bundle="${traduccion}"/>
							</a>
							&nbsp;&nbsp;
						</c:if>
					
						<c:if test="${totalPages > 1}">	
					
							<c:if test="${firstPagedPage > 2}">
								<a class="paginacion" href="${urlBase}?page=1"><b>1</b></a><b>&nbsp;.&nbsp;.&nbsp;.&nbsp;</b> 
							</c:if>
						
							<c:forEach begin="${firstPagedPage}" end="${lastPagedPage}" var="i">
								<c:choose>
								  <c:when test="${page != i}">
										&nbsp;<a class="paginacion" href="${urlBase}?page=${i}"><b>${i}</b></a>&nbsp;
								  </c:when>
								  <c:otherwise>
										&nbsp;<b>${i}</b>&nbsp;
								  </c:otherwise>
								</c:choose>
							</c:forEach>
					
							<c:if test="${lastPagedPage < totalPages-1}">
								<b>&nbsp;.&nbsp;.&nbsp;.&nbsp;</b><a class="paginacion" href="${urlBase}?page=${totalPages}"><b>${totalPages}</b></a>
							</c:if>	
					
						</c:if>
					
						<!-- A la siguiente página -->	
						<c:if test="${page < totalPages}">
							&nbsp;&nbsp;		
							<a class="paginacion" href="${urlBase}?page=${page + 1}">
								<fmt:message key="siguiente" bundle="${traduccion}"/>
							</a>			
						</c:if>	
					
						</center></p>
					
			<%}%>
	</section>
	<aside class="sectionjuegos">
		<%		
		if (todos!=null && !todos.isEmpty()) {
			%>
			<h2><fmt:message key="indextitle2" bundle="${traduccion}"/></h2>
			<%
			for (Juego valor: valoracion) {
				%>
					<div>
					<a class="a2" href="<%=ControllerPaths.JUEGO%>?
							<%=ParameterNames.ACTION%>=<%=Actions.JUEGO%>&amp;<%=ParameterNames.ID%>=
							<%=valor.getIdJuego()%>">
						<img src="<%=request.getContextPath()%>/imgs/icojuego/<%=(Integer)valor.getIdJuego()%>.jpg"></img><p class="pjuego"><%=valor.getNombre()%></p>
					</a>
					
					</div>
					<%}%>
			<%}%>
	</aside>
</section>
<%@include file="/html/common/footer.jsp"%>

