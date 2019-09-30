package com.eddie.model;

public class Edicion implements ValueObject{

	private Integer id = null;
	private Integer idJuego = null;
	private Integer idFormato=null;
	private Integer idTipoEdicion= null;

	// Para implementar como enum ou, como unha simple interface con constantes
	private Double precio = null;
	
	public Edicion() {
		
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getIdJuego() {
		return idJuego;
	}


	public void setIdJuego(Integer idJuego) {
		this.idJuego = idJuego;
	}


	public Integer getIdFormato() {
		return idFormato;
	}


	public void setIdFormato(Integer idFormato) {
		this.idFormato = idFormato;
	}


	public Integer getIdTipoEdicion() {
		return idTipoEdicion;
	}


	public void setIdTipoEdicion(Integer idTipoEdicion) {
		this.idTipoEdicion = idTipoEdicion;
	}


	public Double getPrecio() {
		return precio;
	}


	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	
	
	
}
