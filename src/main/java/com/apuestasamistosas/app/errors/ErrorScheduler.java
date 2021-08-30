
package com.apuestasamistosas.app.errors;


public class ErrorScheduler extends Exception{
    
    public ErrorScheduler(String message){
        super(message);
    }
    
    public static final String NULL_EVENTS = "No hay eventos para supervisar con el planificador";
}
