package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.model.Pais;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;

public interface PaisDAO {
	
	public Pais findById(Connection conexion,Integer id)throws InstanceNotFoundException, DataException;
		
	public List<Pais> findAll(Connection conexion)throws DataException;
}
