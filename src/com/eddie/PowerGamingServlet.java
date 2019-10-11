package com.eddie;

import com.eddie.ecommerce.model.Response;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.eddie.utils.util.Error;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Scanner;

public class PowerGamingServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(PowerGamingServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println("funciona PowerGaming");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonRequest = null;
        try {
            Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
            StringBuilder sbResult = new StringBuilder();
            while (scanner.hasNextLine()) {
                sbResult.append(scanner.nextLine());
            }
            jsonRequest = new Gson().fromJson(sbResult.toString(), JsonObject.class);
            Response respuesta = new Response();

            if(!jsonRequest.get("Metodo").getAsString().equals("Juego") || !jsonRequest.get("Metodo").getAsString().equals("Biblioteca")|| !jsonRequest.get("Metodo").getAsString().equals("Puntuacion")
                    || !jsonRequest.get("Metodo").getAsString().equals("Inicio")|| !jsonRequest.get("Metodo").getAsString().equals("Password")|| !jsonRequest.get("Metodo").getAsString().equals("Usuario")) {

                /*StringBuilder direccion = new StringBuilder();
                direccion.append();
                direccion.append(jsonRequest.get("Servicio"));*/

                Class datos = JsonElement.class;
                Class action = String.class;
                Class idiomaWeb = String.class;
                Class<?> cls = Class.forName("com.eddie.controller.".concat(jsonRequest.get("Metodo").getAsString()).concat(jsonRequest.get("Servicio").getAsString()));
                Object obj = cls.newInstance();

                Method method = cls.getDeclaredMethod("procesarPeticion", datos, action, idiomaWeb);
                respuesta = (Response) method.invoke(obj, jsonRequest.get("Entrada"), jsonRequest.get("Action").getAsString(), jsonRequest.get("IdiomaWeb").getAsString());

            }else{
                respuesta.setStatus("KO");
                respuesta.setStatusMsg(Error.INVALID_REQUEST.getCode());
                logger.warn(Error.INVALID_REQUEST.getMsg());
            }

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
        }
    }
}
