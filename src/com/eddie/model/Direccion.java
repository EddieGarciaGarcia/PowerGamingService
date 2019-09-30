package com.eddie.model;

public class Direccion implements ValueObject{
	private Integer idDireccion=null;
	private String calle=null;
	private String email=null;
	private String piso=null;
	private String numero=null;
	private String codigoPostal=null;
	private Integer idprovincia=null;
	private String localidad=null;
	
	
	public Direccion() {
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getIdDireccion() {
		return idDireccion;
	}


	public void setIdDireccion(Integer idDireccion) {
		this.idDireccion = idDireccion;
	}

	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public Integer getIdprovincia() {
		return idprovincia;
	}
	public void setIdprovincia(Integer idprovincia) {
		this.idprovincia = idprovincia;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	
	
}
