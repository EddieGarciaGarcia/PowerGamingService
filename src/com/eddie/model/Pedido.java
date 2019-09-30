package com.eddie.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido implements ValueObject{

	private Integer idPedido=null;
	private String email=null;
	private Double total=null;
	private Date fechaPedido =null;
	private List<LineaPedido> lineasPedido;
	
	private static final Integer IVA=21;
	
	public Pedido() {
		lineasPedido =new ArrayList<>();
	}

	public Integer getNumeroPedido() {
		return idPedido;
	}
	public void setNumeroPedido(Integer numeroPedido) {
		this.idPedido = numeroPedido;
	}

	public Integer getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fechaPedido) {
		this.fechaPedido = fechaPedido;
	}

	public static Integer getIva() {
		return IVA;
	}

	public List<LineaPedido> getLineasPedido() {
		return lineasPedido;
	}

	public void setLineasPedido(List<LineaPedido> lineasPedido) {
		this.lineasPedido = lineasPedido;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		Pedido p= (Pedido) o;
		if(this.getIdPedido()!=null && this.getIdPedido()==p.getIdPedido()) {
			return this.getIdPedido().equals(p.getIdPedido());
		}else {
			return true;
		}
	}
	
}
