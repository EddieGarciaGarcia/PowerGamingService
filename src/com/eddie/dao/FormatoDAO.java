package com.eddie.dao;

import java.sql.Connection;
import java.util.List;
import com.eddie.model.Formato;
import com.eddie.exceptions.DataException;

public interface FormatoDAO {
	
	List<Formato> findbyIdsFormato(Connection conexion,List<Integer> ids, String idioma) throws DataException;
	
	List<Formato> findAll(Connection conexion, String idioma) throws DataException;
}
