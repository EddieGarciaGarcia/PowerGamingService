package com.eddie.dao;

import java.sql.Connection;
import com.eddie.exceptions.DataException;
import com.eddie.model.Usuario;



public interface UsuarioDAO {
	
	boolean create(Connection connection, Usuario usuario) throws DataException;
	
	boolean update(Connection connection, Usuario usuario) throws DataException;
	
	boolean delete(Connection connection, String email) throws DataException;

	Usuario findById(Connection connection, String email) throws DataException;

}
