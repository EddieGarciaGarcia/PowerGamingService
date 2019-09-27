package com.eddie.dao;


import java.sql.Connection;
import java.util.List;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Provincia;

public interface ProvinciaDAO {
	
	public Provincia findById(Connection conexion,Integer id) throws InstanceNotFoundException, DataException; 
	
	public List<Provincia> findAllByIdPais(Connection conexion, Integer idPais)throws DataException;

	public List<Provincia> findAll(Connection conexion)throws DataException;
}
