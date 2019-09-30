package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.model.Plataforma;


public interface PlataformaDAO {

	Plataforma findbyIdPlataforma(Connection conexion,Integer id) throws DataException;
	
	List<Plataforma>  findAll(Connection conexion) throws DataException;

	List<Plataforma>  findByJuego(Connection conexion, Integer idJuego)throws DataException;

}
