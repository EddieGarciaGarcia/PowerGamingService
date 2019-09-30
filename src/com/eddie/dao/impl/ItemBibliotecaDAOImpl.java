package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.ItemBibliotecaDAO;

import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Resultados;


public class ItemBibliotecaDAOImpl implements ItemBibliotecaDAO{
	
	private static Logger logger=LogManager.getLogger(ItemBibliotecaDAOImpl.class);

	@Override
	public Resultados<ItemBiblioteca> findByUsuario(Connection connection, String email, int startIndex, int count) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		
		ItemBiblioteca ib=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="select email,id_juego,puntuacion,comprado,comentario,fecha_comentario from usuarios_juego where email=?";
			
			pst=connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setString(i++, email);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			int currentCount=0;
			List<ItemBiblioteca> biblioteca = new ArrayList<ItemBiblioteca>();
			if ((startIndex >=1) && rs.absolute(startIndex)) {
				do {
					ib=loadNext(rs);
					biblioteca.add(ib);
					currentCount++;
				}while((currentCount<count) && rs.next());
			}
			int total= JDBCUtils.getTotalRows(rs);
			
			if(logger.isDebugEnabled()) {
				logger.debug("Total peticiones: "+total);
			}
			Resultados<ItemBiblioteca> resultados= new Resultados<ItemBiblioteca>(biblioteca,startIndex,total);
			return resultados;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	
	
/*
	@Override
	public List<ItemBiblioteca> findByUsuarioComprobar(Connection connection,String email) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		
		ItemBiblioteca ib=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="select email,id_juego,puntuacion,comprado,comentario,fecha_comentario from usuarios_juego where email=?";
			
			pst=connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setString(i++, email);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			List<ItemBiblioteca> biblioteca = new ArrayList<ItemBiblioteca>();
			
			while(rs.next()){	
					ib=loadNext(rs);
					biblioteca.add(ib);
			}
				
			
			
			return biblioteca;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}
	
	*/
	
	// public boolean exists(c, email, idJuego)

	
	@Override
	public boolean exists(Connection c, String email, Integer idJuego) throws DataException {
		boolean exist = false;
		if(logger.isDebugEnabled()) {
			logger.debug("Email = {}", email+" Idjuego = {}",idJuego);
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {

			String queryString = "SELECT email,id_juego FROM usuarios_juego WHERE UPPER(email) LIKE UPPER(?) AND id_juego= ?";
			
			logger.debug(queryString);
			preparedStatement = c.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setString(i++, email);
			preparedStatement.setInt(i++, idJuego);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				exist = true;
			}

		} catch (SQLException e) {
			logger.info(e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}

		return exist;
	} 
	


	// public List<Integer>  exists(c, String email, List<Integer> idsJuegos)
	@Override
	public List<Integer> exists(Connection c, String email, List<Integer> idsJuegos) throws DataException {
		if(logger.isDebugEnabled()) {
			logger.debug("Email = {}", email+" Idjuego = {}",idsJuegos);
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Integer> listaIdJuegoEnBiblitoeca=new ArrayList<Integer>();

		try {
			StringBuilder sql= null;
			sql =new StringBuilder("SELECT id_juego FROM usuarios_juego WHERE UPPER(email) = UPPER(?) AND id_juego in (");
			
			JDBCUtils.anhadirIN(sql, idsJuegos);
			
			logger.debug(sql);
			preparedStatement = c.prepareStatement(sql.toString());

			int i = 1;
			preparedStatement.setString(i++, email);

			resultSet = preparedStatement.executeQuery();
			
			
			Integer idJuego = null;
			while (resultSet.next()) {
				idJuego = resultSet.getInt(1);
				listaIdJuegoEnBiblitoeca.add(idJuego);
			}

		} catch (SQLException e) {
			logger.info(e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}

		return listaIdJuegoEnBiblitoeca;

	}
	
	
	@Override
	public List<ItemBiblioteca> findByJuego(Connection connection, Integer idJuego) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Id= "+idJuego);
		}
		
		ItemBiblioteca ib=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="select email,id_juego,puntuacion,comprado,comentario,fecha_comentario from usuarios_juego where id_juego=? order by fecha_comentario desc";
			
			pst=connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setInt(i++, idJuego);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			List<ItemBiblioteca> biblioteca = new ArrayList<ItemBiblioteca>();
			while(rs.next()){
				ib=loadNext(rs);
				biblioteca.add(ib);
			}
			return biblioteca;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	@Override
	public ItemBiblioteca create(Connection connection, ItemBiblioteca b) throws DuplicateInstanceException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("ItemBiblioteca = "+b.toString());
		}
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="Insert Into usuarios_juego(email,id_juego,puntuacion,comprado,comentario,fecha_comentario) "
					+ "values (?,?,?,?,?,?)";
			
			pst=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i=1;
			if(b.getEmail()==null || b.getEmail().equals("")) {
				logger.warn("fallo email null o vacio");
			}else {
				pst.setString(i++,b.getEmail());
			}
			
			pst.setInt(i++, b.getIdJuego());
			
			if(b.getPuntuacion()==null) {
				pst.setNull(i++, Types.NULL);
			}else {
				pst.setInt(i++,b.getPuntuacion());
			}
			if(b.getComprado()==null) {
				pst.setNull(i++, Types.NULL);
			}else {
				pst.setString(i++,b.getComprado());
			}
			if(b.getComentario()==null) {
				pst.setNull(i++, Types.NULL);
			}else {
				pst.setString(i++,b.getComentario());
			}
			
			if(b.getFechaComentario()==null) {
				pst.setNull(i++, Types.NULL);
			}else {
				pst.setDate(i++, (java.sql.Date) b.getFechaComentario());
			}
			
			logger.debug(sql);
			
			int insertRow=pst.executeUpdate();
			
			if(insertRow == 0) {
				throw new SQLException(" No se pudo insertar");
			}
			
			return b;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}
	
	@Override
	public long delete(Connection connection,String email, Integer idJuego) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email+" , IdJuego = "+idJuego);
		}
		
		PreparedStatement preparedStatement = null;

		try {
			String queryString =	
					  "DELETE FROM usuarios_juego " 
					+ "WHERE email like ? and id_juego = ? ";
			
			preparedStatement = connection.prepareStatement(queryString);

			int i = 1;
			preparedStatement.setString(i++, email);
			preparedStatement.setInt(i++, idJuego);
			
			logger.debug(queryString);
			
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(idJuego,"No se elimino el juego correctamente");
			} 
			

			return removedRows;

		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}
	}
	
	@Override
	public ItemBiblioteca update(Connection connection, ItemBiblioteca b) throws DuplicateInstanceException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Biblioteca = "+b.toString());
		}
		
		PreparedStatement preparedStatement = null;
	
		StringBuilder sqlupdate;
		try {	
		
			sqlupdate = new StringBuilder(" UPDATE usuarios_juego");
			
			boolean first = true;
			
			if (b.getPuntuacion()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," puntuacion = ?");
				first=false;
			}
			
			if (b.getComprado()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," comprado = ?");
				first=false;
			}
			
			if (b.getComentario()==null) {
				JDBCUtils.addUpdate(sqlupdate,first," comentario = ?");
				first=false;
			}
			
			if (b.getFechaComentario()==null) {
				JDBCUtils.addUpdate(sqlupdate,first," fecha_comentario = ?");
				first=false;
			}
			
			sqlupdate.append("WHERE email like ? and id_juego = ?");
			
			preparedStatement = connection.prepareStatement(sqlupdate.toString());
			

			int i = 1;
			if (b.getPuntuacion()!=null) 
				preparedStatement.setInt(i++,b.getPuntuacion());
			
			if (b.getComprado()!=null) 
				preparedStatement.setString(i++,b.getComprado());
			if (b.getComentario()!=null) 
				preparedStatement.setString(i++,b.getComentario());
			if (b.getFechaComentario()!=null) 
				preparedStatement.setDate(i++,(java.sql.Date)b.getFechaComentario());
		
			preparedStatement.setString(i++, b.getEmail());
			preparedStatement.setInt(i++, b.getIdJuego());

			logger.debug(sqlupdate);
			
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows > 1) {
				throw new SQLException();
			}     
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);    
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}
		return b;              		
	}
	
	@Override
	public ItemBiblioteca fingByIdEmail(Connection c, String email, Integer idJuego) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Email = {}", email+" Idjuego = {}",idJuego);
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ItemBiblioteca it= null;
		try {

			String queryString = "SELECT email,id_juego,puntuacion,comprado,comentario,fecha_comentario FROM usuarios_juego WHERE email LIKE '"+email+"' AND id_juego="+idJuego;
			
			logger.debug(queryString);
			preparedStatement = c.prepareStatement(queryString);

			int i = 1;

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				it=loadNext(resultSet);
			}

		} catch (SQLException e) {
			logger.info(e.getMessage(), e);
			throw new DataException(e);
		} finally {
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}

		return it;
}
	

	
	public ItemBiblioteca loadNext(ResultSet rs) 
			throws SQLException,DataException{
				int i=1;
				String email  = rs.getString(i++);
				Integer idJuego = rs.getInt(i++);
				Integer puntuacion=rs.getInt(i++);
				String comprado=rs.getString(i++);
				String comentario=rs.getString(i++);
				Date fechaComentario=rs.getDate(i++);
			
				ItemBiblioteca ib= new ItemBiblioteca();
				
				ib.setEmail(email);
				ib.setIdJuego(idJuego);
				ib.setPuntuacion(puntuacion);
				ib.setComprado(comprado);
				ib.setComentario(comentario);
				ib.setFechaComentario(fechaComentario);
				
				
				return ib;
	}






	


	

	
		
}
