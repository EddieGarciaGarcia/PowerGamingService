package com.eddie.dao.impl;

import com.eddie.dao.TipoEdicionDAO;
import com.eddie.exceptions.DataException;
import com.eddie.model.TipoEdicion;
import com.eddie.utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoEdicionDAOImpl implements TipoEdicionDAO {

	private static Logger logger = LogManager.getLogger(TipoEdicionDAOImpl.class);

	public List<TipoEdicion> findbyIdsTipoEdicion(Connection conexion, List<Integer> ids, String idioma) throws DataException {
		List<TipoEdicion> resultados = new ArrayList<>();
		TipoEdicion tipoEdicion;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		StringBuilder query;
		try {
			query = new StringBuilder(" ");
			query.append("select te.id_tipo_edicion, id.nombre ");
			query.append("from tipoedicion te inner join idiomaweb_tipoedicion id on te.id_tipo_edicion=id.id_tipo_edicion ");
			query.append("where id_idioma_web like ");
			query.append(idioma);
			query.append(" and te.id_tipo_edicion in (");
			JDBCUtils.anhadirIN(query, ids);
			preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				tipoEdicion = new TipoEdicion();
				resultados.add(loadNext(resultSet, tipoEdicion));
			}
			return resultados;
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
			throw new DataException(ex);
		} finally {
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	@Override
	public List<TipoEdicion> findAll(Connection conexion, String idioma) throws DataException {
		TipoEdicion tipoEdicion;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		StringBuilder query;
		try {
			query = new StringBuilder();
			query.append("select id_tipo_edicion, nombre ");
			query.append("from idiomaweb_tipoedicion ");
			query.append("where id_idioma_web like ?");
			preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			preparedStatement.setString(1, idioma);
			resultSet = preparedStatement.executeQuery();
			List<TipoEdicion> resultado = new ArrayList<>();
			while (resultSet.next()) {
				tipoEdicion = new TipoEdicion();
				resultado.add(loadNext(resultSet, tipoEdicion));
			}
			return resultado;
		} catch (SQLException ex) {
			logger.error(ex.getMessage(), ex);
			throw new DataException(ex);
		} finally {
			JDBCUtils.closeResultSet(resultSet);
			JDBCUtils.closeStatement(preparedStatement);
		}
	}

	public TipoEdicion loadNext(ResultSet resultSet, TipoEdicion tipoEdicion) throws SQLException {
		tipoEdicion.setIdTipoEdicion(resultSet.getInt("id_tipo_edicion"));
		tipoEdicion.setNombre(resultSet.getString("nombre"));
		return tipoEdicion;
	}
}
