package com.eddie.controller;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BibliotecaController {

    private static Logger logger = LogManager.getLogger(BibliotecaController.class);
    private static UsuarioService usuarioService = null;
    private static JuegoService juegoService = null;

    public BibliotecaController() {
        usuarioService = new UsuarioServiceImpl();
        juegoService = new JuegoServiceImpl();
    }

    public static Response procesarPeticion(JsonElement entrada, String action, String idiomaWeb) {
        JsonObject json = entrada.getAsJsonObject();
        Response respuesta = new Response();

        String idLogin = json.get(Constantes.IDLOGIN).getAsString();

        //Controlar que esta logeado

        Usuario usuario = WebUtils.cache().get(idLogin);
        if (usuario != null && (json.get(Constantes.IDJUEGO).getAsString()!= null || !json.get(Constantes.IDJUEGO).getAsString().equals(""))) {
            Integer idJuego = Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString());
            try {
                if ("Biblioteca".equalsIgnoreCase(action)) {
                    List<ItemBiblioteca> biblioteca = usuarioService.findByUsuario(usuario.getEmail());

                    List<Integer> juegoIDs = new ArrayList<>();
                    for (ItemBiblioteca it : biblioteca) {
                        juegoIDs.add(it.getIdJuego());
                    }
                    List<Juego> juegos = new ArrayList<>();
                    //Lambda expresion stream collectors

                    if (juegoIDs != null && !juegoIDs.isEmpty()) {
                        juegos = juegoService.findByIDs(juegoIDs, idiomaWeb);
                    }
                    Gson gson = new Gson();
                    respuesta.setStatus(Constantes.OK);
                    respuesta.setSalida(gson.toJson(juegos));

                } else if ("DeleteJuego".equalsIgnoreCase(action)) {
                    if (usuarioService.borrarJuegoBiblioteca(usuario.getEmail(), idJuego)) {
                        respuesta.setStatus(Constantes.KO);
                    } else {
                        respuesta.setStatus(Constantes.OK);
                    }
                    return respuesta;
                } else if ("AddJuego".equalsIgnoreCase(action)) {
                    ItemBiblioteca it = new ItemBiblioteca();
                    it.setEmail(usuario.getEmail());
                    it.setIdJuego(idJuego);

                    if (!usuarioService.existsInBiblioteca(usuario.getEmail(), idJuego)) {
                        usuarioService.addJuegoBiblioteca(usuario.getEmail(), it);
                        respuesta.setStatus(Constantes.OK);
                    } else {
                        respuesta.setStatus(Constantes.KO);
                        respuesta.setStatusMsg(Error.UPDATE_FAIL.getCode());
                        logger.warn(Error.UPDATE_FAIL.getMsg());
                    }
                    return respuesta;
                }
            } catch (com.eddie.ecommerce.exceptions.DataException e) {
                logger.info(e.getMessage(), e);
            }
        } else {
            respuesta.setStatus(Constantes.KO);
            respuesta.setStatusMsg(Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}