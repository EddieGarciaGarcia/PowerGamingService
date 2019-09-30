package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.model.LineaPedido;
import com.eddie.exceptions.DataException;

public interface LineaPedidoDAO {

	List<LineaPedido> findByPedido(Connection conexion,Integer idPedido)throws DataException;
	
	LineaPedido findById(Connection conexion,Integer numeroLinea)throws DataException;
	
	boolean create(Connection conexion,LineaPedido lineaPedido) throws DataException;
	
	boolean delete(Connection conexion,Integer id) throws DataException;
	
	boolean deleteByPedido(Connection conexion,Integer id) throws DataException;
}
