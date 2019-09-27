package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
	public Usuario create(Usuario u, Connection connection) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Usuario = "+u.toString());
		}
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try {
		
			String sql;
			sql="Insert Into usuario(email,nombre,apellido1,apellido2,telefono,password, fecha_nacimiento, genero,nombre_user) "
					+ "values (?,?,?,?,?,?,?,?,?)";
			
			pst=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i=1;
			if(u.getEmail()==null || u.getEmail().equals("")) {
				logger.warn("fallo email null o vacio");
			}else {
				pst.setString(i++,u.getEmail());
			}
			pst.setString(i++, u.getNombre());
			pst.setString(i++, u.getApellido1());
			pst.setString(i++, u.getApellido2());
			pst.setString(i++, u.getTelefono());
			pst.setString(i++, PasswordEncryptionUtil.encryptPassword(u.getPassword()));
			pst.setDate(i++, new java.sql.Date(u.getFechaNacimiento().getTime()));
			pst.setString(i++, u.getGenero());
			pst.setString(i++, u.getNombreUser());
			
			logger.debug(sql);
			
			int insertRow=pst.executeUpdate();
			if(insertRow == 0) {
				throw new SQLException(" No se pudo insertar");	
			}
		
			return u;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}

	@Override
	public boolean update(Usuario u, Connection connection) throws InstanceNotFoundException, DataException{
		
		if(logger.isDebugEnabled()) {
			logger.debug("Usuario = "+u.toString());
		}
		
		PreparedStatement preparedStatement = null;
		StringBuilder sqlupdate;
		try {	
			
			sqlupdate = new StringBuilder(" UPDATE Usuario");
			
			boolean first = true;
			
			if (u.getNombre()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," nombre = ?");
				first=false;
			}
			
			if (u.getApellido1()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," apellido1 = ?");
				first=false;
			}
			
			if (u.getApellido2()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," apellido2 = ?");
				first=false;
			}
			
			if (u.getTelefono()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," telefono = ?");
				first=false;
			}
					
			if (u.getPassword()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," password = ?");
				first=false;
			}
			if (u.getFechaNacimiento()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," fecha_nacimiento = ?");
				first=false;
			}
			
			if (u.getNombreUser()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," nombre_user = ?");
				first=false;
			}
			
			if (u.getGenero()!=null) {
				JDBCUtils.addUpdate(sqlupdate,first," genero = ?");
				first=false;
			}
			sqlupdate.append(" WHERE email = ?");
			
			preparedStatement = connection.prepareStatement(sqlupdate.toString());
			

			int i = 1;
			if (u.getNombre()!=null) 
				preparedStatement.setString(i++,u.getNombre());
			
			if (u.getApellido1()!=null) 
				preparedStatement.setString(i++,u.getApellido1());
			if (u.getApellido2()!=null) 
				preparedStatement.setString(i++,u.getApellido2());
			if (u.getTelefono()!=null) 
				preparedStatement.setString(i++,u.getTelefono());
			if (u.getPassword()!=null) 
				preparedStatement.setString(i++,PasswordEncryptionUtil.encryptPassword(u.getPassword()));
			if (u.getFechaNacimiento()!=null) 
				preparedStatement.setDate(i++,new java.sql.Date(u.getFechaNacimiento().getTime()));
			if (u.getNombreUser()!=null) 
				preparedStatement.setString(i++,u.getNombreUser());
			if (u.getGenero()!=null) 
				preparedStatement.setString(i++,u.getGenero());
			
			preparedStatement.setString(i++, u.getEmail());

			int updatedRows = preparedStatement.executeUpdate();

			logger.debug(sqlupdate);
			
			if (updatedRows > 1) {
				throw new SQLException("email duplicado = '" + u.getEmail() + "' en table 'Juego'");
			}     
			return true;
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new DataException(e);    
		} finally {
			JDBCUtils.closeStatement(preparedStatement);
		}              		
	}
	
	@Override
	public long delete(String email, Connection connection) throws DataException{
		
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		
		PreparedStatement preparedStatement = null;

		try {
			String queryString =	
					  "DELETE FROM Usuario " 
					+ "WHERE email = ? ";
			
			preparedStatement = connection.prepareStatement(queryString);

			logger.debug(queryString);
			
			int i = 1;
			preparedStatement.setString(i++, email);

			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(email,Usuario.class.getName());
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
	public Usuario findById(String email, Connection connection) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("Email = "+email);
		}
		
		Usuario u=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		
			String sql;
			sql="select email,nombre,apellido1,apellido2,telefono,password,fecha_nacimiento,genero,nombre_user from usuario where email=?";
			
			pst=connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			int i=1;
			//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
			pst.setString(i++, email);	
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			if(rs.next()){
				u=loadNext(rs);
			}else {
				u=null;
				logger.error("Error "+email+" id introducido incorrecto", Usuario.class.getName());
			}
			return u;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
		
	}
	
	
	public Usuario loadNext(ResultSet rs) 
		throws SQLException,DataException{
			int i=1;
			String email  = rs.getString(i++);
			String nombre = rs.getString(i++);
			String apellido1=rs.getString(i++);
			String apellido2=rs.getString(i++);
			String telefono=rs.getString(i++);
			String password=rs.getString(i++);
			Date fechaNacimiento=rs.getDate(i++);
			String genero=rs.getString(i++);
			String nombreUser=rs.getString(i++);
			
			Usuario u= new Usuario();
			
			u.setEmail(email);
			u.setNombre(nombre);
			u.setApellido1(apellido1);
			u.setApellido2(apellido2);
			u.setTelefono(telefono);
			u.setPassword(password);
			u.setFechaNacimiento(fechaNacimiento);
			u.setNombreUser(nombreUser);
			u.setGenero(genero);
			
			
			return u;
		
	}
}
