package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.PedidoDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Pedido;
import com.eddie.model.Resultados;

public class PedidoDAOImpl implements PedidoDAO{
	
	private static Logger logger=LogManager.getLogger(PedidoDAOImpl.class);

	@Override
	public Resultados<Pedido> findByEmail(Connection conexion,String email, int startIndex, int count) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		
		Pedido p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="select id_pedido,email,iva,total,fecha_pedido from pedido where email=? order by fecha_pedido desc";
			
			pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setString(i++, email);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			List<Pedido> pedidos = new ArrayList<Pedido>();
			int currentCount=0;
			
			if ((startIndex >=1) && rs.absolute(startIndex)) {
				do {
					p=loadNext(rs);
					pedidos.add(p);
					currentCount++;
				}while((currentCount<count) && rs.next());
			}
			
			int total= JDBCUtils.getTotalRows(rs);
			
			if(logger.isDebugEnabled()) {
				logger.debug("Total peticiones: "+total);
			}
			Resultados<Pedido> resultados= new Resultados<Pedido>(pedidos,startIndex,total);
			return resultados;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	@Override
	public Pedido findByEmail(Connection conexion,String email ) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Id = "+email);
		}
		
		Pedido p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="select id_pedido,email,iva,total,fecha_pedido from pedido where email like ? order by id_pedido desc limit 1";
			
			pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setString(i++, email);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			if(rs.next()){
				p=loadNext(rs);
	
			}
			else {
				throw new InstanceNotFoundException("Error "+email+" id introducido incorrecto", Pedido.class.getName());
			}
			return p;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	@Override
	public Pedido create(Connection conexion,Pedido p) throws DuplicateInstanceException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Pedido = "+p.toString());
		}
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="Insert Into usuarios_juego(email,iva,total,fecha_pedido) "
					+ "values (?,?,?,?)";
			
			pst=conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i=1;
			
			pst.setString(i++,p.getEmail());
			pst.setDouble(i++, p.getIva());
			pst.setDouble(i++, p.getTotal());
			pst.setDate(i++, new java.sql.Date(p.getFecha_pedido().getTime()));
		
			logger.debug(sql);
			
			int insertRow=pst.executeUpdate();
			
			if(insertRow == 0) {
				throw new SQLException(" No se pudo insertar");
			}
			
			rs=pst.getGeneratedKeys();
			if(rs.next()) {
				Integer idPedido=rs.getInt(1);
				p.setIdPedido(idPedido);
			}else {
				throw new DataException("Problemas al autogenerar primary key");
			}
			return p;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	
	@Override
	public void delete(Connection conexion,Integer idPedido) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Id = "+idPedido);
		}
		
		PreparedStatement preparedStatement = null;

		try {
			String queryString =	
					  "DELETE FROM pedido " 
					+ "WHERE  id_pedido = ? ";
			
			preparedStatement = conexion.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setInt(i++, idPedido);
			
			logger.debug(queryString);
			
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(idPedido,"No se elimino el pedido correctamente");
			} 


		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}
		
	}

	public Pedido loadNext(ResultSet rs) 
			throws SQLException,DataException{
				int i=1;
				Integer idPedido=rs.getInt(i++);
				String email  = rs.getString(i++);
				Integer iva=rs.getInt(i++);
				Date fechaPedido=rs.getDate(i++);	
				Double total=rs.getDouble(i++);
				
				Pedido p= new Pedido();
				
				p.setIdPedido(idPedido);
				p.setEmail(email);
				iva=p.getIva();
				p.setFecha_pedido(fechaPedido);
				p.setTotal(total);
				
				return p;
				
			
		}

	@Override
	public List<Pedido> findByIds(Connection conexion, List<Integer> ids) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Id= "+ids);
		}
		
		List<Pedido> pedidos=new ArrayList<Pedido>();
		Pedido pedido=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
			StringBuilder sql= null;
			sql=new StringBuilder("select id_pedido, email, iva, total, fecha_pedido from pedido where id_pedido in (");

			JDBCUtils.anhadirIN(sql, ids);
			
			pst=conexion.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			while(rs.next()){
				pedido=loadNext(rs);
				pedidos.add(pedido);
			}
			
			return pedidos;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}
		
}
