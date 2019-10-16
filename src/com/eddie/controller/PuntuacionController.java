package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.ItemBiblioteca;
import com.eddie.ecommerce.model.Juego;
import com.eddie.ecommerce.model.Response;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.util.Constantes;
import com.eddie.utils.util.Error;
import com.eddie.utils.util.WebUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

public class PuntuacionController {

    private static UsuarioService usuarioService;
    private static JuegoService juegoService;
    private static Logger logger = LogManager.getLogger(PuntuacionController.class);

    public PuntuacionController() {
        usuarioService = new UsuarioServiceImpl();
        juegoService = new JuegoServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws DataException {
        JsonObject json = entrada.getAsJsonObject();
        JsonObject respuesta = new JsonObject();

        String idLogin = json.get(Constantes.IDLOGIN).getAsString();

        //Controlar que esta logeado
        Usuario usuario = WebUtils.cache().get(idLogin);
        if (usuario != null && (json.get(Constantes.IDJUEGO).getAsString()!= null || !json.get(Constantes.IDJUEGO).getAsString().equals(""))) {
            Integer idJuego = Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString());
            try {
                if ("ChangePuntuacion".equalsIgnoreCase(action) && (json.get("Puntuacion").getAsString()!= null || !json.get("Puntuacion").getAsString().equals(""))) {
                    Integer puntuacion = Integer.valueOf(json.get("Puntuacion").getAsString());

                    ItemBiblioteca puntuacionUsuario = usuarioService.findByIdEmail(usuario.getEmail(), idJuego);
                    puntuacionUsuario.setPuntuacion(puntuacion);

                    if (usuarioService.borrarJuegoBiblioteca(usuario.getEmail(), idJuego) && usuarioService.create(puntuacionUsuario)) {
                        respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                    } else {
                        respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                        respuesta.addProperty(Constantes.STATUSMSG,Error.UPDATE_FAIL.getCode());
                        logger.warn(Error.UPDATE_FAIL.getMsg());
                    }
                } else if ("AddComentario".equalsIgnoreCase(action) && (json.get("Comentario").getAsString()!= null || !json.get("Comentario").getAsString().equals(""))) {
                    String comentario = json.get("Comentario").getAsString();

                    ItemBiblioteca itemBiblioteca = new ItemBiblioteca();
                    itemBiblioteca.setIdJuego(idJuego);
                    itemBiblioteca.setComentario(comentario);
                    itemBiblioteca.setEmail(usuario.getEmail());
                    Date date = new Date();
                    itemBiblioteca.setFechaComentario(new java.sql.Date(date.getTime()));
                    if (juegoService.addComent(itemBiblioteca)) {
                        respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                    } else {
                        respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                        respuesta.addProperty(Constantes.STATUSMSG,Error.UPDATE_FAIL.getCode());
                        logger.warn(Error.UPDATE_FAIL.getMsg());
                    }
                }else{
                    respuesta.addProperty(Constantes.STATUS, Constantes.OK);
                    respuesta.addProperty(Constantes.STATUSMSG,Error.INVALID_REQUEST.getCode());
                    logger.warn(Error.INVALID_REQUEST.getMsg());
                }
            } catch(com.eddie.ecommerce.exceptions.DataException e) {
                logger.info(e);
            }
        }else{
            respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            respuesta.addProperty(Constantes.STATUSMSG,Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
