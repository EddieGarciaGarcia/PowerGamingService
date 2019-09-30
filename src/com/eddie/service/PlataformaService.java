package com.eddie.service;


import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Plataforma;

public interface PlataformaService {
	
	public Plataforma findbyIdPlataforma(Integer id) throws InstanceNotFoundException, DataException;
	
	//Listado de Plataformas
	public List<Plataforma>  findAll() throws DataException; 
	
	public List<Plataforma>  findByJuego(Integer idJuego) throws DataException;
}
