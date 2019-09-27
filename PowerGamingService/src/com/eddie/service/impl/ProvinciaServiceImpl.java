package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.ProvinciaDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.ProvinciaDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.model.Provincia;
import com.eddie.service.ProvinciaService;

public class ProvinciaServiceImpl implements ProvinciaService{
	
	private static Logger logger=LogManager.getLogger(ProvinciaServiceImpl.class);
	
	ProvinciaDAO pdao=null;
	
	public ProvinciaServiceImpl() {
		pdao=new ProvinciaDAOImpl();
	}

	@Override
	public List<Provincia> findAllByIdPais(Integer idPais) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+idPais);
		}
		List<Provincia> p=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		p=pdao.findAllByIdPais(c, idPais);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return p;
	}

}
