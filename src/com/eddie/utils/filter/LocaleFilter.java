package com.eddie.utils.filter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.utils.config.CookieManager;
import com.eddie.utils.config.LocaleManager;
import com.eddie.utils.config.SessionManager;

/**
 * Filtro para inicializacion del Locale
 */
// En servlet 3.0 la anotacion no provee manera para establecer orden de filtros
// @WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

	private static Logger logger = LogManager.getLogger(LocaleFilter.class.getName());
  	

	
    public LocaleFilter() {       
    }

	public void init(FilterConfig cfg) throws ServletException {
		// Habitualmente la configuracion de los parametros
		// de un filtro se hace en el web.xml.

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpServletResponse httpResponse = ((HttpServletResponse) response);
		
		Locale locale = (Locale) SessionManager.get(httpRequest, WebConstants.USER_LOCALE);
		
		if (locale == null) {
			// No hay Locale configurado para esta sesion.
			
			// Primero intentamos inicializar el locale de cookie.
			Cookie cookieLocale = CookieManager.getCookie(httpRequest, WebConstants.USER_LOCALE);
			if (cookieLocale!=null) {
				
				// El valor "es_ES", "en_GB", etc. provoca una inicializacion 
				// erronea en new Locale(String l). new Locale("es_ES").toString() genera es_es
				
				// Por ello, al igual que cuando viene en el header http, o cuando viene de 
				// un parametro de la request, tambien cuando viene de una cookie hay que
				// pasarlo por:
				
				locale = getLocale(cookieLocale.getValue());
				if (locale!=null && logger.isDebugEnabled()) {
					logger.debug("Locale initialized from cookie: "+locale);
				}				
			} else {
				// En ultimo término, a modo de "por defecto", inicializamos a partir 
				// del header Accept-Language de la request. 
				// Más info: https://www.w3.org/International/questions/qa-accept-lang-locales
				locale = getLocale(httpRequest);
				if (locale!=null && logger.isDebugEnabled()) {
					logger.debug("Locale initialized from header: "+locale);
				}
			}
			if (locale==null) {
				// En ultimo caso, el primero de los soportados como opcion por defecto.
				locale = LocaleManager.getDefault();
				logger.warn("Using default locale: "+locale);			
			}

			SessionManager.set(httpRequest, WebConstants.USER_LOCALE, locale);			
			CookieManager.addCookie(httpResponse, WebConstants.USER_LOCALE, locale.toString(), "/", 365*24*60*60);
		}
		
		// Continuar la invocacion de la cadena de responsabilidad.
		// Solamente no se invocaría si el filtro determinase otro 
		// como por ejemplo en el caso de un filtro de autenticación.
		chain.doFilter(request, response);		
	
	}


	protected Locale getLocale(HttpServletRequest httpRequest) {			
		String acceptLanguageHeader = httpRequest.getHeader("Accept-Language");
		return getLocale(acceptLanguageHeader);		       
	}
	
	protected Locale getLocale(String ranges) {
		// Miramos cuales de los lenguajes establecidos por el usuario en su navegador
		// son soportados por nuesetra web
		List<Locale> matchedLocales = LocaleManager.getMatchedLocales(ranges);
		
		Locale locale = null;
		if (matchedLocales.size()>0) {
			locale = matchedLocales.get(0);
			if (logger.isDebugEnabled()) {
				logger.debug("Matched "+matchedLocales.size()+" locales. Selected: "+locale);
			}
		} else {
			logger.warn("No matched locale: "+ranges);
		}
		return locale;

	}
	
}
