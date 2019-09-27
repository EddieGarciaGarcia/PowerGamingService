package com.eddie.dao;

import java.sql.Connection;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Usuario;



public interface UsuarioDAO {
	
	public Usuario create(Usuario u, Connection connection) throws DuplicateInstanceException, DataException;
	
	public boolean update(Usuario u, Connection connection) throws InstanceNotFoundException, DataException;
	
	public long delete(String email, Connection connection) throws DataException;
	
	public Usuario findById(String email, Connection connection) throws InstanceNotFoundException, DataException;

}
