package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.LineaPedidoDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.LineaPedidoDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.LineaPedido;
import com.eddie.service.LineaPedidoService;

public class LineaPedidoServiceImpl implements LineaPedidoService{
	
	private static Logger logger=LogManager.getLogger(LineaPedidoServiceImpl.class);

	LineaPedidoDAO lpdao=null;
	
	public LineaPedidoServiceImpl() {
		lpdao=new LineaPedidoDAOImpl();
	}
	
	
	@Override
	public List<LineaPedido> findByPedido(Integer idPedido) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+idPedido);
		}
		List<LineaPedido> lineaspedido=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		lineaspedido=lpdao.findByPedido(c, idPedido);

		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return lineaspedido;
	}

	@Override
	public LineaPedido findById(Integer numeroLinea) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Numero linea= "+numeroLinea);
		}
		LineaPedido lp=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		lp = lpdao.findById(c,numeroLinea);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return lp;
	}

	@Override
	public LineaPedido create(LineaPedido lp) throws DuplicateInstanceException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Linea Pedido = "+lp.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		lp = lpdao.create(c,lp);

		commit=true;
		
		}catch(SQLException ed) {
			logger.error(ed.getMessage(),ed);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return lp;
	}

	@Override
	public void delete(Integer id) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Id= "+id);
		}
		
		boolean commit=false;
		Connection c=null;
		try {
	          
            c = ConnectionManager.getConnection();

            c.setAutoCommit(false);

            lpdao.delete(c,id);
            commit = true;
            
        } catch (SQLException ed) {
        	logger.error(ed.getMessage(),ed);
            throw new DataException(ed);

        } finally {
        	JDBCUtils.closeConnection(c, commit);
        }
		
	}


	@Override
	public void deleteByPedido(Integer id) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Id= "+id);
		}
		
		boolean commit=false;
		Connection c=null;
		try {
	          
            c = ConnectionManager.getConnection();

            c.setAutoCommit(false);

            lpdao.deleteByPedido(c,id);
            commit = true;
            
        } catch (SQLException ed) {
        	logger.error(ed.getMessage(),ed);
            throw new DataException(ed);

        } finally {
        	JDBCUtils.closeConnection(c, commit);
        }
		
	}

}
