<%@ page import="com.eddie.utils.util.*, com.eddie.model.*, com.eddie.controller.*, com.eddie.service.*, java.util.List" %>
<%@ page import="java.util.*" %>       
<div id="buscador-form">
	
	<%
		List<Categoria> categoria= (List<Categoria>) request.getAttribute(AttributeNames.CATEGORIA_RESULTADOS);
		List<Creador> creador= (List<Creador>) request.getAttribute(AttributeNames.CREADOR_RESULTADOS);
		List<Plataforma> plataforma= (List<Plataforma>) request.getAttribute(AttributeNames.PLATAFORMA_RESULTADOS);
		List<Idioma> idioma= (List<Idioma>) request.getAttribute(AttributeNames.IDIOMA_RESULTADOS);
	%>
		<form action="<%=ControllerPaths.JUEGO%>" method="post">	
			<input type="hidden" name="<%=ParameterNames.ACTION%>" value="<%=Actions.BUSCAR%>"/>
			<a onmouseover="ver(1)" onmouseout="ocultar(1)" class="dropbtn"><fmt:message key="categoria" bundle="${traduccion}"/>:
			<div id="meumenudes1" class="menu-search" > 
			
				<%for(Categoria c: categoria){%>
				<label class="container"><%=c.getNombre()%>
				<input type="checkbox" name="<%=ParameterNames.CATEGORIA%>" value="<%=c.getIdCategria()%>"/><br>
				<span class="checkmark"></span>
				</label>
				<%}%>
			</div>
			</a>
			<label><fmt:message key="creador" bundle="${traduccion}"/>:</label>
			<select name="<%=ParameterNames.CREADOR%>">
			<option selected disabled><fmt:message key="escoge" bundle="${traduccion}"/></option>
				<%for(Creador cr: creador){%>
				<option value="<%=cr.getIdCreador()%>"><%=cr.getNombre()%></option>
				<%} %>
			</select>
			<a onmouseover="ver(2)" onmouseout="ocultar(2)" class="dropbtn"><fmt:message key="plataforma" bundle="${traduccion}"/>:
			<div id="meumenudes2" class="menu-search2"> 
				<%for(Plataforma p: plataforma){%>
				<label class="container"><%=p.getNombre()%>
				<input type="checkbox" name="<%=ParameterNames.PLATAFORMA%>" value="<%=p.getIdPlatadorma()%>"/><br>
				<span class="checkmark"></span>
				</label>
				<%} %>
			</div>
			</a>
			<a onmouseover="ver(3)" onmouseout="ocultar(3)" class="dropbtn""><fmt:message key="idioma" bundle="${traduccion}"/>:
			<div id="meumenudes3" class="menu-search3"> 
				<%for(Idioma i: idioma){%>
				<label class="container"><%=i.getNombre()%>
				<input type="checkbox" name="<%=ParameterNames.IDIOMA%>" value="<%=i.getIdIdioma()%>"/><br>
				<span class="checkmark"></span>
				</label>
				<%} %>
			</div>
			</a>
			<label><fmt:message key="fecha" bundle="${traduccion}"/>:</label>
			<select name="<%=ParameterNames.FECHA%>">
				<option  selected disabled><fmt:message key="escoge" bundle="${traduccion}"/></option>
				<option value="2019">2019</option>
				<option value="2018">2018</option>
				<option value="2017">2017</option>
				<option value="2016">2016</option>
				<option value="2015">2015</option>
				<option value="2014">2014</option>
				<option value="2013">2013</option>
				<option value="2012">2012</option>
				<option value="2011">2011</option>
				<option value="2010">2010</option>
				<option value="2009">2009</option>
				<option value="2008">2008</option>
				<option value="2007">2007</option>
				<option value="2006">2006</option>
				<option value="2005">2005</option>
				<option value="2004">2004</option>
				<option value="2003">2003</option>
				<option value="2002">2002</option>
				<option value="2001">2001</option>
                <option value="2000">2000</option>
			</select>
			<input class="search" type="text"
					name="<%=ParameterNames.NOMBRE%>" 
					value="<%=ParamsUtils.getParameter(request, ParameterNames.NOMBRE)%>" minlength="0" maxlength="45"/>
					
			<input class="buttoncolor" type="submit" name="buscar" value="<fmt:message key="buscar" bundle='${traduccion}'/>"/>
		</form>
	</p>

</div>	
