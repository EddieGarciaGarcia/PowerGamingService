package com.eddie.dao;

import java.sql.Connection;
import java.util.List;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Resultados;

public interface ItemBibliotecaDAO {

	public Resultados<ItemBiblioteca> findByUsuario(Connection connection,String email, int startIndex, int count) throws DataException;
	
	public boolean exists(Connection c, String email, Integer idJuego) throws DataException;
	
	//Comprobar 
	public List<Integer> exists(Connection c, String email, List<Integer> idsJuegos) throws DataException;	
	
	//Comprobar
	public List<ItemBiblioteca> findByJuego(Connection connection,Integer idJuego) throws DataException;
	
	public ItemBiblioteca create(Connection connection,ItemBiblioteca b) throws DuplicateInstanceException, DataException;
	
	public ItemBiblioteca update (Connection connection,ItemBiblioteca b) throws DuplicateInstanceException, DataException;
	
	public long delete(Connection connection,String email,Integer idJuego) throws InstanceNotFoundException, DataException;
	
	public ItemBiblioteca fingByIdEmail(Connection c, String email, Integer idJuego) throws DataException;
}
