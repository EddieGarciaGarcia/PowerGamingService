package com.eddie.dao.impl;

import com.eddie.dao.PaisDAO;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Pais;
import com.eddie.utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaisDAOImpl implements PaisDAO {

    private static Logger logger = LogManager.getLogger(PaisDAOImpl.class);

    @Override
    public Pais findById(Connection conexion, Integer id) throws DataException {
        Pais pais = new Pais();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
			query = new StringBuilder();
			query.append("select id_pais, nombre ");
			query.append("from pais ");
			query.append("where id_pais = ?");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return loadNext(resultSet, pais);
            } else {
                throw new InstanceNotFoundException("Error " + id + " id introducido incorrecto", Pais.class.getName());
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
    public List<Pais> findAll(Connection conexion) throws DataException {
        Pais pais = new Pais();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
			query = new StringBuilder();
			query.append("select id_pais, nombre ");
			query.append("from pais");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            List<Pais> resultado = new ArrayList<>();
            while (resultSet.next()) {
                resultado.add(loadNext(resultSet, pais));
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

    public Pais loadNext(ResultSet resultSet, Pais pais) throws SQLException {
        pais.setIdPais(resultSet.getInt("id_pais"));
        pais.setNombre(resultSet.getString("nombre"));
        return pais;
    }
}
