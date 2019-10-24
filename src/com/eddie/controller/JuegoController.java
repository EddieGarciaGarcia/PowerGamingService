package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.*;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.ecommerce.utils.CacheManager;
import com.eddie.utils.ArrayUtils;
import com.eddie.utils.Constantes;
import com.eddie.utils.LimpiezaValidacion;
import com.eddie.utils.WebUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JuegoController {
    private static Logger logger = LogManager.getLogger(JuegoController.class);

    private static JuegoService juegoService = null;
    private static UsuarioService usuarioService = null;


    public JuegoController() {
        super();
        juegoService = new JuegoServiceImpl();
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws DataException {
        JsonObject json = entrada.getAsJsonObject();
        JsonObject respuesta = new JsonObject();
        Usuario usuario = null;
        String idLogin =json.has(Constantes.IDLOGIN) ? json.get(Constantes.IDLOGIN).getAsString() : null;
        if (idLogin!=null) {
            usuario = CacheManager.getCacheLogin(Constantes.NOMBRE_CACHE_LOGIN).get(idLogin);
        }
        if ("Buscar".equalsIgnoreCase(action)) {
                // Recuperar parametros
                String nombre = json.has(Constantes.NOMBRE) ? json.get(Constantes.NOMBRE).getAsString() : null;
                String[] categorias = json.has("Categorias") ? json.get("Categorias").getAsString().split(",") : null;
                String creador = json.has("Creador") ? json.get("Creador").getAsString() : null;
                String[] plataforma = json.has("Plataformas") ? json.get("Plataformas").getAsString().split(",") : null;
                String[] idioma = json.has("Idioma") ? json.get("Idioma").getAsString().split(",") : null;
                String fecha = json.has("Fecha") ? json.get("Fecha").getAsString() : null;

                String nombreValid = LimpiezaValidacion.validNombreJuego(nombre);

                JuegoCriteria jc = new JuegoCriteria();

                if (WebUtils.isEmptyOrNull(nombreValid)) {
                    jc.setNombre(nombreValid);
                }
                if (categorias != null && categorias.length != 0) {
                    jc.setCategoriaIDs(ArrayUtils.asInteger(categorias));
                }
                if (idioma != null && idioma.length != 0) {
                    jc.setIdiomaIDs(idioma);
                }
                if (plataforma != null && plataforma.length != 0) {
                    jc.setPlataformaIDs(ArrayUtils.asInteger(plataforma));
                }
                if (creador != null) {
                    Integer creadorValid = Integer.valueOf(creador);
                    jc.setIdCreador(creadorValid);
                }
                if (fecha != null) {
                    Integer fechaformat = Integer.valueOf(fecha);
                    jc.setFechaLanzamiento(fechaformat);
                } else {
                    jc.setNombre(nombreValid);
                }

                List<Juego> juegos = juegoService.findByJuegoCriteria(jc, idiomaWeb);

                List<Integer> idsJuegos = juegos.stream().map(Juego::getIdJuego).collect(Collectors.toList());
                List<Integer> idsJuegosEnBiblioteca = null;
                // Buscar juegos que tiene incluidos en la biblioteca
                if (usuario != null && !(idsJuegos.isEmpty())) {
                    idsJuegosEnBiblioteca = usuarioService.existsInBiblioteca(usuario.getEmail(), idsJuegos);
                }

                if (idsJuegosEnBiblioteca != null && !idsJuegosEnBiblioteca.isEmpty()) {
                    respuesta.add("IdsJuegosBiblioteca",  new Gson().toJsonTree(idsJuegosEnBiblioteca, new TypeToken<List<Integer>>(){}.getType()).getAsJsonArray());
                }

                respuesta.add("JuegosSearch",  new Gson().toJsonTree(juegos, new TypeToken<List<Juego>>(){}.getType()).getAsJsonArray());
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            } else if ("Juego".equalsIgnoreCase(action)) {
                Integer idJuego = Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString());
                Juego juego;
                if(usuario != null){
                    juego = juegoService.findById(idJuego, usuario.getEmail(), idiomaWeb);
                }else{
                    juego = juegoService.findById(idJuego, null, idiomaWeb);
                }

                //Listado de comentarios
                List<ItemBiblioteca> comentarios = juegoService.findByJuego(juego.getIdJuego());

                respuesta.add("Juego",  new Gson().toJsonTree(juego, new TypeToken<Juego>(){}.getType()).getAsJsonObject());
                respuesta.add("Comentarios",  new Gson().toJsonTree(comentarios, new TypeToken<List<ItemBiblioteca>>(){}.getType()).getAsJsonArray());
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            }
        return respuesta;
    }
}
