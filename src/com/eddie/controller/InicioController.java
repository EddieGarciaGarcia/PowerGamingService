package com.eddie.controller;

import com.eddie.ecommerce.model.*;
import com.eddie.ecommerce.service.*;
import com.eddie.ecommerce.service.impl.*;
import com.eddie.utils.util.Constantes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.*;

public class InicioController {

    private static JuegoService juegoService;
    private static CategoriaService categoriaService;
    private static CreadorService creadorService;
    private static PlataformaService plataformaService;
    private static IdiomaService idiomaService;
    private static FormatoService formatoService;
    private static TipoEdicionService tipoEdicionService;
    private static PaisService paisService;
    private static ProvinciaService provinciaService;

    private static Logger logger = LogManager.getLogger(InicioController.class);

    public InicioController() {
        juegoService = new JuegoServiceImpl();
        categoriaService = new CategoriaServiceImpl();
        creadorService = new CreadorServiceImpl();
        plataformaService = new PlataformaServiceImpl();
        idiomaService = new IdiomaServiceImpl();
        formatoService = new FormatoServiceImpl();
        tipoEdicionService = new TipoEdicionServiceImpl();
        paisService = new PaisServiceImpl();
        provinciaService = new ProvinciaServiceImpl();
    }
    public static JsonObject procesarPeticion(JsonElement entrada, String action, String idiomaWeb){
        JsonObject respuesta = new JsonObject();
        try {
            if("DatosInicio".equalsIgnoreCase(action)) {
                List<Juego> todos = juegoService.findAllByDate(idiomaWeb);
                List<Juego> valoracion=juegoService.findAllByValoracion(idiomaWeb);

                respuesta.add("Todos", new Gson().toJsonTree(todos, new TypeToken<List<Juego>>(){}.getType()).getAsJsonArray());
                respuesta.add("Valoracion", new Gson().toJsonTree(valoracion, new TypeToken<List<Juego>>(){}.getType()).getAsJsonArray());
                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            }else if("DatosPrecarga".equalsIgnoreCase(action)) {
                //Precarga de todos los datos en cache
                List<Provincia> provincia = provinciaService.findAll();
                List<Pais> pais = paisService.findAll();
                List<Categoria> categoria = categoriaService.findAll(idiomaWeb);
                List<Creador> creador = creadorService.findAll();
                List<Plataforma> plataforma = plataformaService.findAll();
                List<Idioma> idioma = idiomaService.findAll(idiomaWeb);
                List<Formato> formato = formatoService.findAll(idiomaWeb);
                List<TipoEdicion> tipoEdicion = tipoEdicionService.findAll(idiomaWeb);

                respuesta.add("Provincias", new Gson().toJsonTree(provincia, new TypeToken<List<Provincia>>(){}.getType()).getAsJsonArray());
                respuesta.add("Pais", new Gson().toJsonTree(pais, new TypeToken<List<Pais>>(){}.getType()).getAsJsonArray());
                respuesta.add("Categorias", new Gson().toJsonTree(categoria, new TypeToken<List<Categoria>>(){}.getType()).getAsJsonArray());
                respuesta.add("Creadores", new Gson().toJsonTree(creador, new TypeToken<List<Creador>>(){}.getType()).getAsJsonArray());
                respuesta.add("Plataformas", new Gson().toJsonTree(plataforma, new TypeToken<List<Plataforma>>(){}.getType()).getAsJsonArray());
                respuesta.add("Idiomas", new Gson().toJsonTree(idioma, new TypeToken<List<Idioma>>(){}.getType()).getAsJsonArray());
                respuesta.add("Formatos", new Gson().toJsonTree(formato, new TypeToken<List<Formato>>(){}.getType()).getAsJsonArray());
                respuesta.add("TipoEdiciones", new Gson().toJsonTree(tipoEdicion, new TypeToken<List<TipoEdicion>>(){}.getType()).getAsJsonArray());

                respuesta.addProperty(Constantes.STATUS, Constantes.OK);
            }
        } catch (com.eddie.ecommerce.exceptions.DataException e) {
            logger.debug(e);
        }
        return respuesta;
    }
}
