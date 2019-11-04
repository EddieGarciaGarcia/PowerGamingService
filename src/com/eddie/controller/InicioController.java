package com.eddie.controller;

import com.eddie.ecommerce.model.*;
import com.eddie.ecommerce.service.*;
import com.eddie.ecommerce.service.impl.*;
import com.eddie.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.*;

public class InicioController {

    private static JuegoService juegoService = null;
    private static PaisService paisService = null;

    private static Logger logger = LogManager.getLogger(InicioController.class);

    public InicioController() {
        juegoService = new JuegoServiceImpl();
        paisService = new PaisServiceImpl();
    }
    public static JsonObject procesarPeticion(JsonElement entrada, String action, String idiomaWeb){
        JsonObject respuesta = new JsonObject();
        try {
            if("DatosInicio".equalsIgnoreCase(action)) {
                respuesta.add("Todos", new Gson().toJsonTree(juegoService.findAllByDate(idiomaWeb), new TypeToken<List<Juego>>(){}.getType()).getAsJsonArray());
                respuesta.add("Valoracion", new Gson().toJsonTree(juegoService.findAllByValoracion(idiomaWeb), new TypeToken<List<Juego>>(){}.getType()).getAsJsonArray());
                respuesta.add("DatosPrecargaJuego", new Gson().toJsonTree(juegoService.datosCacheWeb(idiomaWeb), new TypeToken<HashMap<String, List<?>>>(){}.getType()).getAsJsonObject());
                respuesta.add("Pais", new Gson().toJsonTree(paisService.findAll(), new TypeToken<List<Pais>>(){}.getType()).getAsJsonArray());
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            }
        } catch (com.eddie.ecommerce.exceptions.DataException e) {
            logger.debug(e);
        }
        return respuesta;
    }
}
