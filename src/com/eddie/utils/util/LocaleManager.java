package com.eddie.utils.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.utils.config.ConfigurationManager;
import com.eddie.utils.config.ConfigurationParameterNames;

public class LocaleManager {
	
	private static Logger logger = LogManager.getLogger(LocaleManager.class.getName());
	
	private static final List<Locale> EMPTY_LOCALE_LIST = new ArrayList<Locale>();
	
	// Locales soportados por esta web. 
	private static List<Locale> supportedLocales = null;
	static {
		// Cargamos los Locale configurados
		supportedLocales = new ArrayList<Locale>();
		
		String[] supportedLocaleNames= 
				ConfigurationManager.getInstance().
					getParameter(ConfigurationParameterNames.SUPPORTED_LOCALES)
					.split(",");

		// Lista de locales admitidos
        Locale locale = null;
        for (String s: supportedLocaleNames) {
        	locale = Locale.forLanguageTag(s);
        	supportedLocales.add(locale);
        	if (logger.isDebugEnabled()) {
        		logger.debug("Registered locale "+locale);
        	}
        }
        
        if (supportedLocales.size()==0) {
        	logger.fatal("No Locale configured");
        	System.exit(0); // ...
        }
                
	}
	
	public static Locale getDefault() {
		return supportedLocales.get(0);
	}
	
	public static List<Locale> getSupportedLocales() {
		return supportedLocales;
	}
	
	/**
	 * Metodo utilidad para transformar y filtrar Locales 
	 * desde un String con formato tipo valores del header Accept-Language,
	 * como por ejemplo: "en-US;q=1.0,en-GB;q=0.5,fr-FR;q=0.0"
	 *                     
	 * Mas info: 
     * https://docs.oracle.com/javase/tutorial/i18n/locale/matching.html
	 * 
	 * @author https://www.linkedin.com/in/joseantoniolopezperez
	 */
	public static List<Locale> getMatchedLocales(String ranges) {
		
		List<Locale> matched = null;
		
		// En primer lugar vemos es directamente un locale como string como 
		// lo conocemos habitualmente en java, por ejemplo: gl_ES, es_ES, en_GB,
		// ya que una vez inicializado ser� m�s r�pido.
		// En este caso no es v�lido new Locale(String), con un solo par�metro
		// sino que habr�a que usar new Locale(String, String). 
		// Como no reinventamos la rueda, y para evitar todo lo siguiente,
		// que en muchos casos es innecesarios, usamos directamente,
		// (ver javadoc de commons-lang LocaleUtils.toLocale)		
		Locale locale = null;
		try {
			locale = LocaleUtils.toLocale(ranges);
		} catch (IllegalArgumentException iae) {
			// Nothing TODO
			// Significa que viene de un header con  IETF BCP 47 o similar.			
		}
		if (locale!=null && getSupportedLocales().contains(locale)) {
			// Se ha comprobado el contains por Locale funciona como se espera
			if (getSupportedLocales().contains(locale)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Matched pure java locale as string: "+ranges);
				}
				matched = Arrays.asList(locale);
			}
		}
		
		if (matched==null) {
			// No es simplemente un locale tipo gl_ES, es_ES, en_GB, 
			// sino que es en formato IETF BCP 47 o similar.
			// Vease https://docs.oracle.com/javase/tutorial/i18n/locale/matching.html

			// Si el valor de ranges es estupidez entonces LanguageRange lanza
			// una IllegalArgumentException... 
			// Tenemos que protegernos, ya que este m�todo puede ser invocado con un valor
			// procedente de una cookie, parametro de request, ... y por tanto
			// modificado por un usuario
			List<Locale.LanguageRange> languageRanges = null;
			try {
				languageRanges = Locale.LanguageRange.parse(ranges);
			} catch (IllegalArgumentException iae) {
				logger.warn("Invalid ranges: "+ranges, iae);
			}

			if (languageRanges!=null) {
				// Now filter the Locale objects, returning any matches
				matched = Locale.filter(languageRanges, getSupportedLocales() );
			} else {
				// Sabes porque es necesario esto?
				matched = Collections.unmodifiableList(EMPTY_LOCALE_LIST);			
			}
		}
		return matched;
	}
	
	
	
}

