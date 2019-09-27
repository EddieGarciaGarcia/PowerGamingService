package com.eddie.service;

import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Direccion;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Resultados;
import com.eddie.model.Usuario;

public interface UsuarioService {
	
	public Usuario create(Usuario u) throws DuplicateInstanceException, Exception;
	
	public void update(Usuario u) throws InstanceNotFoundException,DataException;
	
	public long delete(String email) throws DataException;
	
	public Usuario findById(String email) throws InstanceNotFoundException,DataException;
	
	public Usuario login(String email, String password)throws DataException;
	
	//Biblioteca
	public Resultados<ItemBiblioteca> findByUsuario(String email, int startIndex, int count)throws DataException;
	
	//A�adir a biblioteca
	public ItemBiblioteca addJuegoBiblioteca(String email, ItemBiblioteca b)throws DuplicateInstanceException,DataException;
	
	public boolean existsInBiblioteca(String email, Integer idJuego)throws DataException;
	public List<Integer> existsInBiblioteca(String email, List<Integer> idsDeJuego)throws DataException;
	
	//Eliminar de la Biblioteca
	public long borrarJuegoBiblioteca(String email,Integer idJuego)throws InstanceNotFoundException, DataException;
	
	public Direccion findByIdDireccion(String email) throws InstanceNotFoundException, DataException;
	
	public Direccion createDireccion(Direccion d) throws DuplicateInstanceException, DataException;
	
	public boolean updateDireccion(Direccion d) throws InstanceNotFoundException, DataException;
	
	public void deleteDireccion(String email) throws DataException;

	//Puntuacion
	public ItemBiblioteca create(ItemBiblioteca it)throws DataException;
	
	public ItemBiblioteca findByIdEmail(String email, Integer idJuego)throws DataException;
}
