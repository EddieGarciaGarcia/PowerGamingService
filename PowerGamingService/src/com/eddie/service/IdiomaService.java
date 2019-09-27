package com.eddie.service;


import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Idioma;

public interface IdiomaService {
	
	public Idioma findById(String id, String idioma) throws InstanceNotFoundException, DataException;;
	
	//Listado de idiomas
	public List<Idioma> findAll( String idioma) throws DataException;
	
	public List<Idioma> findByJuego(Integer idJuego, String idioma) throws DataException;

}
