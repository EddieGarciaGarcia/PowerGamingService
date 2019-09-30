package com.eddie.dao;


import java.sql.Connection;
import java.util.List;
import com.eddie.exceptions.DataException;
import com.eddie.model.Provincia;

public interface ProvinciaDAO {
	
	Provincia findById(Connection conexion,Integer id) throws DataException;
	
	List<Provincia> findAllByIdPais(Connection conexion, Integer idPais)throws DataException;

	List<Provincia> findAll(Connection conexion)throws DataException;
}
