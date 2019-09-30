<%@ page import="com.eddie.service.*, com.eddie.controller.*, java.util.List, com.eddie.model.*,java.util.Map, java.io.IOException,java.io.*" %>

<%@include file="/html/common/header.jsp"%>


	<section class="sjuego">
		<% 
			
			Juego resultados =(Juego) request.getAttribute(AttributeNames.PRODUCTO_RESULTADOS);
			Map<String, ItemBiblioteca> comentarios= (Map<String, ItemBiblioteca>) request.getAttribute(AttributeNames.COMENTARIOS_JUEGO);
			Boolean resultadosBiblioteca = (Boolean)request.getAttribute(AttributeNames.PRODUCTOS_EN_BIBLIOTECA);		
			Creador creador=(Creador)request.getAttribute(AttributeNames.CREADOR_JUEGO);
			Integer puntuacion= (Integer) request.getAttribute(AttributeNames.PUNTUACION);
			
		%>
		<img src="<%=request.getContextPath()%>/imgs/icojuego/<%=resultados.getIdJuego()%>.jpg"></img>
		<div class="juegotitle">
		<h1><%=resultados.getNombre()%></h1>
		<p><fmt:message key="puntuacion" bundle="${traduccion}"/><%=puntuacion.toString()%></p>
		</div>
		<div><video src="<%=request.getContextPath()%>/imgs/icojuego/<%=resultados.getIdJuego()%>.mp4" width="640" height="480" controls></video></div>	
		<p><fmt:message key="fechalanzamiento" bundle="${traduccion}"/> : <%=resultados.getFechaLanzamiento() %><br>
		<fmt:message key="creador" bundle="${traduccion}"/> : <%=creador.getNombre()%><br>
		<fmt:message key="categorias" bundle="${traduccion}"/> : <%for(Categoria c: resultados.getCategoria()){%><%=c.getNombre()%> <%}%><br>
		<fmt:message key="plataformadisponible" bundle="${traduccion}"/> : <%for(Plataforma p: resultados.getPlataformas()){%><%=p.getNombre()%> <%}%><br>
		<fmt:message key="idiomadisponible" bundle="${traduccion}"/> : <%for(Idioma i: resultados.getIdiomas()){%><%=i.getNombre()%> <%}%><br>
		<c:set var="resultados" value="${requestScope.juegos_resultados}" />
		<fmt:message key="${resultados.idJuego}" bundle="${traduccion}"/>
		</p>
		
		<% if(u!=null){	
						if(resultadosBiblioteca==true){
							%>
								<p class="a"><fmt:message key="anhadido" bundle="${traduccion}"/></p>
							<% 
						}else if(resultadosBiblioteca==false){
							%>
								<a class="a" href="<%=ControllerPaths.BIBLIOTECA%>?
									<%=ParameterNames.ACTION%>=<%=Actions.ADDJUEGO%>&amp;<%=ParameterNames.ID%>=
									<%=resultados.getIdJuego()%>"><button><fmt:message key="addbiblioteca" bundle="${traduccion}"/></button></a>
							<% 
								
						}
						%>
						
						
		<%}%>
		
		<div class="addCarrito">
			<form action="<%=ControllerPaths.CARRITO%>" method="post">
				<input class="buttoncolor" type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.ANHADIR%>"/>
				<select name="<%=ParameterNames.IDEDICION%>">
					<%if(resultados.getEdiciones()!=null){for(Edicion e: resultados.getEdiciones()){  %>
					<option value="<%=e.getId()%>"><fmt:message key="formato" bundle="${traduccion}"></fmt:message><%for(Formato f:formato){
									if(f.getIdFormato().equals(e.getIdFormato())){
										%> <%=f.getNombre()%>
									<%}
									}%> 
						<fmt:message key="tipoedicion" bundle="${traduccion}"></fmt:message><%for(TipoEdicion te:tipoEdicion){
									if(te.getIdTipoEdicion().equals(e.getIdTipoEdicion())){
										%> <%=te.getNombre()%>
									<%}}%> <fmt:message key="precio" bundle="${traduccion}"/>: <%=e.getPrecio() %></option><br>
						<%}}%>
				</select>
				<input class="buttoncolor" type="submit" value="<fmt:message key="addcarrito" bundle="${traduccion}"/>"/>
			</form>
		</div>
		
		
		
		<% if(u!=null){%>
			<div class="comentario">
			<form action="<%=ControllerPaths.JUEGO%>" method="post">
				<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.ADDCOMENTARIO%>"/>
				<input type="hidden" name="<%=ParameterNames.ID%>" value="<%=ParamsUtils.getParameter(request, ParameterNames.ID)%>"/>
				<p><fmt:message key="juegomensage" bundle="${traduccion}"/> <%=u.getNombreUser()%> :</p>
				<textarea rows="10" cols="50" name="<%=ParameterNames.COMENTARIO%>"></textarea>
				<input class="buttoncolor" type="submit" name="Modificar" value="<fmt:message key="modificar" bundle="${traduccion}"/>"/>	
			</form>		
			</div>
			<%
		}%>
		
			<div class="comentarios">
			<%if(!comentarios.isEmpty() && comentarios!=null){%>
				<%for (Map.Entry<String, ItemBiblioteca> entry : comentarios.entrySet()) {
				   if(entry.getValue().getComentario()!=null && entry.getValue().getIdJuego()!=null){%>
				   	<div>
					    <h3><%=entry.getKey()%></h3>
					    <p><%=entry.getValue().getFechaComentario()%></p>
					    <p><%=entry.getValue().getComentario()%></p>
				    </div>
				    <%}
				}%>
			<%}%>
		</div>
		
			
	</section>
	
<%@include file="/html/common/footer.jsp"%>
