package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.PlataformaDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Plataforma;

public class PlataformaDAOImpl implements PlataformaDAO{

	private static Logger logger=LogManager.getLogger(PlataformaDAOImpl.class);
	
	@Override
	public Plataforma findbyIdPlataforma(Connection conexion, Integer id) throws InstanceNotFoundException, DataException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("id = "+id);
		}
		
		Plataforma p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
	try {
		
		String sql;
		sql="select id_plataforma, nombre from plataforma where id_plataforma= ? ";
		
		pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		
		int i=1;
		pst.setInt(i++, id);
		
		rs=pst.executeQuery();
		
		logger.debug(sql);
		
		if(rs.next()){
			p=loadNext(rs);
			
		}else {
			throw new InstanceNotFoundException("Error "+id+" id introducido incorrecto", Plataforma.class.getName());
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
	public List<Plataforma> findAll(Connection conexion) throws DataException {
		Plataforma p=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			
			String sql;
			sql="select id_plataforma, nombre from plataforma";

			pst=conexion.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			rs=pst.executeQuery();
			
			logger.debug(sql);
			
			List<Plataforma> resultado=new ArrayList<Plataforma>();
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
	
	@Override
	public List<Plataforma> findByJuego(Connection conexion, Integer idJuego) throws DataException {
		

		if(logger.isDebugEnabled()) {
			logger.debug("id = "+idJuego);
		}
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			String queryString = (
									"select jp.id_plataforma, p.nombre " +
									"from juego_plataforma jp " +
									"inner join plataforma p on jp.id_plataforma=p.id_plataforma " +
									"where jp.id_juego = ?");


			preparedStatement = conexion.prepareStatement(queryString,
					ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int i = 1;
			preparedStatement.setInt(i++, idJuego);

			rs = preparedStatement.executeQuery();

			logger.debug(queryString);
			
			// Recupera la pagina de resultados
			List<Plataforma> plataformas = new ArrayList<Plataforma>();                        
			Plataforma p = null;

			
			while(rs.next()){
					p = loadNext(rs);
					plataformas.add(p);               	
			}

			return plataformas;
	
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new DataException(e);
			} finally {
				JDBCUtils.closeResultSet(rs);
				JDBCUtils.closeStatement(preparedStatement);
			}
	}
	
	public Plataforma loadNext(ResultSet rs) 
			throws DataException,SQLException{
				int i=1;
				Integer idPlataforma  = rs.getInt(i++);
				String nombre = rs.getString(i++);
				
				
				Plataforma p= new Plataforma();
				p.setIdPlatadorma(idPlataforma);
				p.setNombre(nombre);
				
				
				return p;
			
		}

	
}
