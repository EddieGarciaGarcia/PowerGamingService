package com.eddie.dao.impl;

import com.eddie.dao.CategoriaDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Categoria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CategoriaDAOImpl implements CategoriaDAO {

    private static Logger logger = LogManager.getLogger(CategoriaDAOImpl.class);

    @Override
    public Categoria findById(Connection conexion, Integer id, String idioma) throws DataException {
        Categoria categoria;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select id_categoria, nombre");
            query.append(" from categoria_idiomaweb");
            query.append(" where id_categoria= ? and id_idioma_web= ?");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, idioma);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoria = new Categoria();
                return loadNext(resultSet, categoria);
            } else {
                throw new InstanceNotFoundException("Error " + id + " id introducido incorrecto", Categoria.class.getName());
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
    public List<Categoria> findAll(Connection conexion, String idioma) throws DataException {
        Categoria categoria;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        List<Categoria> categorias;
        try {
            query = new StringBuilder();
            query.append("select ciw.id_categoria, ciw.nombre");
            query.append(" from categoria_idiomaweb ciw");
            query.append(" where id_idioma_web like ?");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, idioma);
            resultSet = preparedStatement.executeQuery();

            categorias = new ArrayList<>();
            while (resultSet.next()) {
                categoria = new Categoria();
                categorias.add(loadNext(resultSet,categoria));
            }
            return categorias;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Categoria> findByJuego(Connection conexion, Integer idJuego, String idioma) throws DataException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        List<Categoria> categorias;
        Categoria categoria;
        try {
            query = new StringBuilder();
            query.append("select ciw.id_categoria, ciw.nombre");
            query.append(" from juego_categoria jc ");
            query.append(" inner join categoria c on jc.id_categoria=c.id_categoria");
            query.append(" inner join categoria_idiomaweb ciw on ciw.id_categoria=c.id_categoria");
            query.append(" where jc.id_juego = ? and ciw.id_idioma_web like ? ");

            preparedStatement = conexion.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setInt(1, idJuego);
            preparedStatement.setString(2, idioma);

            resultSet = preparedStatement.executeQuery();

            // Recupera la pagina de resultados
            categorias = new ArrayList<>();
            while (resultSet.next()) {
                categoria = new Categoria();
                categorias.add(loadNext(resultSet, categoria));
            }
            return categorias;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }


    public Categoria loadNext(ResultSet resultSet, Categoria categoria) throws SQLException {
        categoria.setNombre(resultSet.getString("nombre"));
        categoria.setIdCategria(resultSet.getInt("id_categoria"));
        return categoria;
    }
}
