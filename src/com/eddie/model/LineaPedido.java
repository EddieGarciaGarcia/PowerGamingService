package com.eddie.model;

public class LineaPedido implements ValueObject{
	private Integer numeroLinea=null;
	private Integer idEdicion=null;
	private Integer pedido=null;
	private Integer cantidad=null;
	private Double precio=null;
	
	private static final Double IVA=0.0d;
	
	
	public LineaPedido() {
		
	}
	public Integer getNumeroLinea() {
		return numeroLinea;
	}
	public void setNumeroLinea(Integer numeroLinea) {
		this.numeroLinea = numeroLinea;
	}
	public Integer getIdEdicion() {
		return idEdicion;
	}
	public void setIdEdicion(Integer idJuego) {
		this.idEdicion = idJuego;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public static Double getIva() {
		return IVA;
	}
	public Integer getPedido() {
		return pedido;
	}
	public void setPedido(Integer pedido) {
		this.pedido = pedido;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	
}
