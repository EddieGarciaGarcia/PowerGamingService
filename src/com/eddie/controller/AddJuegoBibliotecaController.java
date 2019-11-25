package com.eddie.controller;

import com.eddie.ecommerce.model.ItemBiblioteca;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddJuegoBibliotecaController {
    private static Logger logger = LogManager.getLogger(AddJuegoBibliotecaController.class);
    private static UsuarioService usuarioService = null;

    public AddJuegoBibliotecaController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) throws Exception {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();
        ItemBiblioteca itemBiblioteca = null;

        //Controlar que esta logeado
        Usuario usuario = (Usuario) RedisCache.getInstance().getValue(json.get(Constantes.IDLOGIN).getAsString());

        if (usuario != null && (json.get(Constantes.IDJUEGO).getAsString() != null || !json.get(Constantes.IDJUEGO).getAsString().equals(""))) {
            itemBiblioteca = new ItemBiblioteca();
            itemBiblioteca.setEmail(usuario.getEmail());
            itemBiblioteca.setIdJuego(Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString()));
            if (!usuarioService.existsInBiblioteca(usuario.getEmail(), itemBiblioteca.getIdJuego())) {
                usuarioService.addJuegoBiblioteca(usuario.getEmail(), itemBiblioteca);
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            } else {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
                respuesta.addProperty(Constantes.STATUSMSG, Error.UPDATE_FAIL.getCode());
                logger.warn(Error.UPDATE_FAIL.getMsg());
            }
        }else{
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
