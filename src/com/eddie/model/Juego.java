package com.eddie.model;

import java.util.ArrayList;
import java.util.List;

public class Juego implements ValueObject, Comparable<Juego>{
	private Integer idJuego=null;
	private String nombre = null;
	private Integer fechaLanzamiento = null;
	private Integer idCreador = null;
	private Integer puntuacionMedia=null;
	
	private List<Categoria> categoria = null;	
	private List<Idioma> idiomas=null;
	private List<Plataforma> plataformas=null;
	private List<Edicion> ediciones = null;
	
	public Juego() {
		categoria= new ArrayList<Categoria>();
		idiomas=new ArrayList<Idioma>();
		plataformas= new ArrayList<Plataforma>();
		ediciones= new ArrayList<Edicion>();
	}
	
	public Juego(String nombre, Integer fechaLanzamiento,Integer creador) {
		setNombre(nombre);
		setFechaLanzamiento(fechaLanzamiento);
		setIdCreador(creador);
	}
	
	public Juego(String nombre) {
		setNombre(nombre);

	}
	
	public Integer getIdJuego() {
		return idJuego;
	}

	public void setIdJuego(Integer idJuego) {
		this.idJuego = idJuego;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getFechaLanzamiento() {
		return fechaLanzamiento;
	}

	public void setFechaLanzamiento(Integer fechaLanzamiento) {
		this.fechaLanzamiento = fechaLanzamiento;
	}

	public Integer getIdCreador() {
		return idCreador;
	}

	public void setIdCreador(Integer creador) {
		this.idCreador = creador;
	}
	public Integer getPuntuacionMedia() {
		return puntuacionMedia;
	}

	public void setPuntuacionMedia(Integer puntuacionMedia) {
		this.puntuacionMedia = puntuacionMedia;
	}
	
	@Override
	public int compareTo(Juego j) {
		return this.getFechaLanzamiento().compareTo(j.getFechaLanzamiento());
		
	}
	
	public int compareTo2(Juego j) {
		return this.getNombre().compareTo(j.getNombre());
	}

	public List<Idioma> getIdiomas() {
		return idiomas;
	}

	public void setIdiomas(List<Idioma> idiomas) {
		this.idiomas = idiomas;
	}

	public List<Plataforma> getPlataformas() {
		return plataformas;
	}

	public void setPlataformas(List<Plataforma> plataformas) {
		this.plataformas = plataformas;
	}

	public List<Categoria> getCategoria() {
		return categoria;
	}

	public void setCategoria(List<Categoria> categoria) {
		this.categoria = categoria;
	}

	public List<Edicion> getEdiciones() {
		return ediciones;
	}

	public void setEdiciones(List<Edicion> ediciones) {
		this.ediciones = ediciones;
	}


	/*SI HUBIERA HERENCIA*/
	
	/* 
	 * super.toString( ) + atributos
	 * 
	 * */
	
	
	
	
}

