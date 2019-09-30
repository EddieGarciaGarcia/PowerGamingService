package com.eddie.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.PedidoDAO;
import com.eddie.utils.ConnectionManager;
import com.eddie.utils.JDBCUtils;
import com.eddie.dao.impl.PedidoDAOImpl;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Pedido;
import com.eddie.service.PedidoService;
import com.eddie.model.Resultados;

public class PedidoServiceImpl implements PedidoService{
	
	private static Logger logger=LogManager.getLogger(PedidoServiceImpl.class);
	
	PedidoDAO pdao=null;
	public PedidoServiceImpl() {
		pdao= new PedidoDAOImpl();
	}
	
	@Override
	public Resultados<Pedido> findByEmail(String email, int startIndex, int count) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		Resultados<Pedido> pedidos=null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		pedidos=pdao.findByEmail(c, email, startIndex, count);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return pedidos;
	}

	@Override
	public void delete(Integer idPedido) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Id= "+idPedido);
		}
		
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		pdao.delete(c, idPedido);

		commit=true;

		}catch(SQLException ed) {
			logger.error(ed.getMessage(),ed);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
	}

	@Override
	public Pedido create(Pedido p) throws DuplicateInstanceException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Pedido = "+p.toString());
		}
		
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		p = pdao.create(c,p);

		commit=true;

		}catch(SQLException ed) {
			logger.error(ed.getMessage(),ed);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return p;
	}

	@Override
	public Pedido findByEmail(String email) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+email);
		}
		
		Pedido p =null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		p = pdao.findByEmail(c,email);

		commit=true;

		}catch(SQLException ed) {
			logger.error(ed.getMessage(),ed);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return p;
	}

	@Override
	public List<Pedido> findByIds(List<Integer> ids) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("IdJuego = "+ids);
		}
		List<Pedido> pedido = null;
		boolean commit=false;
		Connection c=null;
		try {
		c=ConnectionManager.getConnection();
		c.setAutoCommit(false);
		
		pedido=pdao.findByIds(c, ids);
		
		}catch(SQLException e) {
			logger.error(e.getMessage(),e);
		}finally {
			JDBCUtils.closeConnection(c, commit);
		}
		return pedido;
	}

}
