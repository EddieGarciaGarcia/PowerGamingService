package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.cache.Cache;
import com.eddie.cache.CacheManager;
import com.eddie.cache.CacheNames;
import com.eddie.dao.CategoriaDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.CategoriaDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Categoria;
import com.eddie.service.CategoriaService;

public class CategoriaServiceImpl implements CategoriaService{

	private static Logger logger=LogManager.getLogger(CategoriaServiceImpl.class);
	
	private CategoriaDAO cdao=null;
	
	public CategoriaServiceImpl() {
		cdao=new CategoriaDAOImpl();
	}
	
	@Override
	public Categoria findById(Integer id, String idioma) throws InstanceNotFoundException, DataException {
		

		Categoria cate = null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		cate = cdao.findById(c, id, idioma);		
		
		}catch(DataException e) {
			logger.error(e.getMessage(),e);
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);;
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return cate;
	}

	@Override
	public List<Categoria> findAll(String idioma) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Idioma = "+idioma);
		}
		
		Cache<String, List> cacheCategoria= CacheManager.getInstance().getCache(CacheNames.CATEGORIACACHE, String.class, List.class);
		
		List<Categoria> categoria=cacheCategoria.get(idioma);
		
		boolean commit=false;
		
		if(categoria!=null) {
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
				
				categoria=cdao.findAll(c, idioma);
				
				cacheCategoria.put(idioma, categoria);
			
			}catch(SQLException e) {
				logger.error(e.getMessage(),e);
			}finally {
				JDBCUtils.closeConnection(c, commit);
			}
		}
		return categoria;
	}

	@Override
	public List<Categoria> findByJuego(Integer idJuego, String idioma) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+idJuego+" , idioma = "+idioma);
		}
		List<Categoria> cat=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		cat=cdao.findByJuego(c, idJuego, idioma);

		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return cat;
	}

}
