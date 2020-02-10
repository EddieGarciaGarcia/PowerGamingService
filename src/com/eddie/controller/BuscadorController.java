package com.eddie.controller;

import com.eddie.ecommerce.exceptions.DataException;
import com.eddie.ecommerce.model.Juego;
import com.eddie.ecommerce.model.JuegoCriteria;
import com.eddie.ecommerce.model.Usuario;
import com.eddie.ecommerce.service.JuegoService;
import com.eddie.ecommerce.service.UsuarioService;
import com.eddie.ecommerce.service.impl.JuegoServiceImpl;
import com.eddie.ecommerce.service.impl.UsuarioServiceImpl;
import com.eddie.gestor.RedisCache;
import com.eddie.utils.Constantes;
import com.eddie.utils.LimpiezaValidacion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class BuscadorController {
    private static Logger logger = LogManager.getLogger(BuscadorController.class);

    private static JuegoService juegoService = null;
    private static UsuarioService usuarioService = null;

    public BuscadorController() {
        juegoService = new JuegoServiceImpl();
        usuarioService = new UsuarioServiceImpl();
    }

    public static JsonObject procesarPeticion(JsonObject datos, Usuario usuario) throws DataException {
        JsonObject json = new JsonObject();
        if(datos.has("Entrada"))json = datos.get("Entrada").getAsJsonObject();
        String idiomaWeb = datos.get("IdiomaWeb").getAsString();
        JsonObject respuesta = new JsonObject();
        JuegoCriteria juegoCriteria = new JuegoCriteria();

        // Recuperar parametros
        String nombre = json.has(Constantes.NOMBRE) ? json.get(Constantes.NOMBRE).getAsString() : null;
        if (nombre != null) {
            juegoCriteria.setNombre(nombre);
        }

        String creador = json.has("Creador") ? json.get("Creador").getAsString() : null;
        if (creador != null) {
            juegoCriteria.setIdCreador(Integer.valueOf(creador));
        }

        String fecha = json.has("Fecha") ? json.get("Fecha").getAsString() : null;
        if (fecha != null) {
            juegoCriteria.setFechaLanzamiento(Integer.valueOf(fecha));
        }

        String[] categorias = null;
        if (json.has("Categorias")) {
            categorias = addValuesCheckBox(json, "Categorias");
            if (categorias.length != 0) { juegoCriteria.setCategoriaIDs(asInteger(categorias)); }
        }

        String[] plataforma = null;
        if (json.has("Plataformas")) {
            plataforma = addValuesCheckBox(json, "Plataformas");
            if (plataforma.length != 0) { juegoCriteria.setPlataformaIDs(asInteger(plataforma));}
        }

        String[] idioma = null;
        if (json.has("Idioma")) {
            idioma = addValuesCheckBox(json, "idioma");
            if (idioma.length != 0) { juegoCriteria.setIdiomaIDs(idioma);}
        }

        List<Juego> juegos = juegoService.findByJuegoCriteria(juegoCriteria, idiomaWeb);

        List<Integer> idsJuegos = juegos.stream().map(Juego::getIdJuego).collect(Collectors.toList());

        // Buscar juegos que tiene incluidos en la biblioteca y setear si existen en ella
        if (usuario != null && !(idsJuegos.isEmpty())) {
            List<Integer> idsJuegosEnBiblioteca = usuarioService.existsInBiblioteca(usuario.getEmail(), idsJuegos);
            if (idsJuegosEnBiblioteca != null && !idsJuegosEnBiblioteca.isEmpty()) {
                juegos.forEach(juego -> {if(idsJuegosEnBiblioteca.contains(juego.getIdJuego())){juego.setExisteEnBiblioteca(true);}});
            }
        }

        respuesta.add("JuegosSearch", new Gson().toJsonTree(juegos, new TypeToken<List<Juego>>() {}.getType()).getAsJsonArray());
        respuesta.addProperty(Constantes.STATUS, Constantes.OK);

        return respuesta;
    }

    private static String[] addValuesCheckBox(JsonObject datos, String variable) {
        String[] array = new String[datos.get(variable).getAsJsonArray().size()];
        for (int i = 0; i < datos.get(variable).getAsJsonArray().size(); i++) {
            array[i] = datos.get(variable).getAsJsonArray().get(i).getAsString();
        }
        return array;
    }

    private static int[] asInteger(String ss[]) {
        int[] ii = new int[ss.length];
        for (int i = 0; i<ss.length; i++) {
            ii[i] = Integer.parseInt(ss[i]);
        }
        return ii;
    }
}
