package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.MailService;
import com.eddie.ecommerce.service.impl.MailServiceImpl;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactoController {
    private static Logger logger = LogManager.getLogger(ContactoController.class);
    private static MailService mailService = null;

    public ContactoController() {
        mailService = new MailServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos, Usuario usuario) throws Exception {
        JsonObject json = datos.get("Entrada").getAsJsonObject();

        JsonObject respuesta = new JsonObject();
        String email = json.has(Constantes.EMAIL) ? json.get(Constantes.EMAIL).getAsString() : null;
        if(email != null) {
            if (mailService.sendMail("powergaming2019@gmail.com", email, json.get("Mensage").getAsString())) {
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            } else {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.SEND_FAIL.getCode());
                logger.warn(Error.SEND_FAIL.getMsg());
            }
        }else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.SEND_FAIL.getCode());
            logger.warn(Error.SEND_FAIL.getMsg());
        }
        return respuesta;
    }
}
