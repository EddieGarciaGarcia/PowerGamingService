package com.eddie.controller;

import com.eddie.ecommerce.model.*;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.util.Constantes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BibliotecaController {

    private static Logger logger = LogManager.getLogger(BibliotecaController.class);
    private static UsuarioService usuarioService=null;
    private static JuegoService juegoService = null;

    public BibliotecaController() {
        usuarioService=new UsuarioServiceImpl();
        juegoService = new JuegoServiceImpl();
    }

    public static Response procesarPeticion(JsonElement entrada, String action, String idiomaWeb){
        JsonObject json = entrada.getAsJsonObject();
        Response respuesta = new Response();

        Usuario usuario = new Usuario();
        usuario.setNombre(json.get("Nombre").toString());
        usuario.setEmail(json.get("Email").toString());

        String id = json.get("idJuego").toString();
        Integer idJuego=Integer.valueOf(id);

        try {
            if("Biblioteca".equalsIgnoreCase(action)) {
                List<ItemBiblioteca> biblioteca= usuarioService.findByUsuario(usuario.getEmail());

                List<Integer> juegoIDs = new ArrayList<>();
                for(ItemBiblioteca it: biblioteca) {
                    juegoIDs.add(it.getIdJuego());
                }
                List<Juego> juegos = new ArrayList<>();
                //Lambda expresion stream collectors

                if(juegoIDs!=null && !juegoIDs.isEmpty()) {
                    juegos = juegoService.findByIDs(juegoIDs, idiomaWeb);
                }
                Gson gson = new Gson();
                respuesta.setStatus("OK");
                respuesta.setSalida(gson.toJson(juegos));

            }else if("DeleteJuego".equalsIgnoreCase(action)) {
                if(usuarioService.borrarJuegoBiblioteca(usuario.getEmail(), idJuego)){
                    respuesta.setStatus("OK");
                }else{
                    respuesta.setStatus("KO");
                }
                return respuesta;
            }else if("AddJuego".equalsIgnoreCase(action)) {
                ItemBiblioteca it= new ItemBiblioteca();
                it.setEmail(usuario.getEmail());
                it.setIdJuego(idJuego);

                if(!usuarioService.existsInBiblioteca(usuario.getEmail(),idJuego)) {
                    usuarioService.addJuegoBiblioteca(usuario.getEmail(), it);
                    respuesta.setStatus("OK");
                }else{
                    respuesta.setStatus("KO");
                }
                return respuesta;
            }
        } catch (com.eddie.ecommerce.exceptions.DataException e) {
            logger.info(e.getMessage(),e);
        }
        return respuesta;
    }
}
