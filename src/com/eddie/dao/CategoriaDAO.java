package com.eddie.dao;

import java.sql.Connection;
import java.util.List;
import com.eddie.model.Categoria;
import com.eddie.exceptions.DataException;

public interface CategoriaDAO {
	
	Categoria findById(Connection conexion,Integer id, String idioma) throws DataException;
	
	List<Categoria>  findAll(Connection conexion, String idioma) throws DataException;
	
	List<Categoria>  findByJuego(Connection conexion, Integer idJuego,String idioma) throws DataException;
}
