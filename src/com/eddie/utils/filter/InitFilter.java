package com.eddie.utils.filter;

import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.controller.Actions;
import com.eddie.controller.ParameterNames;
import com.eddie.utils.config.SessionManager;
import com.eddie.controller.ViewPaths;

/**
 * Servlet Filter implementation class InitFilter
 */
@WebFilter("/usuario/*")
public class InitFilter implements Filter {

	private Logger logger=LogManager.getLogger(InitFilter.class);
	
    public InitFilter() {}

	public void destroy() {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	
		HttpServletRequest httpRequest= (HttpServletRequest) request;
		HttpServletResponse httpResponse= (HttpServletResponse) response;
		String action=httpRequest.getParameter(ParameterNames.ACTION);
		if(!Actions.REGISTRO.equals(action) 
			&& !Actions.PREREGISTRO.equals(action) 
			&& !Actions.LOGIN.equals(action) 
			&& !Actions.MENSAGE.equals(action)
			&& !Actions.FORGOTPASS.equals(action)
			&& !Actions.CHANGEPASS.equals(action)
			&& !Actions.CAMBIAR_IDIOMA.equals(action)
			&& (SessionManager.get((HttpServletRequest)request, SessionAttributeNames.USER))==null) {
			logger.info("Filtro de autentificacion");
			httpResponse.sendRedirect(httpRequest.getContextPath()+ViewPaths.LOGIN);
		}else {
			
			chain.doFilter(request, response);
		}
		
	}

	
	public void init(FilterConfig fConfig) throws ServletException {

	}

}
