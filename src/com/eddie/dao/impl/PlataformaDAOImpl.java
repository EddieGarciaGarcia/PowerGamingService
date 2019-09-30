package com.eddie.dao.impl;

import com.eddie.dao.PlataformaDAO;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Plataforma;
import com.eddie.utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlataformaDAOImpl implements PlataformaDAO {

    private static Logger logger = LogManager.getLogger(PlataformaDAOImpl.class);

    @Override
    public Plataforma findbyIdPlataforma(Connection conexion, Integer id) throws DataException {
        Plataforma plataforma;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
			query = new StringBuilder();
			query.append("select id_plataforma, nombre ");
			query.append("from plataforma ");
			query.append("where id_plataforma= ?");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                plataforma = new Plataforma();
                return loadNext(resultSet, plataforma);
            } else {
                throw new InstanceNotFoundException("Error " + id + " id introducido incorrecto", Plataforma.class.getName());
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Plataforma> findAll(Connection conexion) throws DataException {
        Plataforma plataforma = new Plataforma();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
			query = new StringBuilder();
			query.append("select id_plataforma, nombre ");
			query.append("from plataforma");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            List<Plataforma> resultado = new ArrayList<>();
            while (resultSet.next()) {
                resultado.add(loadNext(resultSet, plataforma));
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

    @Override
    public List<Plataforma> findByJuego(Connection conexion, Integer idJuego) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
		Plataforma plataforma = new Plataforma();
		StringBuilder query;
        try {
			query = new StringBuilder();
			query.append("select jp.id_plataforma, p.nombre ");
			query.append("from juego_plataforma jp ");
			query.append("inner join plataforma p on jp.id_plataforma=p.id_plataforma ");
			query.append("where jp.id_juego = ?");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, idJuego);
            resultSet = preparedStatement.executeQuery();

            // Recupera la pagina de resultados
            List<Plataforma> plataformas = new ArrayList<>();
            while (resultSet.next()) {
                plataformas.add(loadNext(resultSet, plataforma));
            }
            return plataformas;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public Plataforma loadNext(ResultSet rs, Plataforma plataforma) throws SQLException {
		plataforma.setIdPlatadorma(rs.getInt("id_plataforma"));
		plataforma.setNombre(rs.getString("nombre"));
        return plataforma;
    }
}
