package com.eddie.service;

import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Creador;

public interface CreadorService {
	
	public Creador findbyIdCreador(Integer id) throws InstanceNotFoundException, DataException;
	
	//Lista de Creadores
	public List<Creador> findAll() throws DataException;
}
