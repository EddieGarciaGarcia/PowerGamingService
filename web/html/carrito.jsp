<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.eddie.model.*, com.eddie.utils.util.*, com.eddie.utils.model.*, com.eddie.controller.*, java.util.List "%>

<div onmouseover="ver(5)" onmouseout="ocultar(5)" id="carrito">
<img class="imgcarrito"src="<%=request.getContextPath()%>/imgs/carrito.png"/>
<div id="meumenudes5" class="menudes-contido2">
<%
	Carrito carrito = (Carrito) SessionManager.get(request, SessionAttributeNames.CARRITO);
	if (carrito != null) {
			
			for (LineaCarrito lineaCarrito : carrito.getLinea()){
				
%>
			<div class="lineacarrito">
				<img src="<%=request.getContextPath()%>/imgs/icojuego/<%=lineaCarrito.getEdicion().getIdJuego()%>.jpg"></img>
				<div class="edicion"><p><%for(Formato f:formato){
					if(f.getIdFormato().equals(lineaCarrito.getEdicion().getIdFormato())){
						%><%=f.getNombre()%>
					<%}
					}%> 
					<%for(TipoEdicion te:tipoEdicion){
											if(te.getIdTipoEdicion().equals(lineaCarrito.getEdicion().getIdTipoEdicion())){
												%><%=te.getNombre()%>
											<%}}%></br>
											
				<fmt:message key="precio" bundle="${traduccion}"/> <%=lineaCarrito.getEdicion().getPrecio()%> </p></br>
				<a class="a3" href="<%=ControllerPaths.CARRITO %>?<%=ParameterNames.ACTION%>=<%=Actions.ELIMINAR%>&amp;<%=ParameterNames.IDEDICION%>=<%=lineaCarrito.getEdicion().getId()%>">X</a>
				</div>
				<hr>
					
			</div>
			
<% 
				
			}
			%>
			<div class="carritofinal">
				<div>Total: <%=carrito.getTotal()%></div>
				<%if(u!=null){%>
				<a  href=""><button><fmt:message key="guardarpedido" bundle="${traduccion}"/></button></a>
			
			<% }else{%>
				<p><fmt:message key="carritomensage" bundle="${traduccion}"/></p>
				<a  href="<%=request.getContextPath()%><%=ViewPaths.LOGIN%>"><button><fmt:message key="entrar" bundle="${traduccion}"/></button></a>
			</div>
			<%}
	}
%>	
</div>
</div>
