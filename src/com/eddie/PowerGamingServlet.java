package com.eddie;

import com.eddie.controller.UsuarioController;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.gestor.RedisCache;
import com.eddie.gestor.RulesEngine;
import com.eddie.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.eddie.utils.Error;
import org.apache.logging.log4j.core.appender.rolling.action.ZipCompressAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Scanner;

public class PowerGamingServlet extends HttpServlet {

    private static Properties properties;
    private static Logger logger = LogManager.getLogger(PowerGamingServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().append("Served at: ").append(request.getContextPath());
            String idquery = null != request.getParameter("idquery") ? request.getParameter("idquery") : "";

            if (idquery.equals("log4j2") || idquery.equals("WebConfiguration") || idquery.equals("DBConfiguration")) {
                reloadProperties(idquery);
                logger.info("Se recargaron las properties");
                response.getWriter().append("\n ReloadProperties() OK.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");
        String ipAddress =  request.getRemoteAddr();
        logger.info(ipAddress);
        JsonObject jsonRequest = null;
        try {
            Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");
            StringBuilder sbResult = new StringBuilder();

            while (scanner.hasNextLine()) {
                sbResult.append(scanner.nextLine());
            }
            jsonRequest = new Gson().fromJson(sbResult.toString(), JsonObject.class);
            Usuario usuario = null;

            JsonObject jsonResponse = new JsonObject();


            if(jsonRequest.has(Constantes.IDLOGIN) && RedisCache.getInstance().exist(jsonRequest.get(Constantes.IDLOGIN).getAsString(), 1)) {
                usuario = (Usuario) RedisCache.getInstance().getValue(jsonRequest.get(Constantes.IDLOGIN).getAsString(), 1);
                //Es muy complicado que en 1 hora se cambie de ip global tan rapido
                if(!usuario.getIp().equals(ipAddress)){ usuario = null; }
            }
            //envias la request y recibes la response
            jsonResponse = enviarPeticion(jsonRequest, jsonResponse, usuario, ipAddress);

            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=UTF-8");

            out.println(jsonResponse.toString());

            out.flush();
            out.close();
        } catch (Exception e) {
            logger.fatal("Error Grave en servlet Principal :"+ipAddress+" cuidado puede estar dando por culo este pringao");
            e.printStackTrace();
        }
    }

    private static JsonObject enviarPeticion(JsonObject jsonRequest, JsonObject jsonResponse, Usuario usuario, String ip) throws Exception {
        if(RulesEngine.getInstance().comprobacion(jsonRequest.get("Metodo").getAsString())){
            Class datos = JsonObject.class;
            Class user = Usuario.class;
            Class<?> aClass = null;
            try {
                aClass = Class.forName("com.eddie.controller.".concat(jsonRequest.get("Metodo").getAsString()).concat("Controller"));
                Object obj = aClass.newInstance();
                Method method = null;
                method = aClass.getDeclaredMethod("procesarPeticion", datos, user);
                jsonResponse = (JsonObject) method.invoke(obj, jsonRequest, usuario);
            } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                logger.fatal("Problema en la creacion de la peticion");
                e.printStackTrace();
            }
        }else if(jsonRequest.get("Metodo").getAsString().equals("Usuario")) {
            jsonResponse = UsuarioController.procesarPeticion(jsonRequest, ip);
        }else {
            jsonResponse.addProperty("Status","KO");
            jsonResponse.addProperty("StatusMsg",Error.INVALID_REQUEST.getCode());
            logger.warn(Error.INVALID_REQUEST.getMsg());
        }
        return jsonResponse;
    }

    private static void reloadProperties(String nombrePropertie) {
        try {
            InputStream is;
            try {
                is = new FileInputStream(Constantes.RUTA_PROPERTIES.concat(nombrePropertie).concat("properties"));
            } catch (FileNotFoundException e) {
                //Si no encuentra el archivo obtenmos las properties locales.
                logger.error("No se encuentra el archivo de properties en la ruta: " + Constantes.RUTA_PROPERTIES.concat(nombrePropertie).concat("properties"));
                is = PowerGamingServlet.class.getResourceAsStream(nombrePropertie);
                logger.error("Se obtienen las properties de local");
            }
            properties.load(is);
            is.close();
            //Recargar servicio de mail por si cambió alguna property relacionada con el mail.
        } catch (Exception e) {
            logger.error("Error en la carga inicial" + e.getMessage());
        }
    }
}

