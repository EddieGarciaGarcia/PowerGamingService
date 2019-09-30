package com.eddie.service;


import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.model.Provincia;

public interface ProvinciaService {
	
	//Listado de provincias segun el pais escogido
	public List<Provincia> findAllByIdPais(Integer idPais)throws DataException;

}
