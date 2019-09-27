
package com.eddie.model;

public class Creador implements ValueObject{
	
	private Integer idCreador;
	private String nombre;
	
	public Creador() {
		
	}
	
	public Integer getIdCreador() {
		return idCreador;
	}
	public String getNombre() {
		return nombre;
	}
	public void setIdCreador(Integer idcreador) {
		idCreador = idcreador;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
