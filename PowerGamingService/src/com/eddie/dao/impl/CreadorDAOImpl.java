package com.eddie.dao.impl;

import com.eddie.dao.CreadorDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Creador;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreadorDAOImpl implements CreadorDAO {

    private static Logger logger = LogManager.getLogger(CreadorDAOImpl.class);

    @Override
    public Creador findbyIdCreador(Connection conexion, Integer id) throws DataException {

        Creador creador;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
		StringBuilder query;
        try {
            query= new StringBuilder();
            query.append("select id_creador, nombre");
			query.append(" from creador");
			query.append(" where id_creador= ?");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	creador = loadNext(resultSet);
            } else {
                throw new InstanceNotFoundException("Error " + id + " id introducido incorrecto", Creador.class.getName());
            }
            return creador;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Creador> findAll(Connection conexion) throws DataException {
        Creador creador;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        List<Creador> creadores;
        try {
           	query= new StringBuilder();
            query.append("select id_creador, nombre");
            query.append("from creador");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet = preparedStatement.executeQuery();

            creadores = new ArrayList<>();
            while (resultSet.next()) {
                creador = loadNext(resultSet);
                creadores.add(creador);
            }
            return creadores;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }
    public Creador loadNext(ResultSet resultSet) throws SQLException {
        Creador creador = new Creador();
        creador.setIdCreador(resultSet.getInt("id_creador"));
        creador.setNombre(resultSet.getString("nombre"));
        return creador;
    }
}
