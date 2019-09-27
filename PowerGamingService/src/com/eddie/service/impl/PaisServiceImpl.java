package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.PaisDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.PaisDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.model.Pais;
import com.eddie.service.PaisService;

public class PaisServiceImpl implements PaisService{
	
	private static Logger logger=LogManager.getLogger(PaisServiceImpl.class);
	
	 PaisDAO pdao=null;
	 
	 public PaisServiceImpl() {
		 pdao=new PaisDAOImpl();
	 }
	@Override
	public List<Pais> findAll() throws DataException {
		boolean commit=false;
		Connection c=null;
		List<Pais> pais=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		pais=pdao.findAll(c)	;
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return pais;
	}

}
