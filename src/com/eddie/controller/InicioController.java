package com.eddie.controller;

import com.eddie.ecommerce.model.*;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.PaisService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.PaisServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

public class InicioController {

    private static JuegoService juegoService = null;
    private static PaisService paisService = null;

    private static Logger logger = LogManager.getLogger(InicioController.class);

    public InicioController() {
        juegoService = new JuegoServiceImpl();
        paisService = new PaisServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) {
        String idiomaWeb = datos.get("IdiomaWeb").getAsString();
        JsonObject respuesta = new JsonObject();
        try {
            respuesta.add("Todos", new Gson().toJsonTree(juegoService.findAllByDate(idiomaWeb), new TypeToken<List<Juego>>() {}.getType()).getAsJsonArray());
            respuesta.add("Valoracion", new Gson().toJsonTree(juegoService.findAllByValoracion(idiomaWeb), new TypeToken<List<Juego>>() {}.getType()).getAsJsonArray());
            if(!RedisCache.getInstance().exist(Constantes.CATEGORIA+idiomaWeb,2)
                    ||!RedisCache.getInstance().exist(Constantes.CREADORES+idiomaWeb,2)
                    ||!RedisCache.getInstance().exist(Constantes.PLATAFORMAS+idiomaWeb,2)
                    ||!RedisCache.getInstance().exist(Constantes.IDIOMAS+idiomaWeb,2)
                    ||!RedisCache.getInstance().exist(Constantes.TIPOEDICIONES+idiomaWeb,2)
                    ||!RedisCache.getInstance().exist(Constantes.FORMATO+idiomaWeb,2)) {
                HashMap<String, List<?>> datosCache = juegoService.datosCacheWeb(idiomaWeb);
                respuesta.add("DatosPrecargaJuego", new Gson().toJsonTree(datosCache, new TypeToken<HashMap<String, List<?>>>() {}.getType()).getAsJsonObject());
                datosCache.forEach((k,v) -> RedisCache.getInstance().setValue(k+idiomaWeb, v,2, 86400));
            }else{
                HashMap<String, List<?>> datosPre = new HashMap<>();
                datosPre.put(Constantes.CATEGORIA, (List<?>) RedisCache.getInstance().getValue(Constantes.CATEGORIA+idiomaWeb,2));
                datosPre.put(Constantes.CREADORES, (List<?>) RedisCache.getInstance().getValue(Constantes.CREADORES+idiomaWeb,2));
                datosPre.put(Constantes.PLATAFORMAS, (List<?>) RedisCache.getInstance().getValue(Constantes.PLATAFORMAS+idiomaWeb,2));
                datosPre.put(Constantes.IDIOMAS, (List<?>) RedisCache.getInstance().getValue(Constantes.IDIOMAS+idiomaWeb,2));
                datosPre.put(Constantes.TIPOEDICIONES, (List<?>) RedisCache.getInstance().getValue(Constantes.TIPOEDICIONES+idiomaWeb,2));
                datosPre.put(Constantes.FORMATO, (List<?>) RedisCache.getInstance().getValue(Constantes.FORMATO+idiomaWeb,2));
                respuesta.add("DatosPrecargaJuego", new Gson().toJsonTree(datosPre, new TypeToken<HashMap<String, List<?>>>() {}.getType()).getAsJsonObject() );
            }
            respuesta.add("Pais", new Gson().toJsonTree(paisService.findAll(), new TypeToken<List<Pais>>() {}.getType()).getAsJsonArray());
            respuesta.addProperty(Constantes.STATUS, Constantes.OK);

        } catch (com.eddie.ecommerce.exceptions.DataException e) {
            logger.debug(e);
        }
        return respuesta;
    }
}
