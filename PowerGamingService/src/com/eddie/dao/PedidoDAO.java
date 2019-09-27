package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Pedido;
import com.eddie.model.Resultados;
import com.eddie.exceptions.DuplicateInstanceException;

public interface PedidoDAO {
	
	public Resultados<Pedido> findByEmail(Connection conexion,String email, int startIndex, int count)throws InstanceNotFoundException, DataException;
	
	public List<Pedido> findByIds(Connection conexion,List<Integer> ids)throws DataException;
	
	public Pedido findByEmail(Connection conexion,String email)throws InstanceNotFoundException, DataException;
	
	public Pedido create(Connection conexion,Pedido p) throws DuplicateInstanceException, DataException;
	
	public void delete(Connection conexion,Integer idPedido) throws DataException;
}
