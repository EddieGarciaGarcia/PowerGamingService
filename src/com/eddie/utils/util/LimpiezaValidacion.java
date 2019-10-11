package com.eddie.utils.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.cj.util.StringUtils;

public class LimpiezaValidacion {
	private static Pattern pattern = null;
	private static Matcher matcher = null;
	
	public static String validEmail(String email) {
		pattern = Pattern.compile("^([0-9a-zA-Z]{1,20}+[-._+&]{1,5})*[0-9a-zA-Z]{1,20}+@([-0-9a-zA-Z]{1,15})$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(email)) {
			email=email.trim().toLowerCase();
			matcher=pattern.matcher(email);
			if(matcher.matches()) {
				return email;
			}else {
				
				return null;
			}
		}else {
			
			return null;
		}
	}
	
	public static String validNombre(String nombre) {
		pattern = Pattern.compile("^[a-zA-Z������������]{3,45}$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(nombre)) {
			nombre=nombre.trim();
			matcher=pattern.matcher(nombre);
			if(matcher.matches()) {
				return nombre;
			}else {
				
				return null;
			}
		}else {
			
			return null;
		}
	}
	
	
	public static String validApellido(String apellido2) {
		pattern = Pattern.compile("^[a-zA-Z������������]{3,45}$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(apellido2)) {
			apellido2=apellido2.trim();
			matcher=pattern.matcher(apellido2);
			if(matcher.matches()) {
				return apellido2;
			}else {
			
				return null;
			}
		}else {
			
			return null;
		}
	}
	
	public static String validPassword(String password) {
		pattern = Pattern.compile("^[A-Za-z0-9.@_-~#]{8,30}$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(password)) {
			password=password.trim();
			matcher=pattern.matcher(password);
			if(matcher.matches()) {
				return password;
			}else {
				
				return null;
			}
		}else {
			
			return null;
		}
	}
	
	public static String validTelefono(String telefono) {
		pattern = Pattern.compile("^[0-9]{9,30}$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(telefono)) {
			telefono=telefono.trim();
			matcher=pattern.matcher(telefono);
			if(matcher.matches()) {
				return telefono;
			}else {
				
				return null;
			}
		}else {
			
			return null;
		}
	}
	
	public static String validNombreJuego(String nombreJuego) {
		pattern = Pattern.compile("^[a-zA-Z������������0-9]{0,45}$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(nombreJuego)) {
			nombreJuego=nombreJuego.trim();
			matcher=pattern.matcher(nombreJuego);
			if(matcher.matches()) {
				return nombreJuego;
			}else {
				
				return null;
			}
		}else {
			
			return null;
		}
	}
	
	public static String validNombreUser(String nombreuser) {
		pattern = Pattern.compile("^[A-Za-z������������0-9.@_-~#]{5,45}$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(nombreuser)) {
			nombreuser=nombreuser.trim();
			matcher=pattern.matcher(nombreuser);
			if(matcher.matches()) {
				return nombreuser;
			}else {
				
				return null;
			}
		}else {
			
			return null;
		}
	}
	
	public static String validGenero(String genero) {
		pattern = Pattern.compile("^[HMO]{1}$");
		if(!StringUtils.isEmptyOrWhitespaceOnly(genero)) {
			genero=genero.trim();
			matcher=pattern.matcher(genero);
			if(matcher.matches()) {
				return genero;
			}else {
				
				return null;
			}
		}else {
			
			return null;
		}
	}
	
}
