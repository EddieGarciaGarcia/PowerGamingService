package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.ItemBiblioteca;
import com.eddie.ecommerce.model.Response;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.util.Constantes;
import com.eddie.utils.util.Error;
import com.eddie.utils.util.WebUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class PuntuacionController {

    private static UsuarioService usuarioService;
    private static JuegoService juegoService;
    private static Logger logger = LogManager.getLogger(PuntuacionController.class);

    public PuntuacionController() {
        usuarioService = new UsuarioServiceImpl();
        juegoService = new JuegoServiceImpl();
    }

    public static Response procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws DataException {
        JsonObject json = entrada.getAsJsonObject();
        Response respuesta = new Response();

        String idLogin = json.get(Constantes.IDLOGIN).getAsString();

        //Controlar que esta logeado
        Usuario usuario = WebUtils.cache().get(idLogin);
        if (usuario != null) {
            Integer idJuego = Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString());
            try {
                if ("ChangePuntuacion".equalsIgnoreCase(action)) {
                    Integer puntuacion = Integer.valueOf(json.get("Puntuacion").getAsString());

                    ItemBiblioteca puntuacionUsuario = usuarioService.findByIdEmail(usuario.getEmail(), idJuego);
                    puntuacionUsuario.setPuntuacion(puntuacion);
                    if (usuarioService.borrarJuegoBiblioteca(usuario.getEmail(), idJuego) && usuarioService.create(puntuacionUsuario)) {
                        respuesta.setStatus(Constantes.OK);
                    } else {
                        respuesta.setStatus(Constantes.KO);
                        respuesta.setStatusMsg(Error.UPDATE_FAIL.getCode());
                        logger.warn(Error.UPDATE_FAIL.getMsg());
                    }
                } else if ("AddComentario".equalsIgnoreCase(action)) {
                    String comentario = json.get("Comentario").getAsString();

                    ItemBiblioteca itemBiblioteca = new ItemBiblioteca();
                    itemBiblioteca.setIdJuego(idJuego);
                    itemBiblioteca.setComentario(comentario);
                    itemBiblioteca.setEmail(usuario.getEmail());
                    Date date = new Date();
                    itemBiblioteca.setFechaComentario(new java.sql.Date(date.getTime()));
                    if (juegoService.addComent(itemBiblioteca)) {
                        respuesta.setStatus(Constantes.OK);
                    } else {
                        respuesta.setStatus(Constantes.KO);
                        respuesta.setStatusMsg(Error.UPDATE_FAIL.getCode());
                        logger.warn(Error.UPDATE_FAIL.getMsg());
                    }
                }
            } catch(com.eddie.ecommerce.exceptions.DataException e) {
                logger.info(e);
            }
        }else{
            respuesta.setStatus(Constantes.KO);
            respuesta.setStatusMsg(Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
