
package com.apuestasamistosas.app.errors;


public class ErrorEventos extends Exception{
    
    public ErrorEventos(String message){
        super(message);
    }
    
    public static final String NULL_TEAM = "Ninguno de los dos equipos puede estar vacio.";
    public static final String NULL_DATE = "Debe ingresar una fecha.";
    public static final String NULL_TIME = "Debe ingresar una hora.";
    public static final String SAME_TEAM = "No pueden ser el mismo equipo.";
    public static final String NO_TEAM = "Este equipo no existe";
    public static final String WRONG_DATE = "El evento debe cargarse al menos con 48hs de anticipación.";
    public static final String NO_EVENTS = "No hay eventos para mostrar";
    public static final String NO_EXISTS = "Este evento no existe.";
}
