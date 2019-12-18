package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.ItemBiblioteca;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PuntuacionController {

    private static UsuarioService usuarioService = null;
    private static Logger logger = LogManager.getLogger(PuntuacionController.class);

    public PuntuacionController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos, Usuario usuario) throws DataException {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();


        if (usuario != null && (json.get(Constantes.IDJUEGO).getAsString() != null || !json.get(Constantes.IDJUEGO).getAsString().equals(""))
                && (json.get(Constantes.PUNTUACION).getAsString() != null || !json.get(Constantes.PUNTUACION).getAsString().equals(""))) {
            Integer idJuego = Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString());
            ItemBiblioteca puntuacionUsuario = usuarioService.findByIdEmail(usuario.getEmail(), idJuego);
            puntuacionUsuario.setPuntuacion(Integer.valueOf(json.get(Constantes.PUNTUACION).getAsString()));

            if (usuarioService.borrarJuegoBiblioteca(usuario.getEmail(), idJuego) && usuarioService.create(puntuacionUsuario)) {
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            } else {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.UPDATE_FAIL.getCode());
                logger.warn(Error.UPDATE_FAIL.getMsg());
            }
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            respuesta.addProperty(Constantes.STATUSMSG, Error.ID_EXPIRED.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
