package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.EdicionDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.EdicionDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Edicion;
import com.eddie.service.EdicionService;

public class EdicionServiceImpl implements EdicionService{

	private static Logger logger=LogManager.getLogger(EdicionServiceImpl.class);
	
	private EdicionDAO edao=null;
	public EdicionServiceImpl() {
		edao= new EdicionDAOImpl();
	}
	
	
	@Override
	public Edicion finById(Integer id) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Id = "+id);
		}
		Edicion edicion=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		edicion=edao.findByIdEdicion(c, id);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return edicion;
	}
	
	@Override
	public List<Edicion> findByIdJuego(Integer id) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Id = "+id);
		}
		List<Edicion> ediciones=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		ediciones=edao.findByIdJuego(c, id);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return ediciones;
	}
	
	@Override
	public List<Edicion> findByIdsJuego(List<Integer> ids) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Id = "+ids);
		}
		List<Edicion> ediciones=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		ediciones=edao.findByIdsJuego(c, ids);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return ediciones;
	}

	@Override
	public Edicion create(Edicion e) throws DuplicateInstanceException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Edicion = "+e.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		e = edao.create(c,e);

		commit=true;
		
		}catch(SQLException ed) {
			logger.error(ed.getMessage(),ed);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return e;
	}

	@Override
	public boolean update(Edicion e) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Edicion = "+e.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
	          
            c = ConnectionManager.getConnection();

            c.setAutoCommit(false);

            edao.update(c,e);
            commit = true;
            
        } catch (SQLException ed) {
        	logger.error(ed.getMessage(),ed);
            throw new DataException(ed);
        } finally {
        	JDBCUtils.closeConnection(c, commit);
        }
		return true;
	}

	

	

}
