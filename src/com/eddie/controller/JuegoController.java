package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.*;
import com.eddie.ecommerce.service.CreadorService;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.CreadorServiceImpl;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.utils.util.ArrayUtils;
import com.eddie.utils.util.Constantes;
import com.eddie.utils.util.LimpiezaValidacion;
import com.eddie.utils.util.WebUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.eddie.utils.util.Error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JuegoController {
    private static Logger logger = LogManager.getLogger(JuegoController.class);

    private static JuegoService juegoService = null;
    private static UsuarioService usuarioService = null;
    private static CreadorService creadorService = null;


    public JuegoController() {
        super();
        juegoService = new JuegoServiceImpl();
        usuarioService = new UsuarioServiceImpl();
        creadorService = new CreadorServiceImpl();
    }

    public static Response procesarPeticion(JsonElement entrada, String action, String idiomaWeb) throws DataException {
        JsonObject json = entrada.getAsJsonObject();
        Response respuesta = new Response();

        String idLogin =json.get(Constantes.IDLOGIN).getAsString();
        Usuario usuario = WebUtils.cache().get(idLogin);
        if (usuario != null) {
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
                if (usuario.getEmail() != null && !(idsJuegos.isEmpty())) {
                    idsJuegosEnBiblioteca = usuarioService.existsInBiblioteca(usuario.getEmail(), idsJuegos);
                }
                Map<String, List> datos = new HashMap<>();
                datos.put("JuegosSearch", juegos);
                if (idsJuegosEnBiblioteca != null && !idsJuegosEnBiblioteca.isEmpty()) {
                    datos.put("JuegosBiblioteca", idsJuegosEnBiblioteca);
                }
                Gson gson = new Gson();
                respuesta.setSalida(gson.toJson(datos));
                respuesta.setStatus(Constantes.OK);
            } else if ("Juego".equalsIgnoreCase(action)) {
                Integer idJuego = Integer.valueOf(json.get(Constantes.IDJUEGO).getAsString());

                Juego juego = juegoService.findById(idJuego, usuario.getEmail(), idiomaWeb);

                //Listado de comentarios
                List<ItemBiblioteca> comentarios = juegoService.findByJuego(juego.getIdJuego());

                HashMap<String, ItemBiblioteca> comentario = null;
                if (comentarios != null && !comentarios.isEmpty()) {
                    comentario = new HashMap<>();
                    for (ItemBiblioteca i : comentarios) {
                        Usuario u = usuarioService.findById(i.getEmail());
                        comentario.put(u.getNombreUser(), i);
                    }
                }
                Gson gson = new Gson();
                respuesta.setStatusMsg(gson.toJson(juego));
                respuesta.setSalida(gson.toJson(comentario));
                respuesta.setStatus(Constantes.OK);
            }
        }else {
            respuesta.setStatus(Constantes.KO);
            respuesta.setStatusMsg(Error.GENERIC_ERROR.getCode());
            logger.warn(Error.GENERIC_ERROR.getMsg());
        }
        return respuesta;
    }
}
