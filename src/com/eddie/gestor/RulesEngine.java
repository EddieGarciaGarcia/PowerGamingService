package com.eddie.gestor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.ArrayList;

public class RulesEngine {
    private static Logger logger = LogManager.getLogger(RulesEngine.class.getName());

    private static ArrayList<String> reglasEntrada = null;

    private RulesEngine(){
        reglasEntrada = new ArrayList<>(Arrays.asList("Inicio","Juego","Buscador",
                "AddJuegoBiblioteca","DeleteJuegoBiblioteca","Biblioteca","Puntuacion","ChangePassword",
                "ForgotPassword","Contacto","AddComentario","Configuracion","Registro"));
    }

    private static RulesEngine instance = null;

    /**
     * Singleton Thread-Safe.
     */
    public static RulesEngine getInstance() {
        if (instance == null) {
            synchronized(RulesEngine.class) {
                if (instance == null) { // Necesario para proteger una segunda instanciación
                    instance = new RulesEngine();
                }
            }
        }
        return instance;
    }

    public boolean comprobacion(String reglaComprobar){
        for(String regla : reglasEntrada){
            if(regla.equals(reglaComprobar)){
                return true;
            }
        }
        return false;
    }

}
