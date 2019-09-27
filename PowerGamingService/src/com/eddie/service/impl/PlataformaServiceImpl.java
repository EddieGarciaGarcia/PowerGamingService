package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.cache.Cache;
import com.eddie.cache.CacheManager;
import com.eddie.cache.CacheNames;
import com.eddie.dao.PlataformaDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.PlataformaDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Plataforma;
import com.eddie.service.PlataformaService;

public class PlataformaServiceImpl implements PlataformaService{
	
	private static Logger logger=LogManager.getLogger(PlataformaServiceImpl.class);

	PlataformaDAO pdao=null;
	
	public PlataformaServiceImpl() {
		pdao= new PlataformaDAOImpl();
	}
	
	@Override
	public Plataforma findbyIdPlataforma(Integer id) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+id);
		}
		Plataforma p=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		p = pdao.findbyIdPlataforma(c, id);		

		}catch(DataException e) {
			logger.error(e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return p;
	}

	@Override
	public List<Plataforma> findAll() throws DataException {
		int i=1;
		
		Cache<Integer, List> cachePlataforma= CacheManager.getInstance().getCache(CacheNames.PLATAFORMACACHE, Integer.class, List.class);
		
		List<Plataforma> plataforma=cachePlataforma.get(i);
		
		
		boolean commit=false;
		if(plataforma!=null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Acierto cache: {}", i);
			}
		}else {
			if (logger.isDebugEnabled()) {
				logger.debug("Fallo cache: {}", i);
			}
			Connection c=null;
			try {
			c=ConnectionManager.getConnection();
			c.setAutoCommit(false);
			
			plataforma=pdao.findAll(c);
			
			cachePlataforma.put(i, plataforma);
			
			}catch(DataException e) {
				logger.error(e.getMessage(),e);
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
			}finally {
				JDBCUtils.closeConnection(c, commit);
			}
		}
		return plataforma;
	}

	@Override
	public List<Plataforma> findByJuego(Integer idJuego) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id = "+idJuego);
		}
		List<Plataforma> p=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		p=pdao.findByJuego(c, idJuego);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return p;
	}

}
