package com.eddie.service;


import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Categoria;

public interface CategoriaService {
	
	public Categoria findById(Integer id, String idioma) throws InstanceNotFoundException, DataException;
	
	//listado de Categorias
	public List<Categoria>  findAll( String idioma) throws DataException;
	
	//listado de Categorias para cuando se ense�e el juego
	public List<Categoria>  findByJuego(Integer idJuego,String idioma) throws DataException; 

}
