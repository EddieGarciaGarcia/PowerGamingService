package com.eddie.dao;

import com.eddie.model.Direccion;
import java.sql.Connection;
import com.eddie.exceptions.DataException;

public interface DireccionDAO {
	
	Direccion findById(Connection conexion,String email) throws DataException;
	
	boolean create(Connection conexion,Direccion d) throws DataException;
	
	boolean update(Connection conexion,Direccion d) throws DataException;
	
	boolean delete(Connection conexion,String email) throws DataException;
}
