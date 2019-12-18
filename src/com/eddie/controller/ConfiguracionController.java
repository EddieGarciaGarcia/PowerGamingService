package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.eddie.utils.LimpiezaValidacion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfiguracionController {

    private static Logger logger = LogManager.getLogger(ConfiguracionController.class);
    private static UsuarioService usuarioService = null;

    public ConfiguracionController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos, Usuario usuario) throws Exception {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();

        if(usuario != null) {
            if(json.has(Constantes.NOMBRE))usuario.setNombre(LimpiezaValidacion.validNombre(json.get(Constantes.NOMBRE).getAsString()));
            usuario.setApellido1(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO1).getAsString()));
            usuario.setApellido2(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO2).getAsString()));
            usuario.setEmail(json.get(Constantes.EMAIL).getAsString());
            usuario.setTelefono(LimpiezaValidacion.validTelefono(json.get(Constantes.TELEFONO).getAsString()));
            usuario.setPassword(LimpiezaValidacion.validPassword(json.get(Constantes.PASSWORD).getAsString()));
            usuario.setNombreUser(LimpiezaValidacion.validNombreUser(json.get("NombreUsuario").getAsString()));

            if (usuario.getNombre() != null || usuario.getApellido1() != null || usuario.getApellido2() != null
                    || usuario.getTelefono() != null || usuario.getPassword() != null || usuario.getNombreUser() != null) {
                if (usuarioService.update(usuario)) {
                    respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                    respuesta.add("usuario", new Gson().toJsonTree(usuario, new TypeToken<Usuario>() {}.getType()).getAsJsonArray());
                } else {
                    respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                    respuesta.addProperty(Constantes.STATUSMSG, Error.UPDATE_FAIL.getCode());
                    logger.warn(Error.UPDATE_FAIL.getMsg());
                }
            }
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.ID_EXPIRED.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }

}
