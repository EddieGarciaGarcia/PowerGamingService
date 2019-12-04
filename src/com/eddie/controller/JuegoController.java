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

import java.util.List;

public class JuegoController {
    private static Logger logger = LogManager.getLogger(JuegoController.class);

    private static JuegoService juegoService = null;
    private static UsuarioService usuarioService = null;


    public JuegoController() {
        juegoService = new JuegoServiceImpl();
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) throws DataException {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        String idiomaWeb = datos.get("IdiomaWeb").getAsString();
        JsonObject respuesta = new JsonObject();
        Usuario usuario = null;

        if (json.has(Constantes.IDLOGIN)) {
            usuario = (Usuario) RedisCache.getInstance().getValue(json.get(Constantes.IDLOGIN).getAsString(),1);
        }

        Juego juego = null;
        List<ItemBiblioteca> comentarios = null;
        if (json.has(Constantes.IDJUEGO)) {
            if (usuario != null) {
                juego = juegoService.findById(Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString()), usuario.getEmail(), idiomaWeb);
            } else {
                juego = juegoService.findById(Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString()), null, idiomaWeb);
            }
            //Listado de comentarios
            comentarios = juegoService.findByJuego(juego.getIdJuego());

            respuesta.add("Juego", new Gson().toJsonTree(juego, new TypeToken<Juego>() {}.getType()).getAsJsonObject());
            respuesta.add("Comentarios", new Gson().toJsonTree(comentarios, new TypeToken<List<ItemBiblioteca>>() {}.getType()).getAsJsonArray());
            respuesta.addProperty(Constantes.STATUS, Constantes.OK);
        }else{
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
