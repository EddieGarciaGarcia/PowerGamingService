package com.eddie.service.impl;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.impl.CategoriaDAOImpl;
import com.eddie.service.MailService;

public class MailServiceImpl implements MailService {
	
	private static Logger logger=LogManager.getLogger(CategoriaDAOImpl.class);
	
	@Override
	public void sendMail(String to,String subject,String message) {
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName("smtp.googlemail.com");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("powergaming2019@gmail.com", PASSWORD));
		email.setSSLOnConnect(true);
		email.setSubject(subject);
		try {
			email.setFrom("powergaming2019@gmail.com");
			email.setHtmlMsg(message);
			email.addTo(to);
			email.send();
		} catch (EmailException e) {
			logger.error(e.getMessage(),e);
			
		}
		

	}
	public static final String PASSWORD="eddiegg1";
	

	
}
