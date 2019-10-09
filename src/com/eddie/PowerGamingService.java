package com.eddie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;

public class PowerGamingService extends HttpServlet {

    private static Logger logger = LogManager.getLogger(PowerGamingService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().append("Served at: ").append(request.getContextPath());
            String idquery = null != request.getParameter("idquery") ? request.getParameter("idquery") : "";

            if (idquery.equals("reloadProperties")) {
                //RECARGAR LAS PROPERTIES
                logger.info("Se recargaron las properties");
                response.getWriter().append("\n ReloadProperties() OK.");
            }

        } catch (Exception e) {
            logger.error("Error al recargar fichero de properties".concat(e.getMessage()));
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Connection connection = null;

        try{



        }catch(Exception e){



        }finally{
            releaseConnection(connection);
        }
    }

    /**
     * Libera conexion
     * @param con {{@link Connection}} connection
     */
    private void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                logger.fatal("No se pudo cerrar la conexión a la base de datos.", e);
            }
        }
    }
}
