<%@ page import="com.eddie.service.*, com.eddie.controller.*,java.util.List, com.eddie.model.*" %>

<%@include file="/html/common/header.jsp"%>
<%@include file="/html/common/buscador.jsp"%>
<section class="biblioteca">

<c:set var="resultados" value="${requestScope.juegos_resultados}" />

			<h1>
			<c:if test="${not empty total}">
				<fmt:message key="resultados" bundle="${traduccion}">
					<fmt:param value="${total}"></fmt:param>
				</fmt:message>
			</c:if>
			</h1> 
	 	<c:if test="${not empty resultados}">
			<c:forEach items="${resultados}" var="resultado">
				<div class="divjuegobiblioteca">
					<c:url var="urljuegodetalle" scope="page" value="/juego">
						<c:param name="action" value="juego"/>
						<c:param name="id" value="${resultado.idJuego}"/>
					</c:url>
				<a href="${urljuegodetalle}"><img src="${pageContext.request.contextPath}/imgs/icojuego/${resultado.idJuego}.jpg"><p class="pjuego">${resultado.nombre}</p></a>
				</div>
				
			<div class="divbuscador">
				<div class="addCarrito">
					<form action="/PowerGamingWeb/carrito" method="post">
						<input type="hidden" name="action" value="anhadir"/>
						<select name="idedicion">
							<c:if test="${resultado.ediciones!=null}">
								<c:forEach items="${resultado.ediciones}" var="edicion">
									<option value="${edicion.id}">
										<fmt:message key="formato" bundle="${traduccion}"></fmt:message>
										<c:forEach items="${requestScope.formato_resultados}" var="formato">
											<c:if test="${formato.idFormato==edicion.idFormato}">
												${formato.nombre}
											</c:if>
										</c:forEach>
										<fmt:message key="tipoedicion" bundle="${traduccion}"></fmt:message>
										<c:forEach items="${requestScope.tipo_edicion_resultados}" var="tipoedicion">
											<c:if test="${tipoedicion.idTipoEdicion==edicion.idTipoEdicion}">
												${tipoedicion.nombre}
											</c:if>		
										</c:forEach>
										<fmt:message key="precio" bundle="${traduccion}"/>: ${edicion.precio}
									</option><br>
								</c:forEach>
							</c:if>
						</select>
						<input class="buttoncolor" type="submit" value="<fmt:message key="addcarrito" bundle="${traduccion}"/>"/>
					</form>
				</div>
			
			
				<c:if test="${not empty sessionScope['user']}">
				<c:set var="mostrar" value="false"/>
					<c:forEach items="${requestScope.juegos_en_biblioteca}" var="idjuegobiblioteca">
					
							<c:if test="${idjuegobiblioteca==resultado.idJuego}">
								<c:set var="mostrar" value="true"/>
							</c:if>
							
					</c:forEach>
						<c:choose>
							<c:when test="${mostrar!=false}">
								<p class="a">
									<fmt:message key="anhadido" bundle="${traduccion}"></fmt:message>
								</p>
							</c:when>
							<c:otherwise >
								<c:url var="urlbiblioteca" scope="page" value="/biblioteca">
									<c:param name="action" value="anhadir_juego" />
									<c:param name="id" value="${resultado.idJuego}" />
								</c:url>
								<a class="a" href="${urlbiblioteca}"><button>
										<fmt:message key="addbiblioteca" bundle="${traduccion}"></fmt:message>
									</button></a>
							</c:otherwise>
						</c:choose>
						
				</c:if>
			</div>
			</c:forEach>
		</c:if>
		
		<c:if test="${not empty resultados}">
			<p><center>	
			<c:url var="urlBase" value="/juego" scope="page">
				<c:param name="action" value="search"/>
			</c:url>

			<c:if test="${page > 1}">
				<a class="paginacion" href="${urlBase}${url}&page=${page - 1}">
					<fmt:message key="anterior" bundle="${traduccion}"/>
				</a>
				&nbsp;&nbsp;
			</c:if>

			<c:if test="${totalPages > 1}">	
			
				<c:if test="${firstPagedPage > 2}">
					<a class="paginacion"href="${urlBase}${url}&page=1"><b>1</b></a><b>&nbsp;.&nbsp;.&nbsp;.&nbsp;</b> 
				</c:if>
			
				<c:forEach begin="${firstPagedPage}" end="${lastPagedPage}" var="i">
					<c:choose>
					  <c:when test="${page != i}">
							&nbsp;<a class="paginacion" href="${urlBase}${url}&page=${i}"><b>${i}</b></a>&nbsp;
					  </c:when>
					  <c:otherwise>
							&nbsp;<b>${i}</b>&nbsp;
					  </c:otherwise>
					</c:choose>
				</c:forEach>

				<c:if test="${lastPagedPage < totalPages-1}">
					<b>&nbsp;.&nbsp;.&nbsp;.&nbsp;</b><a class="paginacion" href="${urlBase}${url}&page=${totalPages}"><b>${totalPages}</b></a>
				</c:if>	
			</c:if>

			<c:if test="${page < totalPages}">
				&nbsp;&nbsp;		
				<a class="paginacion" href="${urlBase}${url}&page=${page + 1}">
					<fmt:message key="siguiente" bundle="${traduccion}"/>
				</a>			
			</c:if>	
			
			</center></p>
		</c:if>

</section>
<%@include file="/html/common/footer.jsp"%>
