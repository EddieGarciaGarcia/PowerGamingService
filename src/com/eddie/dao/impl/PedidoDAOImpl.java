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

import com.eddie.dao.PedidoDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Pedido;
import com.eddie.model.Resultados;

public class PedidoDAOImpl implements PedidoDAO{
	
	private static Logger logger=LogManager.getLogger(PedidoDAOImpl.class);

	@Override
	public Resultados<Pedido> findByEmail(Connection conexion,String email, int startIndex, int count) throws DataException {
		Pedido pedido = new Pedido();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select id_pedido,email,total,fecha_pedido ");
			query.append("from pedido ");
			query.append("where email=? order by fecha_pedido desc");
			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			preparedStatement.setString(1, email);
			resultSet=preparedStatement.executeQuery();
			List<Pedido> pedidos = new ArrayList<>();
			int currentCount=0;
			if ((startIndex >=1) && resultSet.absolute(startIndex)) {
				do {
					pedidos.add(loadNext(resultSet , pedido));
					currentCount++;
				}while((currentCount<count) && resultSet.next());
			}
			int total= JDBCUtils.getTotalRows(resultSet);
			return new Resultados<>(pedidos,startIndex,total);
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	@Override
	public Pedido findByEmail(Connection conexion, String email) throws DataException {
		Pedido pedido = new Pedido();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select id_pedido,email,total,fecha_pedido ");
			query.append("from pedido ");
			query.append("where email like ? order by id_pedido desc limit 1");
			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			preparedStatement.setString(1, email);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()){
				return loadNext(resultSet,pedido);
			} else {
				throw new InstanceNotFoundException("Error "+email+" id introducido incorrecto", Pedido.class.getName());
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
	public boolean create(Connection conexion,Pedido pedido) throws DataException {
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("Insert Into usuarios_juego(email,iva,total,fecha_pedido) ");
			query.append("values (?,?,?,?)");
			preparedStatement=conexion.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
			int i=1;
			preparedStatement.setString(i++, pedido.getEmail());
			preparedStatement.setDouble(i++, Pedido.getIva());
			preparedStatement.setDouble(i++, pedido.getTotal());
			preparedStatement.setDate(i, new java.sql.Date(pedido.getFechaPedido().getTime()));
			int insertRow=preparedStatement.executeUpdate();
			if(insertRow == 0) {
				return false;
			}
			resultSet=preparedStatement.getGeneratedKeys();
			if(resultSet.next()) {
				Integer idPedido=resultSet.getInt(1);
				pedido.setIdPedido(idPedido);
			}else {
				throw new DataException("Problemas al autogenerar primary key");
			}
			return true;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	
	@Override
	public boolean delete(Connection conexion,Integer idPedido) throws DataException {
		PreparedStatement preparedStatement = null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("DELETE FROM pedido ");
			query.append("WHERE  id_pedido = ? ");
			preparedStatement = conexion.prepareStatement(query.toString());
			preparedStatement.setInt(1, idPedido);
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
	public List<Pedido> findByIds(Connection conexion, List<Integer> ids) throws DataException {
		Pedido pedido = new Pedido();
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select id_pedido, email, total, fecha_pedido ");
			query.append("from pedido ");
			query.append("where id_pedido in (");
			JDBCUtils.anhadirIN(query, ids);
			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			resultSet=preparedStatement.executeQuery();
			List<Pedido> pedidos=new ArrayList<>();
			while(resultSet.next()){
				pedidos.add(loadNext(resultSet, pedido));
			}
			return pedidos;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	public Pedido loadNext(ResultSet resultSet, Pedido pedido) throws SQLException{
		pedido.setIdPedido(resultSet.getInt("id_pedido"));
		pedido.setEmail(resultSet.getString("email"));
		pedido.setFechaPedido(resultSet.getDate("fecha_pedido"));
		pedido.setTotal(resultSet.getDouble("total"));
		return pedido;
	}

}
