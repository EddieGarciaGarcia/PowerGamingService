package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.cache.Cache;
import com.eddie.cache.CacheManager;
import com.eddie.cache.CacheNames;
import com.eddie.dao.IdiomaDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.IdiomaDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Idioma;
import com.eddie.service.IdiomaService;

public class IdiomaServiceImpl implements IdiomaService{
	
	private static Logger logger=LogManager.getLogger(IdiomaServiceImpl.class);
	
	IdiomaDAO idao=null;
	
	public IdiomaServiceImpl() {
		idao=new IdiomaDAOImpl();
	}
	
	@Override
	public Idioma findById(String id, String idioma) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+id+" , idioma = "+idioma);
		}
		Idioma i=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		i = idao.findById(c, id, idioma);		
				
		}catch(DataException e) {
			logger.error(e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return i;
	}

	@Override
	public List<Idioma> findAll(String idioma) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Idioma = "+idioma);
		}
		
		Cache<String, List> cacheIdioma= CacheManager.getInstance().getCache(CacheNames.IDIOMACACHE, String.class, List.class);
		
		List<Idioma> idi=cacheIdioma.get(idioma);
		
		boolean commit=false;
		if(idi!=null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Acierto cache: {}", idioma);
			}
		}else {
			if (logger.isDebugEnabled()) {
				logger.debug("Fallo cache: {}", idioma);
			}
			Connection c=null;
			try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);
			
			idi=idao.findAll(c, idioma);
			
			cacheIdioma.put(idioma, idi);
			
			}catch(SQLException e) {
				logger.error(e.getMessage(),e);
			}finally {
				JDBCUtils.closeConnection(c, commit);
			}
		}
		return idi;
	}

	@Override
	public List<Idioma> findByJuego(Integer idJuego, String idioma) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+idJuego+" , idioma = "+idioma);
		}
		List<Idioma> i=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		i=idao.findByJuego(c, idJuego, idioma);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return i;
	}

}
