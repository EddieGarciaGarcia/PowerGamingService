package com.eddie.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eddie.dao.CategoriaDAO;
import com.eddie.dao.EdicionDAO;
import com.eddie.dao.IdiomaDAO;
import com.eddie.dao.JuegoDAO;
import com.eddie.dao.PlataformaDAO;
import com.eddie.utils.JDBCUtils;
import com.eddie.exceptions.DataException;
import com.eddie.exceptions.DuplicateInstanceException;
import com.eddie.exceptions.InstanceNotFoundException;
import com.eddie.model.Categoria;
import com.eddie.model.Edicion;
import com.eddie.model.Idioma;
import com.eddie.model.Juego;
import com.eddie.model.JuegoCriteria;
import com.eddie.model.Plataforma;
import com.eddie.service.CategoriaService;
import com.eddie.service.IdiomaService;
import com.eddie.service.PlataformaService;
import com.eddie.model.Resultados;
import com.eddie.service.impl.CategoriaServiceImpl;
import com.eddie.service.impl.IdiomaServiceImpl;
import com.eddie.service.impl.PlataformaServiceImpl;


public class JuegoDAOImpl implements JuegoDAO{
		
		private static Logger logger=LogManager.getLogger(JuegoDAOImpl.class);
		
		private CategoriaDAO categoriaDAO = null;
		private PlataformaDAO plataformaDAO = null;
		private IdiomaDAO idiomaDAO=null;
		private EdicionDAO edicionDAO=null;
		
		public JuegoDAOImpl() {
			categoriaDAO = new CategoriaDAOImpl();
			plataformaDAO= new PlataformaDAOImpl();
			idiomaDAO= new IdiomaDAOImpl();
			edicionDAO= new EdicionDAOImpl();
		}
		
		public Resultados<Juego> findByJuegoCriteria(JuegoCriteria jc, String idioma, Connection connection, int startIndex, int count) throws DataException {
			
			if(logger.isDebugEnabled()) {
				logger.debug("JuegoCriteria = "+jc.toString()+" ,idioma = "+idioma);
			}
			
			PreparedStatement pst=null;
			ResultSet rs=null;
			StringBuilder strb=null;
			try {
				
				strb=new StringBuilder("select j.id_juego, j.nombre, j.fecha_lanzamiento, j.id_creador from juego j INNER JOIN juego_idiomaweb jiw ON j.id_juego = jiw.id_juego" );
				
				boolean first=true;
				
			
				if(jc.getCategoriaIDs()!=null && jc.getCategoriaIDs().length>0) {
					strb.append(" inner join juego_categoria jc on j.id_juego=jc.id_juego inner join categoria c on jc.id_categoria=c.id_categoria ");
				}
				
				if(jc.getIdiomaIDs()!=null && jc.getIdiomaIDs().length>0) {
					strb.append(" inner join juego_idioma ji on j.id_juego=ji.id_juego inner join idioma i on ji.id_idioma=i.id_idioma ");
				}
				
				if(jc.getPlataformaIDs()!=null && jc.getPlataformaIDs().length>0) {
					strb.append(" inner join juego_plataforma jp on j.id_juego=jp.id_juego inner join plataforma p on jp.id_plataforma=p.id_plataforma ");
				}
				
				if(jc.getNombre()!=null) {
					JDBCUtils.addClause(strb,first," j.nombre like ? ");
					first=false;
				}
				
				if(jc.getFechaLanzamiento()!=null) {
					JDBCUtils.addClause(strb,first," j.fecha_lanzamiento = ?");
					first=false;
				}
			
				if(jc.getIdCreador()!=null) {
					JDBCUtils.addClause(strb,first," j.id_creador = ? ");
					first=false;
				}
				
				if(idioma!=null) {
					JDBCUtils.addClause(strb,first," jiw.id_idioma_web like ? ");
					first=false;
				}
				
				/*
				if (!jc.getCategoria().isEmpty()) {
					JDBCUtils.addClause(strb, first,addCategoria(jc.getCategoria()).toString());	
					first = false;
				} */
				if (jc.getCategoriaIDs()!=null && jc.getCategoriaIDs().length>0) {
					JDBCUtils.addClause(strb, first, addCategoria(jc.getCategoriaIDs()).toString());
				}
				
				if (jc.getIdiomaIDs()!=null && jc.getIdiomaIDs().length>0) {
					JDBCUtils.addClause(strb, first,addIdioma(jc.getIdiomaIDs()).toString());	
					first = false;
				}
				
				if (jc.getPlataformaIDs()!=null && jc.getPlataformaIDs().length>0) {
					JDBCUtils.addClause(strb, first,addPlataforma(jc.getPlataformaIDs()).toString());	
					first = false;
				}
				
				strb.append(" group by j.id_juego");
				
				pst = connection.prepareStatement(strb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				int i = 1;
				if(jc.getNombre()!=null) {
					pst.setString(i++, "%" +jc.getNombre()+"%");
				}
				if(jc.getFechaLanzamiento()!=null) {
					pst.setInt(i++, jc.getFechaLanzamiento() );
				}
				if(jc.getIdCreador()!=null) {
					pst.setInt(i++, jc.getIdCreador() );
				}
				if (idioma!=null) { 
					pst.setString(i++,idioma);
				}
				
				logger.debug(strb);
			
				rs = pst.executeQuery();
				
				List<Juego> juegos = new ArrayList<Juego>();
				Juego j=null;
				int currentCount=0;
				
				if ((startIndex >=1) && rs.absolute(startIndex)) {
					do {
						j= loadNext(connection, rs, idioma);
						juegos.add(j);
						currentCount++;
					}while((currentCount<count) && rs.next());
				}
				
				int total= JDBCUtils.getTotalRows(rs);
				
				if(logger.isDebugEnabled()) {
					logger.debug("Total peticiones: "+total);
				}
				
				Resultados<Juego> resultados= new Resultados<Juego>(juegos,startIndex,total);
				return resultados;
				}catch (SQLException e) {
					logger.error(e.getMessage(),e);
					throw new DataException(e);
				}finally {
				JDBCUtils.closeResultSet(rs);
				JDBCUtils.closeStatement(pst);
				}
		}

		public Resultados<Juego> findAllByDate(Connection connection, String idioma , int startIndex, int count) throws DataException{
			
				Juego j=null;
				PreparedStatement pst=null;
				ResultSet rs=null;
			try {
				String sql;
				sql="select id_juego, nombre,fecha_lanzamiento, id_creador from juego order by fecha_lanzamiento desc  ";
				
				logger.debug(sql);
				
				pst=connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				
				rs=pst.executeQuery();
				
				List <Juego> juegos=new ArrayList<Juego>();
				int currentCount=0;
				
				if ((startIndex >=1) && rs.absolute(startIndex)) {
					do {
						j= loadNext(connection, rs, idioma);
						juegos.add(j);
						currentCount++;
					}while((currentCount<count) && rs.next());
				}
				
				int total= JDBCUtils.getTotalRows(rs);
				
				if(logger.isDebugEnabled()) {
					logger.debug("Total peticiones: "+total);
				}
				
				Resultados<Juego> resultados= new Resultados<Juego>(juegos,startIndex,total);
				return resultados;
			}catch (SQLException ex) {
				logger.error(ex.getMessage(),ex);
				throw new DataException(ex);
			}finally{
				JDBCUtils.closeResultSet(rs);
				JDBCUtils.closeStatement(pst);
			}
			
			
		}
		
		public Juego findById(Connection connection,Integer idJuego,String idioma) throws InstanceNotFoundException, DataException{
			
			if(logger.isDebugEnabled()) {
				logger.debug("Id= "+idJuego+" , idioma= "+idioma);
			}
			
			
			Juego j=null;
			PreparedStatement pst=null;
			ResultSet rs=null;
			
			CategoriaService categoriaServicio=new CategoriaServiceImpl();
			IdiomaService idiomaServicio=new IdiomaServiceImpl();
			PlataformaService plataformaServicio=new PlataformaServiceImpl();
			
			try {
				String sql;
				sql="select j.id_juego, j.nombre, j.fecha_lanzamiento, j.id_creador, ji.informacion from juego j inner join juego_idiomaweb ji on j.id_juego=ji.id_juego where j.id_juego= ? and ji.id_idioma_web like ?";
				
				pst=connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				
				int i=1;
				//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
				pst.setInt(i++, idJuego);
				pst.setString(i++, idioma);
				rs=pst.executeQuery();
				
				logger.debug(sql);
				
				if(rs.next()){
					j=loadNext(connection, rs, idioma);
				}else {
					throw new InstanceNotFoundException("Error "+idJuego+" id introducido incorrecto", Juego.class.getName());
				}
				
				List<Categoria> categoria = categoriaServicio.findByJuego(idJuego,idioma);
				if (!categoria.isEmpty())
					j.setCategoria(categoria);
				
				List<Plataforma> plataforma = plataformaServicio.findByJuego(idJuego);
				if (!plataforma.isEmpty())
					j.setPlataformas(plataforma);
				
				List<Idioma> idiomas = idiomaServicio.findByJuego(idJuego, idioma);
				if (!idiomas.isEmpty())
					j.setIdiomas(idiomas);
				
				
				
				return j;
			}catch (SQLException ex) {
				logger.error(ex.getMessage(),ex);
				throw new DataException(ex);
			}finally{
				JDBCUtils.closeResultSet(rs);
				JDBCUtils.closeStatement(pst);
			}
		
		}
		
		@Override
		public List<Juego> findAllByValoracion(Connection connection, String idioma) throws DataException {
				Juego j=null;
				PreparedStatement pst=null;
				ResultSet rs=null;
			try {
				String sql;
				sql="select j.id_juego, j.nombre,j.fecha_lanzamiento, j.id_creador\r\n" + 
						"from juego j inner join usuarios_juego uj on j.id_juego=uj.id_juego\r\n" + 
						"group by j.nombre\r\n" + 
						"order by avg(uj.puntuacion) desc Limit 3";
				
				logger.debug(sql);
				
				pst=connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

				rs=pst.executeQuery();
				
				List <Juego> juegos=new ArrayList<Juego>();
				while(rs.next()){
					
					j = loadNext(connection, rs, idioma);
					juegos.add(j);
					
				}
				return juegos;
			}catch (SQLException ex) {
				logger.error(ex.getMessage(),ex);
				throw new DataException(ex);
			}finally{
				JDBCUtils.closeResultSet(rs);
				JDBCUtils.closeStatement(pst);
			}
		}
		
		@Override
		public List<Juego> findByIDs(Connection connection, List<Integer> idsJuegos, String idioma) throws DataException {
			
			if(logger.isDebugEnabled()) {
				logger.debug("Id= "+idsJuegos+" , idioma= "+idioma);
			}
			
			List<Juego> j=new ArrayList<Juego>();
			Juego juego=null;
			PreparedStatement pst=null;
			ResultSet rs=null;
			
			try {
				StringBuilder sql= null;
				sql=new StringBuilder("select j.id_juego, j.nombre, j.fecha_lanzamiento, j.id_creador, ji.informacion from juego j inner join juego_idiomaweb ji on j.id_juego=ji.id_juego where ji.id_idioma_web like ? and j.id_juego in (");

				JDBCUtils.anhadirIN(sql, idsJuegos);
				
				pst=connection.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				
				int i=1;
				//pst.setString(i++,"%"+nombrejuego.toUpperCase()+"%");
				
				pst.setString(i++, idioma);
				rs=pst.executeQuery();
				
				logger.debug(sql);
				
				while(rs.next()){
					juego=loadNext(connection, rs, idioma);
					j.add(juego);
				}
				
				return j;
			}catch (SQLException ex) {
				logger.error(ex.getMessage(),ex);
				throw new DataException(ex);
			}finally{
				JDBCUtils.closeResultSet(rs);
				JDBCUtils.closeStatement(pst);
			}
			
		}
		
		
		public Juego create(Connection connection,Juego j) throws DuplicateInstanceException, DataException{
			
			if(logger.isDebugEnabled()) {
				logger.debug("Juego = "+j.toString());
			}
			
			PreparedStatement pst=null;
			ResultSet rs=null;
			try {
				String sql;
				sql="Insert Into juego(Nombre, fecha_lanzamiento, id_creador) "
						+ "values (?,?,?)";
				
				pst=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i=1;
				pst.setString(i++, j.getNombre());
				pst.setInt(i++, j.getFechaLanzamiento());			
				pst.setInt(i++, j.getIdCreador());
				
				
				logger.debug(sql);
				
				int insertRow=pst.executeUpdate();
				
				if(insertRow == 0) {
					throw new SQLException(" No se pudo insertar");
				}
				
				rs=pst.getGeneratedKeys();
				
				if(rs.next()) {
					Integer idJuego=rs.getInt(1);
					j.setIdJuego(idJuego);
				}else {
					throw new DataException("Problemas al autogenerar primary key");
				}
				
				return j;
			}catch (SQLException ex) {
				logger.error(ex.getMessage(),ex);
				throw new DataException(ex);
			}finally{
				JDBCUtils.closeResultSet(rs);
				JDBCUtils.closeStatement(pst);
			}
			
		}
		public boolean update(Connection connection,Juego j) throws InstanceNotFoundException, DataException{
			
			if(logger.isDebugEnabled()) {
				logger.debug("Juego = "+j.toString());
			}
			
			PreparedStatement preparedStatement = null;
			StringBuilder sqlupdate;
			try {	
				sqlupdate = new StringBuilder(" UPDATE Juego");
				
				boolean first = true;
				
				if (j.getNombre()!=null) {
					JDBCUtils.addUpdate(sqlupdate,first," nombre = ?");
					first=false;
				}
				
				if (j.getFechaLanzamiento()!=null) {
					JDBCUtils.addUpdate(sqlupdate,first," FECHA_LANZAMIENTO = ?");
					first=false;
				}
						
				if (j.getIdCreador()!=null) {
					JDBCUtils.addUpdate(sqlupdate,first," ID_CREADOR = ?");
					first=false;
				}
				
				sqlupdate.append("WHERE id_juego = ?");
				
				logger.debug(sqlupdate);
				
				preparedStatement = connection.prepareStatement(sqlupdate.toString());
				

				int i = 1;
				if (j.getNombre()!=null) 
					preparedStatement.setString(i++,j.getNombre());
				
				if (j.getFechaLanzamiento()!=null) 
					preparedStatement.setInt(i++, j.getFechaLanzamiento());
				
				if (j.getIdCreador()!=null) 
					preparedStatement.setInt(i++,j.getIdCreador());
				

				preparedStatement.setInt(i++, j.getIdJuego());

				int updatedRows = preparedStatement.executeUpdate();

				if (updatedRows > 1) {
					throw new SQLException("Id duplicado = '" + j.getIdJuego() + "' en table 'Juego'");
				}     
				return true;
			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new DataException(e);    
			} finally {
				JDBCUtils.closeStatement(preparedStatement);
			}              		
			
		}
			

		public void delete(Connection connection,Integer id) throws DataException {
			
			if(logger.isDebugEnabled()) {
				logger.debug("Id= "+id);
			}
			
			PreparedStatement preparedStatement = null;

			try {
				String queryString =	
						  "DELETE FROM Juego WHERE id_juego = ? ";
				
				preparedStatement = connection.prepareStatement(queryString);
				
				logger.debug(queryString);
				
				int i = 1;
				preparedStatement.setInt(i++, id);

				int removedRows = preparedStatement.executeUpdate();

				if (removedRows == 0) {
					throw new InstanceNotFoundException(id,Juego.class.getName());
				} 
	

			} catch (SQLException e) {
				logger.error(e.getMessage(),e);
				throw new DataException(e);
			} finally {
				JDBCUtils.closeStatement(preparedStatement);
			}
			
		}
		
		@Override
		public Integer puntuacion(Connection connection, Integer idJuego) throws DataException {
			if(logger.isDebugEnabled()) {
				logger.debug(" Idjuego = {}",idJuego);
			}
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			Integer puntuacion = null;
			try {
				StringBuilder sql= null;
				sql =new StringBuilder("select ROUND(AVG(puntuacion)) from usuarios_juego where id_juego=?");
				
				logger.debug(sql);
				preparedStatement = connection.prepareStatement(sql.toString());

				int i = 1;
				preparedStatement.setInt(i++, idJuego);

				resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next()) {
					puntuacion = resultSet.getInt(1);
				}

			} catch (SQLException e) {
				logger.info(e.getMessage(), e);
				throw new DataException(e);
			} finally {
				JDBCUtils.closeResultSet(resultSet);
				JDBCUtils.closeStatement(preparedStatement);
			}

			return puntuacion;
		}
		
		public Juego loadNext(Connection c, ResultSet rs, String idioma) 
			throws DataException,SQLException{
				int i=1;
				Integer id  = rs.getInt(i++);
				String nombre = rs.getString(i++);
				Integer fechaLanzamiento=rs.getInt(i++);
				Integer idCreador = rs.getInt(i++);
				
				Juego j= new Juego();
				j.setIdJuego(id);
				j.setNombre(nombre);
				j.setFechaLanzamiento(fechaLanzamiento);
				j.setIdCreador(idCreador);
				
				List<Categoria> categorias = categoriaDAO.findByJuego(c, id, idioma);
				j.setCategoria(categorias);
				List<Plataforma> plataforma = plataformaDAO.findByJuego(c, id);
				j.setPlataformas(plataforma);
				List<Idioma> idiomas = idiomaDAO.findByJuego(c, id, idioma);
				j.setIdiomas(idiomas);
				List<Edicion>ediciones = edicionDAO.findByIdJuego(c, id);
				j.setEdiciones(ediciones);
				return j;
				
		}
		
		private StringBuilder addCategoria(int[] categorias) {
		
			boolean inner = true;
			StringBuilder lista = new StringBuilder();
			for (Integer c : categorias) {
				lista.append(inner ? " (c.id_categoria = "+c : " OR c.id_categoria = " + c);
				inner=false;	
			}
			lista.append(" ) ");
			return lista;
		}
		private StringBuilder addPlataforma(int[] plataforma) {
		
			boolean inner = true;
			StringBuilder lista = new StringBuilder();
			for (Integer p : plataforma) {
				lista.append(inner ? " (p.id_plataforma = "+p : " OR p.id_plataforma = " + p);
				inner=false;	
			}
			lista.append(" ) ");
			return lista;
		}
		private StringBuilder addIdioma(String[] idioma) {
			
			boolean inner = true;
			StringBuilder lista = new StringBuilder();
			for (String i : idioma) {
				lista.append(inner ? " (i.id_idioma LIKE '"+ i.toString()+"'" : " OR i.id_idioma LIKE '" + i.toString()+"'");
				inner=false;	
			}
			lista.append(" ) ");
			return lista;
		}

		

		
		
		
}
