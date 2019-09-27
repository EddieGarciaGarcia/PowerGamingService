package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.ProvinciaDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Provincia;

public class ProvinciaDAOImpl implements ProvinciaDAO{
	
	private static Logger logger=LogManager.getLogger(ProvinciaDAOImpl.class);
	
	@Override
	public Provincia findById(Connection conexion, Integer id) throws InstanceNotFoundException,DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id = "+id);
		}
		
		Provincia p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
	try {
	
		String sql;
		sql="select id_provincia, id_pais, nombre from provincia where id_provincia= ? ";
		
		pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		
		int i=1;
		pst.setInt(i++, id);
		
		rs=pst.executeQuery();
		
		logger.debug(sql);
		
		if(rs.next()){
			p=loadNext(rs);
			
		}else {
			throw new InstanceNotFoundException("Error "+id+" id introducido incorrecto", Provincia.class.getName());
		}
		return p;
	}catch (SQLException ex) {
		logger.error(ex.getMessage(),ex);
		throw new DataException(ex);
	}finally{
		JDBCUtils.closeResultSet(rs);
		JDBCUtils.closeStatement(pst);
	}
	}

	@Override
	public List<Provincia> findAllByIdPais(Connection conexion, Integer idPais) throws DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id = "+idPais);
		}
		
		Provincia p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
	try {
		
		String sql;
		sql="select id_provincia, id_pais, nombre from provincia where id_pais= ? ";
		
		pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		
		int i=1;
		pst.setInt(i++, idPais);
		
		logger.debug(sql);
		
		rs=pst.executeQuery();
		
		List<Provincia> provincias=new ArrayList<Provincia>();
		while(rs.next()){
			p=loadNext(rs);
			provincias.add(p);
		}
		return provincias;
	}catch (SQLException ex) {
		logger.error(ex.getMessage(),ex);
		throw new DataException(ex);
	}finally{
		JDBCUtils.closeResultSet(rs);
		JDBCUtils.closeStatement(pst);
	}
	}

	@Override
	public List<Provincia> findAll(Connection conexion) throws DataException {
		Provincia p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			
			String sql;
			sql="select id_provincia, id_pais, nombre from provincia";

			pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			List<Provincia> provincias=new ArrayList<Provincia>();
			while(rs.next()){
				p=loadNext(rs);
				provincias.add(p);
			}
			return provincias;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}
	public Provincia loadNext(ResultSet rs) 
			throws DataException, SQLException{
				int i=1;
				Integer idProvincia  = rs.getInt(i++);
				Integer idPais=rs.getInt(i++);
				String nombre = rs.getString(i++);
				
				
				Provincia p= new Provincia();
				p.setIdProvincia(idProvincia);
				p.setIdPais(idPais);
				p.setNombre(nombre);
				
				
				return p;
			
		}
}
