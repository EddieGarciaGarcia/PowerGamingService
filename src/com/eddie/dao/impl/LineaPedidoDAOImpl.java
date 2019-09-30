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
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.LineaPedido;

public class LineaPedidoDAOImpl implements LineaPedidoDAO{
	
	private static Logger logger=LogManager.getLogger(LineaPedidoDAOImpl.class);

	@Override
	public List<LineaPedido> findByPedido(Connection conexion,Integer idPedido) throws DataException {
		LineaPedido lineaPedido = new LineaPedido();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select numero_linea,id_edicion,id_pedido,cantidad,precio ");
			query.append("from lineapedido ");
			query.append("where id_pedido = ?");
			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			preparedStatement.setInt(1, idPedido);
			resultSet=preparedStatement.executeQuery();
			List<LineaPedido> lineas = new ArrayList<>();
			while(resultSet.next()){
				lineas.add(loadNext(resultSet, lineaPedido));
			}
			return lineas;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	@Override
	public LineaPedido findById(Connection conexion,Integer numeroLinea) throws DataException {
		LineaPedido lineaPedido = new LineaPedido();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select numero_linea,id_edicion,id_pedido,cantidad,precio ");
			query.append("from lineapedido ");
			query.append("where numero_linea=?");
			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			preparedStatement.setInt(1, numeroLinea);
			resultSet=preparedStatement.executeQuery();

			if(resultSet.next()){
				return loadNext(resultSet, lineaPedido);
			}else {
				throw new InstanceNotFoundException("Error "+numeroLinea+" id introducido incorrecto", LineaPedido.class.getName());
			}
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	@Override
	public boolean create(Connection conexion, LineaPedido lineaPedido) throws DataException {
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("Insert Into usuarios_juego(id_edicion,id_pedido,cantidad,precio) ");
			query.append("values (?,?,?,?)");
			preparedStatement=conexion.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
			int i=1;
			preparedStatement.setInt(i++,lineaPedido.getIdEdicion());
			preparedStatement.setInt(i++,lineaPedido.getPedido());
			preparedStatement.setInt(i++,lineaPedido.getCantidad());
			preparedStatement.setDouble(i, lineaPedido.getPrecio());
			int insertRow=preparedStatement.executeUpdate();
			return insertRow != 0;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	@Override
	public boolean delete(Connection conexion,Integer id) throws DataException {
		PreparedStatement preparedStatement = null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("DELETE FROM lineapedido ");
			query.append("WHERE  numero_linea = ?");
			preparedStatement = conexion.prepareStatement(query.toString());
			preparedStatement.setInt(1, id);
			int removedRows = preparedStatement.executeUpdate();
			return removedRows != 0;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}
	}
	
	@Override
	public boolean deleteByPedido(Connection conexion, Integer id) throws DataException {
		PreparedStatement preparedStatement = null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("DELETE FROM lineapedido ");
			query.append("WHERE  id_pedido = ? ");
			preparedStatement = conexion.prepareStatement(query.toString());
			preparedStatement.setInt(1, id);
			int removedRows = preparedStatement.executeUpdate();
			return removedRows != 0;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	public LineaPedido loadNext(ResultSet resultSet, LineaPedido lineaPedido) throws SQLException{
		lineaPedido.setNumeroLinea(resultSet.getInt("numero_linea"));
		lineaPedido.setIdEdicion(resultSet.getInt("id_edicion"));
		lineaPedido.setPedido(resultSet.getInt("id_pedido"));
		lineaPedido.setCantidad(resultSet.getInt("cantidad"));
		lineaPedido.setPrecio(resultSet.getDouble("precio"));
		return lineaPedido;
	}

}
