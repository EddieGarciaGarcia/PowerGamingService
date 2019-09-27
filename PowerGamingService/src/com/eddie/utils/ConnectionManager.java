package com.eddie.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionManager	 {
	
	private static Logger logger = LogManager.getLogger(ConnectionManager.class.getName());
	
	private static ResourceBundle dbConfiguration = ResourceBundle.getBundle("DBConfiguration");
	//IP clase:10.53.124.212:3306
	private static final String DRIVER_CLASS_NAME_PARAMETER = "jdbc.driver.classname";
	private static final String URL_PARAMETER = "jdbc.url";
	private static final String USER_PARAMETER = "jdbc.user";
	private static final String PASSWORD_PARAMETER = "jdbc.password";

	//  Database credentials
	private static String url;
	private static String user;
	private static String password;
	private static final Integer TAMANHOMAX= 50;
	private static final Integer TAMANHOMIN=1;
	
	private static ComboPooledDataSource poolConexiones=null;

	static {

		try {
			String driverClassName = dbConfiguration.getString(DRIVER_CLASS_NAME_PARAMETER);
			url = dbConfiguration.getString(URL_PARAMETER);
			user = dbConfiguration.getString(USER_PARAMETER);
			password = dbConfiguration.getString(PASSWORD_PARAMETER);
			
			 poolConexiones = new ComboPooledDataSource();
			 poolConexiones.setDriverClass(driverClassName);
			 poolConexiones.setJdbcUrl(url);
			 poolConexiones.setUser(user);
			 poolConexiones.setPassword(password);
			 poolConexiones.setMinPoolSize(TAMANHOMIN);
			 poolConexiones.setMaxPoolSize(TAMANHOMAX);
			 
			 
		} catch (Exception e) {
			logger.fatal(e.getMessage(), e); 
		}

	}

	private ConnectionManager() {}

	public static Connection getConnection() throws SQLException {
		return poolConexiones.getConnection();
	}
	
}
