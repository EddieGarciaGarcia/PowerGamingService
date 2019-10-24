package com.eddie.utils;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.utils.CacheManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.Cache;

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

	/**
	 * Crea el id para la URL
	 */
	public static String generateSessionId() {
		return RandomStringUtils.random(40);
	}


	/**
	 * Determina si un String está vacio o es nulo
	 *
	 * @param str String.
	 * @return true si está vacio o es nulo y false en caso contrario.
	 */
	public static boolean isEmptyOrNull(String str){
		if(str == null || "".equals(str))
			return true;
		else
			return false;
	}
}
