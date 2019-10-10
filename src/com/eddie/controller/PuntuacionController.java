package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.ItemBiblioteca;
import com.eddie.ecommerce.model.Response;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
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

    public static Response procesarPeticion(JsonElement entrada, String action, String idiomaWeb) {
        JsonObject json = entrada.getAsJsonObject();
        Response respuesta = new Response();

        Usuario usuario = new Usuario();
        usuario.setNombre(json.get("Nombre").toString());
        usuario.setEmail(json.get("Email").toString());

        String id = json.get("IdJuego").toString();
        Integer idJuego = Integer.valueOf(id);

        try {
            if ("ChangePuntuacion".equalsIgnoreCase(action)) {
                String puntuacion = json.get("Puntuacion").toString();
                Integer puntuacionInteger = Integer.valueOf(puntuacion);

                ItemBiblioteca puntuacionUsuario = usuarioService.findByIdEmail(usuario.getEmail(), idJuego);
                puntuacionUsuario.setPuntuacion(puntuacionInteger);
                if (usuarioService.borrarJuegoBiblioteca(usuario.getEmail(), idJuego) && usuarioService.create(puntuacionUsuario)) {
                    respuesta.setStatus("OK");
                } else {
                    respuesta.setStatus("KO");
                }
            } else if ("AddComentario".equalsIgnoreCase(action)) {
                String comentario = json.get("Comentario").toString();

                ItemBiblioteca itemBiblioteca = new ItemBiblioteca();
                itemBiblioteca.setIdJuego(idJuego);
                itemBiblioteca.setComentario(comentario);
                itemBiblioteca.setEmail(usuario.getEmail());
                Date date = new Date();
                itemBiblioteca.setFechaComentario(new java.sql.Date(date.getTime()));
                if(juegoService.addComent(itemBiblioteca)){
                    respuesta.setStatus("OK");
                }else{
                    respuesta.setStatus("KO");
                }
            }
        } catch (com.eddie.ecommerce.exceptions.DataException e) {
            logger.info(e);
        }
        return respuesta;
    }
}
