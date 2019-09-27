package com.eddie.model;

import java.util.Date;


public class Usuario extends AbstractValueObject implements Comparable<Usuario>{
	
	private String nombre=null;
	private String apellido1=null;
	private String apellido2=null;
	private String nombreUser=null;
	private String password=null;
	private String email=null;
	private Date fechaNacimiento=null;
	private String telefono=null;
	private String genero=null;
	
	
	public Usuario() {
		
	}
	
	public Usuario(String nombre, String apellido1, String apellido2, String password,String telefono, String email, Date fechaNacimiento,String nombreuser,String genero) {
		setNombre(nombre);
		setApellido1(apellido1);
		setApellido2(apellido2);
		setPassword(password);
		setTelefono(telefono);
		setEmail(email);
		setFechaNacimiento(fechaNacimiento);
		setNombreUser(nombreuser);
		setGenero(genero);
		
		
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getNombreUser() {
		return nombreUser;
	}
	public void setNombreUser(String nombreuser) {
		this.nombreUser = nombreuser;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public boolean equals(Object o) {
		String otroEmail=((Usuario) o).getEmail();
		if(this.getEmail()==null && otroEmail==null) {
			return true;
		}
		if(this.getEmail()==null || otroEmail==null) {
			return false;
		}
		
		return getEmail().equalsIgnoreCase(otroEmail);
	}
	
	@Override
	public final int compareTo(Usuario u) {
		if(u.getNombre()==null && this.getNombre()==null) {
			return 0;
		}
		if(u.getNombre()==null) return 1;
		if(this.getNombre()==null)return -1;
		
		return this.getNombre().compareTo(u.getNombre());
		
	}
	
}
