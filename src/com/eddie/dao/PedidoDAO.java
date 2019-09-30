package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.exceptions.DataException;
import com.eddie.model.Pedido;
import com.eddie.model.Resultados;

public interface PedidoDAO {
	
	Resultados<Pedido> findByEmail(Connection conexion,String email, int startIndex, int count)throws DataException;
	
	List<Pedido> findByIds(Connection conexion,List<Integer> ids)throws DataException;
	
	Pedido findByEmail(Connection conexion,String email)throws DataException;
	
	boolean create(Connection conexion,Pedido pedido) throws DataException;
	
	boolean delete(Connection conexion,Integer idPedido) throws DataException;
}
