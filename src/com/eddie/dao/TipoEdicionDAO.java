package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.model.TipoEdicion;


public interface TipoEdicionDAO {
	
	List<TipoEdicion> findbyIdsTipoEdicion(Connection conexion, List<Integer> ids, String idioma) throws DataException;
	
	List<TipoEdicion> findAll(Connection conexion, String idioma) throws DataException;
}
