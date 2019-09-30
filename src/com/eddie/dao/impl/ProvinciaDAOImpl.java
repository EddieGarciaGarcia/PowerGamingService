package com.eddie.dao.impl;

import com.eddie.dao.ProvinciaDAO;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Provincia;
import com.eddie.utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProvinciaDAOImpl implements ProvinciaDAO {

    private static Logger logger = LogManager.getLogger(ProvinciaDAOImpl.class);

    @Override
    public Provincia findById(Connection conexion, Integer id) throws DataException {
        Provincia provincia;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select id_provincia, id_pais, nombre ");
            query.append("from provincia ");
            query.append(" where id_provincia= ?");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                provincia = new Provincia();
                return loadNext(resultSet,provincia);

            } else {
                throw new InstanceNotFoundException("Error " + id + " id introducido incorrecto", Provincia.class.getName());
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
    public List<Provincia> findAllByIdPais(Connection conexion, Integer idPais) throws DataException {
        Provincia provincia;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select id_provincia, id_pais, nombre ");
            query.append("from provincia ");
            query.append("where id_pais= ?");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, idPais);
            resultSet = preparedStatement.executeQuery();
            List<Provincia> provincias = new ArrayList<>();
            while (resultSet.next()) {
                provincia = new Provincia();
                provincias.add(loadNext(resultSet,provincia));
            }
            return provincias;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Provincia> findAll(Connection conexion) throws DataException {
        Provincia provincia;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select id_provincia, id_pais, nombre ");
            query.append("from provincia");
            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            List<Provincia> provincias = new ArrayList<>();
            while (resultSet.next()) {
                provincia = new Provincia();
                provincias.add(loadNext(resultSet,provincia));
            }
            return provincias;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public Provincia loadNext(ResultSet resultSet, Provincia provincia) throws SQLException {
        provincia.setIdProvincia(resultSet.getInt("id_provincia"));
        provincia.setIdPais(resultSet.getInt("id_pais"));
        provincia.setNombre(resultSet.getString("nombre"));
        return provincia;
    }
}
