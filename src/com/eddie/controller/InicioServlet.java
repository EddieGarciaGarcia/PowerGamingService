package com.eddie.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eddie.ecommerce.model.Juego;
import com.eddie.ecommerce.model.Resultados;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.utils.Constantes;
import com.eddie.utils.util.ConfiguracionConstantes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.exceptions.DataException;
import com.eddie.utils.config.ConfigurationManager;
import com.eddie.utils.config.SessionManager;
import com.eddie.utils.util.WebUtils;

/**
 * Servlet implementation class InicioServlet
 */
@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static int pageSize = Integer.valueOf(
			ConfigurationManager.getInstance().getParameter(ConfiguracionConstantes.RESULTS_PAGE_SIZE_DEFAULT))+2;
	
	private static int pagingPageCount = Integer.valueOf(
			ConfigurationManager.getInstance().getParameter(ConfiguracionConstantes.RESULTS_PAGING_PAGE_COUNT));
	private JuegoService juegoService = null;
	private static Logger logger = LogManager.getLogger(JuegoServlet.class);
    public InicioServlet() {
        super();
        juegoService = new JuegoServiceImpl();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String target = null;
		String idiomaPagina=SessionManager.get(request, ConfiguracionConstantes.USER_LOCALE).toString().substring(0,2).toUpperCase();
		
		int page = WebUtils.
				getPageNumber(request.getParameter(ParameterNames.PAGE), 1);
		
		Resultados<Juego> todos;
		
		
		try {
			todos = juegoService.findAllByDate(idiomaPagina,(page-1)*pageSize+1, pageSize);
		
			request.setAttribute(AttributeNames.RESULTADOS_TODOS, todos.getResultados());
			request.setAttribute(AttributeNames.TOTAL, todos.getTotal());
			
			int totalPages = (int) Math.ceil(todos.getTotal()/(double)pageSize);
			int firstPagedPage = Math.max(1, page-pagingPageCount);
			int lastPagedPage = Math.min(totalPages, page+pagingPageCount);
			request.setAttribute(ParameterNames.PAGE, page);
			request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
			request.setAttribute(AttributeNames.FIRST_PAGED_PAGES, firstPagedPage);
			request.setAttribute(AttributeNames.LAST_PAGED_PAGES, lastPagedPage);
		} catch (DataException e) {
			logger.debug(e);
		}
		target= ViewPaths.HOME;
		request.getRequestDispatcher(target).forward(request, response);
		
	}
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}
