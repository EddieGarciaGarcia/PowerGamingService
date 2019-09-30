package com.eddie.dao;

import java.sql.Connection;
import java.util.List;
import com.eddie.exceptions.DataException;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Resultados;

public interface ItemBibliotecaDAO {

	Resultados<ItemBiblioteca> findByUsuario(Connection connection,String email, int startIndex, int count) throws DataException;
	
	boolean exists(Connection connection, String email, Integer idJuego) throws DataException;
	
	//Comprobar 
	List<Integer> exists(Connection connection, String email, List<Integer> idsJuegos) throws DataException;
	
	//Comprobar
	List<ItemBiblioteca> findByJuego(Connection connection,Integer idJuego) throws DataException;
	
	boolean create(Connection connection,ItemBiblioteca itemBiblioteca) throws DataException;
	
	boolean update (Connection connection,ItemBiblioteca itemBiblioteca) throws DataException;
	
	boolean delete(Connection connection,String email,Integer idJuego) throws DataException;
	
	ItemBiblioteca fingByIdEmail(Connection connection, String email, Integer idJuego) throws DataException;
}
