package com.eddie.service;

import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Edicion;

public interface EdicionService {
	
	public Edicion finById(Integer id) throws DataException;
	
	//Busqueda de las ediciones de un juego
	public List<Edicion> findByIdJuego(Integer id) throws DataException;
	
	public List<Edicion> findByIdsJuego(List<Integer> ids) throws DataException;
	
	public Edicion create(Edicion e) throws DuplicateInstanceException, DataException;
	
	public boolean update(Edicion e) throws InstanceNotFoundException, DataException;		
	
}
