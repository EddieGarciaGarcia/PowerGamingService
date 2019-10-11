package com.eddie.controller;

import com.eddie.ecommerce.model.Response;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.util.*;
import com.eddie.utils.util.Error;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;


public class UsuarioController {
    private static Logger logger = LogManager.getLogger(UsuarioController.class);
    private static UsuarioService usuarioService;

    public UsuarioController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static Response procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws Exception {
        JsonObject json = entrada.getAsJsonObject();
        Response respuesta = new Response();
        Usuario usuario;
        Gson gson = new Gson();

        if ("Login".equalsIgnoreCase(action)) {
            String email = LimpiezaValidacion.validEmail(json.get(Constantes.EMAIL).getAsString());
            String password = LimpiezaValidacion.validPassword(json.get(Constantes.PASSWORD).getAsString());

            usuario = usuarioService.login(email, password);
            if (usuario == null) {
                respuesta.setStatus(Constantes.KO);
                respuesta.setStatusMsg(Error.USUARIO_NOT_EXIST.getCode());
                logger.warn(Error.USUARIO_NOT_EXIST.getMsg());
            } else {
                String idLogin = WebUtils.generateSessionId();

                WebUtils.cache().put(idLogin,usuario);

                usuario.setIdLogin(idLogin);
                respuesta.setStatusMsg(Constantes.OK);
                respuesta.setSalida(gson.toJson(usuario));
            }
        } else if ("Logout".equalsIgnoreCase(action)){
            String idLogin = json.get("IdLogin").getAsString();
            WebUtils.cache().remove(idLogin);
            respuesta.setStatus(Constantes.OK);
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
            boolean creado = false;
            if (usuario.getEmail() != null && usuario.getNombre() != null && usuario.getFechaNacimiento() != null && usuario.getPassword() != null) {
                creado = usuarioService.create(usuario);
            }
            if(creado){
                respuesta.setStatusMsg(Constantes.OK);
            }else{
                respuesta.setStatus(Constantes.KO);
                respuesta.setStatusMsg(Error.CREATE_FAIL.getCode());
                logger.warn(Error.CREATE_FAIL.getMsg());
            }
        }else if ("Configuracion".equalsIgnoreCase(action)) {
            String idLogin =json.get(Constantes.IDLOGIN).getAsString();
            Usuario usuarioLogged = WebUtils.cache().get(idLogin);

            usuarioLogged.setNombre(LimpiezaValidacion.validNombre(json.get(Constantes.NOMBRE).getAsString()));
            usuarioLogged.setApellido1(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO1).getAsString()));
            usuarioLogged.setApellido2(LimpiezaValidacion.validApellido(json.get(Constantes.APELLIDO2).getAsString()));
            usuarioLogged.setEmail(LimpiezaValidacion.validEmail(json.get(Constantes.EMAIL).getAsString()));
            usuarioLogged.setTelefono(LimpiezaValidacion.validTelefono(json.get(Constantes.TELEFONO).getAsString()));
            usuarioLogged.setPassword(LimpiezaValidacion.validPassword(json.get(Constantes.PASSWORD).getAsString()));
            usuarioLogged.setNombreUser(LimpiezaValidacion.validNombreUser(json.get("NombreUsuario").getAsString()));

            boolean actualizado = false;
            if (usuarioLogged.getNombre() != null || usuarioLogged.getApellido1() != null || usuarioLogged.getApellido2() != null
                    || usuarioLogged.getTelefono() != null || usuarioLogged.getPassword() != null || usuarioLogged.getNombreUser() != null) {
                actualizado = usuarioService.update(usuarioLogged);
            }
            if(actualizado){
                respuesta.setStatus(Constantes.OK);
            }else{
                respuesta.setStatus(Constantes.KO);
                respuesta.setStatusMsg(Error.UPDATE_FAIL.getCode());
                logger.warn(Error.UPDATE_FAIL.getMsg());
            }
        }
        return respuesta;
    }
}
