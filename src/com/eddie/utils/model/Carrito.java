package com.eddie.utils.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.model.Edicion;

public class Carrito {
	private static Logger logger = LogManager.getLogger(Carrito.class);
	
	private List<LineaCarrito> linea=null;
	private double total;
	
	public Carrito() {
		linea=new ArrayList<LineaCarrito>();
	}
	
	public List<LineaCarrito> getLinea() {
		return linea;
	}

	public void setLinea(List<LineaCarrito> linea) {
		this.linea = linea;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
	public int numLineasTotales() {
		return linea.size();
	}
	
	public void add(LineaCarrito lineaCarrito) {
		linea.add(lineaCarrito);
	}
	
	
	public void deleteJuego(Integer idEdicion) {
		try {
			for (int i=0;i<linea.size();i++) {
				if (linea.get(i).getEdicion().getId()==idEdicion) {
					linea.remove(i);
				}
			}
			calcularTotal();	   
			   
		} catch(NumberFormatException e) {
			  
			logger.error("Error al eliminar del carrito: "+ e.getMessage());	   
		}
	}
	
	public boolean addCarrito(Edicion edicion) {
		
		LineaCarrito lineaCarrito = new LineaCarrito();
		boolean checarDuplicado = false;
		try {
			if (!duplicado(edicion)) {
				checarDuplicado=false;
				lineaCarrito.setEdicion(edicion);
				lineaCarrito.setPrecioTotal(edicion.getPrecio());
				linea.add(lineaCarrito);
				calcularTotal();
			}else {
				checarDuplicado = true;
			}
		} catch (NumberFormatException e) {
			logger.error("Error al a�adir al carrito: "+ e.getMessage());	
		}
		return checarDuplicado;
	}
	
	private boolean duplicado(Edicion edicion) {
		boolean repetido=false;
		
		for (int i=0;i<linea.size();i++) {
			if (linea.get(i).getEdicion().getId()==edicion.getId()) {
				repetido=true;
			}else {
				repetido=false;
			}
		}
		
		return repetido;
	}
	
	public LineaCarrito getJuego(int index) {
		LineaCarrito lineaCarrito = null;
		if (linea.size() > index) {
			lineaCarrito = (LineaCarrito) linea.get(index);
		}
		return lineaCarrito;
	}
	
	private void calcularTotal() {
		  double totalCarrito = 0;
		  for(int contador=0;contador<linea.size(); contador++) {
			  LineaCarrito lineaCarrito = (LineaCarrito) linea.get(contador);
			  totalCarrito+=lineaCarrito.getPrecioTotal();
		    
		  }
		  setTotal(totalCarrito);
	}
	
}
