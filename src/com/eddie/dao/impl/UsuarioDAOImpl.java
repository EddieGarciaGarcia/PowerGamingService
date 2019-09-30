package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.UsuarioDAO;

import com.eddie.utils.JDBCUtils;
import com.eddie.utils.PasswordEncryptionUtil;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO{
	
	private static Logger logger=LogManager.getLogger(UsuarioDAOImpl.class);

	@Override
	public boolean create(Connection connection, Usuario usuario) throws DataException {
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("Insert Into usuario(email,nombre,apellido1,apellido2,telefono,password, fecha_nacimiento, genero,nombre_user) ");
			query.append("values (?,?,?,?,?,?,?,?,?)");
			preparedStatement=connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
			int i=1;
			if(usuario.getEmail()==null || usuario.getEmail().equals("")) {
				logger.warn("fallo email null o vacio");
			}else {
				preparedStatement.setString(i++,usuario.getEmail());
			}
			preparedStatement.setString(i++, usuario.getNombre());
			preparedStatement.setString(i++, usuario.getApellido1());
			preparedStatement.setString(i++, usuario.getApellido2());
			preparedStatement.setString(i++, usuario.getTelefono());
			preparedStatement.setString(i++, PasswordEncryptionUtil.encryptPassword(usuario.getPassword()));
			preparedStatement.setDate(i++, new java.sql.Date(usuario.getFechaNacimiento().getTime()));
			preparedStatement.setString(i++, usuario.getGenero());
			preparedStatement.setString(i, usuario.getNombreUser());

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
	public boolean update(Connection connection,Usuario usuario) throws DataException{
		PreparedStatement preparedStatement = null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("UPDATE Usuario ");
			boolean first = true;
			if (usuario.getNombre()!=null) {
				JDBCUtils.addUpdate(query,first,"nombre = ? ");
				first=false;
			}
			if (usuario.getApellido1()!=null) {
				JDBCUtils.addUpdate(query,first,"apellido1 = ? ");
				first=false;
			}
			if (usuario.getApellido2()!=null) {
				JDBCUtils.addUpdate(query,first,"apellido2 = ? ");
				first=false;
			}
			if (usuario.getTelefono()!=null) {
				JDBCUtils.addUpdate(query,first,"telefono = ? ");
				first=false;
			}
			if (usuario.getPassword()!=null) {
				JDBCUtils.addUpdate(query,first,"password = ? ");
				first=false;
			}
			if (usuario.getFechaNacimiento()!=null) {
				JDBCUtils.addUpdate(query,first,"fecha_nacimiento = ? ");
				first=false;
			}
			if (usuario.getNombreUser()!=null) {
				JDBCUtils.addUpdate(query,first,"nombre_user = ? ");
				first=false;
			}
			if (usuario.getGenero()!=null) {
				JDBCUtils.addUpdate(query,first,"genero = ? ");
			}
			query.append(" WHERE email = ?");
			
			preparedStatement = connection.prepareStatement(query.toString());

			int i = 1;
			if (usuario.getNombre()!=null) preparedStatement.setString(i++,usuario.getNombre());
			if (usuario.getApellido1()!=null) preparedStatement.setString(i++,usuario.getApellido1());
			if (usuario.getApellido2()!=null) preparedStatement.setString(i++,usuario.getApellido2());
			if (usuario.getTelefono()!=null) preparedStatement.setString(i++,usuario.getTelefono());
			if (usuario.getPassword()!=null) preparedStatement.setString(i++,PasswordEncryptionUtil.encryptPassword(usuario.getPassword()));
			if (usuario.getFechaNacimiento()!=null) preparedStatement.setDate(i++,new java.sql.Date(usuario.getFechaNacimiento().getTime()));
			if (usuario.getNombreUser()!=null) preparedStatement.setString(i++,usuario.getNombreUser());
			if (usuario.getGenero()!=null) preparedStatement.setString(i++,usuario.getGenero());
			preparedStatement.setString(i, usuario.getEmail());
			int updatedRows = preparedStatement.executeUpdate();
			return updatedRows == 1;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);    
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}              		
	}
	
	@Override
	public boolean delete(Connection connection, String email) throws DataException{
		PreparedStatement preparedStatement = null;
		StringBuilder query;
		try {
			query= new StringBuilder();
			query.append("DELETE FROM Usuario ");
			query.append("WHERE email = ?");
			preparedStatement = connection.prepareStatement(query.toString());
			preparedStatement.setString(1, email);
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
	public Usuario findById(Connection connection, String email) throws DataException {
		Usuario usuario;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select email,nombre,apellido1,apellido2,telefono,password,fecha_nacimiento,genero,nombre_user ");
			query.append("from usuario ");
			query.append("where email = ?");
			preparedStatement=connection.prepareStatement(query.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			preparedStatement.setString(1, email);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()){
				usuario = new Usuario();
				return loadNext(resultSet, usuario);
			}else{
				throw new InstanceNotFoundException("Error "+email+" id introducido incorrecto", Usuario.class.getName());
			}
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	public Usuario loadNext(ResultSet rs, Usuario usuario) throws SQLException{
			usuario.setEmail( rs.getString("email"));
			usuario.setNombre(rs.getString("nombre"));
			usuario.setApellido1(rs.getString("apellido1"));
			usuario.setApellido2(rs.getString("apellido2"));
			usuario.setTelefono(rs.getString("telefono"));
			usuario.setPassword(rs.getString("password"));
			usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
			usuario.setNombreUser(rs.getString("genero"));
			usuario.setGenero(rs.getString("nombre_user"));
			return usuario;
	}
}
