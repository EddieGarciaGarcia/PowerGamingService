package com.eddie.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eddie.exceptions.DataException;
import com.eddie.model.Edicion;
import com.eddie.service.EdicionService;
import com.eddie.service.impl.EdicionServiceImpl;
import com.eddie.utils.model.Carrito;
import com.eddie.utils.util.SessionAttributeNames;
import com.eddie.utils.util.SessionManager;

/**
 * Servlet implementation class CarritoServlet
 */
@WebServlet("/carrito")
public class CarritoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      private EdicionService edicionService=null;
 
    public CarritoServlet() {
        super();
        edicionService= new EdicionServiceImpl();
    }
    
    protected void eliminarCarrito(HttpServletRequest request, Integer idEdicion) {
		HttpSession session = request.getSession();
		
		Carrito carrito = null;

		Object objCarrito = SessionManager.get(request, SessionAttributeNames.CARRITO);
		if (objCarrito != null) {
			carrito = (Carrito) objCarrito;
		} else {
			carrito = new Carrito();
		}
		carrito.deleteJuego(idEdicion);
	}


	protected void anhadirCarrito(HttpServletRequest request, Integer idEdicion) throws DataException {
		HttpSession session = request.getSession();
		Carrito carrito = null;

		boolean checkDuplicated ;
		
		Edicion anhadir = new Edicion();
		anhadir= edicionService.finById(idEdicion);

		Object objCartBean = SessionManager.get(request, SessionAttributeNames.CARRITO);

		if (objCartBean != null) {
			carrito = (Carrito) objCartBean;
		} else {
			carrito = new Carrito();
			session.setAttribute(SessionAttributeNames.CARRITO, carrito);
		}
		checkDuplicated=carrito.addCarrito(anhadir);
		if (checkDuplicated) {
			request.setAttribute(ParameterNames.DUPLICADO,AttributeNames.YA_EN_CARRITO);
			
		}
}
    
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action= request.getParameter(ParameterNames.ACTION);
		String target= request.getHeader(ViewPaths.REFERER);
		Integer idEdicion = Integer.valueOf(request.getParameter(ParameterNames.IDEDICION));
		
			if(Actions.ANHADIR.equalsIgnoreCase(action)) {
				try {
					anhadirCarrito(request, idEdicion);
				} catch (DataException e) {
					e.printStackTrace();
				}
			}else if(Actions.ELIMINAR.equalsIgnoreCase(action)) {
				eliminarCarrito(request, idEdicion);
			}
		response.sendRedirect(target);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
