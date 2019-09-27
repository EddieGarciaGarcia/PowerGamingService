package com.eddie.dao;

import java.sql.Connection;
import java.util.List;

import com.eddie.model.LineaPedido;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;

public interface LineaPedidoDAO {

	public List<LineaPedido> findByPedido(Connection conexion,Integer idPedido)throws DataException;
	
	public LineaPedido findById(Connection conexion,Integer numeroLinea)throws InstanceNotFoundException, DataException;
	
	public LineaPedido create(Connection conexion,LineaPedido lp) throws DuplicateInstanceException, DataException;
	
	public long delete(Connection conexion,Integer id) throws DataException;
	
	public long deleteByPedido(Connection conexion,Integer id) throws DataException;
}
