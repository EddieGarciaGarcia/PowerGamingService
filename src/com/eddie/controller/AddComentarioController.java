package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.ItemBiblioteca;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class AddComentarioController {
    private static JuegoService juegoService = null;
    private static Logger logger = LogManager.getLogger(AddComentarioController.class);

    public AddComentarioController() {
        juegoService = new JuegoServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) throws DataException {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();

        //Controlar que esta logeado
        Usuario usuario = (Usuario) RedisCache.getInstance().getValue(json.get(Constantes.IDLOGIN).getAsString());
        if (usuario != null && (json.get(Constantes.IDJUEGO).getAsString() != null || !json.get(Constantes.IDJUEGO).getAsString().equals(""))) {
            ItemBiblioteca itemBiblioteca = new ItemBiblioteca();
            itemBiblioteca.setIdJuego(Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString()));
            itemBiblioteca.setComentario(json.get("Comentario").getAsString());
            itemBiblioteca.setEmail(usuario.getEmail());
            Date date = new Date();
            itemBiblioteca.setFechaComentario(new java.sql.Date(date.getTime()));
            if (juegoService.addComent(itemBiblioteca)) {
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            } else {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.UPDATE_FAIL.getCode());
                logger.warn(Error.UPDATE_FAIL.getMsg());
            }
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
