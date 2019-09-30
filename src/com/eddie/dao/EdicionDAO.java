package com.eddie.dao;

import java.sql.Connection;
import java.util.List;
import com.eddie.model.Edicion;
import com.eddie.exceptions.DataException;

public interface EdicionDAO {
	
	 Edicion findByIdEdicion(Connection conexion,Integer id) throws DataException;
	
	 List<Edicion> findByIdJuego(Connection conexion,Integer id) throws DataException;
	
	 List<Edicion> findByIdsJuego(Connection conexion,List<Integer> ids) throws DataException;
	
	 Edicion create(Connection conexion,Edicion e) throws DataException;
	
	 boolean update(Connection conexion,Edicion e) throws DataException;
	
}
