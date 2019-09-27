package com.eddie.service;


import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.model.Pais;

public interface PaisService {
	
	//Listado de Paises
	public List<Pais> findAll()throws DataException;
	
}
