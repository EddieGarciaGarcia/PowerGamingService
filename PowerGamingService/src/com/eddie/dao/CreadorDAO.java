package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.model.Creador;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;


public interface CreadorDAO {

	Creador findbyIdCreador(Connection conexion,Integer id) throws DataException;
	
	List<Creador> findAll(Connection conexion) throws DataException;
	
	
}
