package com.eddie.dao;

import java.sql.Connection;
import java.util.List;
import com.eddie.model.Idioma;
import com.eddie.exceptions.DataException;

public interface IdiomaDAO {
	
	Idioma findById(Connection conexion,String id, String idioma) throws DataException;;
	
	List<Idioma> findAll(Connection conexion, String idioma) throws DataException;
	
	List<Idioma> findByJuego(Connection conexion,Integer idJuego, String idioma) throws DataException;
}
