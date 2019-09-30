package com.eddie.model;


public class JuegoCriteria extends Juego implements ValueObject {
	
	private int[] categoriaIDs = null;
	private int[] plataformaIDs = null;
	private String[] idiomaIDs=null;
 	
	public JuegoCriteria() {		
		
	}
	public int[] getCategoriaIDs() {
		return categoriaIDs;
	}
	public void setCategoriaIDs(int[] categoriaIDs) {
		this.categoriaIDs = categoriaIDs;
	}
	public int[] getPlataformaIDs() {
		return plataformaIDs;
	}
	public void setPlataformaIDs(int[] plataformaIDs) {
		this.plataformaIDs = plataformaIDs;
	}
	public String[] getIdiomaIDs() {
		return idiomaIDs;
	}
	public void setIdiomaIDs(String[] idiomaIDs) {
		this.idiomaIDs = idiomaIDs;
	}
	
}
