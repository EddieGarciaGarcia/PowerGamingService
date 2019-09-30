package com.eddie.service;

import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Pedido;
import com.eddie.model.Resultados;

public interface PedidoService {
	
	//Historial del Usuario
	public Resultados<Pedido> findByEmail(String email, int startIndex, int count) throws InstanceNotFoundException, DataException;
	
	public List<Pedido> findByIds(List<Integer> ids)throws DataException;
	
	//Cancelar pedido
	public void delete(Integer idPedido) throws InstanceNotFoundException, DataException;
	
	public Pedido create(Pedido p) throws DuplicateInstanceException, DataException;
	
	public Pedido findByEmail(String email)throws DataException;
}
