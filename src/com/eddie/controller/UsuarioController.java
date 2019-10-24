package com.eddie.controller;

import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.ecommerce.utils.CacheManager;
import com.eddie.utils.*;
import com.eddie.utils.Error;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UsuarioController {
    private static Logger logger = LogManager.getLogger(UsuarioController.class);
    private static UsuarioService usuarioService = null;

    public UsuarioController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws Exception {
        JsonObject json = entrada.getAsJsonObject();
        JsonObject respuesta = new JsonObject();
        Usuario usuario;

        if ("Login".equalsIgnoreCase(action)) {
            String email = LimpiezaValidacion.validEmail(json.get(Constantes.EMAIL).getAsString());
            String password = LimpiezaValidacion.validPassword(json.get(Constantes.PASSWORD).getAsString());

            usuario = usuarioService.login(email, password);
            if (usuario == null) {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.USUARIO_NOT_EXIST.getCode());
                logger.warn(Error.USUARIO_NOT_EXIST.getMsg());
            } else {
                String idLogin = WebUtils.generateSessionId();

                CacheManager.getCacheLogin(Constantes.NOMBRE_CACHE_LOGIN).put(idLogin,usuario);

                usuario.setIdLogin(idLogin);
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                respuesta.add("usuario",  new Gson().toJsonTree(usuario, new TypeToken<Usuario>(){}.getType()).getAsJsonArray());
            }
        } else if ("Logout".equalsIgnoreCase(action)){
            String idLogin = json.get("IdLogin").getAsString();
            CacheManager.getCacheLogin(Constantes.NOMBRE_CACHE_LOGIN).remove(idLogin);
            respuesta.addProperty(Constantes.STATUS, Constantes.OK);
        } else if ("Registro".equalsIgnoreCase(action)) {
            usuario = new Usuario();
            SimpleDateFormat sdf = (SimpleDateFormat) DateUtils.FORMATODATA;
            Date fechaformat = sdf.parse(json.get("FechaNacimiento").getAsString());
            usuario.setNombre(LimpiezaValidacion.validNombre(json.get(Constantes.NOMBRE).getAsString()));
            usuario.setApellido1(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO1).getAsString()));
            usuario.setApellido2(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO2).getAsString()));
            usuario.setEmail(LimpiezaValidacion.validEmail(json.get(Constantes.EMAIL).getAsString()));
            usuario.setTelefono(LimpiezaValidacion.validTelefono(json.get(Constantes.TELEFONO).getAsString()));
            usuario.setPassword(LimpiezaValidacion.validPassword(json.get(Constantes.PASSWORD).getAsString()));
            usuario.setFechaNacimiento(fechaformat);
            usuario.setGenero(LimpiezaValidacion.validGenero(json.get("Genero").getAsString()));

            if (usuario.getApellido1() != null || usuario.getApellido2() != null) {
                usuario.setNombreUser(usuario.getNombre() + usuario.getApellido1().charAt(0) + usuario.getApellido2().charAt(0));
            } else {
                usuario.setNombreUser(usuario.getNombre());
            }
            if (usuario.getEmail() != null && usuario.getNombre() != null && usuario.getFechaNacimiento() != null && usuario.getPassword() != null) {
                if(usuarioService.create(usuario)){
                    respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                }else{
                    respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                    respuesta.addProperty(Constantes.STATUSMSG, Error.CREATE_FAIL.getCode());
                    logger.warn(Error.CREATE_FAIL.getMsg());
                }
            }
        }else if ("Configuracion".equalsIgnoreCase(action)) {
            String idLogin =json.get(Constantes.IDLOGIN).getAsString();
            usuario = CacheManager.getCacheLogin(Constantes.NOMBRE_CACHE_LOGIN).get(idLogin);

            usuario.setNombre(LimpiezaValidacion.validNombre(json.get(Constantes.NOMBRE).getAsString()));
            usuario.setApellido1(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO1).getAsString()));
            usuario.setApellido2(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO2).getAsString()));
            usuario.setEmail(LimpiezaValidacion.validEmail(json.get(Constantes.EMAIL).getAsString()));
            usuario.setTelefono(LimpiezaValidacion.validTelefono(json.get(Constantes.TELEFONO).getAsString()));
            usuario.setPassword(LimpiezaValidacion.validPassword(json.get(Constantes.PASSWORD).getAsString()));
            usuario.setNombreUser(LimpiezaValidacion.validNombreUser(json.get("NombreUsuario").getAsString()));

            if (usuario.getNombre() != null || usuario.getApellido1() != null || usuario.getApellido2() != null
                    || usuario.getTelefono() != null || usuario.getPassword() != null || usuario.getNombreUser() != null) {
                if(usuarioService.update(usuario)){
                    respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                    respuesta.add("usuario",  new Gson().toJsonTree(usuario, new TypeToken<Usuario>(){}.getType()).getAsJsonArray());
                }else{
                    respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                    respuesta.addProperty(Constantes.STATUSMSG, Error.UPDATE_FAIL.getCode());
                    logger.warn(Error.UPDATE_FAIL.getMsg());
                }
            }
        }
        return respuesta;
    }
}
