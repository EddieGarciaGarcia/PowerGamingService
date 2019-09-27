<%@ page import="com.eddie.controller.*" %>
	<footer class="footer">
            <p>�powergaming<br>
            <fmt:message key="contacto" bundle="${traduccion}"></fmt:message>:<br>
            Email: powergaming2019@gmail.com<br>
            <fmt:message key="telefono" bundle="${traduccion}"></fmt:message> 654634543<br>
            <fmt:message key="mensagefooter" bundle="${traduccion}"></fmt:message>
            <a href="<%=request.getContextPath()%><%=ViewPaths.CONTACTO%>"><button><fmt:message key="contacto" bundle="${traduccion}"></fmt:message></button></a>
            </p>
    </footer>
	</body>
</html>
