package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.LineaPedidoDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.LineaPedido;

public class LineaPedidoDAOImpl implements LineaPedidoDAO{
	
	private static Logger logger=LogManager.getLogger(LineaPedidoDAOImpl.class);

	@Override
	public List<LineaPedido> findByPedido(Connection conexion,Integer idPedido) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("ID= "+idPedido);
		}
		
		LineaPedido lp=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
	
			String sql;
			sql="select numero_linea,id_edicion,id_pedido,cantidad,precio from lineapedido where id_pedido=?";
			
			pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setInt(i++, idPedido);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			List<LineaPedido> lineas = new ArrayList<LineaPedido>();
			while(rs.next()){
				lp=loadNext(rs);
				lineas.add(lp);
			}
			return lineas;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	@Override
	public LineaPedido findById(Connection conexion,Integer numeroLinea) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Numero Linea = "+numeroLinea);
		}
		
		LineaPedido lp=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			
			String sql;
			sql="select numero_linea,id_edicion,id_pedido,cantidad,precio from lineapedido where numero_linea=?";
			
			pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setInt(i++, numeroLinea);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			if(rs.next()){
				lp=loadNext(rs);
			}else {
				throw new InstanceNotFoundException("Error "+numeroLinea+" id introducido incorrecto", LineaPedido.class.getName());
			}
			return lp;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	@Override
	public LineaPedido create(Connection conexion,LineaPedido lp) throws DuplicateInstanceException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Linea de Pedido = "+lp.toString());
		}
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			
			String sql;
			sql="Insert Into usuarios_juego(id_edicion,id_pedido,cantidad,precio) "
					+ "values (?,?,?,?)";
			
			pst=conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i=1;
			
			pst.setInt(i++,lp.getIdEdicion());
			pst.setInt(i++,lp.getPedido());
			pst.setInt(i++,lp.getCantidad());
			pst.setDouble(i++, lp.getPrecio());
			
			int insertRow=pst.executeUpdate();
			
			logger.debug(sql);
			
			if(insertRow == 0) {
				throw new SQLException(" No se pudo insertar");
			}
			
			return lp;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	@Override
	public long delete(Connection conexion,Integer id) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Numero Linea = "+id);
		}
		
		PreparedStatement preparedStatement = null;

		try {
			String queryString =	
					  "DELETE FROM lineapedido " 
					+ "WHERE  numero_linea = ? ";
			
			preparedStatement = conexion.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setInt(i++, id);
			
			logger.debug(queryString);
			
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(id,"No se elimino el pedido correctamente");
			} 

			return removedRows;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}
		
	}
	
	public LineaPedido loadNext(ResultSet rs) 
			throws SQLException,DataException{
				int i=1;
				Integer numeroLinea=rs.getInt(i++);
				Integer idEdicion  = rs.getInt(i++);
				Integer idPedido=rs.getInt(i++);
				Integer cantidad=rs.getInt(i++);
				Double precio=rs.getDouble(i++);
				
				LineaPedido lp= new LineaPedido();
				
				lp.setNumeroLinea(numeroLinea);
				lp.setIdEdicion(idEdicion);
				lp.setPedido(idPedido);
				lp.setCantidad(cantidad);
				lp.setPrecio(precio);
				
				return lp;
				
			
		}

	@Override
	public long deleteByPedido(Connection conexion, Integer id) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("idPedido = "+id);
		}
		
		PreparedStatement preparedStatement = null;

		try {
			String queryString =	
					  "DELETE FROM lineapedido " 
					+ "WHERE  id_pedido = ? ";
			
			preparedStatement = conexion.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setInt(i++, id);
			
			logger.debug(queryString);
			
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(id,"No se elimino el pedido correctamente");
			} 

			return removedRows;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}
	}
		
}
