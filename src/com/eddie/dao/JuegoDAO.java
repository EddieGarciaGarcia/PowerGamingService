package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.model.Juego;
import com.eddie.model.JuegoCriteria;
import com.eddie.model.Resultados;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;

public interface JuegoDAO {
	
	
	public Resultados<Juego> findByJuegoCriteria(JuegoCriteria c, String idioma, Connection connection, int startIndex, int count) throws DataException;
	
	public Resultados<Juego> findAllByDate(Connection connection, String idioma, int startIndex, int count) throws DataException;
	
	public List<Juego> findByIDs(Connection connection, List<Integer> ids, String idioma)throws DataException;
	
	public List<Juego> findAllByValoracion(Connection connection, String idioma) throws DataException;
	
	public Juego findById(Connection connection,Integer id, String idioma)throws InstanceNotFoundException, DataException;
	
	public Juego create(Connection connection,Juego j) throws DuplicateInstanceException, DataException;
	
	public boolean update(Connection connection,Juego j) throws InstanceNotFoundException, DataException;
	
	public void delete(Connection connection,Integer id) throws DataException;
	
	//Media
	public Integer puntuacion(Connection connection, Integer idJuego)throws DataException;
}
