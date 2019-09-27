package com.eddie.dao.impl;

import com.eddie.dao.FormatoDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.model.Formato;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormatoDAOImpl implements FormatoDAO {

    private static Logger logger = LogManager.getLogger(FormatoDAOImpl.class);

    public List<Formato> findbyIdsFormato(Connection conexion, List<Integer> ids, String idioma) throws DataException {
        List<Formato> formatos;
        Formato formato;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder(" ");
            query.append("select id_formato, nombre ");
            query.append("from idiomaweb_formato");
            query.append("where id_idioma_web =");
            query.append(idioma);
            query.append(" and id_formato in (");
            //Se añaden todos los ids a la query
            JDBCUtils.anhadirIN(query, ids);

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet = preparedStatement.executeQuery();

            formatos = new ArrayList<>();
            while (resultSet.next()) {
                formato = loadNext(resultSet);
                formatos.add(formato);
            }
            return formatos;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public List<Formato> findAll(Connection conexion, String idioma) throws DataException {
        Formato formato;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
		List<Formato> formatos;
        try {
			query = new StringBuilder();
			query.append("select id_formato, nombre ");
			query.append("from idiomaweb_formato ");
			query.append("where id_idioma_web like ?");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, idioma);
            resultSet = preparedStatement.executeQuery();

            formatos= new ArrayList<>();
            while (resultSet.next()) {
                formato = loadNext(resultSet);
                formatos.add(formato);
            }
            return formatos;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }

    }

    public Formato loadNext(ResultSet resultSet) throws SQLException {
        Formato formato = new Formato();
        formato.setIdFormato(resultSet.getInt("id_formato"));
        formato.setNombre(resultSet.getString("nombre"));
        return formato;
    }
}
