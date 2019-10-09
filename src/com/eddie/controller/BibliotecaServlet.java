package com.eddie.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.model.Resultados;
import com.eddie.exceptions.DataException;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Juego;
import com.eddie.model.Usuario;
import com.eddie.service.JuegoService;
import com.eddie.service.UsuarioService;
import com.eddie.service.impl.JuegoServiceImpl;
import com.eddie.service.impl.UsuarioServiceImpl;
import com.eddie.utils.config.ConfigurationManager;
import com.eddie.utils.config.SessionManager;
import com.eddie.utils.util.WebUtils;

/**
 * Servlet implementation class BibliotecaServlet
 */
@WebServlet("/biblioteca")
public class BibliotecaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static int pageSize = Integer.valueOf(
			ConfigurationManager.getInstance().getParameter(
						ConfigurationParameterNames.RESULTS_PAGE_SIZE_DEFAULT)); 

	private static int pagingPageCount = Integer.valueOf(
			ConfigurationManager.getInstance().getParameter(
						ConfigurationParameterNames.RESULTS_PAGING_PAGE_COUNT));
	
	private static Logger logger = LogManager.getLogger(UsuarioServlet.class);  
	private UsuarioService usuarioService=null;
	private JuegoService juegoService = null;
	
    public BibliotecaServlet() {
        super();
        usuarioService=new UsuarioServiceImpl();
        juegoService = new JuegoServiceImpl();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String action = request.getParameter(ParameterNames.ACTION);
			String idiomaPagina=SessionManager.get(request,WebConstants.USER_LOCALE).toString().substring(0,2).toUpperCase();
			
			if (logger.isDebugEnabled()) {
				logger.debug("Action {}: {}", action, ToStringBuilder.reflectionToString(request.getParameterMap()));
			}
			 
			String target = null;
			boolean redirect=false;
			Usuario user=(Usuario) SessionManager.get(request, SessionAttributeNames.USER);
			
			if(Actions.BIBLIOTECA.equalsIgnoreCase(action)) {
				
				int page = WebUtils.
						getPageNumber(request.getParameter(ParameterNames.PAGE), 1);
				
				Resultados<ItemBiblioteca> results=usuarioService.findByUsuario(user.getEmail(),(page-1)*pageSize+1, pageSize);
				List<ItemBiblioteca> puntuacion=new ArrayList<ItemBiblioteca>();
				for(ItemBiblioteca it: results.getResultados()) {
					puntuacion.add(it);
				}
				
				List<Juego> juegos =null;
				//Lambda expresion stream collectors
				List<Integer> juegoIDs = results.getResultados().stream().map(ItemBiblioteca::getIdJuego).collect(Collectors.toList());
				if(juegoIDs!=null && !juegoIDs.isEmpty()) {
					juegos =juegoService.findByIDs(juegoIDs, idiomaPagina);
				}
				request.setAttribute(AttributeNames.LISTADO_RESULTADOS_BIBLIOTECA, juegos);
				request.setAttribute(AttributeNames.TOTAL, results.getTotal());
				request.setAttribute(AttributeNames.PUNTUACION, puntuacion);
				
				int totalPages = (int) Math.ceil(results.getTotal()/(double)pageSize);
				int firstPagedPage = Math.max(1, page-pagingPageCount);
				int lastPagedPage = Math.min(totalPages, page+pagingPageCount);
				request.setAttribute(ParameterNames.PAGE, page);
				request.setAttribute(AttributeNames.TOTAL_PAGES, totalPages);
				request.setAttribute(AttributeNames.FIRST_PAGED_PAGES, firstPagedPage);
				request.setAttribute(AttributeNames.LAST_PAGED_PAGES, lastPagedPage);
				
				
				target= ViewPaths.BIBLIOTECA;
				
			}else if(Actions.DELETEJUEGO.equalsIgnoreCase(action)) {
				String idJuego = request.getParameter(ParameterNames.ID);
				
				Integer id=Integer.valueOf(idJuego);
				
				usuarioService.borrarJuegoBiblioteca(user.getEmail(), id);
				
				target= ControllerPaths.BIBLIOTECA+"?"+ParameterNames.ACTION+"="+Actions.BIBLIOTECA+"&"+ParameterNames.EMAIL+"="+user.getEmail();
				redirect=true;
			}else if(Actions.ADDJUEGO.equalsIgnoreCase(action)) {
				String idJuego = request.getParameter(ParameterNames.ID);
				Integer id=Integer.valueOf(idJuego);
			
				ItemBiblioteca it= new ItemBiblioteca();
				it.setEmail(user.getEmail());
				it.setIdJuego(id);
				
				Boolean results=usuarioService.existsInBiblioteca(user.getEmail(),id);

				if(results==false) {
					usuarioService.addJuegoBiblioteca(user.getEmail(), it);	
					target=request.getHeader(ViewPaths.REFERER);
				}else if(results==true){
					target=request.getHeader(ViewPaths.REFERER);
					
				}
				response.sendRedirect(target);
			}
			else {
				logger.error("Action desconocida");
				target= ViewPaths.INICIO;
			}
			if(!Actions.ADDJUEGO.equalsIgnoreCase(action)) {
				if(redirect==true) {
					logger.info("Redirect to "+target);
					response.sendRedirect(target);
				}else {
					logger.info("forwarding to "+target);
					request.getRequestDispatcher(target).forward(request, response);
				}
			}
		} catch (DataException e) {
			logger.info(e.getMessage(),e);
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
