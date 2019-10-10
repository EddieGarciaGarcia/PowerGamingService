package com.eddie;

import com.eddie.ecommerce.model.Response;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Scanner;

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
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");

        Connection connection = null;
        JsonObject jsonRequest = null;
        try {
            Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
            StringBuilder sbResult = new StringBuilder();
            while (scanner.hasNextLine()) {
                sbResult.append(scanner.nextLine());
            }
            jsonRequest = new Gson().fromJson(sbResult.toString(), JsonObject.class);
            String servicio = jsonRequest.get("Servicio").toString();
            String metodo = jsonRequest.get("Metodo").toString();
            String direccion = servicio + metodo;

            Class datos = JsonElement.class;
            Class action = String.class;
            Class idiomaWeb = String.class;
            Class<?> cls = Class.forName("com.eddie.controller.".concat(direccion));
            Object obj = cls.newInstance();

            Method method = cls.getDeclaredMethod("procesarPeticion", datos, action, idiomaWeb);
            Response respuesta = (Response) method.invoke(obj, jsonRequest.get("Entrada"), jsonRequest.get("Action").toString(), jsonRequest.get("IdiomaWeb").toString());

            JsonObject responseJSON = new JsonObject();
            responseJSON.addProperty("Status",respuesta.getStatus());
            responseJSON.addProperty("StatusMsg",respuesta.getStatusMsg());
            responseJSON.addProperty("Salida",respuesta.getSalida());

            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=UTF-8");

            out.println(responseJSON.toString());

            out.flush();
            out.close();
        } catch (Exception e) {
            logger.fatal("Error Grave en servlet Principal");
        } finally {
            releaseConnection(connection);
        }
    }

    /**
     * Libera conexion
     *
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
