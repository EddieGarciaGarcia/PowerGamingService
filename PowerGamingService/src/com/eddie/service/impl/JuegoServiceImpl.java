package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.ItemBibliotecaDAO;
import com.eddie.dao.JuegoDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.ItemBibliotecaDAOImpl;
import com.eddie.dao.impl.JuegoDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Juego;
import com.eddie.model.JuegoCriteria;
import com.eddie.service.JuegoService;
import com.eddie.model.Resultados;

public class JuegoServiceImpl implements JuegoService{

	private static Logger logger=LogManager.getLogger(JuegoServiceImpl.class);
	
	private JuegoDAO jdao=null;
	private ItemBibliotecaDAO ibDao=null;
	
	public JuegoServiceImpl() {
		jdao=new JuegoDAOImpl();
		ibDao=new ItemBibliotecaDAOImpl();
	}
	
	@Override
	public Resultados<Juego> findByJuegoCriteria(JuegoCriteria jc, String idioma, int startIndex, int count) throws DataException {
	
		if(logger.isDebugEnabled()) {
			logger.debug("Juego Criteria = "+jc.toString()+" , idioma = "+idioma);
		}
		Resultados<Juego> juegos=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		juegos=jdao.findByJuegoCriteria(jc, idioma, c, startIndex, count);
	
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		
		}
		return juegos;
		
	}

	@Override
	public Resultados<Juego> findAllByDate(String idioma , int startIndex, int count) throws DataException {
		boolean commit=false;
		Connection c=null;
		Resultados<Juego> juegos=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		juegos=jdao.findAllByDate(c, idioma, startIndex, count);
		
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return juegos;
	}

	@Override
	public List<Juego> findAllByValoracion(String idioma) throws DataException {
		boolean commit=false;
		Connection c=null;
		List<Juego> juegos=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		juegos=jdao.findAllByValoracion(c, idioma);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return juegos;
	}

	@Override
	public Juego findById(Integer id,String idioma) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+id+" , idioma = "+idioma);
		}
		Juego j=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		
		j = jdao.findById(c,id, idioma);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return j;
	}

	@Override
	public Juego create(Juego j) throws DataException {

		if(logger.isDebugEnabled()) {
			logger.debug("Juego = "+j.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		j = jdao.create(c,j);

		commit=true;
		
		
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return j;
	}

	@Override
	public boolean update(Juego j) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Juego = "+j.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
	          
            c = ConnectionManager.getConnection();

            c.setAutoCommit(false);

            jdao.update(c,j);
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
	public void delete(Integer id) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Id = "+id);
		}
		
		Connection connection = null;
        boolean commit = false;

        try {
          
            connection = ConnectionManager.getConnection();

            connection.setAutoCommit(false);

            jdao.delete(connection, id);          
                       
        } catch (SQLException e) {
        	logger.error(e.getMessage(),e);
            throw new DataException(e);

        } finally {
        	JDBCUtils.closeConnection(connection, commit);
        }		
		
	}

	@Override
	public boolean addComent(ItemBiblioteca it) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("ItemBiblioteca = "+it.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		it.setPuntuacion(null);
		it.setComprado("N");
		
		boolean existe=ibDao.exists(c, it.getEmail(), it.getIdJuego());
		
		if(existe==true) {
			ibDao.delete(c, it.getEmail(), it.getIdJuego());
		}
		ibDao.create(c, it);
		
		commit=true;
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return true;
	}

	@Override
	public boolean borrarComent(ItemBiblioteca it) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("ItemBiblioteca = "+it.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		it.setFechaComentario(null);
		it.setComentario(null);
		it.setPuntuacion(null);
		it.setComprado("No");
		ibDao.update(c, it);
		
		commit=true;
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return true;
	}

	@Override
	public List<ItemBiblioteca> findByJuego(Integer idJuego) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("IdJuego = "+idJuego);
		}
		List<ItemBiblioteca> biblio = null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		biblio=ibDao.findByJuego(c, idJuego);
		
		
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return biblio;
	}

	@Override
	public List<Juego> findByIDs(List<Integer> ids, String idioma) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("IdJuego = "+ids);
		}
		List<Juego> juegosBiblio = null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		juegosBiblio=jdao.findByIDs(c, ids, idioma);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return juegosBiblio;
	}

	@Override
	public Integer puntuacion(Integer idJuego) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("IdJuego = "+idJuego);
		}
		Integer juegoPuntuacion=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		juegoPuntuacion=jdao.puntuacion(c, idJuego);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return juegoPuntuacion;
	}


}
