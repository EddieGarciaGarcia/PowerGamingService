package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.model.Juego;
import com.eddie.model.JuegoCriteria;
import com.eddie.model.Resultados;
import com.eddie.exceptions.DataException;

public interface JuegoDAO {
	
	
	Resultados<Juego> findByJuegoCriteria(Connection connection, JuegoCriteria juegoCriteria, String idioma, int startIndex, int count) throws DataException;
	
	Resultados<Juego> findAllByDate(Connection connection, String idioma, int startIndex, int count) throws DataException;
	
	List<Juego> findByIDs(Connection connection, List<Integer> ids, String idioma)throws DataException;
	
	List<Juego> findAllByValoracion(Connection connection, String idioma) throws DataException;
	
	Juego findById(Connection connection,Integer id, String idioma)throws DataException;
	
	Juego create(Connection connection,Juego juego) throws DataException;
	
	boolean update(Connection connection,Juego juego) throws DataException;
	
	boolean delete(Connection connection,Integer id) throws DataException;
	
	//Media
	Integer puntuacion(Connection connection, Integer idJuego)throws DataException;
}
