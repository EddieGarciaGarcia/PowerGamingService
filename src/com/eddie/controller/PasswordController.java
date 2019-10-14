package com.eddie.controller;

import com.eddie.ecommerce.model.Response;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.MailService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.MailServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.util.Constantes;
import com.eddie.utils.util.Error;
import com.eddie.utils.util.LimpiezaValidacion;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PasswordController {
    private static Logger logger = LogManager.getLogger(PasswordController.class);
    private static UsuarioService usuarioService;
    private static MailService mailService;

    public PasswordController() {
        usuarioService = new UsuarioServiceImpl();
        mailService = new MailServiceImpl();
    }

    public static Response procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws Exception {
        JsonObject json = entrada.getAsJsonObject();
        Response respuesta = new Response();
        String email = LimpiezaValidacion.validEmail(json.has(Constantes.EMAIL) ? json.get(Constantes.EMAIL).getAsString() : null);
        if (email != null) {
            if ("ForgotPassword".equalsIgnoreCase(action)) {
                Usuario usuario = usuarioService.findById(email);
                if (usuario == null) {
                    respuesta.setStatus(Constantes.KO);
                    respuesta.setStatusMsg(Error.USUARIO_NOT_EXIST.getCode());
                    logger.warn(Error.USUARIO_NOT_EXIST.getMsg());
                } else {
                    StringBuilder enlace;
                    enlace = new StringBuilder();
                    boolean enviado = mailService.sendMail(email, "Restauracion de contrase�a",
                            "<html><h2>Power Gaming</h2><h4>Pulse en enlace o copielo entero para cambiar su contrase�a:</h4>"
                                    + "<a href='" + enlace + "'>Restablecer contrase�a</a><p>" + enlace + "</p></html>");
                    if (enviado) {
                        respuesta.setStatus(Constantes.OK);
                    } else {
                        respuesta.setStatus(Constantes.KO);
                        respuesta.setStatusMsg(Error.SEND_FAIL.getCode());
                        logger.warn(Error.SEND_FAIL.getMsg());
                    }
                }
            } else if ("ChangePassword".equalsIgnoreCase(action)) {
                String password = json.get(Constantes.PASSWORD).getAsString();
                Usuario usuario = usuarioService.findById(email);
                usuario.setPassword(password);
                boolean actualizado = usuarioService.update(usuario);
                if (actualizado) {
                    respuesta.setStatus(Constantes.OK);
                } else {
                    respuesta.setStatus(Constantes.KO);
                    respuesta.setStatusMsg(Error.UPDATE_FAIL.getCode());
                    logger.warn(Error.UPDATE_FAIL.getMsg());
                }
            } else if ("Mensage".equalsIgnoreCase(action)) {
                String mensage = json.get("Mensage").getAsString();
                boolean enviado = mailService.sendMail("powergaming2019@gmail.com", email, mensage);
                if (enviado) {
                    respuesta.setStatus(Constantes.OK);
                } else {
                    respuesta.setStatus(Constantes.KO);
                    respuesta.setStatusMsg(Error.SEND_FAIL.getCode());
                    logger.warn(Error.SEND_FAIL.getMsg());
                }
            }
        } else {
            respuesta.setStatus(Constantes.KO);
            respuesta.setStatusMsg(Error.INVALID_REQUEST.getCode());
            logger.warn(Error.INVALID_REQUEST.getMsg());
        }

        return respuesta;
    }
}
