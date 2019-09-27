package com.eddie.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

public class PowerGamingExceptions extends Exception{
	
	public PowerGamingExceptions() {
		super();
	}
	
	public PowerGamingExceptions(String message) {
		this(message, null);		
	}
	
	public PowerGamingExceptions(Throwable cause) {
		this(null,cause);		
	}
	
	public PowerGamingExceptions(String message, Throwable cause) {
		super(message,cause);		
	}			
	
	public void printStackTrace() {
		if (getCause()!=null) {
			getCause().printStackTrace();
		} else {
			super.printStackTrace();
		}
	}
	

	public void printStackTrace(PrintStream s) {
		if (getCause()!=null) {
			getCause().printStackTrace(s);
		} else {
			super.printStackTrace(s);
		}
	}	
	
	public void printStackTrace(PrintWriter w) {
		if (getCause()!=null) {
			getCause().printStackTrace(w);
		} else {
			super.printStackTrace(w);
		}
	}	
}
