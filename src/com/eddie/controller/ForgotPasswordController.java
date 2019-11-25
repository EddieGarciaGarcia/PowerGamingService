package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.MailService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.MailServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgotPasswordController {
    private static Logger logger = LogManager.getLogger(ForgotPasswordController.class);
    private static UsuarioService usuarioService = null;
    private static MailService mailService = null;

    public ForgotPasswordController() {
        usuarioService = new UsuarioServiceImpl();
        mailService = new MailServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) throws Exception {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();
        Usuario usuario = null;
        String email = json.has(Constantes.EMAIL) ? json.get(Constantes.EMAIL).getAsString() : null;
        if (email != null) {
            usuario = usuarioService.findById(email);
            if (usuario == null) {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.USUARIO_NOT_EXIST.getCode());
                logger.warn(Error.USUARIO_NOT_EXIST.getMsg());
            } else {
                StringBuilder enlace = new StringBuilder();
                String cabecera = "Restauracion de contrase�a";
                String cuerpo = "<html><h2>Power Gaming</h2><h4>Pulse en enlace o copielo entero para cambiar su contrase�a:</h4>"
                        + "<a href='" + enlace + "'>Restablecer contrase�a</a><p>" + enlace + "</p></html>";

                if (mailService.sendMail(email, cabecera, cuerpo)) {
                    respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                } else {
                    respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                    respuesta.addProperty(Constantes.STATUSMSG, Error.SEND_FAIL.getCode());
                    logger.warn(Error.SEND_FAIL.getMsg());
                }
            }
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.INVALID_REQUEST.getCode());
            logger.warn(Error.INVALID_REQUEST.getMsg());
        }

        return respuesta;
    }
}
