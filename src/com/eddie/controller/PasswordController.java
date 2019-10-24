package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.MailService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.MailServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.eddie.utils.LimpiezaValidacion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordController {
    private static Logger logger = LogManager.getLogger(PasswordController.class);
    private static UsuarioService usuarioService = null;
    private static MailService mailService = null;

    public PasswordController() {
        usuarioService = new UsuarioServiceImpl();
        mailService = new MailServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws Exception {
        JsonObject json = entrada.getAsJsonObject();
        JsonObject respuesta = new JsonObject();

        String email = LimpiezaValidacion.validEmail(json.has(Constantes.EMAIL) ? json.get(Constantes.EMAIL).getAsString() : null);
        if (email != null) {
            if ("ForgotPassword".equalsIgnoreCase(action)) {
                Usuario usuario = usuarioService.findById(email);
                if (usuario == null) {
                    respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                    respuesta.addProperty(Constantes.STATUSMSG,Error.USUARIO_NOT_EXIST.getCode());
                    logger.warn(Error.USUARIO_NOT_EXIST.getMsg());
                } else {
                    StringBuilder enlace;
                    enlace = new StringBuilder();
                    boolean enviado = mailService.sendMail(email, "Restauracion de contrase�a",
                            "<html><h2>Power Gaming</h2><h4>Pulse en enlace o copielo entero para cambiar su contrase�a:</h4>"
                                    + "<a href='" + enlace + "'>Restablecer contrase�a</a><p>" + enlace + "</p></html>");
                    if (enviado) {
                        respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                    } else {
                        respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                        respuesta.addProperty(Constantes.STATUSMSG,Error.SEND_FAIL.getCode());
                        logger.warn(Error.SEND_FAIL.getMsg());
                    }
                }
            } else if ("ChangePassword".equalsIgnoreCase(action)) {
                String password = json.get(Constantes.PASSWORD).getAsString();
                Usuario usuario = usuarioService.findById(email);
                usuario.setPassword(password);
                boolean actualizado = usuarioService.update(usuario);
                if (actualizado) {
                    respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                } else {
                    respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                    respuesta.addProperty(Constantes.STATUSMSG,Error.UPDATE_FAIL.getCode());
                    logger.warn(Error.UPDATE_FAIL.getMsg());
                }
            } else if ("Mensage".equalsIgnoreCase(action)) {
                String mensage = json.get("Mensage").getAsString();
                boolean enviado = mailService.sendMail("powergaming2019@gmail.com", email, mensage);
                if (enviado) {
                    respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                } else {
                    respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                    respuesta.addProperty(Constantes.STATUSMSG,Error.SEND_FAIL.getCode());
                    logger.warn(Error.SEND_FAIL.getMsg());
                }
            }
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG,Error.INVALID_REQUEST.getCode());
            logger.warn(Error.INVALID_REQUEST.getMsg());
        }

        return respuesta;
    }
}
