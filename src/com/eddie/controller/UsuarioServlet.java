package com.eddie.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eddie.utils.util.ConfiguracionConstantes;
import com.eddie.utils.util.Constantes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.exceptions.DataException;
import com.eddie.model.Usuario;
import com.eddie.service.MailService;
import com.eddie.service.UsuarioService;
import com.eddie.service.impl.MailServiceImpl;
import com.eddie.service.impl.UsuarioServiceImpl;
import com.eddie.utils.model.Errors;
import com.eddie.utils.util.DateUtils;
import com.eddie.utils.config.SessionManager;
import com.eddie.utils.config.CookieManager;
import com.eddie.utils.config.LocaleManager;
import com.eddie.utils.util.LimpiezaValidacion;

/**
 * Servlet implementation class UsuarioServlet
 */
@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Logger logger = LogManager.getLogger(UsuarioServlet.class);
	
	private UsuarioService userv=null;
	private MailService mservice=null;
	
	
    public UsuarioServlet() {
        super();
        userv=new UsuarioServiceImpl();
        mservice=new MailServiceImpl();
   
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String action = request.getParameter(ParameterNames.ACTION);

			if (logger.isDebugEnabled()) {
				logger.debug("Action {}: {}", action, ToStringBuilder.reflectionToString(request.getParameterMap()));
			}
			
			 
			Errors errors = new Errors(); 
			String target = null;
			boolean redirect=false;
			
			if (Actions.LOGIN.equalsIgnoreCase(action)) {
				
				// Recuperacion
				String email = request.getParameter(ParameterNames.EMAIL);
				String password = request.getParameter(ParameterNames.PASSWORD);
				
				String emailValid = LimpiezaValidacion.validEmail(email);
				String passwordValid = LimpiezaValidacion.validPassword(password);
				
				if (StringUtils.isEmpty(emailValid)) {
					errors.add(ParameterNames.EMAIL,ErrorCodes.INCORRECT_EMAIL);
				}
				if (StringUtils.isEmpty(password)) {
					errors.add(ParameterNames.PASSWORD, ErrorCodes.INCORRECT_PASSWORD);
				}
					
				Usuario usuario = null;
				if (!errors.hasErrors()) {
					usuario = userv.login(emailValid, passwordValid);
					
				}
				if(usuario == null) {
					errors.add(ParameterNames.ACTION,ErrorCodes.AUTHENTICATION_ERROR);
				}	
				if (errors.hasErrors()) {	
					if (logger.isDebugEnabled()) {
						logger.debug("Autenticacion fallida: {}", errors);
					}		
					request.setAttribute(AttributeNames.ERRORS, errors);
					target = ViewPaths.LOGIN;
					
				} else {	
					SessionManager.set(request, SessionAttributeNames.USER, usuario);		
					target = request.getContextPath()+ViewPaths.INICIO;
					redirect=true;
				}
				
			}else if (Actions.LOGOUT.equalsIgnoreCase(action)) {
				SessionManager.set(request, SessionAttributeNames.USER, null);
				target = request.getContextPath()+ViewPaths.INICIO;
				redirect=true;
			}else if(Actions.REGISTRO.equalsIgnoreCase(action)) {
				String nombre=request.getParameter(ParameterNames.NOMBRE);
				String apellido1=request.getParameter(ParameterNames.APELLIDO1);
				String apellido2=request.getParameter(ParameterNames.APELLIDO2);
				String email=request.getParameter(ParameterNames.EMAIL);
				String telefono=request.getParameter(ParameterNames.TELEFONO);
				String password=request.getParameter(ParameterNames.PASSWORD);
				String fecha=request.getParameter(ParameterNames.FECHANACIMIENTO);
				String genero=request.getParameter(ParameterNames.GENERO);
				
				Usuario u= new Usuario();
				
				SimpleDateFormat sdf=(SimpleDateFormat) DateUtils.FORMATODATA;
				Date fechaformat=sdf.parse(fecha);
				
				u.setNombre(LimpiezaValidacion.validNombre(nombre));
				u.setApellido1(LimpiezaValidacion.validApellido(apellido1));
				u.setApellido2(LimpiezaValidacion.validApellido(apellido2));
				u.setEmail(LimpiezaValidacion.validEmail(email));
				u.setTelefono(LimpiezaValidacion.validTelefono(telefono));
				u.setPassword(LimpiezaValidacion.validPassword(password));
				u.setFechaNacimiento(fechaformat);
				u.setGenero(LimpiezaValidacion.validGenero(genero));
				
				if(u.getApellido1()!=null || u.getApellido2()!=null) {
					u.setNombreUser(u.getNombre()+u.getApellido1().charAt(0)+u.getApellido2().charAt(0));
				}else {
					u.setNombreUser(u.getNombre());
				}
				if(u.getEmail()!=null && u.getNombre()!=null && u.getFechaNacimiento()!=null && u.getPassword()!=null) {
					userv.create(u);
					target = ViewPaths.LOGIN;
				}else {
					errors.add(ParameterNames.REGISTRO, ErrorCodes.REGISTER_ERROR);
					request.setAttribute(AttributeNames.ERRORS, errors);
					target= ViewPaths.REGISTRO;
				}		
				
			}else if(Actions.PRECONFIGURACION.equalsIgnoreCase(action)) {
				Usuario user=(Usuario) SessionManager.get(request, SessionAttributeNames.USER);
				request.setAttribute(AttributeNames.USER, user);

				target = ViewPaths.MYACCOUNT;
			
			}else if(Actions.CONFIGURACION.equalsIgnoreCase(action)) {
				Usuario user=(Usuario) SessionManager.get(request, SessionAttributeNames.USER);
				String nombre=request.getParameter(ParameterNames.NOMBRE);
				String apellido1=request.getParameter(ParameterNames.APELLIDO1);
				String apellido2=request.getParameter(ParameterNames.APELLIDO2);
				String telefono=request.getParameter(ParameterNames.TELEFONO);
				String passwordconfig=request.getParameter(ParameterNames.PASSWORD);
				String nombreUser=request.getParameter(ParameterNames.NOMBREUSER);
				
				Usuario userupdate=new Usuario();
				
				userupdate.setNombre(LimpiezaValidacion.validNombre(nombre));
				userupdate.setApellido1(LimpiezaValidacion.validApellido(apellido1));
				userupdate.setApellido2(LimpiezaValidacion.validApellido(apellido2));
				userupdate.setTelefono(LimpiezaValidacion.validTelefono(telefono));
				userupdate.setPassword(LimpiezaValidacion.validPassword(passwordconfig));
				userupdate.setNombreUser(LimpiezaValidacion.validNombreUser(nombreUser));
				userupdate.setEmail(user.getEmail());
				
				if(userupdate.getNombre()!=null || userupdate.getApellido1()!=null || userupdate.getApellido2()!=null 
						|| userupdate.getTelefono()!=null || userupdate.getPassword()!=null || userupdate.getNombreUser()!=null) {
					userv.update(userupdate);
				}
				target = ControllerPaths.USUARIO+"?"+ParameterNames.ACTION+"="+Actions.PRECONFIGURACION;
				redirect=true;
				
			}else if(Actions.MENSAGE.equalsIgnoreCase(action)) {
				Usuario u=(Usuario) SessionManager.get(request, SessionAttributeNames.USER);
				String mensage= request.getParameter(ParameterNames.MENSAGE);
				if(u!=null) {
					mservice.sendMail("powergaming2019@gmail.com",u.getEmail(), mensage);
				}else {
					String email= request.getParameter(ParameterNames.EMAIL);
					mservice.sendMail("powergaming2019@gmail.com",email, mensage);
				}
				
				target=ViewPaths.CONTACTO;
			}else if(Actions.FORGOTPASS.equalsIgnoreCase(action)) {
				String email= request.getParameter(ParameterNames.EMAIL);
				String emailvalid=LimpiezaValidacion.validEmail(email);
				Usuario u=userv.findById(emailvalid);
				if(u==null) {
					target=ControllerPaths.USUARIO+"?"+ParameterNames.ACTION+"="+Actions.PREREGISTRO;
					redirect=true;
				}else {
					StringBuilder enlace=new StringBuilder();
					enlace=new StringBuilder(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/"+request.getContextPath()+ViewPaths.CHANGEPASS+"?"+ParameterNames.EMAIL+"="+emailvalid);
				mservice.sendMail(emailvalid,"Restauracion de contrase�a", 
						"<html><h2>Power Gaming</h2><h4>Pulse en enlace o copielo entero para cambiar su contrase�a:</h4>"
						+ "<a href='"+enlace+"'>Restablecer contrase�a</a><p>"+enlace+"</p></html>");
					
				target=request.getContextPath()+ViewPaths.INICIO;
				redirect=true;
				}
			}else if(Actions.CHANGEPASS.equalsIgnoreCase(action)){
				String email= request.getParameter(ParameterNames.EMAIL);
				String pass= request.getParameter(ParameterNames.PASSWORD);
				Usuario u=userv.findById(email);
					
				u.setPassword(pass);
				
				userv.update(u);
				
				target=request.getContextPath()+ViewPaths.LOGIN;
				redirect=true;
			} else if (Actions.CAMBIAR_IDIOMA.equalsIgnoreCase(action)) {
				String localeName = request.getParameter(ParameterNames.LOCALE);
				// Recordar que hay que validar... lo que nos envian, incluso en algo como esto.
				// Buscamos entre los Locale soportados por la web:
				List<Locale> results = LocaleManager.getMatchedLocales(localeName);
				Locale newLocale = null;
				if (results.size()>0) {
					newLocale = results.get(0);
				} else {
					logger.warn("Request locale not supported: "+localeName);
					newLocale = LocaleManager.getDefault();
				}

				SessionManager.set(request, ConfiguracionConstantes.USER_LOCALE, newLocale);
				CookieManager.addCookie(response, ConfiguracionConstantes.USER_LOCALE, newLocale.toString(), "/", 365*24*60*60);

				if (logger.isDebugEnabled()) {
					logger.debug("Locale changed to "+newLocale);
				}

				target=request.getHeader(ViewPaths.REFERER);
				
				if (logger.isDebugEnabled()) {
					logger.debug("Locale changed to "+newLocale);
				}

				response.sendRedirect(target);
			}else {
				logger.error("Action desconocida");
				target= request.getContextPath()+ViewPaths.ERROR404;
			}
			if(!Actions.CAMBIAR_IDIOMA.equalsIgnoreCase(action)) {
				if(redirect) {
					logger.info("Redirect to "+target);
					response.sendRedirect(target);
				}else {
					logger.info("forwarding to "+target);
					request.getRequestDispatcher(target).forward(request, response);
				}
			}
			} catch (SQLException e) {	
				logger.info(e.getMessage(),e);
			} catch (DataException e) {
				logger.info(e.getMessage(),e);
			} catch (Exception e) {
				logger.info(e.getMessage(),e);
			}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
