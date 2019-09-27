package com.eddie.utils.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ParamsUtils {

	public static final String getParameter(HttpServletRequest request, String name) {		
		String value = (String) request.getParameter(name);							
		if (value==null) value = "";
		return value;
	}
	
	public static String trimmer(String param) {
		return param.trim();
	}
	
	public static String URLBuilder (String url, Map<String, String> valores) throws UnsupportedEncodingException {
		int cont = 1;
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(url);
		for(String mapKey: valores.keySet()) {
			if(cont == 1) urlBuilder.append(WebConstants.QUESTION_MARK);
			urlBuilder.append(URLEncoder.encode(trimmer(mapKey), WebConstants.ENCODING)).append(WebConstants.EQUAL)
				.append(URLEncoder.encode(trimmer(valores.get(mapKey)), WebConstants.ENCODING));
			if(cont != valores.size()) {
				urlBuilder.append(WebConstants.AMPERSAND_URL);
			}
			cont++;
		}
		return urlBuilder.toString();
}
	
}
