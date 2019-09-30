package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Plataforma;


public interface PlataformaDAO {

	public Plataforma findbyIdPlataforma(Connection conexion,Integer id) throws InstanceNotFoundException, DataException;
	
	public List<Plataforma>  findAll(Connection conexion) throws DataException; 

	public List<Plataforma>  findByJuego(Connection conexion, Integer idJuego)throws DataException;

}
