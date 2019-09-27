package com.eddie.service;


import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Formato;

public interface FormatoService {
	public List<Formato> findbyIdsFormato(List<Integer> ids, String idioma) throws InstanceNotFoundException, DataException;
	//Lista de Formatos
	public List<Formato> findAll(String idioma) throws DataException;
}
