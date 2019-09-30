package com.eddie.utils.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 * Commodity method para facilitar la implementacion de la paginacion, etc.
 *
 */
public class WebUtils {
	private static Logger logger = LogManager.getLogger(WebUtils.class.getName());
	
	/**
	 * Obtiene el valor entero de un valor de parametro currentPageValue
	 */
	public static final int getPageNumber(String pageValue, int defaultValue) {
		int pageNumber = defaultValue;
		if (pageValue!=null) {
			try {
				pageNumber = Integer.valueOf(pageValue);
			} catch (NumberFormatException e) {
				logger.warn("Parece que hay un usuario navegando que se considera muy listo: "+pageValue);		
			}
		}
		return pageNumber;
	}
}
