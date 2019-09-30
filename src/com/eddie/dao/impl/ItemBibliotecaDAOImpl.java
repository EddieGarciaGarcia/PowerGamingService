package com.eddie.dao.impl;

import com.eddie.dao.ItemBibliotecaDAO;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.ItemBiblioteca;
import com.eddie.model.Resultados;
import com.eddie.utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ItemBibliotecaDAOImpl implements ItemBibliotecaDAO {

    private static Logger logger = LogManager.getLogger(ItemBibliotecaDAOImpl.class);

    @Override
    public Resultados<ItemBiblioteca> findByUsuario(Connection connection, String email, int startIndex, int count) throws DataException {
        ItemBiblioteca itemBiblioteca;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select email,id_juego,puntuacion,comprado,comentario,fecha_comentario ");
            query.append("from usuarios_juego ");
            query.append("where email=?");

            preparedStatement = connection.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            int currentCount = 0;
            List<ItemBiblioteca> biblioteca = new ArrayList<>();
            if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                do {
                    itemBiblioteca = new ItemBiblioteca();
                    biblioteca.add(loadNext(resultSet, itemBiblioteca));
                    currentCount++;
                } while ((currentCount < count) && resultSet.next());
            }
            int total = JDBCUtils.getTotalRows(resultSet);

            return new Resultados<>(biblioteca, startIndex, total);
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public boolean exists(Connection connection, String email, Integer idJuego) throws DataException {
        boolean exist = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("SELECT email,id_juego ");
            query.append("FROM usuarios_juego ");
            query.append("WHERE UPPER(email) LIKE UPPER(?) AND id_juego = ?");

            preparedStatement = connection.prepareStatement(query.toString());

            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, idJuego);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                exist = true;
            }
        } catch (SQLException e) {
            logger.info(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
        return exist;
    }

    @Override
    public List<Integer> exists(Connection connection, String email, List<Integer> idsJuegos) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("SELECT id_juego FROM usuarios_juego ");
            query.append("WHERE UPPER(email) = UPPER(?) AND id_juego in (");

            JDBCUtils.anhadirIN(query, idsJuegos);
            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            Integer idJuego;
            List<Integer> listaIdJuegoEnBiblitoeca = new ArrayList<>();
            while (resultSet.next()) {
                idJuego = resultSet.getInt(1);
                listaIdJuegoEnBiblitoeca.add(idJuego);
            }
            return listaIdJuegoEnBiblitoeca;
        } catch (SQLException e) {
            logger.info(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }


    @Override
    public List<ItemBiblioteca> findByJuego(Connection connection, Integer idJuego) throws DataException {
        ItemBiblioteca itemBiblioteca;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select email,id_juego,puntuacion,comprado,comentario,fecha_comentario ");
            query.append("from usuarios_juego ");
            query.append("where id_juego=? order by fecha_comentario desc");

            preparedStatement = connection.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            preparedStatement.setInt(1, idJuego);
            resultSet = preparedStatement.executeQuery();

            List<ItemBiblioteca> biblioteca = new ArrayList<>();
            while (resultSet.next()) {
                itemBiblioteca = new ItemBiblioteca();
                biblioteca.add(loadNext(resultSet, itemBiblioteca));
            }
            return biblioteca;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public boolean create(Connection connection, ItemBiblioteca biblioteca) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("Insert Into usuarios_juego(email,id_juego,puntuacion,comprado,comentario,fecha_comentario) ");
            query.append("values (?,?,?,?,?,?)");
            preparedStatement = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            if (biblioteca.getEmail() == null || biblioteca.getEmail().equals("")) {
                logger.warn("fallo email null o vacio");
            } else {
                preparedStatement.setString(i++, biblioteca.getEmail());
            }
            preparedStatement.setInt(i++, biblioteca.getIdJuego());

            if (biblioteca.getPuntuacion() == null) {
                preparedStatement.setNull(i++, Types.NULL);
            } else {
                preparedStatement.setInt(i++, biblioteca.getPuntuacion());
            }
            if (biblioteca.getComprado() == null) {
                preparedStatement.setNull(i++, Types.NULL);
            } else {
                preparedStatement.setString(i++, biblioteca.getComprado());
            }
            if (biblioteca.getComentario() == null) {
                preparedStatement.setNull(i++, Types.NULL);
            } else {
                preparedStatement.setString(i++, biblioteca.getComentario());
            }
            if (biblioteca.getFechaComentario() == null) {
                preparedStatement.setNull(i, Types.NULL);
            } else {
                preparedStatement.setDate(i, (java.sql.Date) biblioteca.getFechaComentario());
            }
            int insertRow = preparedStatement.executeUpdate();
            return insertRow != 0;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public boolean delete(Connection connection, String email, Integer idJuego) throws DataException {
        PreparedStatement preparedStatement = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("DELETE FROM usuarios_juego ");
            query.append("WHERE email like ? and id_juego = ?");
            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, idJuego);

            int removedRows = preparedStatement.executeUpdate();

            return removedRows != 0;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public boolean update(Connection connection, ItemBiblioteca itemBiblioteca) throws DataException {
        PreparedStatement preparedStatement = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("UPDATE usuarios_juego");
            boolean first = true;
            if (itemBiblioteca.getPuntuacion() != null) {
                JDBCUtils.addUpdate(query, first, " puntuacion = ?");
                first = false;
            }
            if (itemBiblioteca.getComprado() != null) {
                JDBCUtils.addUpdate(query, first, " comprado = ?");
                first = false;
            }
            if (itemBiblioteca.getComentario() == null) {
                JDBCUtils.addUpdate(query, first, " comentario = ?");
                first = false;
            }
            if (itemBiblioteca.getFechaComentario() == null) {
                JDBCUtils.addUpdate(query, first, " fecha_comentario = ?");
            }
            query.append("WHERE email like ? and id_juego = ?");
            preparedStatement = connection.prepareStatement(query.toString());
            int i = 1;
            if (itemBiblioteca.getPuntuacion() != null) preparedStatement.setInt(i++, itemBiblioteca.getPuntuacion());
            if (itemBiblioteca.getComprado() != null) preparedStatement.setString(i++, itemBiblioteca.getComprado());
            if (itemBiblioteca.getComentario() != null) preparedStatement.setString(i++, itemBiblioteca.getComentario());
            if (itemBiblioteca.getFechaComentario() != null)
                preparedStatement.setDate(i++, (java.sql.Date) itemBiblioteca.getFechaComentario());
            preparedStatement.setString(i++, itemBiblioteca.getEmail());
            preparedStatement.setInt(i, itemBiblioteca.getIdJuego());
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows == 1;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public ItemBiblioteca fingByIdEmail(Connection connection, String email, Integer idJuego) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ItemBiblioteca itemBiblioteca;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("SELECT email,id_juego,puntuacion,comprado,comentario,fecha_comentario ");
            query.append("FROM usuarios_juego ");
            query.append("WHERE email = ? AND id_juego = ?");
            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, idJuego);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                itemBiblioteca = new ItemBiblioteca();
                return loadNext(resultSet, itemBiblioteca);
            }else{
                throw new InstanceNotFoundException("Error "+email+" id introducido incorrecto", ItemBiblioteca.class.getName());
            }
        } catch (SQLException e) {
            logger.info(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public ItemBiblioteca loadNext(ResultSet resultSet, ItemBiblioteca itemBiblioteca) throws SQLException{
        itemBiblioteca.setEmail(resultSet.getString("email"));
        itemBiblioteca.setIdJuego(resultSet.getInt("id_juego"));
        itemBiblioteca.setPuntuacion(resultSet.getInt("puntuacion"));
        itemBiblioteca.setComprado(resultSet.getString("comprado"));
        itemBiblioteca.setComentario(resultSet.getString("comentario"));
        itemBiblioteca.setFechaComentario(resultSet.getDate("fecha_comentario"));
        return itemBiblioteca;
    }
}
