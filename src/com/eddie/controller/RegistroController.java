package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.Constantes;
import com.eddie.utils.DateUtils;
import com.eddie.utils.Error;
import com.eddie.utils.LimpiezaValidacion;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;

public class RegistroController {
    private static Logger logger = LogManager.getLogger(RegistroController.class);
    private static UsuarioService usuarioService = null;

    public RegistroController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos, Usuario user) throws Exception {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();
        Usuario usuario =  new Usuario();

        SimpleDateFormat sdf = (SimpleDateFormat) DateUtils.FORMATODATA;

        if(json.has(Constantes.NOMBRE))usuario.setNombre(json.get(Constantes.NOMBRE).getAsString());
        if(json.has(Constantes.APELLIDO1))usuario.setApellido1(json.get(Constantes.APELLIDO1).getAsString());
        if(json.has(Constantes.APELLIDO2)) usuario.setApellido2(json.get(Constantes.APELLIDO2).getAsString());
        if(json.has(Constantes.EMAIL))usuario.setEmail(json.get(Constantes.EMAIL).getAsString());
        if(json.has(Constantes.TELEFONO))usuario.setTelefono(json.get(Constantes.TELEFONO).getAsString());
        if(json.has(Constantes.PASSWORD))usuario.setPassword(json.get(Constantes.PASSWORD).getAsString());
        if(json.has("FechaNacimiento"))usuario.setFechaNacimiento(sdf.parse(json.get("FechaNacimiento").getAsString()));
        if(json.has("Genero"))usuario.setGenero(json.get("Genero").getAsString());

        if (usuario.getApellido1() != null || usuario.getApellido2() != null) {
            usuario.setNombreUser(usuario.getNombre() + usuario.getApellido1().charAt(0) + usuario.getApellido2().charAt(0));
        } else {
            usuario.setNombreUser(usuario.getNombre());
        }
        if (usuario.getEmail() != null && usuario.getNombre() != null && usuario.getFechaNacimiento() != null && usuario.getPassword() != null) {
            if (usuarioService.create(usuario)) {
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            } else {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.CREATE_FAIL.getCode());
                logger.warn(Error.CREATE_FAIL.getMsg());
            }
        }else{
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
