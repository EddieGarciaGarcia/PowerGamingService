package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.gestor.ConfigurationManager;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.*;
import com.eddie.utils.Error;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsuarioController {
    private static Logger logger = LogManager.getLogger(UsuarioController.class);
    private static UsuarioService usuarioService = null;

    public UsuarioController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) throws Exception {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();
        Usuario usuario = null;
        if(json.has("IdLogin")) {
            RedisCache.getInstance().delValue(json.get("IdLogin").getAsString(),1);
            respuesta.addProperty(Constantes.STATUS, Constantes.OK);
        }else{
            usuario = usuarioService.login(json.get(Constantes.EMAIL).getAsString(), LimpiezaValidacion.validPassword(json.get(Constantes.PASSWORD).getAsString()));
            if (usuario == null) {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.USUARIO_NOT_EXIST.getCode());
                logger.warn(Error.USUARIO_NOT_EXIST.getMsg());
            } else {
                String idLogin = String.valueOf(RandomUtils.nextInt());
                RedisCache.getInstance().setValue(idLogin, usuario,1, 3600);
                usuario.setIdLogin(idLogin);
                usuario.setPassword(null);
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                respuesta.add("usuario",  new Gson().toJsonTree(usuario, new TypeToken<Usuario>(){}.getType()).getAsJsonObject());
            }
        }
        return respuesta;
    }
}
