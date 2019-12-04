package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.eddie.utils.Error;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteJuegoBibliotecaController {
    private static Logger logger = LogManager.getLogger(DeleteJuegoBibliotecaController.class);
    private static UsuarioService usuarioService = null;

    public DeleteJuegoBibliotecaController() {
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos) throws DataException {
        JsonObject json = datos.get("Entrada").getAsJsonObject();
        JsonObject respuesta = new JsonObject();

        //Controlar que esta logeado
        Usuario usuario = (Usuario) RedisCache.getInstance().getValue(json.get(Constantes.IDLOGIN).getAsString(),1);

        if (usuario != null && (json.get(Constantes.IDJUEGO).getAsString() != null || !json.get(Constantes.IDJUEGO).getAsString().equals(""))) {
            if (usuarioService.borrarJuegoBiblioteca(usuario.getEmail(), Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString()))) {
                respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            } else {
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            }
        } else {
            respuesta.addProperty(Constantes.STATUS, Constantes.KO);
            respuesta.addProperty(Constantes.STATUSMSG, Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
