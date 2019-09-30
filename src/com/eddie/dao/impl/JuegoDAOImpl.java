package com.eddie.dao.impl;

import com.eddie.dao.*;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.*;
import com.eddie.service.CategoriaService;
import com.eddie.service.IdiomaService;
import com.eddie.service.PlataformaService;
import com.eddie.service.impl.CategoriaServiceImpl;
import com.eddie.service.impl.IdiomaServiceImpl;
import com.eddie.service.impl.PlataformaServiceImpl;
import com.eddie.utils.JDBCUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JuegoDAOImpl implements JuegoDAO {
    private static Logger logger = LogManager.getLogger(JuegoDAOImpl.class);
    private CategoriaDAO categoriaDAO;
    private PlataformaDAO plataformaDAO;
    private IdiomaDAO idiomaDAO;
    private EdicionDAO edicionDAO;

    public JuegoDAOImpl() {
        categoriaDAO = new CategoriaDAOImpl();
        plataformaDAO = new PlataformaDAOImpl();
        idiomaDAO = new IdiomaDAOImpl();
        edicionDAO = new EdicionDAOImpl();
    }

    public Resultados<Juego> findByJuegoCriteria(Connection connection, JuegoCriteria juegoCriteria, String idioma, int startIndex, int count) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select j.id_juego, j.nombre, j.fecha_lanzamiento, j.id_creador ");
            query.append("from juego j INNER JOIN juego_idiomaweb jiw ON j.id_juego = jiw.id_juego ");
            boolean first = true;
            if (juegoCriteria.getCategoriaIDs() != null && juegoCriteria.getCategoriaIDs().length > 0) {
                query.append(" inner join juego_categoria jc on j.id_juego=jc.id_juego inner join categoria c on jc.id_categoria=c.id_categoria ");
            }
            if (juegoCriteria.getIdiomaIDs() != null && juegoCriteria.getIdiomaIDs().length > 0) {
                query.append(" inner join juego_idioma ji on j.id_juego=ji.id_juego inner join idioma i on ji.id_idioma=i.id_idioma ");
            }
            if (juegoCriteria.getPlataformaIDs() != null && juegoCriteria.getPlataformaIDs().length > 0) {
                query.append(" inner join juego_plataforma jp on j.id_juego=jp.id_juego inner join plataforma p on jp.id_plataforma=p.id_plataforma ");
            }
            if (juegoCriteria.getNombre() != null) {
                JDBCUtils.addClause(query, first, " j.nombre like ? ");
                first = false;
            }
            if (juegoCriteria.getFechaLanzamiento() != null) {
                JDBCUtils.addClause(query, first, " j.fecha_lanzamiento = ?");
                first = false;
            }
            if (juegoCriteria.getIdCreador() != null) {
                JDBCUtils.addClause(query, first, " j.id_creador = ? ");
                first = false;
            }
            if (idioma != null) {
                JDBCUtils.addClause(query, first, " jiw.id_idioma_web like ? ");
                first = false;
            }
            if (juegoCriteria.getCategoriaIDs() != null && juegoCriteria.getCategoriaIDs().length > 0) {
                JDBCUtils.addClause(query, first, addCategoria(juegoCriteria.getCategoriaIDs()).toString());
            }
            if (juegoCriteria.getIdiomaIDs() != null && juegoCriteria.getIdiomaIDs().length > 0) {
                JDBCUtils.addClause(query, first, addIdioma(juegoCriteria.getIdiomaIDs()).toString());
                first = false;
            }
            if (juegoCriteria.getPlataformaIDs() != null && juegoCriteria.getPlataformaIDs().length > 0) {
                JDBCUtils.addClause(query, first, addPlataforma(juegoCriteria.getPlataformaIDs()).toString());
            }
            query.append(" group by j.id_juego");
            preparedStatement = connection.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int i = 1;
            if (juegoCriteria.getNombre() != null) { preparedStatement.setString(i++, "%" + juegoCriteria.getNombre() + "%"); }
            if (juegoCriteria.getFechaLanzamiento() != null) { preparedStatement.setInt(i++, juegoCriteria.getFechaLanzamiento()); }
            if (juegoCriteria.getIdCreador() != null) { preparedStatement.setInt(i++, juegoCriteria.getIdCreador()); }
            if (idioma != null) { preparedStatement.setString(i, idioma); }
            resultSet = preparedStatement.executeQuery();
            List<Juego> juegos = new ArrayList<>();
            Juego juego;
            int currentCount = 0;
            if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                do {
                    juego = new Juego();
                    juegos.add(loadNext(connection, resultSet, idioma, juego));
                    currentCount++;
                } while ((currentCount < count) && resultSet.next());
            }
            int total = JDBCUtils.getTotalRows(resultSet);
            return new Resultados<>(juegos, startIndex, total);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public Resultados<Juego> findAllByDate(Connection connection, String idioma, int startIndex, int count) throws DataException {
        Juego juego;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
			query = new StringBuilder();
			query.append("select id_juego, nombre,fecha_lanzamiento, id_creador ");
			query.append("from juego ");
			query.append("order by fecha_lanzamiento desc");
            preparedStatement = connection.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            List<Juego> juegos = new ArrayList<>();
            int currentCount = 0;
            if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                do {
                    juego = new Juego();
                    juegos.add(loadNext(connection, resultSet, idioma, juego));
                    currentCount++;
                } while ((currentCount < count) && resultSet.next());
            }
            int total = JDBCUtils.getTotalRows(resultSet);
			return new Resultados<>(juegos, startIndex, total);
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public Juego findById(Connection connection, Integer idJuego, String idioma) throws DataException {
        Juego juego;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
        	query = new StringBuilder();
        	query.append("select j.id_juego, j.nombre, j.fecha_lanzamiento, j.id_creador, ji.informacion ");
        	query.append("from juego j inner join juego_idiomaweb ji on j.id_juego=ji.id_juego ");
        	query.append(" where j.id_juego = ? and ji.id_idioma_web = ?");
            preparedStatement = connection.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setInt(1, idJuego);
            preparedStatement.setString(2, idioma);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                juego = new Juego();
                return loadNext(connection, resultSet, idioma, juego);
            } else {
                throw new InstanceNotFoundException("Error " + idJuego + " id introducido incorrecto", Juego.class.getName());
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
    public List<Juego> findAllByValoracion(Connection connection, String idioma) throws DataException {
        Juego juego;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
        	query = new StringBuilder();
        	query.append("select j.id_juego, j.nombre,j.fecha_lanzamiento, j.id_creador ");
        	query.append("from juego j inner join usuarios_juego uj on j.id_juego = uj.id_juego ");
        	query.append("group by j.nombre order by avg(uj.puntuacion) desc Limit 3");
            preparedStatement = connection.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = preparedStatement.executeQuery();
            List<Juego> juegos = new ArrayList<>();
            while (resultSet.next()) {
                juego = new Juego();
                juegos.add(loadNext(connection, resultSet, idioma, juego));
            }
            return juegos;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    @Override
    public List<Juego> findByIDs(Connection connection, List<Integer> idsJuegos, String idioma) throws DataException {
        Juego juego;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
		StringBuilder query;
        try {
            query = new StringBuilder();
            query.append("select j.id_juego, j.nombre, j.fecha_lanzamiento, j.id_creador, ji.informacion ");
            query.append("from juego j inner join juego_idiomaweb ji on j.id_juego=ji.id_juego ");
            query.append("where ji.id_idioma_web like ? and j.id_juego in (");
            JDBCUtils.anhadirIN(query, idsJuegos);
            preparedStatement = connection.prepareStatement(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, idioma);
            resultSet = preparedStatement.executeQuery();
			List<Juego> juegos = new ArrayList<>();
            while (resultSet.next()) {
                juego = new Juego();
                juegos.add(loadNext(connection, resultSet, idioma ,juego));
            }
            return juegos;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public Juego create(Connection connection, Juego juego) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringBuilder query;
        try {
        	query = new StringBuilder();
        	query.append("Insert Into juego(Nombre, fecha_lanzamiento, id_creador) ");
        	query.append("values (?,?,?)");
            preparedStatement = connection.prepareStatement(query.toString(), Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            preparedStatement.setString(i++, juego.getNombre());
            preparedStatement.setInt(i++, juego.getFechaLanzamiento());
            preparedStatement.setInt(i, juego.getIdCreador());
            int insertRow = preparedStatement.executeUpdate();
            if (insertRow == 0) {
                throw new SQLException(" No se pudo insertar");
            }
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Integer idJuego = resultSet.getInt(1);
                juego.setIdJuego(idJuego);
            } else {
                throw new DataException("Problemas al autogenerar primary key");
            }
            return juego;
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DataException(ex);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public boolean update(Connection connection, Juego juego) throws DataException {
        PreparedStatement preparedStatement = null;
        StringBuilder query;
        try {
            query = new StringBuilder();
			query.append("UPDATE Juego ");
            boolean first = true;
            if (juego.getNombre() != null) {
                JDBCUtils.addUpdate(query, first, "nombre = ? ");
                first = false;
            }
            if (juego.getFechaLanzamiento() != null) {
                JDBCUtils.addUpdate(query, first, "FECHA_LANZAMIENTO = ? ");
                first = false;
            }
            if (juego.getIdCreador() != null) {
                JDBCUtils.addUpdate(query, first, "ID_CREADOR = ? ");
            }
            query.append("WHERE id_juego = ? ");
            preparedStatement = connection.prepareStatement(query.toString());
            int i = 1;
            if (juego.getNombre() != null) preparedStatement.setString(i++, juego.getNombre());
            if (juego.getFechaLanzamiento() != null) preparedStatement.setInt(i++, juego.getFechaLanzamiento());
            if (juego.getIdCreador() != null) preparedStatement.setInt(i++, juego.getIdCreador());
            preparedStatement.setInt(i, juego.getIdJuego());
            int updatedRows = preparedStatement.executeUpdate();
			return updatedRows == 1;
		} catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeStatement(preparedStatement);
        }
    }


    public boolean delete(Connection connection, Integer id) throws DataException {
        PreparedStatement preparedStatement = null;
		StringBuilder query;
        try {
        	query = new StringBuilder();
        	query.append("DELETE FROM Juego ");
        	query.append("WHERE id_juego = ?");
            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setInt(1, id);
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
    public Integer puntuacion(Connection connection, Integer idJuego) throws DataException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer puntuacion = null;
		StringBuilder query;
        try {
            query = new StringBuilder();
			query.append("select ROUND(AVG(puntuacion)) ");
			query.append("from usuarios_juego ");
			query.append("where id_juego = ?");
            preparedStatement = connection.prepareStatement(query.toString());
            preparedStatement.setInt(1, idJuego);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                puntuacion = resultSet.getInt(1);
            }
			return puntuacion;
        } catch (SQLException e) {
            logger.info(e.getMessage(), e);
            throw new DataException(e);
        } finally {
            JDBCUtils.closeResultSet(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }
    }

    public Juego loadNext(Connection connection, ResultSet resultSet, String idioma, Juego juego) throws DataException, SQLException {
        juego.setIdJuego(resultSet.getInt("id_juego"));
        juego.setNombre(resultSet.getString("nombre"));
        juego.setFechaLanzamiento(resultSet.getInt("fecha_lanzamiento"));
        juego.setIdCreador(resultSet.getInt("id_creador"));

        List<Categoria> categorias = categoriaDAO.findByJuego(connection, juego.getIdJuego(), idioma);
        juego.setCategoria(categorias);
        List<Plataforma> plataforma = plataformaDAO.findByJuego(connection, juego.getIdJuego());
        juego.setPlataformas(plataforma);
        List<Idioma> idiomas = idiomaDAO.findByJuego(connection, juego.getIdJuego(), idioma);
        juego.setIdiomas(idiomas);
        List<Edicion> ediciones = edicionDAO.findByIdJuego(connection, juego.getIdJuego());
        juego.setEdiciones(ediciones);
        return juego;
    }

    private StringBuilder addCategoria(int[] categorias) {
        boolean inner = true;
        StringBuilder lista = new StringBuilder();
        for (Integer c : categorias) {
            lista.append(inner ? " (c.id_categoria = " + c : " OR c.id_categoria = " + c);
            inner = false;
        }
        lista.append(" ) ");
        return lista;
    }

    private StringBuilder addPlataforma(int[] plataforma) {
        boolean inner = true;
        StringBuilder lista = new StringBuilder();
        for (Integer p : plataforma) {
            lista.append(inner ? " (p.id_plataforma = " + p : " OR p.id_plataforma = " + p);
            inner = false;
        }
        lista.append(" ) ");
        return lista;
    }

    private StringBuilder addIdioma(String[] idioma) {
        boolean inner = true;
        StringBuilder lista = new StringBuilder();
        for (String i : idioma) {
            lista.append(inner ? " (i.id_idioma LIKE '" + i + "'" : " OR i.id_idioma LIKE '" + i + "'");
            inner = false;
        }
        lista.append(" ) ");
        return lista;
    }
}
