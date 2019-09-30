package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.cache.Cache;
import com.eddie.cache.CacheManager;
import com.eddie.cache.CacheNames;
import com.eddie.dao.CreadorDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.CreadorDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Creador;
import com.eddie.service.CreadorService;

public class CreadorServiceImpl implements CreadorService{

	private static Logger logger=LogManager.getLogger(CreadorServiceImpl.class);
	
	CreadorDAO cdao=null;
	
	public CreadorServiceImpl() {
		cdao=new CreadorDAOImpl();
	}
	
	@Override
	public Creador findbyIdCreador(Integer id) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+id);
		}
		Creador cre=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		cre= cdao.findbyIdCreador(c, id);		

		}catch(DataException e) {
			logger.error(e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return cre;
	}

	@Override
	public List<Creador> findAll() throws DataException {
		int i=1;
		
		Cache<Integer, List> cacheCreador= CacheManager.getInstance().getCache(CacheNames.CREADORCACHE, Integer.class, List.class);
		
		List<Creador> creador=cacheCreador.get(i);
		
		boolean commit=false;
		if(creador!=null) {
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
			
			creador=cdao.findAll(c);
			
			cacheCreador.put(i, creador);
			
			}catch(SQLException e) {
				logger.error(e.getMessage(),e);
			}finally {
				JDBCUtils.closeConnection(c, commit);
			}
		}
		return creador;
	}

}
