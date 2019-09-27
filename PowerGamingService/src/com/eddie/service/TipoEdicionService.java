package com.eddie.service;

import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.TipoEdicion;

public interface TipoEdicionService {
	
	public List<TipoEdicion> findbyIdsTipoEdicion(List<Integer> ids, String idioma) throws InstanceNotFoundException, DataException;
	
	//Listado de Tipo de edicion
	public List<TipoEdicion> findAll(String idioma) throws DataException;
}
