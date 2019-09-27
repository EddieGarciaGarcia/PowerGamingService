package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.DireccionDAO;
import com.eddie.dao.ItemBibliotecaDAO;
import com.eddie.dao.UsuarioDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.utils.PasswordEncryptionUtil;
import com.eddie.dao.impl.DireccionDAOImpl;
import com.eddie.dao.impl.ItemBibliotecaDAOImpl;
import com.eddie.dao.impl.UsuarioDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Direccion;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Usuario;
import com.eddie.service.MailService;
import com.eddie.model.Resultados;
import com.eddie.service.UsuarioService;

public class UsuarioServiceImpl implements UsuarioService{

	private static Logger logger=LogManager.getLogger(UsuarioServiceImpl.class);

	private UsuarioDAO usuarioDao=null;
	private ItemBibliotecaDAO itemBibliotecaDao=null;
	private DireccionDAO direccionDao=null;
	private MailService mailService=null;
	public UsuarioServiceImpl() {
		usuarioDao=new UsuarioDAOImpl();
		itemBibliotecaDao=new ItemBibliotecaDAOImpl();
		direccionDao=new DireccionDAOImpl();
		mailService=new MailServiceImpl();
	}

	@Override
	public Usuario create(Usuario u) throws Exception {

		if(logger.isDebugEnabled()) {
			logger.debug("Usuario = "+u.toString());
		}

		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			Usuario u2 = usuarioDao.create(u,c);
			
			mailService.sendMail(u.getEmail(), "Bienvenido a mi p�gina web","<html><h1>Bienvenido a Power Gaming</h1><p>Hola muy buenas te has registrado correctamente</p></html>");

			commit=true;
			return u2;

		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
			throw e;
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
	}

	@Override
	public void update(Usuario u) throws DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Usuario = "+u.toString());
		}

		boolean commit=false;
		Connection c=null;
		try {

			c = ConnectionManager.getConnection();

			c.setAutoCommit(false);

			usuarioDao.update(u, c);
			commit = true;

		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		} finally {
			JDBCUtils.closeConnection(c, commit);
		}

	}

	@Override
	public long delete(String  email) throws DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}

		Connection connection = null;
		boolean commit = false;

		try {

			connection = ConnectionManager.getConnection();

			connection.setAutoCommit(false);

			long result = usuarioDao.delete(email, connection);            
			commit=true;  
			return result;

		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeConnection(connection, commit);
		}		

	}

	@Override
	public Usuario findById(String email) throws DataException{

		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		Usuario u =null;
		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);


			u = usuarioDao.findById(email,c);



		}catch(DataException e) {
			logger.error(e.getMessage(),e);
		} catch (SQLException e) {
			logger.debug(e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return u;
	}

	@Override
	public Usuario login(String email, String password) throws DataException{

		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		
		Connection c = null;
		Usuario u=null;
		
			if(email == null) {
				return null;
			}
			if(password == null){
				return null;
			}
			try {
			c = ConnectionManager.getConnection();
			c.setAutoCommit(true);
			u = usuarioDao.findById(email, c);
			
			} catch (SQLException e) {
				logger.debug(e);
			}finally {
				JDBCUtils.closeConnection(c);
			}
			if(u==null) {
				return u;
			}
			if(PasswordEncryptionUtil.checkPassword(password, u.getPassword())) {
				if(logger.isDebugEnabled()) {
					logger.debug("Usuario"+u.getEmail()+" autenticado");
				}
				return u;
			}else {
				return null;
			}
		

	}

	@Override
	public Resultados<ItemBiblioteca> findByUsuario(String email, int startIndex, int count) throws DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		Resultados<ItemBiblioteca> biblio=null;
		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			biblio=itemBibliotecaDao.findByUsuario(c, email, startIndex, count);



		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return biblio;
	}

	@Override
	public ItemBiblioteca addJuegoBiblioteca(String email,ItemBiblioteca b) throws DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Biblioteca = "+b.toString());
		}



		boolean existe= existsInBiblioteca(email,b.getIdJuego());

		if(existe) {
			// ou retornas null ou lanzas unha excepiont... 
		}

		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			b.setComentario("N");
			b.setFechaComentario(null);
			b.setComentario(null);
			b.setPuntuacion(0);
			b = itemBibliotecaDao.create(c, b);

			commit=true;


		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return b;
	}

	@Override
	public long borrarJuegoBiblioteca(String email, Integer idJuego) throws DataException{

		if(logger.isDebugEnabled()) {
			logger.debug("id= "+idJuego+" , email = "+email);
		}

		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);


			itemBibliotecaDao.delete(c, email, idJuego);

			commit=true;

			return idJuego;
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return idJuego;

	}

	@Override
	public Direccion findByIdDireccion(String email) throws InstanceNotFoundException, DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		Direccion d =null;
		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			d = direccionDao.findById(c,email);


		}catch(DataException | SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return d;
	}

	@Override
	public Direccion createDireccion(Direccion d) throws DuplicateInstanceException, DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Direccion = "+d.toString());
		}

		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);


			d = direccionDao.create(c, d);

			commit=true;		


		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return d;
	}

	@Override
	public boolean updateDireccion(Direccion d) throws InstanceNotFoundException, DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Direccion = "+d.toString());
		}

		boolean commit=false;
		Connection c=null;
		try {

			c = ConnectionManager.getConnection();

			c.setAutoCommit(false);

			direccionDao.update(c, d);
			commit = true;

		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);

		} finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return true;
	}

	@Override
	public void deleteDireccion(String email) throws DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}

		boolean commit=false;
		Connection c=null;
		try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			direccionDao.delete(c, email);

			commit=true;
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}

	}

	@Override
	public List<Integer> existsInBiblioteca(String email, List<Integer> idsDeJuego) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email+", idJuego "+idsDeJuego);
		}
		boolean commit=false;
		Connection c=null;
		List<Integer> result=new ArrayList<Integer>();
		try {	
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			result=itemBibliotecaDao.exists(c, email, idsDeJuego);
			
			commit=true;
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return result;
	}

	@Override
	public boolean existsInBiblioteca(String email, Integer idJuego) throws DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email+", idJuego "+idJuego);
		}
		boolean commit=false;
		Connection c=null;
		boolean result=false;
		try {	
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			result=itemBibliotecaDao.exists(c, email, idJuego);
			
			commit=true;
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return result;
	}

	@Override
	public ItemBiblioteca create(ItemBiblioteca it) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("it = "+it.toString());
		}

		boolean commit=false;
		Connection c=null;
		try {

			c = ConnectionManager.getConnection();

			c.setAutoCommit(false);

			itemBibliotecaDao.create(c, it);
			commit = true;

		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);

		} finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return it;
	}

	@Override
	public ItemBiblioteca findByIdEmail(String email, Integer idJuego) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email+", idJuego "+idJuego);
		}
		boolean commit=false;
		Connection c=null;
		ItemBiblioteca result=null;
		try {	
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);

			result=itemBibliotecaDao.fingByIdEmail(c, email, idJuego);
			
			commit=true;
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return result;
	}


}
