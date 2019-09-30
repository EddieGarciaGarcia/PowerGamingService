package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.cache.Cache;
import com.eddie.cache.CacheManager;
import com.eddie.cache.CacheNames;
import com.eddie.dao.TipoEdicionDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.TipoEdicionDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.TipoEdicion;
import com.eddie.service.TipoEdicionService;

public class TipoEdicionServiceImpl implements TipoEdicionService{
	
	private static Logger logger=LogManager.getLogger(TipoEdicionServiceImpl.class);

	TipoEdicionDAO tedao=null;
	
	public TipoEdicionServiceImpl() {
		tedao=new TipoEdicionDAOImpl();
	}
	
	@Override
	public List<TipoEdicion> findbyIdsTipoEdicion(List<Integer> ids, String idioma) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+ids+" , idioma = "+idioma);
		}
		List<TipoEdicion> te=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		te = tedao.findbyIdsTipoEdicion(c, ids, idioma);	
				
		}catch(DataException e) {
			logger.error(e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return te;
	}

	@Override
	public List<TipoEdicion> findAll(String idioma) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Idioma = "+idioma);
		}
		
		Cache<String, List> cacheTipoEdicion= CacheManager.getInstance().getCache(CacheNames.TIPOEDICIONCACHE, String.class, List.class);
		
		List<TipoEdicion> tipoEdicion=cacheTipoEdicion.get(idioma);
		
		boolean commit=false;
		if(tipoEdicion!=null) {
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
			
			tipoEdicion=tedao.findAll(c, idioma);
			
			cacheTipoEdicion.put(idioma, tipoEdicion);
			
			}catch(SQLException e) {
				logger.error(e.getMessage(),e);
			}finally {
				JDBCUtils.closeConnection(c, commit);
			}
		}
		return tipoEdicion;
	}

}
