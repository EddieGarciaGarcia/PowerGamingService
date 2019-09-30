package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.model.Pais;
import com.eddie.exceptions.DataException;

public interface PaisDAO {
	
	Pais findById(Connection conexion,Integer id)throws DataException;
		
	List<Pais> findAll(Connection conexion)throws DataException;
}
