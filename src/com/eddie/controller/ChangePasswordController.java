package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChangePasswordController {

    private static Logger logger = LogManager.getLogger(ChangePasswordController.class);
    private static UsuarioService usuarioService = null;

    public ChangePasswordController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos, Usuario user) throws Exception {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();

        String email = json.has(Constantes.EMAIL) ? json.get(Constantes.EMAIL).getAsString() : null;
        String password = json.get(Constantes.PASSWORD).getAsString();
        if (email != null || password != null) {
            Usuario usuario = usuarioService.findById(email);
            usuario.setPassword(password);
            if (usuarioService.update(usuario)) {
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            } else {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.UPDATE_FAIL.getCode());
                logger.warn(Error.UPDATE_FAIL.getMsg());
            }
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.INVALID_REQUEST.getCode());
            logger.warn(Error.INVALID_REQUEST.getMsg());
        }
        return respuesta;
    }
}
