package com.eddie.dao.impl;

import com.eddie.dao.IdiomaDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Idioma;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IdiomaDAOImpl implements IdiomaDAO {

    private static Logger logger = LogManager.getLogger(IdiomaDAOImpl.class);

    @Override
    public Idioma findById(Connection conexion, String id, String idiomaS) throws DataException {
        Idioma idioma;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select id_idioma, nombre ");
            query.append("from idioma_idiomaweb ");
            query.append(" where id_idioma= ? and id_idioma_web like ?");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, idiomaS);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idioma = loadNext(resultSet);
            } else {
                throw new InstanceNotFoundException("Error " + id + " id introducido incorrecto", Idioma.class.getName());
            }
            return idioma;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Idioma> findAll(Connection conexion, String idiomaS) throws DataException {
        Idioma idioma;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
		List<Idioma> idiomas;
		try {
			query = new StringBuilder();
			query.append("select id_idioma, nombre ");
			query.append("from idioma_idiomaweb ");
			query.append("where id_idioma_web like ?");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, idiomaS);
            resultSet = preparedStatement.executeQuery();

			idiomas = new ArrayList<>();
            while (resultSet.next()) {
                idioma = loadNext(resultSet);
                idiomas.add(idioma);
            }
            return idiomas;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }


    @Override
    public List<Idioma> findByJuego(Connection conexion, Integer idJuego, String idiomaS) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
		StringBuilder query;
		Idioma idioma;
		List<Idioma> idiomas;
		try {
			query = new StringBuilder();
			query.append("select iiw.id_idioma, iiw.nombre ");
			query.append("from juego_idioma ji ");
			query.append("inner join idioma i on ji.id_idioma=i.id_idioma ");
			query.append("inner join idioma_idiomaweb iiw on iiw.id_idioma=i.id_idioma ");
			query.append("where ji.id_juego = ? AND iiw.id_idioma_web like ? ");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setInt(1, idJuego);
            preparedStatement.setString(2, idiomaS);

            rs = preparedStatement.executeQuery();

            // Recupera la pagina de resultados
			idiomas= new ArrayList<>();
            while (rs.next()) {
                idioma = loadNext(rs);
                idiomas.add(idioma);
            }
            return idiomas;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(rs);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public Idioma loadNext(ResultSet resultSet) throws SQLException {
        Idioma idioma = new Idioma();
        idioma.setIdIdioma(resultSet.getString("id_idioma"));
        idioma.setNombre(resultSet.getString("nombre"));
        return idioma;
    }


}
