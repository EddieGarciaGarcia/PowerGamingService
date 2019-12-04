package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.ItemBiblioteca;
import com.eddie.ecommerce.model.Juego;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BibliotecaController {

    private static Logger logger = LogManager.getLogger(BibliotecaController.class);
    private static UsuarioService usuarioService = null;
    private static JuegoService juegoService = null;

    public BibliotecaController() {
        usuarioService = new UsuarioServiceImpl();
        juegoService = new JuegoServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) throws DataException {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        String idiomaWeb = datos.get("IdiomaWeb").getAsString();
        JsonObject respuesta = new JsonObject();

        //Controlar que esta logeado
        Usuario usuario = (Usuario) RedisCache.getInstance().getValue(json.get(Constantes.IDLOGIN).getAsString(),1);

        if (usuario != null) {
            List<ItemBiblioteca> biblioteca = usuarioService.findByUsuario(usuario.getEmail());

            List<Integer> juegoIDs = new ArrayList<>();
            for (ItemBiblioteca it : biblioteca) {
                juegoIDs.add(it.getIdJuego());
            }
            List<Juego> juegos = null;

            if (!juegoIDs.isEmpty()) {
                juegos = juegoService.findByIDs(juegoIDs, idiomaWeb);
            }
            respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            respuesta.add("Puntuacion", new Gson().toJsonTree(biblioteca, new TypeToken<List<ItemBiblioteca>>() {}.getType()).getAsJsonArray());
            respuesta.add("JuegosBiblioteca", new Gson().toJsonTree(juegos, new TypeToken<List<Juego>>() {}.getType()).getAsJsonArray());
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
