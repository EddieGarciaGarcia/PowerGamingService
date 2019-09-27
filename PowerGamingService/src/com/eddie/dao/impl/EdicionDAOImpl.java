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

import com.eddie.dao.EdicionDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Edicion;

public class EdicionDAOImpl implements EdicionDAO{

	private static Logger logger=LogManager.getLogger(EdicionDAOImpl.class);
	
	@Override
	public Edicion findByIdEdicion(Connection conexion,Integer id) throws DataException {

		Edicion edicion;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select id_edicion,id_juego,id_formato,id_tipo_edicion,precio ");
			query.append("from edicion ");
			query.append("where id_edicion = ?");

			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			preparedStatement.setInt(1, id);
			resultSet=preparedStatement.executeQuery();
			
			if(resultSet.next()){
				edicion = loadNext(resultSet);
			}else {
				throw new InstanceNotFoundException("Error "+id+" id introducido incorrecto", Edicion.class.getName());
			}
			return edicion;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}
	
	@Override
	public List<Edicion> findByIdJuego(Connection conexion, Integer id) throws DataException {
		Edicion edicion;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		List<Edicion> ediciones;
		try {
			query = new StringBuilder();
			query.append("select id_edicion,id_juego,id_formato,id_tipo_edicion,precio ");
			query.append("from edicion ");
			query.append("where id_juego = ?");

			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			preparedStatement.setInt(1, id);
			resultSet=preparedStatement.executeQuery();

			ediciones=new ArrayList<>();
			while(resultSet.next()){
				edicion = loadNext(resultSet);
				ediciones.add(edicion);
			}
			return ediciones;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}
	
	@Override
	public List<Edicion> findByIdsJuego(Connection conexion, List<Integer> ids) throws DataException {
		Edicion edicion;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		List<Edicion> ediciones;
		try {

			query=new StringBuilder();
			query.append("select id_edicion,id_juego,id_formato,id_tipo_edicion,precio ");
			query.append("from edicion ");
			query.append("where id_juego in (");
			//Se añaden todos los ids por lo que se deben buscar
			JDBCUtils.anhadirIN(query, ids);

			preparedStatement=conexion.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			resultSet=preparedStatement.executeQuery();
			
			ediciones=new ArrayList<>();
			while(resultSet.next()){
				edicion=loadNext(resultSet);
				ediciones.add(edicion);
			}
			return ediciones;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}
	@Override
	public Edicion create(Connection conexion,Edicion edicion) throws DataException {
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("Insert Into edicion(id_juego,id_formato,id_tipo_edicion,precio) ");
			query.append("values (?,?,?,?)");
			
			preparedStatement=conexion.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setInt(1,edicion.getIdJuego());
			preparedStatement.setInt(2, edicion.getIdFormato());
			preparedStatement.setInt(3, edicion.getIdTipoEdicion());
			preparedStatement.setDouble(4, edicion.getPrecio());

			int insertRow=preparedStatement.executeUpdate();
			
			if(insertRow == 0) {
				throw new SQLException(" No se pudo insertar");
			}
			resultSet=preparedStatement.getGeneratedKeys();
			if(resultSet.next()) {
				Integer idEdicion=resultSet.getInt(1);
				edicion.setId(idEdicion);
			}else {
				throw new DataException("Problemas al autogenerar primary key");
			}
			return edicion;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	@Override
	public boolean update(Connection conexion,Edicion edicion) throws DataException {
		PreparedStatement preparedStatement = null;
		StringBuilder query;
		try {
			query = new StringBuilder(" UPDATE edicion");
			boolean first = true;
			if (edicion.getIdJuego()!=null) {
				JDBCUtils.addUpdate(query,first," id_juego = ?");
				first=false;
			}
			if (edicion.getIdFormato()!=null) {
				JDBCUtils.addUpdate(query,first," id_formato = ?");
				first=false;
			}
			if (edicion.getIdTipoEdicion()!=null) {
				JDBCUtils.addUpdate(query,first," id_tipo_edicion = ?");
				first=false;
			}
			if (edicion.getPrecio()!=null) {
				JDBCUtils.addUpdate(query,first," precio = ?");
			}
			query.append("WHERE email = ?");
			
			preparedStatement = conexion.prepareStatement(query.toString());

			int i = 1;
			if (edicion.getIdJuego()!=null) preparedStatement.setInt(i++,edicion.getIdJuego());
			if (edicion.getIdFormato()!=null) preparedStatement.setInt(i++,edicion.getIdFormato());
			if (edicion.getIdTipoEdicion()!=null) preparedStatement.setInt(i++,edicion.getIdTipoEdicion());
			if (edicion.getPrecio()!=null) preparedStatement.setDouble(i++,edicion.getPrecio());

			preparedStatement.setInt(i, edicion.getId());

			int updatedRows = preparedStatement.executeUpdate();

			return updatedRows != 0 && updatedRows <= 1;
		} catch (SQLException se) {
			logger.error(se.getMessage(),se);
			throw new DataException(se);    
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}              		
	
	}

	public Edicion loadNext(ResultSet resultSet)throws SQLException{
		Edicion edicion=new Edicion();
		edicion.setId(resultSet.getInt("id_edicion"));
		edicion.setIdJuego(resultSet.getInt("id_juego"));
		edicion.setIdFormato(resultSet.getInt("id_formato"));
		edicion.setIdTipoEdicion(resultSet.getInt("id_tipo_edicion"));
		edicion.setPrecio(resultSet.getDouble("precio"));
		
		return edicion;
	}
}
