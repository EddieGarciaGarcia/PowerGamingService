package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.PaisDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Pais;

public class PaisDAOImpl implements PaisDAO{
	
	private static Logger logger=LogManager.getLogger(PaisDAOImpl.class);

	@Override
	public Pais findById(Connection conexion, Integer id) throws InstanceNotFoundException,DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id= "+id);
		}
		
		Pais p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
	try {
		
		String sql;
		sql="select id_pais, nombre from pais where id_pais= ? ";
		
		pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		
		int i=1;
		pst.setInt(i++, id);
		
		rs=pst.executeQuery();
		
		logger.debug(sql);
		
		if(rs.next()){
			p=loadNext(rs);
			
		}else {
			throw new InstanceNotFoundException("Error "+id+" id introducido incorrecto", Pais.class.getName());
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
	public List<Pais> findAll(Connection conexion) throws DataException {
		Pais p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			
			String sql;
			sql="select id_pais, nombre from pais";

			pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			List<Pais> resultado=new ArrayList<Pais>();
			while(rs.next()){
				p=loadNext(rs);
				resultado.add(p);
			}
			return resultado;
		}catch (SQLException ex) {
			logger.error(ex.getMessage(),ex);
			throw new DataException(ex);
		}finally{
			JDBCUtils.closeResultSet(rs);
			JDBCUtils.closeStatement(pst);
		}
	}
	public Pais loadNext(ResultSet rs) 
			throws DataException,SQLException{
				int i=1;
				Integer idPais  = rs.getInt(i++);
				String nombre = rs.getString(i++);
				
				
				Pais p= new Pais();
				p.setIdPais(idPais);
				p.setNombre(nombre);
				
				
				return p;
			
		}
}
